package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * @Description: 通知主机开启红外学习
 */
public class Start_InfraredResult_10 extends BaseJsonModule<RedRay> {

	private static final int CODE_START = 10;
	private RedRay rayCode;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {

		try {
			if (FinalValue.OBJ_PHONE.equals(baseJsonObj.obj)
					&& CODE_START == baseJsonObj.code) {
				if (baseJsonObj.result != 1) {
					notifyListeners(false, null);
					return;
				}
				if (baseJsonObj instanceof MyJsonObj1) {
					MyJsonObj1 myJsonObj = (MyJsonObj1) baseJsonObj;
					Map<String, String> map = myJsonObj.getData();
					rayCode = new RedRay();
					rayCode.setBtn_code(Integer.valueOf(map.get("btn_code")));
					rayCode.setPageType(Integer.valueOf(map.get("pageType")));
					rayCode.setR_name(map.get("r_name"));
					notifyListeners(true, rayCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RedRay getData() {
		if (rayCode != null)
			return rayCode;
		return null;
	}

}