package com.ixhuiyunproject.huiyun.ixconfig.bean;

/**
 * @Description: 红外码 r_name和btn_code合并非重
 * @date 2015年4月22日 下午2:12:59
 * @version V1.0
 */
public class RedRay extends EntityBase {
	/**
	 * 页面上按键的代号
	 */
	private int btn_code;

	/**
	 * 名称 空调等
	 */
	private String r_name;

	/**
	 * 页面样式
	 */
	private int pageType;

	/**
	 * 红外转发器的地址
	 */
	private int d_address;

	/**
	 * 红外转发器的记录号 int(0-217)
	 */
	private int d_code;

	public int getBtn_code() {
		return btn_code;
	}

	public void setBtn_code(int btn_code) {
		this.btn_code = btn_code;
	}

	public String getR_name() {
		return r_name;
	}

	public void setR_name(String r_name) {
		this.r_name = r_name;
	}

	public int getPageType() {
		return pageType;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public int getD_address() {
		return d_address;
	}

	public void setD_address(int d_address) {
		this.d_address = d_address;
	}

	public int getD_code() {
		return d_code;
	}

	public void setD_code(int d_code) {
		this.d_code = d_code;
	}

	public RedRay(int btn_code) {
		super();
		this.btn_code = btn_code;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((r_name == null) ? 0 : r_name.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedRay other = (RedRay) obj;
		if (r_name == null) {
			if (other.r_name != null)
				return false;
		} else if (!r_name.equals(other.r_name))
			return false;
		return true;
	}

	/**
	 */
	public RedRay() {
		super();
	}

	/**
	 * @param btn_code
	 * @param r_name
	 * @param pageType
	 * @param d_address
	 * @param d_code
	 */
	public RedRay(int btn_code, String r_name, int pageType, int d_address,
			int d_code) {
		super();
		this.btn_code = btn_code;
		this.r_name = r_name;
		this.pageType = pageType;
		this.d_address = d_address;
		this.d_code = d_code;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RedRay [btn_code=" + btn_code + ", r_name=" + r_name
				+ ", pageType=" + pageType + ", d_address=" + d_address
				+ ", d_code=" + d_code + "]";
	}

}
