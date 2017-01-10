package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import android.os.Handler;
import android.os.Message;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ContrlTestHandler extends Handler {
	
	private static ContrlTestHandler contrlTestHandler;
	public static synchronized ContrlTestHandler getIntance(){
		synchronized (ContrlTestHandler.class) {
			if(contrlTestHandler==null){
				contrlTestHandler=new ContrlTestHandler();
			}
			return contrlTestHandler;
		}
	}
	private ContrlTestHandler() {
	}
	public boolean isrecontrl=true;
	public synchronized boolean isIsrecontrl() {
			return isrecontrl;
		
	}
	public  synchronized void setIsrecontrl(boolean isrecontrl) {
		this.isrecontrl = isrecontrl;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case 2:
			contrlDevice();
			setIsrecontrl(true);
			System.out.println("sendMessage"+isrecontrl);
			UIUtils.getHandler().postDelayed(timing, 5*1000);//添加延时消息
			break;
		default:
			break;
		}
	}
	
	/**
	 * 启动计时线程 5秒没有数据返回错误
	 */
	public Thread timing = new Thread(new Runnable() {
		@Override
		public void run() {
			if (isIsrecontrl()) {
				System.out.println("没有数据返回发送");
				sendMessage();
			}
		}
	});
	
	public void sendMessage() {
		Message message = Message.obtain();
		message.what = 2;
		this.sendMessageDelayed(message, 0);
	}
	

	/**
	 */
	public void contrlDevice() {
		OutDevice device = null ;
		List<OutDevice> devices = DeviceManager.outDeviceList;
		if(devices.size()!=0){
			int number = new Random().nextInt(devices.size());
			device= devices.get(number);
		}
		final MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		final Map<String, String> map = new HashMap<String, String>();
		jsonobj.code = 27;
		jsonobj.obj = FinalValue.OBJ_MASTER;
		map.put("token", StaticValue.user.getToken());
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
