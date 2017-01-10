package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
public class SetPanKeyResult_76 extends BaseJsonModule<Object> {
	private final int SET_PANKEY = 76;
	String sceneName;
	@SuppressWarnings("unused")
	private MyJsonObj1 jsonObj;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& SET_PANKEY == baseJsonObj.getCode()) {
			setPankeyReturn(baseJsonObj);
		}
	}

	/**
	 * 设置pan key的返回
	 * @param baseJsonObj
	 */
	private void setPankeyReturn(BaseJsonObj baseJsonObj) {
		try {
			jsonObj = null;
			if (baseJsonObj instanceof MyJsonObj1)
				jsonObj = (MyJsonObj1) baseJsonObj;
			else
				return;
			if (baseJsonObj.result != 1) {
				notifyListeners(false, null);
				return;
			} else {
				notifyListeners(true, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getData() {
		return null;
	}

}
