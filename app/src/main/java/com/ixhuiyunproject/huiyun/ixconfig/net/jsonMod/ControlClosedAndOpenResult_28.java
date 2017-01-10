package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;
/**   
* @Title: ControlClosedAndOpenResult_28.java 
* @Package com.huiyun.ixconfig.net.jsonMod 
* @Description: 控制开关
* @author Yangshao  
* @date 2015年1月15日 下午4:21:34 
* @version V1.0   
*/
public class ControlClosedAndOpenResult_28 extends BaseJsonModule<OutDevice> {
	
	private static final int CONTRL_SWITCH = 28;
	private OutDevice device = new OutDevice();
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CONTRL_SWITCH == baseJsonObj.getCode()) {
			contrlSwitch(baseJsonObj);
		}
	}
	 /**
	 *  Function:控制开关
	 *  @author Howard  DateTime 
	 *  2015年1月15日 下午4:31:13
	 *  @param baseJsonObj
	 */
	private void contrlSwitch(BaseJsonObj baseJsonObj) {
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
				device.setPhoneCode(Integer.valueOf(map.get("phoneCode")));
				device.setState(Integer.valueOf(map.get("state")));
				notifyListeners(true, device);
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public OutDevice getData() {
		return device;
	}
}
