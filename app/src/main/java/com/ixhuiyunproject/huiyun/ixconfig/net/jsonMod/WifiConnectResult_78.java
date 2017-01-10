package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

public class WifiConnectResult_78 extends
		BaseJsonModule<Object> {

	private static final int CODE_DOWN = 78;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			uploadAreaResult(baseJsonObj);
		}
	}

	private void uploadAreaResult(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.obj)
				&& CODE_DOWN == baseJsonObj.code) {
			if (baseJsonObj.result != 1) {
				notifyListeners(false, null);
				return;
			}
			notifyListeners(true, null);
		}
	}

	@Override
	public <K> K getData() {
		return null;
	}

}
