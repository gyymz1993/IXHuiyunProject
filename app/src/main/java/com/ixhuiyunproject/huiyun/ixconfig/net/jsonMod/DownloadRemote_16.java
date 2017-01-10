package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.MyJsonObjRedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;

import java.util.List;

/**
 * @Description: 下载所有遥控器
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class DownloadRemote_16 extends BaseJsonModule<List<RedRay>> {

	private final int DOWN_REMOTO = 16;
	private List<RedRay> rayCodes;
	RedRay redCode = new RedRay();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& DOWN_REMOTO == baseJsonObj.getCode()) {
			downloadRemote(baseJsonObj);

		}
	}

	/**
	 * Function: 下载遥控器
	 * 
	 * @author Howard DateTime 2015年1月15日 下午7:59:03
	 * @param baseJsonObj
	 *            {device_disc:xxx,pageType:int},
	 */
	private void downloadRemote(BaseJsonObj baseJsonObj) {
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		try {
			MyJsonObjRedRay jsonObj = null;
			if (baseJsonObj instanceof MyJsonObjRedRay)
				jsonObj = (MyJsonObjRedRay) baseJsonObj;
			else
				return;
			rayCodes = jsonObj.data.list;
			notifyListeners(true, rayCodes);
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RedRay> getData() {
		return rayCodes;
	}

}
