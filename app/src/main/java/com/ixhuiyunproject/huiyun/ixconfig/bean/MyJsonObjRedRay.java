package com.ixhuiyunproject.huiyun.ixconfig.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 单用于红外码的
 * @author lzy_torah
 *
 */
public class MyJsonObjRedRay extends BaseJsonObj {
	public Data2 data;

	public static class Data2 {
		public List<RedRay> list = new ArrayList<RedRay>();
	}
}