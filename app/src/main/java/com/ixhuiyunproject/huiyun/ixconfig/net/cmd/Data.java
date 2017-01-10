package com.ixhuiyunproject.huiyun.ixconfig.net.cmd;

/**
 * 定义CMD的常量
 * 2014.10.23  修改了部分功能码的值
 * 2014.10.30  添加命令位的常量值，和移动设备客户端的默认地址
 * 2014.11.27  添加功能设置类型、输入/出类型，增加部分常量
 * 2014.12.08  添加了场景相关的定义
 *
 */
public class Data {
	//
	// 网关类型
	//
	public static final int GATEWAY_IS_MASTER = 1;
	public static final int GATEWAY_IS_SLAVE  = 0;
	
	//
	// EEPROM储存格式 至少要用at24c32
	//
	public static final int CC101_LOCAL_ADDR =   0;
	public static final int CC101_MASTER_ADDR =  1;
	public static final int CC101_M_S_BIT_ADDR = 2; // 网关主从设定标志
	
	public static final int KEY_TYPE_BASIC_ADDR = 24;
	public static final int KEY_TYPE_OFFSET     = 12;
	public static final int KEY_DATA_BASIC_ADDR = 25;
	public static final int KEY_DATA_OFFSET     = 12;
	
	public static final int KEY_DATA_BASIC_MEMORY_VALUE_ADDR = 25;
	public static final int KEY_DATA_BASIC_RELAY_VALUE_ADDR  = 26;
	
	//
	// 用户自定义码
	//
	public static final int CUSTOM_CODE_RAY = 1;
	public static final int CUSTOM_CODE_RF  = 2;
	
	//
	// 移动设备客户端的地址（默认为253）
	//
	public static final int ADDR_WIFI       = 253;
	public static final int ADDR_SLAVE_MAX  = 250;
	public static final int ADDR_SLAVE_MIN  = 6;
	public static final int ADDR_MASTER_MAX = 5;
	public static final int ADDR_MASTER_MIN = 1;
	public static final int ADDR_BROADCAST  = 0;
	public static final int ADDR_SELF       = 254;
	
	//
	// 功能码
	//
	public static final int FUNC_CODE_SEARCH           = 1;
	public static final int FUNC_CODE_SEARCH_RETURN    = 2;
		
	public static final int FUNC_CODE_DEV_BLINK        = 3;
	public static final int FUNC_CODE_DEV_BLINK_RETURN = 4;
		
	public static final int FUNC_CODE_DEV_HAND         = 5;
	public static final int FUNC_CODE_DEV_HAND_RETURN  = 6;
		
	public static final int FUNC_CODE_SET_ADDR         = 7;
	public static final int FUNC_CODE_SET_ADDR_RETURN  = 8;
		
	public static final int FUNC_CODE_IO_W             = 9;
	public static final int FUNC_CODE_IO_W_RETURN      = 10;
	public static final int FUNC_CODE_IO_R             = 11;
	public static final int FUNC_CODE_IO_R_RETURN      = 12;
	public static final int FUNC_CODE_IO_NOTICE        = 13;
	public static final int FUNC_CODE_IO_NOTICE_RETURN = 14;
		
	public static final int FUNC_CODE_BYTE_W           = 15;
	public static final int FUNC_CODE_BYTE_W_RETURN    = 16;
	public static final int FUNC_CODE_BYTE_R           = 17;
	public static final int FUNC_CODE_BYTE_R_RETURN    = 18;
		
	public static final int FUNC_CODE_MULTI_W          = 21;
	public static final int FUNC_CODE_MULTI_W_RETURN   = 22;
	public static final int FUNC_CODE_MULTI_R          = 23;
	public static final int FUNC_CODE_MULTI_R_RETURN   = 24;
		
	public static final int FUNC_CODE_SET_W            = 27;
	public static final int FUNC_CODE_SET_W_RETURN     = 28;
	public static final int FUNC_CODE_SET_R            = 29;
	public static final int FUNC_CODE_SET_R_RETURN     = 30;
	
	//
	// 功能码(红外)
	//
	public static final int FUNC_CODE_LEARN_PASSWAY        = 9;
	public static final int FUNC_CODE_LEARN_PASSWAY_RETURN = 10;
	public static final int FUNC_CODE_RAY_LEARN            = 27;
	public static final int FUNC_CODE_RAY_LEARN_RETURN     = 28;
	public static final int FUNC_CODE_RAY_CONTROL         = 31;
	public static final int FUNC_CODE_RAY_CONTROL_RETURN  = 32;

	//
	// 长度
	//
	public static final int LENGTH_CPUID    = 12;
	public static final int LENGTH_DEV_INFO = 6;
	
	//
	// 指定位
	//
	public static final int P_TARGET_ADDR = 0;
	public static final int P_LOCAL_ADDR  = 1;
	public static final int P_FUNC_CODE   = 2;
	public static final int P_PAN_KEY     = 3;
	public static final int P_OBJECT_NUM  = 4;
	public static final int P_DATA        = 5;
	
	//
	// 接收数据、缓存的容量,以及其它
	//
	public static final int MAX_RECEIVE_LENGTH = 20480;
	public static final int BUFFER_CAPACITY    = 40960;
	public static final int PACK_MAX_LENGTH    = 20480;
	public static final int PACK_EXTRA_LENGTH  = 5;
	public static final int JSONPACK_EXTRA_LENGTH  = 6;
	
	//
	// 功能设置的类型
	//
	public static final int SETFUNC_TYPE_RELATION_ADD = 1;
	public static final int SETFUNC_TYPE_RELATION_DEL = 2;
	public static final int SETFUNC_TYPE_SCENE_ADD = 3;
	public static final int SETFUNC_TYPE_SCENE_DEL = 4;
	
	public static final int READFUNC_TYPE_RELATION_QUERY = 1;
	public static final int READFUNC_TYPE_GET_SCENE = 2;
	public static final int READFUNC_TYPE_SCENE_QUERY = 3;
	
	//
	// 输入/出类型
	//
	public static final int SLAVE_TYPE_PANEL   = 1;
	public static final int SLAVE_TYPE_GATEWAY = 2;
	
	//
	// 虚拟按键编号
	//
	public static final int OBJ_SCENE = 0;
}
