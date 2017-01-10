package com.ixhuiyunproject.huiyun.ixconfig.server;

import java.util.Map;

public class JsonResult {
	private int result_code;
	private Map<String,Object> data;
	
	
	public int getResult_code() {
		return result_code;
	}
	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
