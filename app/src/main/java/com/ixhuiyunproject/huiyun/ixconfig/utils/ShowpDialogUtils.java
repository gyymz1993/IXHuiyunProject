package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class ShowpDialogUtils {

	private  Activity activity;

	public ShowpDialogUtils(Activity mactivity) {
		activity = mactivity;
	}

	/**
	 * 关闭等待窗口
	 */
	public  void dismissDialog() {
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	}

	/**
	 * 显示登录窗口
	 */
	public  void showpDialog(String showpDialogMessage) {
		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = showpDialogMessage;
		handler.sendMessage(msg);
	}

	/**
	 * 显示有可能永久等待的登录窗口
	 */
	public  void showpDialogEternity(String showpDialogMessage) {
		Message msg = Message.obtain();
		msg.what = 2;
		msg.obj = showpDialogMessage;
		handler.sendMessage(msg);
	}

	private  ProgressDialog pDialog;
	private  Handler handler = new Handler() {
		/**
		 * 方法一：setCanceledOnTouchOutside(false); 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
		 * 方法二： setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用 按返回键也不起作用
		 */
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// 最多等待5秒的等待对话框
				pDialog = new ProgressDialog(activity);
				pDialog.setMessage(msg.obj.toString());
				pDialog.setProgressStyle(Activity.TRIM_MEMORY_BACKGROUND);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(5000);
							pDialog.dismiss();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			} else if (msg.what == 2) {
				// 可能无限等待的等待对话框
				pDialog = new ProgressDialog(activity);
				pDialog.setMessage(msg.obj.toString());
				pDialog.setProgressStyle(Activity.TRIM_MEMORY_BACKGROUND);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
		};

	};

}
