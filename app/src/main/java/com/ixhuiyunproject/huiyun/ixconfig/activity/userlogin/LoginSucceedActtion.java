package com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin;

import android.os.Message;

import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.InDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Slave;
import com.ixhuiyunproject.huiyun.ixconfig.bean.TimerTask;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.ContrlTestHandler;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.NewContrlFragment;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.scene.NewSceneFragment;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSucceedActtion {
	/**
	 * 从主机下载相应的信息
	 */
	public static void getDataFromMaster() {
		/** 下载场景 */
		downloadScene();
		/** 下载遥控器 */
		downloadRemoteController();
		/** 下载区域 */
		downloadArea();
		/** 下载输出设备 */
		downloadOutputDevice();
		if (!StaticValue.isRemote()) {
			/** 下载输入设备 */
			downloadInputDevice();
		}

		/** 状态变更通知 */
		deviceStateChangeNotification();

		downloadRemote();

		/**
		 * 下载定时任务
		 */
		downTimeTask();
	}

	/**
	 * 下载所有定时任务
	 */
	public static void downTimeTask() {
		MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		jsonobj.code = 71;
		jsonobj.obj = "master";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("token", StaticValue.user.getToken());
		jsonobj.setData(maps);
		NetJsonUtil.getInstance().addCmdForSend(jsonobj);
		JSONModuleManager.getInstance().result_72
				.setOnCmdReseivedListener(new OnResultListener<List<TimerTask>>() {
					@Override
					public void onResult(boolean isSecceed,
							final List<TimerTask> obj) {
						if (isSecceed) {
							System.out.println("下载到了定时任务" + obj.size() + "个");
							FragmentContainer.TASK_LIST = obj;

							// 把监听设置为空
							JSONModuleManager.getInstance().result_72
									.setOnCmdReseivedListener(null);
						}
					}
				});
	}

	/**
	 * 下载所有场景
	 */
	public static void downloadScene() {
		MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		jsonobj.code = 51;
		jsonobj.obj = "master";
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("token", StaticValue.user.getToken());
		jsonobj.setData(maps);

		NetJsonUtil.getInstance().addCmdForSend(jsonobj);
		System.out.println("开始下载场景");
		JSONModuleManager.getInstance().result_52
				.setOnCmdReseivedListener(new OnResultListener<List<SceneItem>>() {
					@Override
					public void onResult(boolean isSecceed,
							final List<SceneItem> obj) {
						if (isSecceed) {
							System.out.println("下载到了场景" + obj.size() + "个");
							FragmentContainer.SCENE_LIST = obj;
							UIUtils.runInMainThread(new Runnable() {
								@Override
								public void run() {
									NewSceneFragment.sendMessage(obj);
								}
							});
							// 把监听设置为空
							JSONModuleManager.getInstance().result_52
									.setOnCmdReseivedListener(null);
						}
					}
				});
	}

	/**
	 * 只初始化数据
	 */
	public static void downloadRemote() {
		JSONModuleManager.getInstance().result_16
				.setOnCmdReseivedListener(new OnResultListener<List<RedRay>>() {

					@Override
					public void onResult(boolean isSecceed, List<RedRay> obj) {
						if (isSecceed) {
							for (RedRay code : obj) {
								System.out.println("下载的"
										+ code.getR_name() + "类型"
										+ code.getPageType());
							}

							FragmentContainer.remoteControllerList = obj;
						} else {// 处理数据失败
							LogUtils.e("RedRayManager：处理收到的遥控器名失败");
						}
						JSONModuleManager.getInstance().result_16
								.setOnCmdReseivedListener(null);// 不再接收数据
					}
				});
		// 进页面先得到数据，初始化后回调
		// FragmentContainer.remoteControllerList = JSONModuleManager
		// .getInstance().result_16.getData();
	}

	/**
	 * 下载所有区域
	 */
	public static void downloadArea() {
		MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		jsonobj.code = 37;
		jsonobj.data = new HashMap<String, String>();
		jsonobj.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(jsonobj);
		JSONModuleManager.getInstance().result_38
				.setOnCmdReseivedListener(new OnResultListener<List<String>>() {
					@Override
					public void onResult(boolean isSecceed,
							final List<String> obj) {
						if (isSecceed) {
							System.out.println("下载到了区域" + obj.size() + "个");
							FragmentContainer.AREAS_LIST = obj;
							UIUtils.runInMainThread(new Runnable() {
								@Override
								public void run() {
									Message msg = Message.obtain();
									msg.what = 1;
									msg.obj = obj;
									NewContrlFragment.sendMessage(msg);
									System.out.println("下载区域更新UI");
								}
							});
							// 把监听设置为空
							JSONModuleManager.getInstance().result_38
									.setOnCmdReseivedListener(null);
						}
					}
				});
	}

	/**
	 * 下载输出设备
	 */
	public static void downloadOutputDevice() {
		// 发送下载输出设备的信息
		JSONModuleManager.getInstance().result_24
				.setOnCmdReseivedListener(new OnResultListener<List<OutDevice>>() {

					@Override
					public void onResult(boolean isSecceed,
							final List<OutDevice> allDevices) {
						LogUtils.i("下载输出设备：" + allDevices.size() + "个");
						// 挑出所有属于网关自身的设备
						List<OutDevice> allGatewaySelf = new ArrayList<OutDevice>();
						for (OutDevice dev : allDevices) {
							if (dev.getType() == OutDevice.TYPE_GATEWAY
									&& dev.getNumber() == 0) {
								allGatewaySelf.add(dev);
							}
						}
						// 保存到本地
						DeviceManager.gatewayList = allGatewaySelf;
						allDevices.removeAll(allGatewaySelf);
						DeviceManager.outDeviceList = allDevices;
						// for (OutDevice device : allDevices) {
						// System.out.println("输出设备" + device.toString());
						// }

						UIUtils.runInMainThread(new Runnable() {
							@Override
							public void run() {
								Message msg = Message.obtain();
								msg.what = 2;
								msg.obj = allDevices;
								// System.out.println("下载所有设备更新UI"+allDevices.size());
								NewContrlFragment.sendMessage(msg);
								// ContrlFragment.refreshState();

							}
						});
						// 把监听设置为空
						JSONModuleManager.getInstance().result_24
								.setOnCmdReseivedListener(null);
					}
				});
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 23;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
	}

	/**
	 * 下载输入设备
	 */
	public static void downloadInputDevice() {
		// 发送下载输入设备的信息
		JSONModuleManager.getInstance().result_22
				.setOnCmdReseivedListener(new OnResultListener<List<InDevice>>() {

					@Override
					public void onResult(boolean isSecceed, List<InDevice> obj) {
						if (isSecceed) {
							DeviceManager.inDeviceList = obj;
						}
					}
				});
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 21;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
	}

	/**
	 * 下载遥控器信息
	 */
	public static void downloadRemoteController() {
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 15;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
	}

	/**
	 * Function: 状态变更通知
	 */
	public static void deviceStateChangeNotification() {
		JSONModuleManager.getInstance().result_30
				.setOnCmdReseivedListener(new OnResultListener<OutDevice>() {
					@Override
					public void onResult(boolean isSecceed, OutDevice obj) {
						if (isSecceed) {
							if (DeviceManager.outDeviceList.contains(obj)) {
								OutDevice outDevice = DeviceManager.outDeviceList
										.get(DeviceManager.outDeviceList
												.indexOf(obj));
								if (obj.getState() != -1) {
									outDevice.setState(obj.getState());
									/**
									 * 收到返回发送数据
									 */
									UIUtils.getHandler().removeCallbacks(ContrlTestHandler.getIntance().timing); //去除延时消息
									ContrlTestHandler.getIntance().sendMessage();
								}
								UIUtils.runInMainThread(new Runnable() {
									@Override
									public void run() {
										NewContrlFragment.refreshState();
									}
								});
							} else {
								System.out.println("状态改变：未包含该设备！！");
							}
						}
					}
				});
	}


	/**
	 * 保存登录信息
	 */
	public static void saveLoginInfo(String userName, String password) {
		SpUtils.saveString("userName", userName);
		SpUtils.saveString("password", password);
	}

	/**
	 * 自动登录
	 */
	public static void autoLogin() {
		String userName = SpUtils.getString("userName");
		String password = SpUtils.getString("password");
		if (userName.trim().equals("") || password.trim().equals("")) {
			return;
		}

		// TODO 登录

	}

	/**
	 * 下载从机信息
	 */
	public static void downloadSlave() {
		// 发送下载输出设备的信息
		JSONModuleManager.getInstance().result_58
				.setOnCmdReseivedListener(new OnResultListener<List<Slave>>() {

					@Override
					public void onResult(boolean isSucceed, List<Slave> obj) {
						if (isSucceed) {
							LogUtils.i("下载从机：" + obj.size() + "个");
							// 保存到本地
							DeviceManager.slaveList = obj;
							for (Slave slave : DeviceManager.slaveList) {
								System.out.println("从机信息：" + slave.getAddr()
										+ "," + slave.getType());
							}

							// 把监听设置为空
							JSONModuleManager.getInstance().result_58
									.setOnCmdReseivedListener(null);
						}
					}
				});
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 57;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
	}
}
