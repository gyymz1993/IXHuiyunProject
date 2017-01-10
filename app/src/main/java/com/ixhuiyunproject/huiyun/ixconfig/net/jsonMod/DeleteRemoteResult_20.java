package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

public class DeleteRemoteResult_20 extends BaseJsonModule<Object> {

	private static final int CODE_DOWN = 20;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			deleteRemoteResult(baseJsonObj);
		}
	}

	private void deleteRemoteResult(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		@SuppressWarnings("unused")
		MyJsonObj1 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj1) {
			myjsonObj = (MyJsonObj1) baseJsonObj;
			notifyListeners(true, 1);
		} else {
			System.out.println("逻辑错误");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getData() {
		return null;
	}

}
