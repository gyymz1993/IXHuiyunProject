package com.ixhuiyunproject.huiyun.ixconfig.bean;


public class TimerTask {
	public int funCode;
	public String date;
	private int phoneCode;// 瀵瑰簲鐨勬墜鏈烘寜閿爜锛屽彧鏈夎緭鍑鸿澶囨湁
	private int state;//
	private String sceneName;//

	private int isRepeat;//

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((sceneName == null) ? 0 : sceneName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimerTask other = (TimerTask) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (sceneName == null) {
			if (other.sceneName != null)
				return false;
		} else if (!sceneName.equals(other.sceneName))
			return false;
		return true;
	}

	/**
	 * @param date
	 * @param time
	 * @param phoneCode
	 * @param state
	 */
	public TimerTask(String date, int phoneCode, int state) {
		super();
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
	}

	public int getFunCode() {
		return funCode;
	}

	public void setFunCode(int funCode) {
		this.funCode = funCode;
	}

	/**
	 * @param fun_code
	 * @param date
	 * @param phoneCode
	 * @param state
	 * @param sceneName
	 */
	public TimerTask(int funcode, String date, int phoneCode, int state,
			String sceneName) {
		super();
		this.funCode = funcode;
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
		this.sceneName = sceneName;
	}

	/**
	 * @param funCode
	 * @param date
	 * @param phoneCode
	 * @param state
	 * @param sceneName
	 * @param isRepeat
	 */
	public TimerTask(int funCode, String date, int phoneCode, int state,
			String sceneName, int isRepeat) {
		super();
		this.funCode = funCode;
		this.date = date;
		this.phoneCode = phoneCode;
		this.state = state;
		this.sceneName = sceneName;
		this.isRepeat = isRepeat;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	/**
	 */
	public TimerTask() {
		super();
	}

	@Override
	public String toString() {
		return "TimerTask [funCode=" + funCode + ", date=" + date
				+ ", phoneCode=" + phoneCode + ", state=" + state
				+ ", sceneName=" + sceneName + ", isRepeat=" + isRepeat + "]";
	}

	/**
	 * @param funCode
	 * @param date
	 * @param sceneName
	 */
	public TimerTask(int funCode, String date, String sceneName) {
		super();
		this.funCode = funCode;
		this.date = date;
		this.sceneName = sceneName;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

}
