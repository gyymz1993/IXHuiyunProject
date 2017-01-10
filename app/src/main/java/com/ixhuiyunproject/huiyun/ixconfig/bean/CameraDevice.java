package com.ixhuiyunproject.huiyun.ixconfig.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * 摄像机设备
 * @author lzy_torah
 *
 */
@Table(name = "cameraDevice")
public class CameraDevice extends EntityBase {

	/** 在线 */
	public static final int state_connected = 4;
	/** 运行中 */
	public static final int state_play = 5;
	/** 0:没有连接成功 */
	public static final int tag_notConn = 0;
	/** 1:连接成功 */
	public static final int tag_conncted = 1;
	/** 2:连接中 */
	public static final int tag_conning = 2;
	/** 3:播放中 */
	public static final int tag_playing = 3;

	/** mac码 */
	@Transient
	String mac;
	/** 摄像机所在区域名称 */
	@Column(column = "name")
	String name = "摄像头";
	/** 摄像机id */
	@Column(column = "did")
	String did;
	/** 用户名 */
	@Transient
	String user = "admin";
	/** 密码 */
	@Transient
	String psw = "888888";

	/** 连接状态 
	 * @deprecated
	 * */
	int state;
	/** 当前的状态 */
	private int tag;

	/**	0:没有连接成功  tag_notConn = 0;
	 *  1:连接成功 tag_conncted = 1;
		2:连接中  tag_conning = 2;
		3:播放中 tag_playing = 3;
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * 请保证在主线程中调用
	 * 	0:没有连接成功  tag_notConn = 0;
	 *  1:连接成功 tag_conncted = 1;
		2:连接中  tag_conning = 2;
		3:播放中 tag_playing = 3;
	 * @param tag
	 */
	public void setTag(final int tag) {
		this.tag = tag;
	}

	/**
	 * @return
	 * @deprecated
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 * @deprecated
	 */
	public void setState(int state) {
		this.state = state;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((did == null) ? 0 : did.hashCode());
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
		CameraDevice other = (CameraDevice) obj;
		if (did == null) {
			if (other.did != null)
				return false;
		} else if (!did.equals(other.did))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CameraDevice [mac=" + mac + ", name=" + name + ", did=" + did
				+ ", user=" + user + ", psw=" + psw + ", tag=" + tag + "]";
	}

}
