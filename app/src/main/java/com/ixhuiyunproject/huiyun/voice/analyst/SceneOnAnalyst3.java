package com.ixhuiyunproject.huiyun.voice.analyst;


import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Pattern;

/**
 * 场景生效
 * 
 * @author torah
 * 
 */
public class SceneOnAnalyst3 extends BaseAnalyst<Object> {

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^.*模式");
	}

	@Override
	public void execute() {
		int index = getSentence().indexOf("模式");
		String sceneName = getSentence().substring(Math.max(index - 5, 0),
				index);

		LogUtils.i("取出的场景名" + sceneName);
		SceneItem scene = SendJsonUtil.judgeSceneItemExist(sceneName);
		if (scene != null) {
			SendJsonUtil.sendSceneControlCmd(scene);
		} else {
			LogUtils.e("未找到场景");
		}
	}
}
