package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

public class SettingRelationResult_26 extends BaseJsonModule<Object> {

	private static final int CODE_SET_RELATION = 26;
	
	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_SET_RELATION == baseJsonObj.getCode()) {
			settingRelation(baseJsonObj);
		}
	}

	private void settingRelation(BaseJsonObj baseJsonObj){
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
		} else {
			notifyListeners(true, 1);
		}
	}
	
	@Override
	public <K> K getData() {
		return null;
	}

}
