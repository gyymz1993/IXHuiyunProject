package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadOutDevices_24 extends BaseJsonModule<List<OutDevice>>{
	
	private static final int CODE_DOWN = 24;
	List<OutDevice> devices=new ArrayList<OutDevice>();
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			downLoadResult(baseJsonObj);
		}
	
	}
	 /**
	  *  Function:
	  *  @author Yangshao 
	  *  2015年1月10日 下午3:07:03
	  *  @param baseJsonObj
	  */
	private void downLoadResult(BaseJsonObj baseJsonObj) {
		devices.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
		} else
			return;
		List<Map<String, String>> keymaps= myjsonObj.data.list;
		for(Map<String,String> map:keymaps){
			OutDevice device=new OutDevice();
			device.setArea(map.get("area"));
			device.setName(map.get("name"));
			device.setPhoneCode(Integer.valueOf(map.get("phoneCode")));
			device.setType(Integer.valueOf(map.get("type")));
			device.setAddress(Integer.valueOf(map.get("address")));
			device.setNumber(Integer.valueOf(map.get("number")));
			device.setDetails(map.get("detail"));
			device.setState(Integer.valueOf(map.get("state")));
			devices.add(device);
		}
		if(devices.size()!=0){
			//System.out.println(""+devices.size());
			notifyListeners(true, devices);
		}else{
			//System.out.println(devices.size());
			notifyListeners(false, null);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OutDevice> getData() {
		return devices;
	}

}
