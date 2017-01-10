package com.ixhuiyunproject.huiyun.ixconfig.bean;

/**
 * 网关输出设定
 * @author lzn
 *
 */
public class GatewayOutputInfo {
	public static final int COMM_TYPE_232 = 0;
	public static final int COMM_TYPE_485 = 1;
	public static final int COMM_TYPE_KNX = 2;
	public static final int DETAIL_TYPE_EXECUTE_PPS = 2;
	
	private int number; // 编号
	private String area; // 区域
	private String name; // 名称
	private int commuType; // 通讯类型
	private int detailType; // 详细类型
	private int executeId; // 参数1，执行器ID
	private int switchId; // 参数2，开关号
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getCommuType() {
		return commuType;
	}
	public void setCommuType(int commuType) {
		this.commuType = commuType;
	}
	public int getDetailType() {
		return detailType;
	}
	public void setDetailType(int detailType) {
		this.detailType = detailType;
	}
	public int getExecuteId() {
		return executeId;
	}
	public void setExecuteId(int executeId) {
		this.executeId = executeId;
	}
	public int getSwitchId() {
		return switchId;
	}
	public void setSwitchId(int switchId) {
		this.switchId = switchId;
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
}
