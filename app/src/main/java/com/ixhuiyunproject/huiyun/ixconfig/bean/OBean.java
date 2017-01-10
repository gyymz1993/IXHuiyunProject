package com.ixhuiyunproject.huiyun.ixconfig.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OBean implements Serializable,Parcelable {  
    private static final long serialVersionUID = 1L;  
      
    private String messageName;  
      
    private String xmlPath;  
  
    public OBean(){}  
      
    private OBean(Parcel parcel){  
        messageName = parcel.readString();  
        xmlPath     = parcel.readString();        
    }  
    @Override  
    public void writeToParcel(Parcel dest, int flags) {  
        dest.writeString(messageName);  
        dest.writeString(xmlPath);  
    }  
    public static final Creator<OBean> CREATOR = new Creator<OBean>() {
  
        @Override  
        public OBean createFromParcel(Parcel source) {  
            return new OBean(source);  
        }  
  
        @Override  
        public OBean[] newArray(int size) {  
            return new OBean[size];  
        }  
    };  
      
    public String getMessageName() {  
        return messageName;  
    }  
  
    public void setMessageName(String messageName) {  
        this.messageName = messageName;  
    }  
  
    public String getXmlPath() {  
        return xmlPath;  
    }  
  
    public void setXmlPath(String xmlPath) {  
        this.xmlPath = xmlPath;  
    }  
  
    public static long getSerialversionuid() {  
        return serialVersionUID;  
    }  
  
    @Override  
    public int describeContents() {  
        return 0;  
    }  
  
}  