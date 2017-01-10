package com.ixhuiyunproject.huiyun.ixconfig;

import android.graphics.Color;

import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;


/**
 *	存放全局的变量
 * @author lzy_torah
 *
 */
public class StaticValue {
	public static int FAMILY_PAN_KEY=1;
	/**主机 */
	public static OutDevice MASTER=null;
	/** 标示软件运行状态，一些全局循环线程的开闭 */
	private static boolean isRunning=true;
	/** 是否是远程登陆 */
	private static boolean isRemote=false;
	public static boolean isRemote() {
		return isRemote;
	}

	public static void setRemote(boolean isRemote) {
		StaticValue.isRemote = isRemote;
		NetJsonUtil.getInstance().closeOtherState(isRemote);
	}

	public static boolean isRunning(){
		return isRunning;
	}
	
	public static void setRunning(boolean isRunning){
		StaticValue.isRunning = isRunning;
		if(isRunning == false){
			NetJsonUtil.getInstance().closeAllSocket();
		}
	}

	//储存User
	public static User user;
	public static int COLOR=Color.BLACK;
	
}
