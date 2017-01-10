package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.content.SharedPreferences;

/**SharePerference工具
 * @author torah
 *
 */
public class SpUtils {
	public static final String SP_IKEY_AREAID = "areaid";
	public static final String SP_SKEY_SPID = "spid";
	public static final String SP_BKEY_LOGIN = "login";
	public static final String key_remember_me = "remember_me";
	public static final String key_username = "user";
	public static final String key_password = "password";
	public static final String key_language = "language";
	private static SharedPreferences sharedPreferences = UIUtils.getContext()
			.getSharedPreferences("config", 0);

	/**
	 * 得到配置文件
	 * @return
	 */
	public static SharedPreferences getSp() {
		return sharedPreferences;
	}

	/**保存string值到sp文件
	 * @param key
	 * @param value
	 */
	public static void saveString(String key, String value) {
		sharedPreferences.edit().putString(key, value).commit();
	}

	/**保存boolean键值到sp文件
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(String key, boolean value) {
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**得到boolean值
	 * @param key
	 * @param defValue
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * 取字符串，默认“”
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(String key) {
		return sharedPreferences.getString(key, "");
	}

}
