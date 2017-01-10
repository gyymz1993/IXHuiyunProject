package com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data;


import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;

public class ComboOutput {
	public static final int OUT_DEV = 0; // 输出设备
	public static final int SCENE   = 1; // 场景
	public static final int NOLINK  = 2; // 不进行组合
	
	public int type;
	public OutDevice outDev;
	public String sceneName;
	
	public ComboOutput(){
		type = OUT_DEV;
		outDev = null;
		sceneName = null;
	}
}
