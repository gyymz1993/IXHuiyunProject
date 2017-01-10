package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.Map;

/**
 * @Title: ContrlSceneResult_54.java
 * @Package com.huiyun.ixconfig.net.jsonMod
 * @Description: 控制场景
 * @author Yangshao
 * @date 2015年1月26日 上午9:13:57
 * @version V1.0
 */
public class ContrlSceneResult_56 extends BaseJsonModule<String> {

	private final int CONTRL_SCENE = 56;
	String sceneName;

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& CONTRL_SCENE == baseJsonObj.getCode()) {
			contrlEareResult(baseJsonObj);
		}
	}

	/**
	 * Function: 控制场景
	 * 
	 * @author 2015年1月26日 上午9:15:09
	 * @param baseJsonObj
	 */
	private void contrlEareResult(BaseJsonObj baseJsonObj) {
		try {
			MyJsonObj1 jsonObj = null;
			if (baseJsonObj instanceof MyJsonObj1)
				jsonObj = (MyJsonObj1) baseJsonObj;
			else
				return;
			Map<String, String> map = jsonObj.getData();
			sceneName = map.get("scene_name").toString();
			if (baseJsonObj.result != 1) {
				notifyListeners(false, sceneName);
				return;
			} else {
				notifyListeners(true, sceneName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			notifyListeners(false, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getData() {
		return null;
	}

}
