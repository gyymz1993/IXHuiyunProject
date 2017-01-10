package com.ixhuiyunproject.huiyun.ixconfig.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;

@Table(name = "predevice", execAfterTableCreated = "CREATE UNIQUE INDEX index_chipId ON predevice(chipId,number)")
public class OutDevice extends EntityBase implements DeviceCategory ,Serializable,Parcelable{
	
	private static final long serialVersionUID = 1L;  
	@Column(column = "area")
	private String area;// 区域
	@Column(column = "name")
	private String name;// 名字，用户可自定义的名称
	@Column(column = "number")
	private int number = 0;// 编号
	@Column(column = "details")
	private String details;// 大类，主机自动设置的名称
	@Column(column = "address")
	private int address;// 设备地址
	@Column(column = "type")
	private int type;// 输入设备1，风机2,主机3,手机4，红外转发5,射频转发6，带继电器灯7.网关输出12.//所有可以当输出设备的要以2结尾，如12,22等
	@Column(column = "phoneCode")
	private int phoneCode;// 对应的手机按键码，只有输出设备有，输入设备为0
	@Transient
	private boolean checked;// 在组合页面使用，储存是否被选中的状态

	@Transient
	private int state;

	/**
	 * Function: 获取device的状态(1表示开，0表示关)
	 * 
	 * @author lzn 2015-1-15 下午1:12:07
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(int phoneCode) {
		this.phoneCode = phoneCode;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public OutDevice() {
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
		result = prime * result + phoneCode;
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
		OutDevice other = (OutDevice) obj;
		if (phoneCode != other.phoneCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OutDevice [area=" + area + ", name=" + name + ", number="
				+ number + ", details=" + details + ", address=" + address
				+ ", type=" + type + ", phoneCode=" + phoneCode + ", checked="
				+ checked + ", state=" + state + "]";
	}
	
	 private OutDevice(Parcel parcel){  
			area = parcel.readString();  
			name     = parcel.readString();    
			number = parcel.readInt();  
			details     = parcel.readString(); 
			address = parcel.readInt();  
			type     = parcel.readInt(); 
			phoneCode = parcel.readInt();  
			state     = parcel.readInt(); 
	    }  
	    @Override  
	    public void writeToParcel(Parcel dest, int flags) {  
	        dest.writeString(area);  
	        dest.writeString(name);  
	        dest.writeInt(number);  
	        dest.writeString(details);  
	        dest.writeInt(address);  
	        dest.writeInt(type);  
	        dest.writeInt(phoneCode);  
	        dest.writeInt(state);  
	    }  
	    public static final Creator<OutDevice> CREATOR = new Creator<OutDevice>() {
	  
	        @Override  
	        public OutDevice createFromParcel(Parcel source) {  
	            return new OutDevice(source);  
	        }  
	  
	        @Override  
	        public OutDevice[] newArray(int size) {  
	            return new OutDevice[size];  
	        }  
	    };

		@Override
		public int describeContents() {
			return 0;
		}  
		 
	    public static long getSerialversionuid() {  
	        return serialVersionUID;  
	    }  

}
