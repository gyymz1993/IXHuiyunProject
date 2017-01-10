package com.ixhuiyunproject.huiyun.ixconfig.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于普通的Json对象
 * 
 * @author lzy_torah
 * 
 */
public class BaseJsonObj {
	public String obj;
	public int code;
	public int result;

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * 适用于data中只有普通的键值对
	 * 
	 * @author lzy_torah
	 *
	 */
	public static class MyJsonObj1 extends BaseJsonObj {
		public Map<String, String> data = new HashMap<String, String>();

		public Map<String, String> getData() {
			return data;
		}

		public void setData(Map<String, String> data) {
			this.data = data;
		}
	}

	/**
	 * 适用于data中有集合的
	 * 
	 * @author lzy_torah
	 *
	 */
	public static class MyJsonObj2 extends BaseJsonObj {
		public Data data=new Data();
		public static class Data {
			public List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			public String token;
			/**
			 * @return list
			 */
			public List<Map<String, String>> getList() {
				return list;
			}
			/**
			 * @param list 要设置的 list
			 */
			public void setList(List<Map<String, String>> list) {
				this.list = list;
			}
			/**
			 * @return token
			 */
			public String getToken() {
				return token;
			}
			/**
			 * @param token 要设置的 token
			 */
			public void setToken(String token) {
				this.token = token;
			}
		}
		/**
		 * @return data
		 */
		public Data getData() {
			return data;
		}
		/**
		 * @param data 要设置的 data
		 */
		public void setData(Data data) {
			this.data = data;
		}
		
		
	}
	
	/** 适用于data中既有普通的键值对又有集合的
	 * @author lzn
	 *
	 */
	public static class MyJsonObj3 extends BaseJsonObj {

		public Data3 data;

		public static class Data3 {
			public Map<String, String> info;
			public List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			
			public Map<String, String> getInfo() {
				return info;
			}
			public void setInfo(Map<String, String> info) {
				this.info = info;
			}
			public List<Map<String, String>> getList() {
				return list;
			}
			public void setList(List<Map<String, String>> list) {
				this.list = list;
			}
		}

		public Data3 getData() {
			return data;
		}

		public void setData(Data3 data) {
			this.data = data;
		}
	}
	
	


}
