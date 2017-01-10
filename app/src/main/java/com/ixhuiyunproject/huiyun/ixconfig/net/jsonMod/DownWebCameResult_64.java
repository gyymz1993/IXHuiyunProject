package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Webcam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 下载摄像头信息 TODO
 * 
 * @author lzy_torah WebCameResult_64
 */
public class DownWebCameResult_64 extends BaseJsonModule<List<Webcam>> {

	private static final int CODE_DOWN = 64;
	List<Webcam> webcams = new ArrayList<Webcam>();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			downLoadResult(baseJsonObj);
		}

	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 下午3:07:03
	 * @param baseJsonObj
	 */
	private void downLoadResult(BaseJsonObj baseJsonObj) {
		webcams.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
		} else
			return;
		List<Map<String, String>> keymaps = myjsonObj.data.list;
		for (Map<String, String> map : keymaps) {
			Webcam device = new Webcam();
			device.setDid(map.get("did"));
			device.setArea(map.get("area"));
			webcams.add(device);
		}
		if (webcams.size() != 0) {
			notifyListeners(true, webcams);
		} else {
			notifyListeners(false, null);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Webcam> getData() {
		return webcams;
	}

}
