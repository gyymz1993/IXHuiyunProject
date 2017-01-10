package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

/**
 * 吐司工具
 * 
 * @author tor
 *
 */
public class ToastUtils {
	
	/**
	 * 测试时使用
	 * 
	 * @param obj
	 */
	@SuppressLint("ShowToast")
	public static void showToastWhendebug(final Object obj) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				if (obj == null)
					Toast.makeText(UIUtils.getContext(), "对象为空", 0).show();
				else
					Toast.makeText(UIUtils.getContext(), obj.toString(), 0)
							.show();
			}
		});

	}

	/**
	 * 上线运行时使用
	 * 
	 * @param obj
	 */
	@SuppressLint("ShowToast")
	public static void showToastReal(final Object obj) {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				if (obj == null)
					Toast.makeText(UIUtils.getContext(), "对象为空", 0).show();
				else
					Toast.makeText(UIUtils.getContext(), obj.toString(), 0)
							.show();
			}
		});

	}
}
