package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * 接受开关值变化通知
 */
public class ReadStateResult_30 extends BaseJsonModule<OutDevice> {

	private static final int READ_STATE = 30;
	private OutDevice device = new OutDevice();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& READ_STATE == baseJsonObj.getCode()) {
			ReadState(baseJsonObj);
		}
	}

	/**
	 * Function:
	 * 
	 * @author Howard DateTime 2015年1月15日 下午4:45:48
	 * @param baseJsonObj
	 *            返回：data内容：{state:[ {phoneCode:int,state:int},{...},... }
	 *            //state:0关，1开
	 */
	private void ReadState(BaseJsonObj baseJsonObj) {
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
		// TODO 自动生成的方法存根
		return device;
	}

}
