package com.ixhuiyunproject.huiyun.ixconfig;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.iflytek.cloud.SpeechUtility;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;
import com.ixhuiyunproject.huiyun.ixconfig.push.SettingTag;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.label.service.ToastService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.abtollc.sdk.AbtoApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * @author torah
 * 
 */
public class BaseApplication extends AbtoApplication {
	private static BaseApplication mBaseApplication = null;
	private static Looper mMainThreadLooper = null;
	private static Handler mMainThreadHandler = null;
	private static int mMainThreadId;
	private static Thread mMainThread = null;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.e("BaseApplication====onCreate");
		StaticValue.setRunning(true);
		BaseApplication.mBaseApplication = this;
		BaseApplication.mMainThreadLooper = getMainLooper();
		BaseApplication.mMainThreadHandler = new Handler();
		BaseApplication.mMainThreadId = android.os.Process.myTid();
		BaseApplication.mMainThread = Thread.currentThread();

		// 注册crashHandler
		// TODO 发送以前没发送的报告
		// CrashHandler catchHandlerUtils = CrashHandler.getInstance();
		// catchHandlerUtils.init(this);
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this); // 初始化 JPush
		initPush("false");
		// 科大讯飞的appid:54fd0f23
		SpeechUtility.createUtility(this, "appid=54fd0f23");
		// 启动图标
		if (SpUtils.getBoolean(Sp_Key.B_WILL_STRAT, true)) {
			Intent intent = new Intent(this, ToastService.class);
			startService(intent);
		}
		
		// ImageLoader的初始化
		initImageLoader();
	}
	
	/**
	 * ImageLoader的初始化
	 */
	private void initImageLoader(){
		ImageLoaderConfiguration.Builder config = 
				new ImageLoaderConfiguration.Builder(this);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app
		ImageLoader.getInstance().init(config.build());
	}

	public static void initPush(String strTag) {
		SettingTag settingTag = new SettingTag();
		settingTag.setTag(strTag);
	}

	public static BaseApplication getApplication() {
		return mBaseApplication;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}
}
