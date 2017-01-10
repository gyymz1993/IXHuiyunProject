package com.ixhuiyunproject.huiyun.ixconfig.bean;

/** 输入设备
 * @author lzn
 *
 */
public class InDevice implements DeviceCategory {
	
	private String area;// 区域
	private String name;// 名字，用户可自定义的名称
	private int number = 0;// 编号
	private String details;// 大类，主机自动设置的名称
	private int address;// 设备地址
	private int type;// 输入设备1，输出设备个位数为2,主机3,手机4，网关输出12.//所有可以当输出设备的要以2结尾，如12,22等
	

	public InDevice() {
		super();
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}


	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address;
		result = prime * result + number;
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
		InDevice other = (InDevice) obj;
		if (address != other.address)
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InDevice [area=" + area + ", name=" + name + ", number="
				+ number + ", details=" + details + ", address=" + address
				+ ", type=" + type + "]";
	}


}
