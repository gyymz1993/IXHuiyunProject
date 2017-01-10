package com.ixhuiyunproject.huiyun.ixconfig.net.cmd;


import com.ixhuiyunproject.huiyun.crypt.RC4;
import com.ixhuiyunproject.huiyun.ixconfig.utils.BytesStringUtils;

/** 负责对json的CMD进行打包，或者把已经打包的数据解包成CMD
 * @author lzn
 *
 */
public class JsonPack {
	
	// 该变量决定是否启用RC4加密解密
	private static boolean CRYPT = true;
	
	/**数据打包
	 * 起始码0xFE
	 * @param custom_code 由其他协议自定义的
	 * @param data_length 输入数据的长度
	 * @param in_buf 要打包的数据
	 * @return 打包后的数据
	 */
	public static byte[] Data_Pack(int custom_code, int data_length, byte[] in_buf){
		byte[] out_buf = new byte[Data.JSONPACK_EXTRA_LENGTH + data_length];
		
	    int crc_temp;
	    out_buf[0]=(byte) 0xfe;
	    out_buf[1]=(byte) custom_code;
	    out_buf[2]=(byte) (data_length>>8 & 0xff);
	    out_buf[3]=(byte) (data_length & 0xff);
	    if(CRYPT){
	    	byte[] mid_buf = RC4.getInstance().crypt(in_buf, data_length);
	    	for(int i=0; i<data_length; i++)
		    	out_buf[4+i] = mid_buf[i];
	    } else {
	    	for(int i=0; i<data_length; i++)
		    	out_buf[4+i] = in_buf[i];
	    }
	    crc_temp=CRC.caCrc16(out_buf, 0, data_length+3);
	    out_buf[4+data_length]=(byte) ((crc_temp >> 8) & 0xff);
	    out_buf[5+data_length]=(byte) (crc_temp & 0xff);
	    
	    return out_buf;
	}
	
	public static byte[] Data_Pack(int custom_code, byte[] in_buf){
		return Data_Pack(custom_code, in_buf.length, in_buf);
	}

	/**数据解包
	 * 起始码0xFE
	 * @param length 输入数据的长度
	 * @param in_buf 解包前的数据
	 * @return 解包后的数据
	 */
	public static byte[] Data_Unpack(int length, byte[] in_buf){
		if(length <= Data.JSONPACK_EXTRA_LENGTH) return null;
		byte[] out_buf = new byte[length-Data.JSONPACK_EXTRA_LENGTH];
	
	    int crc_temp, dataLen;
	    dataLen = (BytesStringUtils.toUnsignedInt(in_buf[2])<<8 & 0xff00) |
	    		(BytesStringUtils.toUnsignedInt(in_buf[3]) & 0xff);
	    crc_temp=CRC.caCrc16(in_buf, 0, dataLen+3);
	    if(in_buf[0] == (byte) 0xfe &&
	    		(byte)((crc_temp>>8) & 0xff) == (byte)in_buf[dataLen+4] && 
	    		(byte)(crc_temp & 0xff) == (byte)in_buf[dataLen+5] &&
	    		(length-Data.JSONPACK_EXTRA_LENGTH) == dataLen){
	        for(int i=0; i<length-Data.JSONPACK_EXTRA_LENGTH; i++)
	        	out_buf[i] = in_buf[i+4];
	        if(CRYPT){
	        	return RC4.getInstance().crypt(out_buf, out_buf.length);
	        } else {
	        	return out_buf;
	        }
	        
	    } else {
	        return null;
	    }
	}
	
	public static byte[] Data_Unpack(byte[] in_buf){
		return Data_Unpack(in_buf.length, in_buf);
	}
	
	public static boolean Check_CustomCode(byte[] buf, int custom_code){
		if(buf.length <= Data.PACK_EXTRA_LENGTH)
			return false;
		if(buf[1] == (byte)custom_code)
			return true;
		return false;
	}
	
	public static int Get_CustomCode(byte[] buf){
		return BytesStringUtils.toUnsignedInt(buf[1]);
	}
}