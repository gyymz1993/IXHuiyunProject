package com.ixhuiyunproject.huiyun.label.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.CommonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.NetConnectStateUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;


/**
 * wifi和手机网络改变状态后会通知
 * @author torah
 *
 */
public class NetworkStateChangeReceiver extends BroadcastReceiver {
	private static NetState currentNetState;
	private static OnResultListener<String> listener;

	/**
	 * 设置网络状态变化后的监听
	 * @param listener
	 */
	public static void setOnNetStateChangedListener(
			OnResultListener<String> listener) {
		NetworkStateChangeReceiver.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean wifiIsOn = NetConnectStateUtil.isWifi(context);
		boolean netWorkConnect = NetConnectStateUtil.isNetWorkConnect(context);
		boolean checkNetState = CommonUtil.checkNetState(context);
		NetState newNetState = null;
		if (checkNetState && netWorkConnect) {
			if (wifiIsOn) {
				newNetState = new NetState(true, true, true);
			} else {
				newNetState = new NetState(false, true, true);
			}
		} else {
			newNetState = new NetState(false, false, false);
		}
		if (!newNetState.equals(currentNetState)) {
			currentNetState = newNetState;
			notifyListeners(newNetState);
		}
		LogUtils.i("checkNetState " + checkNetState + "；wifi " + wifiIsOn
				+ "；netWorkConnect " + netWorkConnect);
	}

	public static NetState getCurrentNetState() {
		if(currentNetState==null){
			boolean wifiIsOn = NetConnectStateUtil.isWifi(UIUtils.getContext());
			boolean netWorkConnect = NetConnectStateUtil.isNetWorkConnect(UIUtils.getContext());
			currentNetState=new NetState(wifiIsOn, netWorkConnect, true);
		}
		return currentNetState;
	}

	/**通知监听者
	 * @param NetState
	 * @param content
	 */
	private void notifyListeners(NetState newNetState) {
		if (listener == null) {
			return;
		}
		if (newNetState.isWifi) {
			if (newNetState.canPingServer)
				listener.onResult(true, "wifi,有网络");
			else
				listener.onResult(false, "wifi,无网络");
		} else {
			if (newNetState.hasNet)
				listener.onResult(true, "无wifi,有网络");
			else
				listener.onResult(false, "无wifi,无网络");
		}

	}

	/**
	 * 保存网络连接状态
	 * @author torah
	 *
	 */
	public static class NetState {
		public NetState(boolean isWifi, boolean hasNet, boolean canPingServer) {
			this.isWifi = isWifi;
			this.hasNet = hasNet;
			this.canPingServer = canPingServer;
		}
		public boolean isWifi;
		public boolean hasNet;
		public boolean canPingServer;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (canPingServer ? 1231 : 1237);
			result = prime * result + (hasNet ? 1231 : 1237);
			result = prime * result + (isWifi ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NetState other = (NetState) obj;
			if (canPingServer != other.canPingServer)
				return false;
			if (hasNet != other.hasNet)
				return false;
			if (isWifi != other.isWifi)
				return false;
			return true;
		}

	}
}
