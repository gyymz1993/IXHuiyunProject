package com.ixhuiyunproject.huiyun.ixconfig.bean;


/**
 * 用户
 * @author lzy_torah
 *
 */
public class User {
	public int TYPE_ADMIN=1;
	public int TYPE_USER=2;
	
	private String name;
	private String password;
	private int type;   //1管理员  2普通用户
	private String token;
	/**
	 * @return 获得 token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token 设置 token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/* （非 Javadoc）
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [name=" + name + ", password=" + password + ", type="
				+ type + ", token=" + token + "]";
	}
	
	
	
}
