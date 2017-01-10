package com.ixhuiyunproject.huiyun.ixconfig.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 声音的关键字
 * @author torah
 *
 */
public class JsonVoiceKeyWord {
	public List<KeyWord> userword = new ArrayList<KeyWord>();

	public static class KeyWord {
		public String name;
		public List<String> words=new ArrayList<String>();
	}
}
