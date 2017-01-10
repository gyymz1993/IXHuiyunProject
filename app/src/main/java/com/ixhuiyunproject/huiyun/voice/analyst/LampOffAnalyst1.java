package com.ixhuiyunproject.huiyun.voice.analyst;

import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Pattern;

public class LampOffAnalyst1 extends BaseAnalyst<Object>{

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^关灯.*");
	}

	@Override
	public void execute() {
		int index = getSentence().indexOf("关灯");
		String DeviceName = getSentence().substring(index + 2,
				Math.min(index + 7, getSentence().length()));
		//int code=RedCodeUtils.code(DeviceName);
		//DeviceName+=code;
		OutDevice d = SendJsonUtil.judgeOutDeviceExist(DeviceName);
		if (d != null) {
			SendJsonUtil.sendLampControlCmd(d, 0);
		} else {
			LogUtils.e("未找到设备");
		}
	}
	

}
