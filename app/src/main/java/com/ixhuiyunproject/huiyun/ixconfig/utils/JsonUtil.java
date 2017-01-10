package com.ixhuiyunproject.huiyun.ixconfig.utils;

import com.google.gson.Gson;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.MyJsonObjRedRay;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj3;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析json的工具类，用于艾享智能家居的通信模块
 * 
 * 
 * @author lzy_torah
 * 
 */
public class JsonUtil {
	static Gson gson = new Gson();

	/**
	 * 返回一个已经设定好的JsonObj1,用于向主机发送命令。 自动设定了obj,result
	 * 
	 * @return
	 */
	public static MyJsonObj1 getAJsonObj1ForMaster() {
		MyJsonObj1 jsonobj = new MyJsonObj1();
		jsonobj.setResult(1);
		jsonobj.setObj(FinalValue.OBJ_MASTER);
		return jsonobj;
	}

	/**
	 * 返回一个已经设定好的JsonObj2,用于向主机发送命令。 自动设定了obj,result
	 * 
	 * @return
	 */
	public static MyJsonObj2 getAJsonObj2ForMaster() {
		MyJsonObj2 jsonobj = new MyJsonObj2();
		jsonobj.setResult(1);
		jsonobj.setObj(FinalValue.OBJ_MASTER);
		return jsonobj;
	}

	/**
	 * 返回一个已经设定好的JsonObj3,用于向主机发送命令。 自动设定了obj,result
	 * 
	 * @return
	 */
	public static MyJsonObj3 getAJsonObj3ForMaster() {
		MyJsonObj3 jsonobj = new MyJsonObj3();
		jsonobj.setResult(1);
		jsonobj.setObj(FinalValue.OBJ_MASTER);
		return jsonobj;
	}

	/** 所有用RedRay的码集合 */
	static List<Integer> jsonobjRedRaycodeList = new ArrayList<Integer>();
	/** MyJsonObj2：格式为List<Map<String, String>> list */
	static List<Integer> downDevice = new ArrayList<Integer>();
	static {
		jsonobjRedRaycodeList.add(14);// 下载红外码
		jsonobjRedRaycodeList.add(16);// 下载遥控器

		downDevice.add(36);
		downDevice.add(38);
		downDevice.add(22);
		downDevice.add(24);
		downDevice.add(52);
		downDevice.add(58);
		downDevice.add(64);
		downDevice.add(72);
	}

	/**
	 * 将byte数组解析成json对象。
	 * 
	 * @details 如果结果不为1，只需转成BaseJsonObj返回即可 如果逻辑为1，需要根据code码转成不同的对象
	 * @param bytes
	 * @return
	 */
	public static BaseJsonObj analyzeBytes(byte[] bytes) {
		// 1.判断非空。
		if (bytes == null)
			return null;
		// 2.bytes转string，string转json对象,返回
		BaseJsonObj myJsonObj = null;
		try {
			String jsonStr = new String(bytes, "utf-8");
			LogUtils.i("收到Json字符: " + jsonStr);
			BaseJsonObj baseJsonObj = gson.fromJson(jsonStr, BaseJsonObj.class);
			if (baseJsonObj.result == 1) {
				if (jsonobjRedRaycodeList.contains(baseJsonObj.code)) {
					return gson.fromJson(jsonStr, MyJsonObjRedRay.class);
				}
				if (downDevice.contains(baseJsonObj.code)) {
					return gson.fromJson(jsonStr, MyJsonObj2.class);
				} else {// 普通的json对象
					myJsonObj = gson.fromJson(jsonStr, MyJsonObj1.class);
				}
			} else {
				return baseJsonObj;
			}
		} catch (Exception e) {
			LogUtils.i("JsonUtil:byte转obj失败");
			e.printStackTrace();
		}
		return myJsonObj;
	}

	/**
	 * 将对象转成字符串
	 * 
	 * @param jsonobj
	 * @return
	 */
	public static byte[] jsonTobytes(BaseJsonObj jsonobj) {
		byte[] bytes = null;
		String json = gson.toJson(jsonobj);
		try {
			if (json != null)
				bytes = json.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return bytes;
	}
}
