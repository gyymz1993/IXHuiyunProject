package com.ixhuiyunproject.huiyun.ixconfig.bean;


import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;

public class DialogMsg {
	/** 要显示的信息 */
	private String message;
	/** 秒 */
	private int timeMills;
	/**  对话框消失后的 */
	private OnResultListener<Object> listener;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTimeMills() {
		return timeMills;
	}

	public void setTimeMills(int timeMills) {
		this.timeMills = timeMills;
	}

	public OnResultListener<Object> getListener() {
		return listener;
	}

	public void setListener(OnResultListener<Object> listener) {
		this.listener = listener;
	}
	
}
