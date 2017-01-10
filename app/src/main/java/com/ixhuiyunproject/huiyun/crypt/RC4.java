package com.ixhuiyunproject.huiyun.crypt;

import java.io.UnsupportedEncodingException;


/**   
 * @Description: RC4加密解密算法
 * @date 2015-3-5 下午1:25:48 
 * @version V1.0   
 */
public class RC4 {
	static {
		System.loadLibrary("RC4");
	}

	private static String key = "huiyunix5201314";
//	private static String key = "广州市惠芸电气科技有限公司";

	private static RC4 instance;

	public static RC4 getInstance() {
		synchronized (RC4.class) {
			if (instance == null) {
				instance = new RC4();
			}
			return instance;
		}
	}

	private RC4() {
//		keyAfterEncrypt = initKey(transStringToByteArray(key));
		try {
			keyAfterEncrypt = initKey(key.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private byte[] keyAfterEncrypt;

	public byte[] crypt(byte[] val, int len) {
		return crypt(val, keyAfterEncrypt, len);
	}

	private native byte[] initKey(byte[] key);

	private native byte[] crypt(byte[] val, byte[] key, int len);

	/** 字符串转byte数组 */
	private static byte[] transStringToByteArray(String str) {
		char[] c = str.toCharArray();
		byte[] b = new byte[c.length * 2];
		for (int i = 0; i < b.length; i = i + 2) {
			b[i] = (byte) (c[i / 2] >> 8 & 0xff);
			b[i + 1] = (byte) (c[i / 2] & 0xff);
		}
		return b;
	}

	/** byte数组转字符串 */
	private static String transByteArrayToString(byte[] b) {
		char[] c = new char[b.length / 2];
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) ((((char) b[i * 2]) << 8 & 0xff00) | ((char) b[i * 2 + 1] & 0xff));
		}
		String str = String.valueOf(c);
		return str;
	}

}
