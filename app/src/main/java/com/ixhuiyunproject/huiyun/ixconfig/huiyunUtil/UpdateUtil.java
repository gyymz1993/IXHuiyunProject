package com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.VideoSplashActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Version;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 检查更新的工具类
 */
public class UpdateUtil {
	public static Version versions;
	public static OnResultListener<Version> onResultListener;

	public static void setOnResultListener(
			OnResultListener<Version> monResultListener) {
		onResultListener = monResultListener;
	}

	/**
	 * 获取服务器最新版本的信息
	 */
	public static void getVersion() {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, FinalValue.URL_UPDAT_INFO,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						System.out.println(current + "/" + total);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.i(responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							int version = jsonObject.getInt("version");
							String desc = jsonObject.getString("desc");
							String url = jsonObject.getString("url");
							versions = new Version(version, desc, url);
							if (onResultListener != null) {
								onResultListener.onResult(true, versions);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
					}
				});
	}

	@SuppressWarnings("rawtypes")
	public static HttpHandler handler = null;
	public static void downLoadNewApk(final Context context) {
		/**
		 * 点击取消停止下载
		 */
		showDownloadDialog(context, new OnResultListener<Object>() {
			@Override
			public void onResult(boolean isSecceed, Object obj) {
				handler.cancel();
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
			}
		});
		HttpUtils http = new HttpUtils();
		if (versions != null) {
			handler = http.download(versions.getUrl(), mSavePath, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
					true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					new RequestCallBack<File>() {
						@Override
						public void onStart() {
							System.out.println("conn...");
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							int x = (int) (current * 100 / (int) total);
							System.out.println(x + "%");
							textView.setText(x + "%");
							System.out.println("onLoading" + current + "/"
									+ total);
							mProgress.setProgress(x);
						}

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							System.out.println("downloaded:"
									+ responseInfo.result.getPath());
							UIUtils.runInMainThread(new Runnable() {
								@Override
								public void run() {
									SystemClock.sleep(300);
									/*** 安装    */
									mDownloadDialog.dismiss();
									VideoSplashActivity.activity.finish();
									installApk(context);
								}
							});
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							System.out.println("onFailure" + msg);
						}
					});
		}

	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(
					"com.huiyun.ix_configreconstitute", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 安装APK文件
	 */
	// 下载保存路径
	public static String mSavePath = "/sdcard/ix.apk";
	public static File apkfile = new File(mSavePath);

	public static void installApk(Context context) {
		if (!isExis()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	public static boolean isExis() {
		File apkfile = new File(mSavePath);
		if (apkfile.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 显示软件下载对话框
	 */
	public static ProgressBar mProgress;
	public static Builder builder;
	public static Dialog mDownloadDialog;
	public static TextView textView;
	public static boolean cancelUpdate = false;

	public static void showDownloadDialog(Context mContext,
			final OnResultListener<Object> listener) {
		// 构造软件下载对话框
		builder = new Builder(mContext);
		builder.setTitle("软件更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		textView = (TextView) v.findViewById(R.id.up_text);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 设置取消状态
				dialog.dismiss();
				listener.onResult(cancelUpdate, null);
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
	}

}
