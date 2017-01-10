package com.ixhuiyunproject.huiyun.ixconfig.bean;//package com.huiyun.ixconfig.bean;
//
///**
// * 红外线码
// * 
// * @author lzy_torah
// * 
// */
//@Deprecated
//public class RedRayCode extends EntityBase {
//
//	/** 页面型号 */
//	private int pageType;
//	/**
//	 * 功能代号
//	 */
//	private int function_code;
//	/**
//	 * 名称 空调等
//	 */
//	private String device_disc;
//
//	/**
//	 * @return 获得 device_disc
//	 */
//	public String getDevice_disc() {
//		return device_disc;
//	}
//
//	/**
//	 * @param device_disc
//	 *            设置 device_disc
//	 */
//	public void setDevice_disc(String device_disc) {
//		this.device_disc = device_disc;
//	}
//
//	public RedRayCode(int function_code) {
//		super();
//		this.function_code = function_code;
//	}
//
//	public RedRayCode() {
//		super();
//	}
//
//	public int getFunction_code() {
//		return function_code;
//	}
//
//	public void setFunction_code(int function_code) {
//		this.function_code = function_code;
//	}
//
//	public int getPageType() {
//		return pageType;
//	}
//
//	public void setPageType(int pageType) {
//		this.pageType = pageType;
//	}
//
//	public boolean equals(Object obj) {
//		if (obj instanceof RedRayCode) {
//			RedRayCode st = (RedRayCode) obj;
//			return (device_disc.equals(st.device_disc));
//		} else {
//			return super.equals(obj);
//		}
//	}
//
//	@Override
//	public int hashCode() {
//		return device_disc.hashCode();
//	}
//
//	@Override
//	public String toString() {
//		return "RedRayCode [pageType=" + pageType + ", function_code="
//				+ function_code + ", device_disc=" + device_disc + "]";
//	};
//
//}
