package com.ixhuiyunproject.huiyun.ixconfig.activity.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Function:
 * 
 * @author YangShao 2015年4月8日 下午1:42:20
 * @version 1.0
 */
public class WifiUtil {

	// 定义WifiManager对象
	private WifiManager mWifiManager;
	private final static String TAG = "WifiAdmin";

	public interface WifiOpenSuccess {
		void onResult(boolean isSecceed);
	}

	public WifiOpenSuccess openSuccess;

	/**
	 * 
	 */
	public WifiOpenSuccess getOpenSuccess() {
		return openSuccess;
	}

	public void setOpenSuccess(WifiOpenSuccess openSuccess) {
		this.openSuccess = openSuccess;
	}

	/**
	 * 构造方法
	 */
	public WifiUtil(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 检查当前Wifi网卡状态
	 */
	boolean success = false;

	public int checkNetCardState() {
		if (mWifiManager.getWifiState() == 0) {
			Log.i(TAG, "网卡正在关闭");
			return 0;
		} else if (mWifiManager.getWifiState() == 1) {
			Log.i(TAG, "网卡已经关闭");
			return 1;
		} else if (mWifiManager.getWifiState() == 2) {
			Log.i(TAG, "网卡正在打开");
			return 2;
		} else if (mWifiManager.getWifiState() == 3) {
			Log.i(TAG, "网卡已经打开");
			return 3;
		} else {
			Log.i(TAG, "---_---晕......没有获取到状态---_---");
			return -1;
		}
	}

	/**
	 * 打开Wifi网卡
	 */
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 转换IP地址
	 * 
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);
	}

	// 添加一个网络并连接
	public void addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(wcg);
		boolean b = mWifiManager.enableNetwork(wcgID, true);
		System.out.println("a--" + wcgID);
		System.out.println("b--" + b);
	}

	/**
	 * 配置wifi
	 * 
	 * @param SSID
	 * @param Password
	 * @param Type
	 * @return
	 */
	// 然后是一个实际应用方法，只验证过没有密码的情况：
	public WifiConfiguration createWifiInfo(String SSID, String Password,
			int Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		WifiConfiguration tempConfig = null;
		tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	private WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		if (existingConfigs != null && existingConfigs.size() != 0)
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
					return existingConfig;
				}
			}
		return null;
	}

}