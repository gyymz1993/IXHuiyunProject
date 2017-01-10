package com.ixhuiyunproject.huiyun.ixconfig.utils;


import com.ixhuiyunproject.R;

import java.util.Random;

public class RandomOfImageBg {
	/**
	 * 
	 * Function: 随机取背景
	 * 
	 * @author Howard DateTime 2015年1月24日 下午4:38:44
	 * @return
	 */
	public static int getSceneImageBg() {
		int number = new Random().nextInt(6) + 1;
		switch (number) {
		case 1:
			return R.drawable.random_a;
		case 2:
			return R.drawable.random_b;
		case 3:
			return R.drawable.random_c;
		case 4:
			return R.drawable.random_d;
		case 5:
			return R.drawable.random_e;
		case 6:
			return R.drawable.random_f;
		default:
			break;
		}
		return 0;
	}

	public static int getSceneImage(int number) {
		switch (number) {
		case 1:
			return R.drawable.scene_home1;
		case 2:
			return R.drawable.scene_home2;
		case 3:
			return R.drawable.scene_home3;
		case 4:
			return R.drawable.scene_home4;
		case 5:
			return R.drawable.scene_home5;
		case 6:
			return R.drawable.scene_home6;
		case 7:
			return R.drawable.scene_home7;
		case 8:
			return R.drawable.scene_home8;
		default:
			break;
		}
		return 0;
	}
	
	public static int getSceneBg(int number) {
		switch (number) {
		case 1:
			return R.drawable.scene_bg1;
		case 2:
			return R.drawable.scene_bg2;
		case 3:
			return R.drawable.scene_bg3;
		case 4:
			return R.drawable.scene_bg4;
		case 5:
			return R.drawable.scene_bg5;
		case 6:
			return R.drawable.scene_bg6;
		case 7:
			return R.drawable.scene_bg7;
		case 8:
			return R.drawable.scene_bg8;
		default:
			break;
		}
		return 0;
	}

	public static int getRemoteImage(int number) {
		switch (number) {
		case 1:
			return R.drawable.remote_add_tv;
		case 2:
			return R.drawable.remote_add_air_;
		case 3:
			return R.drawable.remote_add_yx;
		case 4:
			return R.drawable.remote_add_mi;
		default:
			break;
		}
		return 0;
	}
}
