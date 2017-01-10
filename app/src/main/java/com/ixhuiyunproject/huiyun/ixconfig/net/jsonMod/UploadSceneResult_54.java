package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

public class UploadSceneResult_54 extends BaseJsonModule<Object> {

	private static final int UPLOAD_SCENE = 54;
	
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& UPLOAD_SCENE == baseJsonObj.getCode()) {
			uploadSceneResult(baseJsonObj);
		}
	}
	
	private void uploadSceneResult(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
		} else {
			notifyListeners(true, null);
		}
	}

	@Override
	public <K> K getData() {
		return null;
	}

}
