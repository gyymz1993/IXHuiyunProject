package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import android.os.Handler;
import android.os.Message;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ContrlHandler extends Handler {

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case 1:
			Message message = Message.obtain();
			message.what = 2;
			this.sendMessageDelayed(message, 0);
			break;
		case 2:
			contrlDevice() ;
			sendMessage();
			break;

		default:
			break;
		}
	}

	public void sendMessage() {
		Message message = Message.obtain();
		message.what = 1;
		this.sendMessageDelayed(message, 5000);
	}

	/**
	 * @param holder
	 */
	public void contrlDevice() {
		List<OutDevice> devices = DeviceManager.outDeviceList;
		System.out.println("DeviceManager.outDeviceList"+DeviceManager.outDeviceList.size());
		System.out.println("StaticValue.user"+ StaticValue.user);
		OutDevice device = null;
		if(devices.size()!=0){
			int number = new Random().nextInt(devices.size());
			device = devices.get(number);
		}
		final MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		final Map<String, String> map = new HashMap<String, String>();
		jsonobj.code = 27;
		jsonobj.obj = FinalValue.OBJ_MASTER;
		map.put("token", StaticValue.user.getToken());
		// 窗帘
		int state = device.getState();
		if (state == 1) {
			map.put("state", 0 + "");
		} else {
			map.put("state", 1 + "");
		}
		map.put("phoneCode", device.getPhoneCode() + "");
		jsonobj.setData(map);
		NetJsonUtil.getInstance().addCmdForSend(jsonobj);
	}
}
