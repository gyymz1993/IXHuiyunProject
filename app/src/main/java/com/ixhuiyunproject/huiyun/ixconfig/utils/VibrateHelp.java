package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.content.Context;
import android.os.Vibrator;

import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;

/**
 * 震动帮助类 androidManifest.xml中加入 以下权限 <uses-permission
 * android:name="android.permission.VIBRATE" />
 */
public class VibrateHelp {
	private static Vibrator vibrator;

	/**
	 * 简单震动
	 * 
	 * @param context
	 *            调用震动的Context
	 * @param millisecond
	 *            震动的时间，毫秒
	 */
	@SuppressWarnings("static-access")
	public static void vSimple(Context context, int millisecond) {
		if (SpUtils.getBoolean(Sp_Key.Logo_Vibrate, true)) {
			vibrator = (Vibrator) context
					.getSystemService(context.VIBRATOR_SERVICE);
			vibrator.vibrate(millisecond);
		}

	}

	/**
	 * 复杂的震动
	 * 
	 * @param context
	 *            调用震动的Context
	 * @param pattern
	 *            震动形式
	 * @param repeate
	 *            震动的次数，-1不重复，非-1为从pattern的指定下标开始重复
	 */
	@SuppressWarnings("static-access")
	public static void vComplicated(Context context, long[] pattern, int repeate) {
		vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, repeate);
	}

	/**
	 * 停止震动
	 */
	public static void stop() {
		if (vibrator != null) {
			vibrator.cancel();
		}
	}
}