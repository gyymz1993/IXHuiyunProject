package com.ixhuiyunproject.huiyun.ixconfig.fragment;


import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.bean.TimerTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 所有Fragment 的公共值
 */
public class FragmentContainer {
	/**
	 * 存储 所有 AREA
	 */
	public static List<String> AREAS_LIST = new ArrayList<String>();
	/**
	 * 所有场景
	 */
	public static List<SceneItem> SCENE_LIST = new ArrayList<SceneItem>();

	/**
	 * 所有的定时任务
	 */
	public static List<TimerTask> TASK_LIST = new ArrayList<TimerTask>();

	/**
	 * 所有遥控器
	 */
	public static List<RedRay> remoteControllerList = new ArrayList<RedRay>();

	/**
	 * 添加到remoteControllerList
	 * 
	 * @param redRayCode
	 */
	public static void addToRemoteControllerList(RedRay redRayCode) {
		synchronized (remoteControllerList) {
			remoteControllerList.add(redRayCode);
		}
		FragmentFactory.redRayManager.flushData();// 刷新数据
	}

	public static void removeRemoteControllerList(RedRay redRayCode) {
		synchronized (remoteControllerList) {
			remoteControllerList.remove(redRayCode);
		}
		FragmentFactory.redRayManager.flushData();// 刷新数据
	}
}
