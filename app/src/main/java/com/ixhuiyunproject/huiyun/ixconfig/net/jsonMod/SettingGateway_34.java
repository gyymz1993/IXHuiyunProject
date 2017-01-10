package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

/**
* @Package com.huiyun.ixconfig.net.jsonMod 
* @Description: 设置网关
* @author Yangshao  
* @date 2015年1月15日 下午4:25:13 
* @version V1.0   
*/
public class SettingGateway_34 extends BaseJsonModule<Object> {

	private static final int SETTING_GATEWAY = 34;
	
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& SETTING_GATEWAY == baseJsonObj.getCode()) {
			if (baseJsonObj.result == 1) {
				notifyListeners(true, null);
			} else {
				notifyListeners(false, null);
			}
		}
	}

	@Override
	public <K> K getData() {
		return null;
	}

}
