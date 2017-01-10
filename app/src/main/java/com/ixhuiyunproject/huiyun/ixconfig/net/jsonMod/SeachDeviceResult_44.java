package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

/**
 * @Description: 通知主机查找设备
 */
public class SeachDeviceResult_44 extends BaseJsonModule<Object> {

	// 注册
	private static final int CODE_NOTIF = 44;

	@Override
	public void handleCMDdata(BaseJsonObj myJsonObj) {
		try {
			if (FinalValue.OBJ_PHONE.equals(myJsonObj.obj)
					&& CODE_NOTIF == myJsonObj.code) {
				if (myJsonObj.result != 1) {
					notifyListeners(false, null);
					return;
				}
				notifyListeners(true, null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@Override
	public <K> K getData() {
		return null;
	}

}