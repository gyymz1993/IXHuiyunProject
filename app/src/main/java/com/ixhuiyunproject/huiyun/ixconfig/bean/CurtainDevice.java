package com.ixhuiyunproject.huiyun.ixconfig.bean;

/** 专门为临时窗帘控制器使用的输出设备扩展类
 * @author lzn
 *
 */
public class CurtainDevice extends OutDevice {
	private int phoneCodeSecond;
	private int numberSecond;
	private int stateSecond;
	private int currentNumber;
	
	
	public int getPhoneCodeSecond() {
		return phoneCodeSecond;
	}
	public void setPhoneCodeSecond(int phoneCodeSecond) {
		this.phoneCodeSecond = phoneCodeSecond;
	}
	public int getNumberSecond() {
		return numberSecond;
	}
	public void setNumberSecond(int numberSecond) {
		this.numberSecond = numberSecond;
	}
	public int getStateSecond() {
		return stateSecond;
	}
	public void setStateSecond(int stateSecond) {
		this.stateSecond = stateSecond;
	}
	public int getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}
}
