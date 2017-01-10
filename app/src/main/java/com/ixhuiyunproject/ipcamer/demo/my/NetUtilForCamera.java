package com.ixhuiyunproject.ipcamer.demo.my;


import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.CameraDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Webcam;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetUtilForCamera {
	/**
	 * 下载摄像头信息
	 */
	public static void downLoadCamera(
			final OnResultListener<List<CameraDevice>> listener) {
		MyJsonObj1 myjsonObj = JsonUtil.getAJsonObj1ForMaster();
		if (!StaticValue.isRemote()) {// 本地
		} else {
			myjsonObj.obj = FinalValue.OBJ_SERVER;
		}
		myjsonObj.code = 63;
		myjsonObj.data.put("token", StaticValue.user.getToken());
		JSONModuleManager.getInstance().result_64
				.setOnCmdReseivedListener(new OnResultListener<List<Webcam>>() {

					@Override
					public void onResult(boolean isSecceed, List<Webcam> obj) {
						// 下载到了所有摄像头的名称
						if (isSecceed) {
							if (obj.size() > 0) {
								List<CameraDevice> cameraList = new ArrayList<CameraDevice>();
								for (Webcam cam : obj) {
									CameraDevice d = new CameraDevice();
									d.setDid(cam.getDid());
									if (cameraList.contains(d)) {
										CameraDevice cameraDevice = cameraList
												.get(cameraList.indexOf(d));
										cameraDevice.setName(cam.getArea());
									} else {
										d.setName(cam.getArea());
										cameraList.add(d);
									}
								}
								if (listener != null) {
									listener.onResult(true, cameraList);
								}
							} else {
								LogUtils.e("下载摄像头没有数据");
								if (listener != null) {
									listener.onResult(false, null);
								}
							}
							JSONModuleManager.getInstance().result_64
									.setOnCmdReseivedListener(null);
						} else {
							LogUtils.e("下载摄像头失败");
							if (listener != null) {
								listener.onResult(false, null);
							}
						}
					}
				});
		NetJsonUtil.getInstance().addCmdForSend(myjsonObj);
	}

	/**上传摄像头
	 * @param list
	 * @param listener
	 */
	public static void uploadCamera(List<CameraDevice> list,
			final OnResultListener<Object> listener) {
		// TODO
		MyJsonObj2 json2 = JsonUtil.getAJsonObj2ForMaster();
		json2.code = 61;
		json2.data.token = StaticValue.user.getToken();
		for (CameraDevice cam : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("did", cam.getDid());
			map.put("area", cam.getName());
			json2.data.list.add(map);
		}
		JSONModuleManager.getInstance().result_62
				.setOnCmdReseivedListener(new OnResultListener<Object>() {

					@Override
					public void onResult(boolean isSecceed, Object obj) {
						if (isSecceed) {
							if (listener != null) {
								listener.onResult(true, null);
							}
							JSONModuleManager.getInstance().result_62
									.setOnCmdReseivedListener(null);
						}
					}
				});
		NetJsonUtil.getInstance().addCmdForSend(json2);
	}
}
