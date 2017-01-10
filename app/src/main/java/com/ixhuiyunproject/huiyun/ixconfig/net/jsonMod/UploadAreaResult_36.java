package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

import java.util.List;
import java.util.Map;

public class UploadAreaResult_36 extends
		BaseJsonModule<Object> {

	private static final int CODE_DOWN = 36;
	@SuppressWarnings("unused")
	private List<Map<String, String>> list;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			uploadAreaResult(baseJsonObj);
		}
	}

	private void uploadAreaResult(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		@SuppressWarnings("unused")
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
			/*list = myjsonObj.data.list;
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).get("area"));
			}*/
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
