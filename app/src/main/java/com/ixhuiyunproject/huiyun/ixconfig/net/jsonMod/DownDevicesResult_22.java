package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.InDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 下载设备信息的结果
 * @author lzy_torah
 *
 */
public class DownDevicesResult_22 extends BaseJsonModule<List<InDevice>> {
	
	private static final int CODE_DOWN = 22;
	List<InDevice> devices=new ArrayList<InDevice>();
	
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
			InDevice device=new InDevice();
			device.setArea(map.get("area"));
			device.setName(map.get("name"));
			device.setType(Integer.valueOf(map.get("type")));
			device.setAddress(Integer.valueOf(map.get("address")));
			device.setNumber(Integer.valueOf(map.get("number")));
			device.setDetails(map.get("detail"));
//			System.out.println("下载输入设备：" + device.toString());
			devices.add(device);
		}
		if(devices.size()!=0){
			notifyListeners(true, devices);
		}else{
			notifyListeners(false, null);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InDevice> getData() {
		return devices;
	}

}
