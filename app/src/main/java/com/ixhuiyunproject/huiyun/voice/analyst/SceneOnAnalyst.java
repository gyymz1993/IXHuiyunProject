package com.ixhuiyunproject.huiyun.voice.analyst;


import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Pattern;

/**
 * 场景生效
 * @author torah
 *
 */
public class SceneOnAnalyst extends BaseAnalyst<Object> {

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^场景.*");
	}
	@Override
	public void execute() {
		int index = getSentence().indexOf("场景");
		String sceneName=getSentence().substring(index + 2,
						Math.min(index + 7, getSentence().length()));
		LogUtils.i("取出的场景名" + sceneName);
		
		SceneItem scene = SendJsonUtil.judgeSceneItemExist(sceneName);
		if (scene != null) {
			SendJsonUtil.sendSceneControlCmd(scene);
		} else {
			LogUtils.e("未找到场景");
		}
	}

}
