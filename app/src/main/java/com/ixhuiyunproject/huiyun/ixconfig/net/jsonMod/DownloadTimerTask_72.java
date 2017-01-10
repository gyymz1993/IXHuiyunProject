package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.TimerTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DownloadTimerTask_72 extends BaseJsonModule<List<TimerTask>> {

	private static final int CODE_DOWN = 72;
	List<TimerTask> timerTasks = new ArrayList<TimerTask>();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CODE_DOWN == baseJsonObj.getCode()) {
			downLoadResult(baseJsonObj);
		}

	}

	/**
	 * Function:
	 * 
	 * @author Yangshao 2015年1月10日 下午3:07:03
	 * @param baseJsonObj
	 */
	private void downLoadResult(BaseJsonObj baseJsonObj) {
		timerTasks.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
		} else
			return;
		List<Map<String, String>> keymaps = myjsonObj.data.list;
		for (Map<String, String> map : keymaps) {
			TimerTask timertask = new TimerTask();
			timertask.setDate(map.get("date"));
			timertask.setFunCode(Integer.valueOf(map.get("funcode")));
			timertask.setSceneName(map.get("scene_name"));
			timertask.setIsRepeat(Integer.valueOf(map.get("isRepeat")));
			timerTasks.add(timertask);
		}
		if (timerTasks.size() != 0) {
			notifyListeners(true, timerTasks);
		} else {
			// System.out.println(devices.size());
			notifyListeners(false, null);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimerTask> getData() {
		return timerTasks;
	}

}
