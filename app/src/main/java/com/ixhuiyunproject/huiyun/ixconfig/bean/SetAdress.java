package com.ixhuiyunproject.huiyun.ixconfig.bean;

public class SetAdress {
	
	private int adress;
	
	private String CUPID;

	public int getAdress() {
		return adress;
	}

	public void setAdress(int adress) {
		this.adress = adress;
	}

	public String getCUPID() {
		return CUPID;
	}

	public void setCUPID(String cUPID) {
		CUPID = cUPID;
	}

	public SetAdress(int adress, String cUPID) {
		super();
		this.adress = adress;
		CUPID = cUPID;
	}

	public SetAdress() {
		super();
	}
	
	
	

}
