package com.ixhuiyunproject.huiyun.ixconfig.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.SystemClock;

import com.ixhuiyunproject.huiyun.ixconfig.bean.DialogMsg;

/**
 * 管理dialog
 * 
 * @author lzy_torah
 *
 */
public class DialogUtil {
	private static ProgressDialog pDialog;

	/**
	 * 显示等待中的dialog
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void showProgressDialog(Activity activity, final DialogMsg msg) {
		pDialog = new ProgressDialog(activity);
		pDialog.setMessage(msg.getMessage());
		pDialog.setProgressStyle(40);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		if (msg.getTimeMills() > 0) {
			new Thread() {
				public void run() {
					SystemClock.sleep(msg.getTimeMills());
					dismissProgressDialog();
					if(msg.getListener()!=null){
						msg.getListener().onResult(true, null);
					}
				};
			}.start();
		}
	}

	/**
	 * 进度框消失
	 */
	public static void dismissProgressDialog() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
	}
}
