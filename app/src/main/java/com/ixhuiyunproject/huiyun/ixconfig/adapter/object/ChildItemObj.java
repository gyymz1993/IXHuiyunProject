package com.ixhuiyunproject.huiyun.ixconfig.adapter.object;

/**
 * 用于AddToAreaListAdapter,作为折叠列表的子项
 * @author lzn
 *
 */
public class ChildItemObj {
	public boolean checked;
	public String area;
	public String name;
	public int address;
	public int number;
	public int phoneCode;
	public int type;
	
	public ChildItemObj(){
		
	}
	
	public ChildItemObj(boolean checked, String name){
		this.checked = checked;
		this.name = name;
	}
}
