package com.ixhuiyunproject.huiyun.ixconfig.inter;

/**
 * 有结果后回调
 * @author torahs
 *
 * @param <T>
 */
public interface OnResultListener<T> {
	void onResult(boolean isSecceed, T obj);
}