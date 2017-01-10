package com.ixhuiyunproject.huiyun.ixconfig.bean;

import android.graphics.Color;

/**   
 * @Description: 处理颜色状态的转换
 * @author lzn
 * @date 2015-1-22 上午10:01:58 
 * @version V1.0   
 */
public class ColorState {
	
	 /**
	 *  Function: 根据rgb值获得状态值
	 *  @author lzn 
	 *  2015-1-22 上午10:05:59
	 *  @param b
	 *  @param r
	 *  @param g
	 *  @return
	 */
	public static int getRGBValue(int r, int g, int b){
		if(b == 255) b=254; // 蓝色值不能为255
		return ((b<<16) & 0xff0000)|((r<<8) & 0xff00) | (g & 0xff);
	}
	
	
	 /**
	 *  Function: 获取红色值
	 *  @author lzn 
	 *  2015-1-22 上午10:08:19
	 *  @param state
	 *  @return
	 */
	public static int getRed(int state){
		return ((state>>8) & 0xff);
	}
	
	/**
	 *  Function: 获取绿色值
	 *  @author lzn 
	 *  2015-1-22 上午10:08:19
	 *  @param state
	 *  @return
	 */
	public static int getGreen(int state){
		return (state & 0xff);
	}
	
	/**
	 *  Function: 获取蓝色值
	 *  @author lzn 
	 *  2015-1-22 上午10:08:19
	 *  @param state
	 *  @return
	 */
	public static int getBlue(int state){
		int blue = ((state>>16) & 0xff);
		return (blue == 255)?254:blue;
	}
	
	/**
	 * ARGB 转 BRG
	 *  Function:
	 *  @author Howard  DateTime 
	 *  2015年1月22日 下午5:16:02
	 */
	public static int getBRGfromARGB(int ARGB){
		int r = Color.red(ARGB);
		int g = Color.green(ARGB);
		int b = Color.blue(ARGB);
		if(b == 255) b=254; // 蓝色值不能为255
		return ((b<<16) & 0xff0000)|((r<<8) & 0xff00) | (g & 0xff);
	}
	
	/**
	 * BRG 转 ARGB
	 *  Function:
	 *  @author Howard  DateTime 
	 *  2015年1月22日 下午5:16:02
	 */
	public static int getARGBfromBRG(int brg){
		int r = ColorState.getRed(brg);
		int g = ColorState.getGreen(brg);
		int b = ColorState.getBlue(brg);
		return Color.argb(0xff, r, g, b);
	}
	
}
