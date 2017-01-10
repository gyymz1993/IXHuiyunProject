package com.ixhuiyunproject.huiyun.ixconfig.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * @Package com.huiyun.ixconfig.bean
 * @Description: 
 * @author Yangshao
 * @date 2015年1月6日 下午5:17:47
 * @version V1.0
 */
@Table(name = "scene", execAfterTableCreated = "CREATE UNIQUE INDEX index_scene_name ON scene(scene_name,phone_code)")
public class SceneItem extends EntityBase {

	/**
	 * 场景名称
	 */
	@Column(column = "scene_name")
	private String scene_name;

	/**
	 * phonecode码
	 */
	@Column(column = "phone_code")
	private int phone_code;

	/**
	 * 状态
	 */
	@Column(column = "state")
	private int state;

	/**
	 * 场景所对应的图标
	 */
	@Column(column = "image_bg")
	private int image_bg;

	public String getScene_name() {
		return scene_name;
	}

	public void setScene_name(String scene_name) {
		this.scene_name = scene_name;
	}

	public int getPhone_code() {
		return phone_code;
	}

	public void setPhone_code(int phone_code) {
		this.phone_code = phone_code;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getImage_bg() {
		return image_bg;
	}

	public void setImage_bg(int image_bg) {
		this.image_bg = image_bg;
	}

	
	 /**
	 * @param scene_name
	 * @param phone_code
	 * @param state
	 * @param image_bg
	 */
	public SceneItem(String scene_name, int phone_code, int state, int image_bg) {
		super();
		this.scene_name = scene_name;
		this.phone_code = phone_code;
		this.state = state;
		this.image_bg = image_bg;
	}

	
	 /**
	 */
	public SceneItem() {
		super();
	}

	@Override
	public String toString() {
		return "Scene [scene_name=" + scene_name + ", phone_code=" + phone_code
				+ ", state=" + state + ", image_bg=" + image_bg + "]";
	}

	
	public boolean equals(Object obj) {  
	      if(obj instanceof SceneItem){  
	    	  SceneItem st=(SceneItem) obj;  
	            return (scene_name.equals(st.scene_name));  
	        }else{  
	            return super.equals(obj);  
	        }  
	    }  
	  
	    @Override  
	   public int hashCode() {  
	        return scene_name.hashCode();  
	    }  

}
