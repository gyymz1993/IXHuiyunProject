package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * @Title: ContrlUser.java
 * @Package com.huiyun.master.contrl
 * @Description: 注册管理员返回
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class RegisterAdminResult_2 extends BaseJsonModule<User> {

	// 注册
	private static final int CODE_REGISTER = 2;

	/**
	 * Function:管理员注册 返回
	 */
	private void resgisterAdmin(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			LogUtils.i("masterId 错误");
			return;
		}
		try {
			MyJsonObj1 jsonObj = null;
			if (baseJsonObj instanceof MyJsonObj1)
				jsonObj = (MyJsonObj1) baseJsonObj;
			else
				return;
			ToastUtils.showToastReal("成功");
			Map<String, String> map = jsonObj.getData();
			if (StaticValue.user == null) {
				throw new RuntimeException("RegisterAdminResult_2 逻辑错误");
			} else {
				if (map.get("user").equals(StaticValue.user.getName())) {
					StaticValue.user.setToken(map.get("token").toString());
					StaticValue.user.setType(1);
				} else {
					throw new RuntimeException("RegisterAdminResult_2 非本次注册");
				}
			}
			notifyListeners(true, StaticValue.user);
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("检查传入参数");
		}
	}

	@Override
	public void handleCMDdata(BaseJsonObj jsonObj) {
		if (FinalValue.OBJ_PHONE.equals(jsonObj.getObj())
				&& jsonObj.getCode() == CODE_REGISTER) {
			resgisterAdmin(jsonObj);
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
