package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;

public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
	 * */
	public static final boolean DEBUG = true;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler instance;
	/** 程序的Context对象 */
	private Context mContext;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();

	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".txt";

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	boolean flag = true;

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (flag == false)
			return;
		flag = false;
		if (!handleException(ex) && mDefaultHandler != null) {
		} else {
			// Sleep一会后结束程序
			Log.e("TEST", "sleep and finish");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			Log.e("err", "即将退出");
		}
		System.out.println("执行关闭程序");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getMessage();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序出现异常，即将退出。\n" + msg,
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}

		}.start();
		// 收集设备信息
		// 保存错误报告文件
		saveCrashInfoToFile(ex);

		return true;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	private void saveCrashInfoToFile(Throwable ex) {
		ex.printStackTrace();
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();
			String fileName = "error" + timestamp + CRASH_REPORTER_EXTENSION;
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "huiyun"
					+ File.separator + fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileOutputStream trace = new FileOutputStream(file);
			mDeviceCrashInfo.store(trace, "");
			trace.flush();
			trace.close();
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}
	}
}