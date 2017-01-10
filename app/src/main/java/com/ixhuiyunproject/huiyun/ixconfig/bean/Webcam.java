package com.ixhuiyunproject.huiyun.ixconfig.bean;

/**
 * @Description:
 * @date 2015年2月6日 下午4:05:17
 * @version V1.0
 */
public class Webcam extends EntityBase {

	private String did;

	private String area;

	public String getDid() {
		return did;
	}

	public Webcam() {
		super();
	}

	public Webcam(String did, String area) {
		super();
		this.did = did;
		this.area = area;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Webcam [did=" + did + ", area=" + area + "]";
	}

	public boolean equals(Object obj) {
		if (obj instanceof Webcam) {
			Webcam st = (Webcam) obj;
			return (did.equals(st.did));
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		return did.hashCode();
	}

}
