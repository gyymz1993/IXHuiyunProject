package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.MyJsonObjRedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 下载红外设备返回
 * @date 2015年1月10日 下午3:10:35
 * @version V1.0
 */
public class DownloadRedRayCodeResult_14 extends
		BaseJsonModule<List<RedRay>> {
	private static final int CODE_DOWN = 14;
	List<RedRay> devices = new ArrayList<RedRay>();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			downRedRayResult(baseJsonObj);
		}
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 下午3:12:08
	 * @param baseJsonObj
	 */
	private void downRedRayResult(BaseJsonObj baseJsonObj) {
		devices.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObjRedRay myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObjRedRay) {
			myjsonObj = (MyJsonObjRedRay) baseJsonObj;
				@SuppressWarnings("unused")
				List<RedRay> list = myjsonObj.data.list;
				notifyListeners(true, devices);
		}else{
			System.out.println("逻辑错误");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RedRay> getData() {
		return devices;
	}

}
