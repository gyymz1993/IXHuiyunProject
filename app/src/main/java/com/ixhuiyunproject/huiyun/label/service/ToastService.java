package com.ixhuiyunproject.huiyun.label.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.CommonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.voice.VoiceCore;
import com.ixhuiyunproject.ipcamer.demo.my.CameraCoreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签
 * 
 * @author torah
 * 
 */
public class ToastService extends Service {
	/** 展示监控还是语音 */
	public static boolean showCamera = false;
	private SharedPreferences sp;
	private View showedView;
	private WindowManager.LayoutParams params;
	private WindowManager wm;
	private boolean has_show = false;
	/** 带动作的图片 */
	public static ImageView img;
	public static TextView tv;

	@Override
	public void onCreate() {
		sp = SpUtils.getSp();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();
		VoiceCore.getInstance().initSpeechRecognizer(this);// 初始化语音
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {// 每次调用启动服务会调用此方法。
		if (intent != null && intent.getIntExtra("flush", 0) != 0) {
			hideToast();
			showToast();
		} else if (sp.getBoolean(Sp_Key.B_WILL_STRAT, true)) {
			showToast();
		} else {
			hideToast();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 显示标签
	 */
	public void showToast() {
		if (has_show) {
			return;
		}
		if (!SpUtils.getBoolean(Sp_Key.B_WILL_STRAT, true)) {
			return;
		}
		has_show = true;
		// 初始化界面组件
		showedView = getView();
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.x = sp.getInt("toast_x", 100);
		params.y = sp.getInt("toast_y", 100);
		params.format = PixelFormat.TRANSLUCENT;// 半透明
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		// 添加到屏幕
		wm.addView(showedView, params);
		// 设置触摸事件逻辑
		img.setClickable(true);
		img.setFocusable(true);
		img.setOnTouchListener(new OnTouchListener() {
			float startX;
			float startY;
			float distX;
			float distY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getRawX();
					startY = event.getRawY();
					distX = startX - params.x;
					distY = startY - params.y;
					break;
				case MotionEvent.ACTION_MOVE:
					params.x = (int) (event.getRawX() - distX);
					params.y = (int) (event.getRawY() - distY);
					wm.updateViewLayout(showedView, params);
					break;
				case MotionEvent.ACTION_UP:
					int toast_x = Math.max(0, (int) (event.getRawX() - distX));
					int toast_y = Math.max(0, (int) (event.getRawY() - distY));
					toast_x = Math.min(toast_x,
							CommonUtil.getScreenWidth(ToastService.this));
					toast_y = Math.min(toast_y,
							CommonUtil.getScreenHeight(ToastService.this));
					sp.edit().putInt("toast_x", toast_x).commit();
					sp.edit().putInt("toast_y", toast_y).commit();
					break;
				}
				return false;
			}
		});
	}

	public View getView() {
		if (!showCamera) {
			View view = View.inflate(this, R.layout.voice_main, null);
			img = (ImageView) view.findViewById(R.id.ib_voice);
			tv = (TextView) view.findViewById(R.id.tv_voice);
			tv.setVisibility(View.GONE);
			if (!sp.getBoolean(Sp_Key.B_SHOW_TEXT, false)) {
				// 显示图片
				img.setVisibility(View.VISIBLE);
				String uriStr = sp.getString(Sp_Key.S_PIC_URI, null);
				setOnclickAction(img);
				if (TextUtils.isEmpty(uriStr)) {
					img.setImageResource(R.drawable.vioce_icon);
				} else {
					img.setImageURI(Uri.parse(uriStr));
				}
			}
			return view;
		} else {
			View view = View.inflate(this, R.layout.view_camera_toast, null);
			img = (ImageView) view.findViewById(R.id.iv_camera);
			cameraHandler.sendEmptyMessageDelayed(2,500);
			return view;
		}
	}

	/**
	 * 设置点击事件
	 * 
	 * @param view
	 */
	private void setOnclickAction(View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				img.setImageResource(R.drawable.vioce_icon_bg);
				VoiceCore.getInstance().begainSpeechRecognizer();
			}
		});
	}

	/**
	 * 隐藏吐司
	 */
	public void hideToast() {
		if (!has_show) {
			return;
		}
		has_show = false;
		if (wm != null && showedView != null) {
			wm.removeView(showedView);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	@SuppressWarnings("unused")
	private boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = mActivityManager
				.getRunningTasks(1);
		return getHomes().contains(
				runningTasks.get(0).topActivity.getPackageName());
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> queryIntentActivities = packageManager
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : queryIntentActivities) {
			String str = ri.activityInfo.packageName;
			names.add(str);
		}
		return names;
	}

	/**处理视屏监控的界面问题
	 * @author torah
	 *
	 */
	static Handler cameraHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(!showCamera){
				return;//如果不是展示监控，就不处理
			}
			switch (msg.what) {
			case 1:
				Bitmap bmp = (Bitmap) msg.obj;
				Bitmap bitmap = Bitmap.createScaledBitmap(bmp, normalWidth
						* currentSize, normalHeight * currentSize, true);
				// 核心的显示方法
				img.setImageBitmap(bitmap);
				break;
			case 2:
				normalWidth = img.getWidth();
				normalHeight = img.getHeight();
				break;
			default:
				break;
			}
		}
	};
	static OnResultListener<Bitmap> cameraListener = new OnResultListener<Bitmap>() {

		@Override
		public void onResult(boolean isSecceed, Bitmap obj) {
			if (isSecceed) {
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = obj;
				cameraHandler.sendMessage(msg);
			}
		}
	};

	/**
	 * 使用悬浮窗进行监控
	 */
	public static void startCamera() {
		ToastService.showCamera=true;
		CameraCoreUtil.getInstance().addVideoPiclistener(cameraListener);
	}

	/**使用悬浮窗进行语音
	 * 
	 */
	public static void closeCameraAndstartVoice() {
		ToastService.showCamera=false;
		CameraCoreUtil.getInstance().removeVideoPiclistener(cameraListener);
	}

	static int currentSize = 1;
	static int normalWidth=20;
	static int normalHeight=20;

	/**
	 * 更改摄像头图片的大小
	 */
	public static void changeCameraSize() {
		currentSize++;
		if (currentSize > 4) {
			currentSize = 1;
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				normalWidth * currentSize, normalHeight * currentSize);
		img.setLayoutParams(lp);// 修改成16:9
	}
}
