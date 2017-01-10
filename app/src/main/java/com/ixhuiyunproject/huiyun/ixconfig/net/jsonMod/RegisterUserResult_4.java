package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;

import java.util.Map;

/**
 * @Title: ContrlUser.java
 * @Package com.huiyun.master.contrl
 * @Description: 注册普通用户
 * @author Yangshao
 * @date 2015年1月7日 下午1:26:10
 * @version V1.0
 */
public class RegisterUserResult_4 extends BaseJsonModule<User> {

	// 注册
	private static final int CODE_REGISTER = 4;
	private User user = new User();

	/**
	 * Function:管理员注册 返回
	 * 
	 * @author Yangshao 2015年1月7日 下午1:38:52
	 * 
	 *         masterId:主机id username:用户名 psw：密码
	 */
	private void resgisterUser(BaseJsonObj baseJsonObj) {
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
				user.setToken(map.get("token").toString());
				StaticValue.user = user;
				notifyListeners(true, user);
		} catch (NullPointerException e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@Override
	public void handleCMDdata(BaseJsonObj myJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(myJsonObj.getObj())
				&& CODE_REGISTER == myJsonObj.getCode()) {
			resgisterUser(myJsonObj);
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
