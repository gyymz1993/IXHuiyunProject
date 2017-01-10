package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;

import java.util.List;

/**
 * @Description: 所有区域下载
 * @date 2015年1月10日 下午5:43:06
 * @version V1.0
 */
public class TimerTaskResult_68 extends BaseJsonModule<Integer> {

	private final int TIME_TASK = 68;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& TIME_TASK == baseJsonObj.getCode()) {
			timerTaskResult(baseJsonObj);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getData() {
		return null;
	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 上午9:53:18
	 * @param jsonObj
	 * @param socketId
	 */
	private void timerTaskResult(BaseJsonObj jsonObj) {
		if (FinalValue.OBJ_PHONE.equals(jsonObj.obj)
				&& TIME_TASK == jsonObj.code) {
			if (jsonObj.result != 1) {
				notifyListeners(false, null);
				return;
			}
			notifyListeners(true, null);
		}
	}

}
