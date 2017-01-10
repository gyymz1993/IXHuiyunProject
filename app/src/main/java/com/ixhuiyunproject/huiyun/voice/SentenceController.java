package com.ixhuiyunproject.huiyun.voice;


import com.ixhuiyunproject.huiyun.voice.analyst.BaseAnalyst;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断并处理句子含义
 * 
 * @author torah 
 * 
 */
public class SentenceController {
	private List<BaseAnalyst<?>> analystList = new ArrayList<BaseAnalyst<?>>();
	private AnalystManager manager;

	public SentenceController() {
		manager = new AnalystManager();
		manager.initAnalyst(this);// 初始化Pattern分析者
	}

	/**
	 * 添加一个模式处理模式
	 * 
	 * @param analyst
	 */
	public synchronized void addAModuleAnalyst(BaseAnalyst<?> analyst) {
		analystList.add(analyst);
	}

	/**
	 * 判断该句子是否符合预设定义
	 * 
	 * @param sentence
	 */
	public void judgeSentence(String sentence) {
		for (int i = 0; i < analystList.size(); i++) {
			BaseAnalyst<?> analyst = analystList.get(i);
			try {
				if (analyst.judgeIsMartch(sentence)) {// 符合预设模式
					analyst.execute();
				}
			} catch (Exception e) {
			}
		}
	}

}
