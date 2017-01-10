package com.ixhuiyunproject.huiyun.ixconfig.utils;

public class FileLogUtil {
	static String fileStr = FileUtils
			.getWritableFile("huiyun", "hunyunlog.txt");

	/**将信息写入文件
	 * @param obj
	 */
	public static void fileLog(Object obj) {
		if (obj == null) {
			writeToFile("null");
		} else {
			writeToFile(obj.toString());
		}
	}

	private static void writeToFile(String string) {
		FileUtils.writeFile(string, fileStr, false);
	}

}
