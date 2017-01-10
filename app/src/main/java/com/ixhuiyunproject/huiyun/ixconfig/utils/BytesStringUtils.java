package com.ixhuiyunproject.huiyun.ixconfig.utils;

public class BytesStringUtils {
	public static boolean udpHexShow = false;
	public static boolean tcpHexShow = true;
	
	/** char数组转byte数组 */
	public static byte[] transCAtoBA(char[] c, int length){
		byte[] b = new byte[length];
		for(int i=0; i<b.length; i++)
			b[i] = (byte) c[i];
		return b;
	}
	
	/** byte数组转char数组 */
	public static char[] transBAtoCA(byte[] b, int length){
		char[] c = new char[length];
		for(int i=0; i<c.length; i++)
			c[i] = (char) b[i];
		return c;
	}
	
	
	
	// 将ip地址转换成int类型，0表示失败
	public static int changeIpAddrToInteger(String s){
		try {
			String[] s2 = s.split("\\.");
			if(s2.length != 4) return 0;
			int i, a, b, c, d;
			a = Integer.parseInt(s2[0]);
			b = Integer.parseInt(s2[1]);
			c = Integer.parseInt(s2[2]);
			d = Integer.parseInt(s2[3]);
			i = ((a<<24) & 0xff000000) | ((b<<16) & 0xff0000) | ((c<<8) & 0xff00) | (d & 0xff);
			return i;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	
	
	/** 把带空格的如12 23 42 23 11转成byte数组
	 * @param s
	 * @return
	 */
	public static byte[] toArrayByteShow(String s){
		try {
			String[] s2 = s.trim().split(" ");
			byte[] b = new byte[s2.length];
			for(int i=0; i<b.length; i++){
				b[i] = (byte) Integer.parseInt(s2[i],16);
			}
			return b;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 把byte数组转成12 23 44 21 11格式的字符串
	 * @param ba
	 * @return
	 */
	public static String toStringShow(byte[] ba){
		String s = "";
		for(byte b:ba){
			s += String.format("%02X ", b);
		}
		return s;
	}
	
	public static byte[] packOld(byte[] buf){
		if(buf.length < 7) return null;
		byte[] pack = new byte[buf.length + 6];
		pack[0] = 0x68;
		pack[1] = 0x00;
		pack[2] = (byte) (buf.length-7);
		pack[3] = 0x68;
		for(int i=0; i<buf.length; i++)
			pack[4+i] = buf[i];
		pack[4+buf.length] = (byte)caCrc(buf,0,buf.length-1);
		pack[5+buf.length] = 0x16;
		return pack;
	}
	
	/**
     * 从一个byte[]数组中截取一部分
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
	
	/**
	 * DPCI校验码计算
	 * 
	 * @param end_index
	 */
	public static char caCrc(byte[] buf, int start_index, int end_index) {
		int crc1 = 0x00;
		char crc = 0x00;
		for (int i = start_index; i <end_index+1; i++) {
			crc1 = (buf[i]^0x86)+crc;
		    crc = (char)(crc1&0xff);
		}
		return crc;
	}
	/** 将byte转换成无符号的int型 */
	public static int toUnsignedInt(byte b){
		int i = b;
		if(i < 0) i+=256;
		return i;
	}
}
