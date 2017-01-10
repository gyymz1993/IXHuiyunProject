package com.ixhuiyunproject.huiyun.ixconfig.net.container;


import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.Data;

/**
 * 缓存区(json命令专用)
 * 功能：以FIFO的方式往里面导入数据或者导出命令
 */
public class JsonCommandBuffer {
	private byte[] buffer;
	private int lenValid;
	
	public static final int P_CMD_LEN = 2;
	public static final int RESULT_NO_PROBLEM = 1;
	public static final int RESULT_EMPTY = 0;
	public static final int RESULT_ERROR_DATA = -1;
	public static final int RESULT_NO_ENOUGH = -2;
	
	public JsonCommandBuffer(int size){
		buffer = new byte[size];
		lenValid = 0;
	}
	
	public boolean importByte(byte[] b, int len){
		if(len < 0 || len + lenValid > buffer.length)
			return false;
		for(int i=0; i<len; i++)
			buffer[lenValid + i] = b[i];
		lenValid += len;
		
		return true;
	}
	
	public byte[] exportByte(int len){
		if(len < 1 || len > lenValid) return null;
		byte[] b = new byte[len];
		for(int i=0; i<len; i++)
			b[i] = buffer[i];
		for(int i=0; i<lenValid-len; i++)
			buffer[i] = buffer[len+i];
		lenValid -= len;
		
		return b;
	}
	
	public int getValidLength(){
		return lenValid;
	}
	
	public byte[] exportPack(){
		if(check() != RESULT_NO_PROBLEM)
			return null;
		int lenData = (toUnsignedInt(buffer[2])<<8 & 0xff00) |
				(toUnsignedInt(buffer[3]) & 0xff);
		int len = lenData + Data.JSONPACK_EXTRA_LENGTH;
		return exportByte(len);
	}
	
	public int check(){
		int lenData = (toUnsignedInt(buffer[2])<<8 & 0xff00) |
				(toUnsignedInt(buffer[3]) & 0xff);
		
		if(lenValid < 0)
			return RESULT_ERROR_DATA;
		if(lenValid == 0)
			return RESULT_EMPTY;
		if(buffer[0] != (byte)0xFE)
			return RESULT_ERROR_DATA;
		if(lenValid < 3)
			return RESULT_NO_ENOUGH;
		if(lenData < 0 || lenData + Data.JSONPACK_EXTRA_LENGTH > Data.PACK_MAX_LENGTH)
			return RESULT_ERROR_DATA;
		if(lenData + Data.JSONPACK_EXTRA_LENGTH > lenValid)
			return RESULT_NO_ENOUGH;
		return RESULT_NO_PROBLEM;
	}
	
	public void clean(){
		lenValid = 0;
	}
	
	/** 将byte转换成无符号的int型 */
	private int toUnsignedInt(byte b){
		int i = b;
		if(i < 0) i+=256;
		return i;
	}
	
	public String show(){
		String show = "";
		for(int i=0; i<lenValid; i++)
			show += String.format("%02x ", buffer[i]);
		return show;
	}
}
