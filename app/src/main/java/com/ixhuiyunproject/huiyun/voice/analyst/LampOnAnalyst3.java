package com.ixhuiyunproject.huiyun.voice.analyst;


import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.SendJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import java.util.regex.Pattern;

public class LampOnAnalyst3 extends BaseAnalyst<Object>{

	@Override
	protected Pattern initPattern() {
		return Pattern.compile("^.*开灯");
	}

	@Override
	public void execute() {
		int index = getSentence().indexOf("开灯");
		String DeviceName =  getSentence().substring(Math.max(index - 5, 0),
				index);
		//int code=RedCodeUtils.code(DeviceName);
	//	DeviceName+=code;
		OutDevice d = SendJsonUtil.judgeOutDeviceExist(DeviceName);
		if (d != null) {
			SendJsonUtil.sendLampControlCmd(d, 1);
		} else {
			LogUtils.e("未找到设备");
		}
	}
	

}
