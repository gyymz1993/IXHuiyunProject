package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

public class NetConnectStateUtil {

	/*** 检查网络有连接 */
	public static boolean isNetWorkConnect(Context context) {
		boolean netSataus = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			netSataus = cm.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	/*** 检查网络有效性,不要在主线程调用 */
	public static boolean isNetWorkAvailable(Context context, String urlString) {
		boolean netSataus = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (isNetWorkConnect(context)) {
			URL url;
			InetAddress iAddress = null;
			try {
				url = new URL(urlString);
				iAddress = InetAddress.getByName(url.getHost());
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return false;
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return false;
			}
			netSataus = cm.requestRouteToHost(cm.getActiveNetworkInfo()
					.getType(), ipToInt(iAddress.getHostAddress()));
		}
		return netSataus;
	}

	public static int ipToInt(String addr) {
		String[] addrArray = addr.split("\\.");
		int num = 0;
		for (int i = 0; i < addrArray.length; i++) {
			int power = 3 - i;
			num += ((Integer.parseInt(addrArray[i]) % 256 * Math
					.pow(256, power)));
		}
		return num;
	}

	/*** 检查GPS */
	public static boolean isGpsEnable(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		@SuppressWarnings("rawtypes")
		List accessibleProviders = lm.getProviders(true);
		boolean value = accessibleProviders != null
				&& accessibleProviders.size() > 0;
		return value;
	}

	/*** 检查wifi */
	public static boolean isWifiEnable(Context context) {
		ConnectivityManager connMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager telMan = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((connMan.getActiveNetworkInfo() != null && connMan
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telMan
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/*** 检查3G */
	public static boolean is3G(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	// check wifi network is or not
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null
				&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

}
