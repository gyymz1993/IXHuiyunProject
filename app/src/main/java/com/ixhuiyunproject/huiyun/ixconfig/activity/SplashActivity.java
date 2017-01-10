package com.ixhuiyunproject.huiyun.ixconfig.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Version;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.UpdateDialog;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.UpdateUtil;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 进行一些初始化的操作
 * 
 * @author lzy
 * 
 */
@Deprecated
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplashActivity extends Activity {
	public static SplashActivity activity;
	private final int WHAT = 1;
	private final int WATTINGTIME = 1000;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 提前找主机
		NetJsonUtil.getInstance().findMasteByUDP();
		activity = this;
		context = this;
		// 初始化视频连接，准备跳转
		preparetoJump();
		// initView();
		update();
		onMoblickAgent();
	}

	public void onMoblickAgent() {
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
	}

	private void update() {
		UpdateUtil.setOnResultListener(new OnResultListener<Version>() {
			@Override
			public void onResult(boolean isSecceed, Version obj) {
				if (isSecceed == true && null != obj) {
					int serviceCode = obj.getVersion_id();
					// 版本判断
					if (serviceCode > UpdateUtil.getVersionCode(context)) {
						// 弹出提示框警告
						UpdateDialog dlg = new UpdateDialog(context, "软件更新",
								obj.getDesc());
						/**
						 * 停止运行当前的Handler
						 */
						handle.removeMessages(WHAT);
						dlg.setConfirmListener(new OnResultListener<Object>() {
							@Override
							public void onResult(boolean isSecceed, Object obj) {
								if (isSecceed) {
									if (UpdateUtil.isExis()) {
										UpdateUtil.apkfile.delete();
									}
									UpdateUtil.downLoadNewApk(context);
								} else {
									Intent intent = new Intent(context,
											LoginActivity.class);
									startActivity(intent);
									SplashActivity.this.finish();
								}
							}
						});
						dlg.show(getFragmentManager(), "");
					}
				}
				UpdateUtil.setOnResultListener(null);
			}
		});
		UpdateUtil.getVersion();
	}

	/**
	 * 准备跳转主页面
	 */
	private void preparetoJump() {
		// 跳转下一页
		Message message = Message.obtain();
		message.what = WHAT;
		handle.sendMessageDelayed(message, 3 * WATTINGTIME);
	}

	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT:
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

}
