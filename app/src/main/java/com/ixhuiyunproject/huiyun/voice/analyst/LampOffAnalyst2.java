package com.ixhuiyunproject.huiyun.voice.analyst;


import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**开灯
 * @author torah
 *
 */
public class LampOffAnalyst2 extends BaseAnalyst<Object> {

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^关.*灯");
	}

	@Override
	public void execute() {
		Matcher matcher2 = getParttern().matcher(getSentence());
		int start = matcher2.start();
		int end = Math.min(getSentence().indexOf("灯", start) + 1, getSentence()
				.length());
		OutDevice d2 = SendJsonUtil.judgeOutDeviceExist(getSentence()
				.substring(start, end));
		if (d2 != null) {
			SendJsonUtil.sendLampControlCmd(d2, 0);
		} else {
			LogUtils.e("未找到设备");
		}
	}

}
