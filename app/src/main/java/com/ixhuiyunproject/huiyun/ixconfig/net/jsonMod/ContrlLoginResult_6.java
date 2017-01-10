package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * 登陆的结果
 * 
 * @author lzy_torah
 *
 */
public class ContrlLoginResult_6 extends BaseJsonModule<User> {
	// 登录
	private static final int CODE_LOGIN = 6;

	@Override 
	public void handleCMDdata(BaseJsonObj jsonObj) {
		if (FinalValue.OBJ_PHONE.equals(jsonObj.getObj())
				&& CODE_LOGIN == jsonObj.getCode()) {
			login(jsonObj);
		}
	}

	/**
	 * Function: 登录判断
	 * 
	 * @author Yangshao 2015年1月9日 上午10:48:44
	 * @param jsonObj
	 */
	private void login(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		try {
			MyJsonObj1 jsonObj = null;
			if (baseJsonObj instanceof MyJsonObj1)
				jsonObj = (MyJsonObj1) baseJsonObj;
			else
				return;
			Map<String, String> map = jsonObj.getData();
			if (StaticValue.user == null) {
				throw new RuntimeException("ContrlLoginResult_6 逻辑错误");
			} else {
				StaticValue.user.setToken(map.get("token").toString());
				StaticValue.user.setType(Integer.valueOf(map.get("type")
						.toString()));
				/**
				 * 得到家庭ID 存入SP
				 */
				SpUtils.saveString("familyId", map.get("familyId"));
			}
			notifyListeners(true, StaticValue.user);
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getData() {
		if (StaticValue.user != null)
			return StaticValue.user;
		return null;
	}

}
