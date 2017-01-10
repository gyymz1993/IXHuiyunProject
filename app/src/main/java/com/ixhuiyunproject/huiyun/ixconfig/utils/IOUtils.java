package com.ixhuiyunproject.huiyun.ixconfig.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/** 安全的关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				e.printStackTrace();
				io=null;
			}
		}
		return true;
	}
}

