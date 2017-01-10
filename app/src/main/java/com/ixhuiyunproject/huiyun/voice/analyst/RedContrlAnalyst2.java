package com.ixhuiyunproject.huiyun.voice.analyst;

import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Pattern;

/**开灯
 * @author torah
 *
 */
public class RedContrlAnalyst2 extends BaseAnalyst<Object> {

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^遥.*控");
	}

	@Override
	public void execute() {
		int index = getSentence().indexOf("场景");
		String redName = getSentence().substring(Math.max(index - 5, 0),
				index);

		LogUtils.i("取出的遥控名字" + redName);
		//String temp=getSentence().substring(getSentence().length()-1);
		int code=RedCodeUtils.code(redName);
		System.out.println("按键码"+code);
		LogUtils.i("取出的遥控名字" + redName);
		
		RedRay scene = SendJsonUtil.judgeRedRayExist(redName);
		if (scene != null&&code!=-1) {
			scene.setBtn_code(code);
			SendJsonUtil.sendRedContrlCmd(scene);
		} else {
			LogUtils.e("未找到遥控");
		}
	}

}
