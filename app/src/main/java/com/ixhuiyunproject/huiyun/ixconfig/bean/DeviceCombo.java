package com.ixhuiyunproject.huiyun.ixconfig.bean;


public class DeviceCombo extends EntityBase{
	private int currentId;// 当前组编号
	private int allCount;//共有多少组
	private boolean disable = false;// 是不是要失效，false表示不失效
	private int inStyle;
	private int inAddr;
	private int inNumber;
	private int outStyle;
	private int outAddr;
	private int outNumber;
	private OutDevice inDevice;
	private OutDevice outDevice;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + inAddr;
		result = prime * result + inNumber;
		result = prime * result + outAddr;
		result = prime * result + outNumber;
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
		DeviceCombo other = (DeviceCombo) obj;
		if (inAddr != other.inAddr)
			return false;
		if (inNumber != other.inNumber)
			return false;
		if (outAddr != other.outAddr)
			return false;
		if (outNumber != other.outNumber)
			return false;
		return true;
	}

	public OutDevice getInDevice() {
		return inDevice;
	}

	public void setInDevice(OutDevice inDevice) {
		this.inDevice = inDevice;
	}

	public OutDevice getOutDevice() {
		return outDevice;
	}

	public void setOutDevice(OutDevice outDevice) {
		this.outDevice = outDevice;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public int getCurrentId() {
		return currentId;
	}

	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}

	public int getInStyle() {
		return inStyle;
	}

	public void setInStyle(int inStyle) {
		this.inStyle = inStyle;
	}

	public int getInAddr() {
		return inAddr;
	}

	public void setInAddr(int inAddr) {
		this.inAddr = inAddr;
	}

	public int getInNumber() {
		return inNumber;
	}

	public void setInNumber(int inNumber) {
		this.inNumber = inNumber;
	}

	public int getOutStyle() {
		return outStyle;
	}

	public void setOutStyle(int outStyle) {
		this.outStyle = outStyle;
	}

	public int getOutAddr() {
		return outAddr;
	}

	public void setOutAddr(int outAddr) {
		this.outAddr = outAddr;
	}

	public int getOutNumber() {
		return outNumber;
	}

	public void setOutNumber(int outNumber) {
		this.outNumber = outNumber;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		DeviceCombo combo=	(DeviceCombo)super.clone();
		try{
			combo.setInDevice((OutDevice) inDevice.clone());
		}catch(Exception e){
		}
		try{
			combo.setOutDevice((OutDevice) outDevice.clone());
		}catch(Exception e){
		}
		return combo;
	}

	/**
	 * @return allCount
	 */
	public int getAllCount() {
		return allCount;
	}

	/**
	 * @param allCount 要设置的 allCount
	 */
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

}
