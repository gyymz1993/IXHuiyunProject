package com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod;

import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: DownloadAllSceneResult_50.java
 * @Package com.huiyun.ixconfig.net.jsonMod
 * @Description: 下载所有场景
 * @author Yangshao
 * @date 2015年1月26日 上午9:14:49
 * @version V1.0
 */
public class DownloadAllSceneResult_52 extends BaseJsonModule<List<SceneItem>> {

	private final int DOWN_SCENE = 52;
	List<Map<String, String>> keymaps;
	List<SceneItem> scenes = new ArrayList<SceneItem>();

	@Override
	public void handleCMDdata(BaseJsonObj baseJsonObj) {
		if (FinalValue.OBJ_PHONE.equals(baseJsonObj.getObj())
				&& DOWN_SCENE == baseJsonObj.getCode()) {
			downSceneResult(baseJsonObj);
		}
	}

	/**
	 * Function: 下载所有场景返回
	 * 
	 * @author 2015年1月26日 上午9:14:30
	 * @param baseJsonObj
	 */
	private void downSceneResult(BaseJsonObj baseJsonObj) {
		scenes.clear();
		if (baseJsonObj.result != 1) {
			notifyListeners(false, null);
			return;
		}
		MyJsonObj2 myjsonObj = null;
		if (baseJsonObj instanceof MyJsonObj2) {
			myjsonObj = (MyJsonObj2) baseJsonObj;
		} else
			return;
		keymaps = myjsonObj.data.list;
		for (Map<String, String> map : keymaps) {
			SceneItem item=new SceneItem();
			item.setScene_name(map.get("scene_name"));
			item.setImage_bg(Integer.valueOf(map.get("image_bg")));
			scenes.add(item);
		}
		if (scenes.size() != 0) {
			notifyListeners(true, scenes);
		} else {
			notifyListeners(false, null);
		}
	}

	@Override
	public <K> K getData() {
		return null;
	}

}
