package com.ixhuiyunproject.huiyun.ixconfig.net.container;

import java.util.ArrayList;
import java.util.List;

/**
 * 该容器用于装载命令，会自动把相同的命令过滤掉
 *
 */
public class PackContainer {
	private List<byte[]> container;
	
	public PackContainer(){
		container = new ArrayList<byte[]>();
	}
	
	public List<byte[]> getContainer(){
		return container;
	}
	
	public void clear(){
		container.clear();
	}
	
	/**
	 * 添加byte数组到容器中，如果存在相同的，添加失败
	 */
	public boolean add(byte[] pack){
		if(contain(pack)) return false;
		container.add(pack);
		return true;
	}
	
	/**
	 * 查看容器中是否存在相同的byte数组
	 */
	public boolean contain(byte[] pack){
		for(byte[] b:container){
			if(compare(b, pack))
				return true;
		}
		return false;
	}
	
	/**
	 * 比较两个byte数组是否相同
	 */
	public boolean compare(byte[] a, byte[] b){
		if(a.length != b.length)
			return false;
		for(int i=0; i<a.length; i++){
			if(a[i] != b[i])
				return false;
		}
		return true;
	}
}
