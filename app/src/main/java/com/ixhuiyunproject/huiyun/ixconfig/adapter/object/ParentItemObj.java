package com.ixhuiyunproject.huiyun.ixconfig.adapter.object;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于AddToAreaListAdapter,作为折叠列表的父项
 * @author lzn
 *
 */
public class ParentItemObj {
	public String details;
	public List<ChildItemObj> children = new ArrayList<ChildItemObj>();
	
	public ParentItemObj(){
		children = new ArrayList<ChildItemObj>();
	}
	
	public ParentItemObj(String details, List<ChildItemObj> children){
		this.details = details;
		this.children = children;
	}
}
