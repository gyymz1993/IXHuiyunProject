package com.ixhuiyunproject.huiyun.ixconfig.bean;

/**
 * 所有想自动存入数据库的类必须继承，既必须有id字段
 * 
 * @author Torah
 *
 */
public abstract class EntityBase implements Cloneable {
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
