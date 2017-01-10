package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

public class UploadDevicesResult_40 extends BaseJsonModule<Object> {

	private static final int CODE_DOWN = 40;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			uploadDeviceResult(baseJsonObj);
		}
	}

	private void uploadDeviceResult(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		notifyListeners(true, 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getData() {
		return null;
	}

}
