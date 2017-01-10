package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**   
* @Title: DownloadGateway_32.java 
* @Package com.huiyun.ixconfig.net.jsonMod 
* @Description: 下载网关设备
* @author Yangshao  
* @date 2015年1月15日 下午4:25:13 
* @version V1.0   
*/
public class DownloadGateway_32 extends BaseJsonModule<OutDevice> {
	
	private static final int DOWN_GATEWAY = 32;
	private OutDevice device = new OutDevice();
	
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& DOWN_GATEWAY == baseJsonObj.getCode()) {
			downGateWay(baseJsonObj);
		}
	}

	 /**
	 *  Function:
	 *  @author Howard  DateTime 
	 *  2015年1月15日 下午4:40:32
	 *  @param baseJsonObj
	 *  gatewayDevice:[
    	{address:int,type:int},{...}.
	 */
	private void downGateWay(BaseJsonObj baseJsonObj) {
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
			if (jsonObj.getResult() == 1) {
				Map<String, String> map = jsonObj.getData();
				device.setAddress(Integer.valueOf(map.get("address")));
				device.setType(Integer.valueOf(map.get("type")));
				notifyListeners(true, device);
			} else {
				notifyListeners(false, null);
			}
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
