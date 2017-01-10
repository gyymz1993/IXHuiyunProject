package com.ixhuiyunproject.huiyun.voice.analyst;

/**
 * Function:
 * 
 * @author YangShao 2015年5月13日 上午10:34:17
 * @version 1.0
 */
public class RedCodeUtils {

	public static int code(String number) {
		if(number.endsWith("一")||number.endsWith("1")) return 1;
		if(number.endsWith("二")||number.endsWith("2")) return 2;
		if(number.endsWith("三")||number.endsWith("3")) return 3;
		if(number.endsWith("四")||number.endsWith("4")) return 4;
		if(number.endsWith("五")||number.endsWith("5")) return 5;
		if(number.endsWith("六")||number.endsWith("6")) return 6;
		if(number.endsWith("七")||number.endsWith("7")) return 7;
		if(number.endsWith("八")||number.endsWith("8")) return 8;
		if(number.endsWith("九")||number.endsWith("9")) return 9;
		if(number.endsWith("十")||number.endsWith("10")) return 10;
		if(number.endsWith("是一")||number.endsWith("时一")||number.endsWith("十一")||number.endsWith("11")) return 11;
		if(number.endsWith("是二")||number.endsWith("时二")||number.endsWith("十二")||number.endsWith("12")) return 12;
		if(number.endsWith("是三")||number.endsWith("时三")||number.endsWith("十三")||number.endsWith("13")) return 13;
		if(number.endsWith("是四")||number.endsWith("时四")||number.endsWith("十四")||number.endsWith("14")) return 14;
		if(number.endsWith("是五")||number.endsWith("时五")||number.endsWith("十五")||number.endsWith("15")) return 15;
		if(number.endsWith("是六")||number.endsWith("时六")||number.endsWith("十六")||number.endsWith("16")) return 16;
		if(number.endsWith("是七")||number.endsWith("时七")||number.endsWith("十七")||number.endsWith("17")) return 17;
		if(number.endsWith("是八")||number.endsWith("时八")||number.endsWith("十把")||number.endsWith("18")) return 18;
		return -1;
	}

	public static void main(String[] args) {
		//test(3213);
	//	String[] str = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		//test(912321321);
	}
}
