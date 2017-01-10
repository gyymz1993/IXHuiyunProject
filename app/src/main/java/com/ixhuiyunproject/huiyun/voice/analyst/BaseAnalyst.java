package com.ixhuiyunproject.huiyun.voice.analyst;

import java.util.regex.Pattern;

/**单个Pattern及其相关操作
 * @author torah
 *
 * @param <T>
 */
public abstract class BaseAnalyst<T> {
	private Pattern parttern;
	private String sentence;
	public String getSentence() {
		return sentence;
	}

	protected Pattern getParttern() {
		return parttern;
	}

	public BaseAnalyst(){
		parttern=initPattern();
	}
	
	/**该分析者适配的类型
	 * @return
	 */
	protected abstract Pattern initPattern() ;
	/**判断sentence是否匹配
	 * @param sentence
	 * @return
	 */
	public boolean judgeIsMartch(String sentence) {
		this.sentence=sentence;
		return getParttern().matcher(sentence).matches();
	}
	/**
	 * 执行
	 */
	public abstract void execute();
}
