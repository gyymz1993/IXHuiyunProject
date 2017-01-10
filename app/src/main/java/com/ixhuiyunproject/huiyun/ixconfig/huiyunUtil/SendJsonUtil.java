package com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil;

import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.redray.BaseRayFragment;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;

/**
 * 执行命令
 * 
 * @author torah
 * 
 */
public class SendJsonUtil {

	/**
	 * 判断场景是否存在
	 * 
	 * @param sceneName
	 * @return
	 */
	public static SceneItem judgeSceneItemExist(String sceneName) {
		if (StringUtils.isEmpty(sceneName)) {
			return null;
		}
		SceneItem item = null;
		List<SceneItem> sceneList = FragmentContainer.SCENE_LIST;
		for (SceneItem scene : sceneList) {
			if (sceneName.contains(scene.getScene_name())) {
				if (item == null) {
					item = scene;
				} else {
					if (scene.getScene_name().length() > item.getScene_name()
							.length()) {
						item = scene;
					}
				}
			}
		}
		return item;
	}

	/**
	 * 判断场景是否存在
	 * 
	 * @param redName
	 * @return
	 */
	public static RedRay judgeRedRayExist(String redName) {
		if (StringUtils.isEmpty(redName)) {
			return null;
		}
		RedRay item = null;
		List<RedRay> remoteControllerList = FragmentContainer.remoteControllerList;
		for (RedRay red : remoteControllerList) {
			if (redName.contains(red.getR_name())) {
				if (item == null) {
					item = red;
				} else {
					if (red.getR_name().length() > item.getR_name().length()) {
						item = red;
					}
				}
			}
		}
		return item;
	}

	/**
	 * 场景生效
	 * 
	 * @param scene
	 */
	public static void sendSceneControlCmd(SceneItem scene) {
		ToastUtils.showToastReal(scene.getScene_name() + " 生效");
		MyJsonObj1 jsonobj1 = JsonUtil.getAJsonObj1ForMaster();
		jsonobj1.code = 55;
		jsonobj1.data.put("token", StaticValue.user.getToken());
		jsonobj1.data.put("scene_name", scene.getScene_name());
		NetJsonUtil.getInstance().addCmdForSend(jsonobj1);
	}

	public static void sendRedContrlCmd(RedRay redRay){
		MyJsonObj1 myJsonObj = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj.data = new HashMap<String, String>();
		myJsonObj.code = 11;// 控制的功能码是11
		myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_AIR+"");// 添加页面类型
		myJsonObj.data.put("r_name", redRay.getR_name());
		myJsonObj.data.put("btn_code", redRay.getBtn_code()+"");
		myJsonObj.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj);// 添加要发送的命令
	}
	/**
	 * 判断输出设备是否存在
	 * 
	 * @param deviceName
	 * @return
	 */
	public static OutDevice judgeOutDeviceExist(String deviceName) {
		OutDevice rightDevice = null;
		List<OutDevice> outDeviceList = DeviceManager.outDeviceList;
		for (OutDevice d : outDeviceList) {
			if (!StringUtils.isEmpty(d.getArea())) {// 有区域名的才生效
				if (deviceName.contains(d.getName())) {// 包含设备名称
					if (rightDevice == null) {// 第一次找到设备
						rightDevice = d;
					} else {
						if (d.getName().length() > rightDevice.getName()
								.length()) {// 第二次找到设备，
							rightDevice = d;
						}
					}
				}
			}
		}
		return rightDevice;
	}

	/**
	 * 发送设备开关命令
	 * 
	 * @param d
	 * @param state
	 *            0关 1 开
	 */
	public static void sendLampControlCmd(OutDevice d, int state) {
		ToastUtils.showToastReal(d.getName() + "：" + state);
		MyJsonObj1 jsonobj1 = JsonUtil.getAJsonObj1ForMaster();
		jsonobj1.code = 27;
		jsonobj1.data.put("token", StaticValue.user.getToken());
		jsonobj1.data.put("phoneCode", d.getPhoneCode() + "");
		jsonobj1.data.put("state", state + "");
		NetJsonUtil.getInstance().addCmdForSend(jsonobj1);
	}

	public static RedRay getRedRay() {
		return null;

	}
}
