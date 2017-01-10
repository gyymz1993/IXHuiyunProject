package com.ixhuiyunproject.ipcamer.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.ipcamer.demo.BridgeService.PlayInterface;
import com.ixhuiyunproject.vstc2.nativecaller.NativeCaller;

import java.nio.ByteBuffer;
import java.util.Date;


public class SmallPlayActivity extends Activity implements OnTouchListener,
		OnGestureListener, OnClickListener, PlayInterface {
	private static final String LOG_TAG = "SmallPlayActivity";
	private static final int FULLSCREEN = 0;
	private static final int STANDARD = 1;
	private static final int MAGNIFY = 2;
	private int playmode = 1;
	private static final int AUDIO_BUFFER_START_CODE = 0xff00ff;
	/** surfaceView */
	private SurfaceView playSurface = null;
	/** surfaceHolder */
	private SurfaceHolder playHolder = null;
	private byte[] videodata = null;
	private int videoDataLen = 0;
	private int nVideoWidth = 0;
	private int nVideoHeight = 0;
	public int nVideoWidths = 0;
	// public byte[] videodatas = null;
	public int nVideoHeights = 0;
	private View progressView = null;
	private boolean bProgress = true;// true:等待中。false:监控中
	@SuppressWarnings("deprecation")
	private GestureDetector gt = new GestureDetector(this);
	@SuppressWarnings("unused")
	private int nSurfaceHeight = 0;
	private int nResolution = 0;
	private int nBrightness = 0;
	private int nContrast = 0;
	@SuppressWarnings("unused")
	private int nMode = 0;
	@SuppressWarnings("unused")
	private int nFlip = 0;
	@SuppressWarnings("unused")
	private int nFramerate = 0;
	private boolean bInitCameraParam = false;
	private boolean bManualExit = false;
	private TextView textosd = null;
	private String strName = null;
	/** 当前的设备id */
	private String strDID = null;
	private int streamType = ContentCommon.MJPEG_SUB_STREAM;
	private PopupWindow popupWindow_about = null;
	private View osdView = null;
	private boolean bDisplayFinished = true;
	/** surfaceHolder的回调 */
	private surfaceCallback videoCallback = new surfaceCallback();
	private int nPlayCount = 0;
	private CustomBuffer AudioBuffer = null;
	private AudioPlayer audioPlayer = null;
	private boolean bAudioStart = false;
	private int nStreamCodecType;
	private int nP2PMode = ContentCommon.PPPP_MODE_P2P_NORMAL;
	private TextView textTimeoutTextView = null;
	private boolean bTimeoutStarted = false;
	private int nTimeoutRemain = 180;
	private boolean isTakeVideo = false;
	private boolean isLeftRight = false;
	private boolean isUpDown = false;
	private PopupWindow mPopupWindowProgress;
	private final int BRIGHT = 1;// PPPPCameraControl参数：亮度
	private final int CONTRAST = 2;// PPPPCameraControl参数：对比度
	private boolean isHorizontalMirror = false;
	private boolean isVerticalMirror = false;
	private boolean isUpDownPressed = false;
	private boolean isShowtoping = false;
	private ImageView vidoeView;
	private ImageView videoViewStandard;
	private ImageButton ptzAudio;
	private ImageButton ptzPlayMode;
	private Button ptzResolutoin;
	private Animation showAnim;
	private boolean isTakepic = false;
	private boolean isMcriophone = false;
	private boolean isExit = false;
	private PopupWindow resolutionPopWindow;
	private Animation dismissAnim;
	private View ptzOtherSetAnimView;
	private int timeTag = 0;
	private int timeOne = 0;
	private int timeTwo = 0;
	private ImageButton button_back;
	private BitmapDrawable drawable = null;
	// private LinkedList<byte[]> bs = null;
	private MyBrodCast brodCast = null;

	/**
	 * 接收断线信息
	 * @author lzy_torah
	 *
	 */
	class MyBrodCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (arg1.getIntExtra("ifdrop", 2) != 2) {
				PPPPMsgHandler.sendEmptyMessage(1004);
			}
		}
	}

	/**
	 * 断线后退出页面
	 * **/
	private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1004:
				Toast.makeText(SmallPlayActivity.this, "相机断线", 0).show();
				SmallPlayActivity.this.finish();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * surfaceView的回调
	 * @author lzy_torah
	 *
	 */
	private class surfaceCallback implements SurfaceHolder.Callback {
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			if (holder == playHolder) {
				streamType = 10;
				NativeCaller.StartPPPPLivestream(strDID, streamType);
			}
			LogUtils.e("宽和高：" + width + " " + height);
		}

		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// finish();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();

		}
		if (resolutionPopWindow != null && resolutionPopWindow.isShowing()) {
			resolutionPopWindow.dismiss();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!bProgress) {// 双击退出
				Date date = new Date();
				if (timeTag == 0) {
					timeOne = date.getSeconds();
					timeTag = 1;
					Toast.makeText(SmallPlayActivity.this,
							R.string.main_show_back, 0).show();
				} else if (timeTag == 1) {
					timeTwo = date.getSeconds();
					if (timeTwo - timeOne <= 3) {
						// lzy:跳转
						Intent intent = new Intent(SmallPlayActivity.this,
								AddCameraActivity.class);
						BridgeService.setPlayInterface(null);
						startActivity(intent);
						SmallPlayActivity.this.finish();
						timeTag = 0;
					} else {
						timeTag = 1;
						Toast.makeText(SmallPlayActivity.this,
								R.string.main_show_back, 0).show();
					}
				}
			} else {
				showSureDialog1();
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!bProgress) {// 监控中
				showActionButtons();
			} else {// 退出
				showSureDialog1();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示控制按钮
	 */
	private void showActionButtons() {
		showTop();
		showBottom();
	}

	/**
	 * 退出确定dialog
	 * */
	public void showSureDialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app);
		builder.setTitle(getResources().getString(R.string.exit)
				+ getResources().getString(R.string.app_name));
		builder.setMessage(R.string.exit_alert);
		builder.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Process.killProcess(Process.myPid());
						Intent intent = new Intent("finish");
						sendBroadcast(intent);
						SmallPlayActivity.this.finish();
					}
				});
		builder.setNegativeButton(R.string.str_cancel, null);
		builder.show();
	}

	/**
	 * 显示头部标题和控制
	 */
	private void showTop() {
		if (isShowtoping) {
			isShowtoping = false;
			topbg.setVisibility(View.GONE);
			topbg.startAnimation(dismissTopAnim);
			ll_control.setVisibility(View.GONE);
		} else {
			isShowtoping = true;
			topbg.setVisibility(View.VISIBLE);
			topbg.startAnimation(showTopAnim);
			ll_control.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 视频参数恢复默认值
	 */
	private void defaultVideoParams() {
		nBrightness = 1;
		nContrast = 128;
		NativeCaller.PPPPCameraControl(strDID, BRIGHT, 0);// 亮度
		NativeCaller.PPPPCameraControl(strDID, CONTRAST, 128);// 对比度
		showToast(R.string.ptz_default_vedio_params);// 视频参数恢复默认值
	}

	/**展示toast
	 * @param i
	 */
	private void showToast(int i) {
		Toast.makeText(SmallPlayActivity.this, i, 0).show();
	}

	private void updateTimeout() {
		textTimeoutTextView.setText(getString(R.string.p2p_relay_mode_time_out)
				+ nTimeoutRemain + getString(R.string.str_second));
	}

	private Handler timeoutHandle = new Handler() {
		public void handleMessage(Message msg) {

			if (nTimeoutRemain > 0) {
				nTimeoutRemain = nTimeoutRemain - 1;
				updateTimeout();
				Message msgMessage = new Message();
				timeoutHandle.sendMessageDelayed(msgMessage, 1000);
			} else {
				if (!isExit) {
					Toast.makeText(getApplicationContext(),
							R.string.p2p_view_time_out, Toast.LENGTH_SHORT)
							.show();
				}
				finish();
			}
		}
	};

	private void startTimeout() {
		if (!bTimeoutStarted) {
			Message msgMessage = new Message();
			timeoutHandle.sendMessageDelayed(msgMessage, 1000);
			bTimeoutStarted = true;
		}
	}

	private void setViewVisible() {
		if (bProgress) {
			bProgress = false;
			progressView.setVisibility(View.INVISIBLE);
			osdView.setVisibility(View.VISIBLE);
			if (nP2PMode == ContentCommon.PPPP_MODE_P2P_RELAY) {
				updateTimeout();
				textTimeoutTextView.setVisibility(View.VISIBLE);
				startTimeout();
			}
			getCameraParams();
		}
	}

	private Bitmap mBmp;
	private Handler mHandler = new Handler() {

		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			if (msg.what == 1 || msg.what == 2) {// 收到callBaceVideoData回调后发送此信息
				setViewVisible();
			}
			if (!isPTZPrompt) {
				isPTZPrompt = true;
				showToast(R.string.ptz_control);// 请按菜单键进行云台控制
			}
			switch (msg.what) {
			case 1: // h264：视屏进入此
			{
				byte[] rgb = new byte[nVideoWidths * nVideoHeights * 2];
				NativeCaller.YUV4202RGB565(videodata, rgb, nVideoWidths,
						nVideoHeights);
				ByteBuffer buffer = ByteBuffer.wrap(rgb);
				rgb = null;
				/* ByteBuffer buffer = ByteBuffer.wrap(videodata); */
				mBmp = Bitmap.createBitmap(nVideoWidths, nVideoHeights,
						Bitmap.Config.RGB_565);
				mBmp.copyPixelsFromBuffer(buffer);
				int width = getWindowManager().getDefaultDisplay().getWidth();
				int height = getWindowManager().getDefaultDisplay().getHeight();

				vidoeView.setVisibility(View.GONE);
				Bitmap bitmap = null;
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							nVideoWidths, nVideoHeights);// nVideoHeights * 3 /
															// 4);
					lp.gravity = Gravity.CENTER;
					// myGlSurfaceView.setLayoutParams(lp);
					bitmap = Bitmap.createScaledBitmap(mBmp, 320, 240, true);
					Log.e("info", "竖屏");

				} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							width, height);
					lp.gravity = Gravity.CENTER;
					// myGlSurfaceView.setLayoutParams(lp);
					bitmap = Bitmap.createScaledBitmap(mBmp, width, height,
							true);
					Log.e("info", "横屏");
				}

				// myRender.writeSample(videodata, nVideoWidth, nVideoHeight);
				// videoViewStandard.setVisibility(View.GONE);
				// vidoeView.setImageBitmap(mBmp);

				videoViewStandard.setImageBitmap(bitmap);
				videoViewStandard.setVisibility(View.VISIBLE);
				playSurface.setBackgroundColor(0xff000000);
				// Drawable drawable = new BitmapDrawable(bitmap);
				// playSurface.setBackgroundDrawable(drawable);
			}
				break;
			case 2: // JPEG
			{
				// ptzTakeVideo.setVisibility(View.GONE);
				// myGlSurfaceView.setVisibility(View.GONE);
				mBmp = BitmapFactory
						.decodeByteArray(videodata, 0, videoDataLen);
				if (mBmp == null) {
					Log.d(LOG_TAG, "bmp can't be decode...");
					bDisplayFinished = true;
					return;
				}

				nVideoWidth = mBmp.getWidth();
				nVideoHeight = mBmp.getHeight();

				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					videoViewStandard.setVisibility(View.GONE);
					vidoeView.setVisibility(View.VISIBLE);
					vidoeView.setImageBitmap(mBmp);

				} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					videoViewStandard.setImageBitmap(mBmp);
					videoViewStandard.setVisibility(View.VISIBLE);
					vidoeView.setVisibility(View.GONE);
				}
				if (isTakepic) {
					isTakepic = false;
					// takePicture(mBmp);
				}

			}
				break;
			case 3: //
			{
				displayResolution();
			}
				break;
			}

			if (msg.what == 1 || msg.what == 2) {

				// showTimeStamp();
				bDisplayFinished = true;

				nPlayCount++;
				if (nPlayCount >= 100) {
					nPlayCount = 0;
				}
			}
		}

	};

	/**
	 * 判断当前的分辨率
	 */
	protected String displayResolution() {
		/*
		 * 0->640x480 1->320x240 2->160x120; 3->1280x720 4->640x360 5->1280x960
		 */
		String strCurrResolution = null;

		switch (nResolution) {
		case 0:// vga
			strCurrResolution = "640x480";
			break;
		case 1:// qvga
			strCurrResolution = "320x240";
			break;
		case 2:
			strCurrResolution = "160x120";
			break;
		case 3:// 720p
			strCurrResolution = "1280x720";
			break;
		case 4:
			strCurrResolution = "640x360";
			break;
		case 5:
			strCurrResolution = "1280x960";
			break;
		default:
			strCurrResolution = "特殊分辨率";
		}
		// ToastUtils.showToastWhendebug(strCurrResolution);
		return strCurrResolution;
	}

	/**
	 * 切换VGA和QVGA的弹框:lzy修改成画面清晰度
	 */
	public void initExitPopupWindow2() {
		LayoutInflater li = LayoutInflater.from(this);
		View popv = li.inflate(R.layout.popup_d, null);
		Button button_load = (Button) popv.findViewById(R.id.add_check_load);
		Button button_phone = (Button) popv.findViewById(R.id.add_check_phone);
		popupWindow_about = new PopupWindow(popv,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow_about.setAnimationStyle(R.style.AnimationPreview);
		popupWindow_about.setFocusable(true);
		popupWindow_about.setOutsideTouchable(true);
		popupWindow_about.setBackgroundDrawable(new ColorDrawable(0));
		button_load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// NativeCaller.PPPPCameraControl(SystemValue.deviceId, 36, 36);
				popupWindow_about.dismiss();
				ptzResolutoin.setText("VGA");
				setResolution(0);
			}
		});
		button_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// NativeCaller.PPPPCameraControl(SystemValue.deviceId, 37, 37);
				popupWindow_about.dismiss();
				ptzResolutoin.setText("720p");
				setResolution(3);
			}
		});
		popupWindow_about
				.setOnDismissListener(new PopupWindow.OnDismissListener() {

					@Override
					public void onDismiss() {
						popupWindow_about.dismiss();
					}
				});
		popupWindow_about.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow_about.dismiss();
				}
				return false;
			}
		});
	}

	/**
	 * 得到设备的参数
	 */
	private void getCameraParams() {
		NativeCaller.PPPPGetSystemParams(strDID,
				ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);
	}

	/** 断线了 */
	private Handler msgHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Log.d("tag", "断线了");
				Toast.makeText(getApplicationContext(),
						R.string.pppp_status_disconnect, Toast.LENGTH_SHORT)
						.show();
				finish();
			}
		}
	};

	private Handler msgStreamCodecHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (nStreamCodecType == ContentCommon.PPPP_STREAM_TYPE_JPEG) {
				ToastUtils.showToastReal("JPEG");
				// textCodec.setText(JPEG);
			} else {
				// textCodec.setText("H.264");
				ToastUtils.showToastReal("H.264");
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getDataFromOther();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ip_activity_smal_play);
		// 得到设备名称和设备id
		strName = SystemValue.deviceName;
		strDID = SystemValue.deviceId;
		findView();
		InitParams();
		// 初始化声音相关
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);
		// myvideoRecorder = new CustomVideoRecord(this, strDID);
		BridgeService.setPlayInterface(this);
		// 初始化surfaceHolder
		playHolder = playSurface.getHolder();
		playHolder.setFormat(PixelFormat.RGB_565);
		playHolder.addCallback(videoCallback);

		playSurface.setOnTouchListener(this);
		playSurface.setLongClickable(true);

		getCameraParams();
		initAnimations();// 初始化动画

		// prompt user how to control ptz when first enter play
		SharedPreferences sharePreferences = getSharedPreferences("ptzcontrol",
				MODE_PRIVATE);
		isPTZPrompt = sharePreferences.getBoolean("ptzcontrol", false);
		if (!isPTZPrompt) {// 是第一次启动，就提示： 请按菜单键进行云台控制
			Editor edit = sharePreferences.edit();
			edit.putBoolean("ptzcontrol", true);
			edit.commit();
		}
		initExitPopupWindow2();
		brodCast = new MyBrodCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("drop");
		SmallPlayActivity.this.registerReceiver(brodCast, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		bManualExit = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		bManualExit = true;
	}

	/**
	 * 初始化动画
	 */
	private void initAnimations() {
		dismissTopAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_top_anim_dismiss);
		showTopAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_top_anim_show);
		showAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_otherset_anim_show);
		dismissAnim = AnimationUtils.loadAnimation(this,
				R.anim.ptz_otherset_anim_dismiss);
	}

	/**
	 * 得到屏幕的尺寸
	 */
	private void InitParams() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// nSurfaceHeight = dm.heightPixels;// lzy注掉
		nSurfaceHeight = dm.widthPixels * 3 / 4;// 未使用
		textosd.setText(strName);// 显示用户名
	}

	/**
	 * 启动声音
	 */
	private void StartAudio() {
		synchronized (this) {
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStart();
			NativeCaller.PPPPStartAudio(strDID);
		}
	}

	/**
	 * 关闭声音
	 */
	private void StopAudio() {
		synchronized (this) {
			audioPlayer.AudioPlayStop();
			AudioBuffer.ClearAll();
			NativeCaller.PPPPStopAudio(strDID);
		}
	}

	/**
	 * 设置分辨率
	 * 
	 * 
	 * @param Resolution 0：vga 。1：qvga。3：720p
	 */
	protected void setResolution(int Resolution) {
		LogUtils.i("分辨率：");
		NativeCaller.PPPPCameraControl(strDID, 16, Resolution);
	}

	/**
	 * 初始化控件
	 */
	@SuppressWarnings("deprecation")
	private void findView() {
		playSurface = (SurfaceView) findViewById(R.id.playSurface);
		playSurface.setBackgroundColor(0xff000000);
		//控制按钮
		ll_control = (LinearLayout) findViewById(R.id.ll_control);
		
		ImageView imgUp = null;
		ImageView imgDown = null;
		ImageView imgRight = null;
		ImageView imgLeft = null;
		// 上下左右按键
		imgUp = (ImageView) findViewById(R.id.imgup);
		imgDown = (ImageView) findViewById(R.id.imgdown);
		imgRight = (ImageView) findViewById(R.id.imgright);
		imgLeft = (ImageView) findViewById(R.id.imgleft);
		imgUp.setOnClickListener(this);
		imgDown.setOnClickListener(this);
		imgLeft.setOnClickListener(this);
		imgRight.setOnClickListener(this);

		button_back = (ImageButton) findViewById(R.id.login_top_back);
		button_back.setOnClickListener(this);
		vidoeView = (ImageView) findViewById(R.id.vedioview);
		videoViewStandard = (ImageView) findViewById(R.id.vedioview_standard);
		// 等待框
		progressView = (View) findViewById(R.id.progressLayout);
		textosd = (TextView) findViewById(R.id.textosd);
		textTimeoutTextView = (TextView) findViewById(R.id.textTimeout);
		osdView = (View) findViewById(R.id.osdlayout);
		initMenuButtons();// 初始化下边菜单按钮

		topbg = (RelativeLayout) findViewById(R.id.top_bg);// 上边菜单键的根view
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.top_bg);
		// 菜单背景图片平铺效果
		drawable = new BitmapDrawable(bitmap);
		drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		drawable.setDither(true);
		topbg.setBackgroundDrawable(drawable);
		ptzOtherSetAnimView.setBackgroundDrawable(drawable);
	}

	/**
	 * 初始化菜单按钮
	 */
	private void initMenuButtons() {
		// 镜像按钮
		ptzHoriMirror2 = (ImageButton) findViewById(R.id.ptz_hori_mirror);
		ptzVertMirror2 = (ImageButton) findViewById(R.id.ptz_vert_mirror);
		// 巡航按钮
		ptzHoriTour2 = (ImageButton) findViewById(R.id.ptz_hori_tour);
		ptzVertTour2 = (ImageButton) findViewById(R.id.ptz_vert_tour);
		// 通话按钮
		ptzAudio = (ImageButton) findViewById(R.id.ptz_audio);
		// 亮度和对比度
		ImageButton ptzBrightness = (ImageButton) findViewById(R.id.ptz_brightness);
		ImageButton ptzContrast = (ImageButton) findViewById(R.id.ptz_contrast);
		// VGA按钮
		ptzResolutoin = (Button) findViewById(R.id.ptz_resoluti);
		// 是否全屏、拉伸
		ptzPlayMode = (ImageButton) findViewById(R.id.ptz_playmode);
		// 下边菜单
		ptzOtherSetAnimView = findViewById(R.id.ptz_othersetview_anim);
		// 回复初始设置
		ImageButton ptzDefaultSet = (ImageButton) findViewById(R.id.ptz_default_set);
		ptzHoriMirror2.setOnClickListener(this);
		ptzVertMirror2.setOnClickListener(this);
		ptzHoriTour2.setOnClickListener(this);
		ptzVertTour2.setOnClickListener(this);
		ptzAudio.setOnClickListener(this);
		ptzBrightness.setOnClickListener(this);
		ptzContrast.setOnClickListener(this);
		ptzResolutoin.setOnClickListener(this);
		ptzPlayMode.setOnClickListener(this);
		ptzDefaultSet.setOnClickListener(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		mBaseMatrix = new Matrix();
		mSuppMatrix = new Matrix();
		mDisplayMatrix = new Matrix();
		videoViewStandard.setImageMatrix(mDisplayMatrix);
	}

	private boolean isDown = false;
	private boolean isSecondDown = false;
	private float x1 = 0;
	private float x2 = 0;
	private float y1 = 0;
	private float y2 = 0;

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (!isDown) {
			x1 = event.getX();
			y1 = event.getY();
			isDown = true;
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			originalScale = getScale();
			break;
		case MotionEvent.ACTION_POINTER_UP:

			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs((x1 - x2)) < 25 && Math.abs((y1 - y2)) < 25) {

				if (resolutionPopWindow != null
						&& resolutionPopWindow.isShowing()) {
					resolutionPopWindow.dismiss();
				}

				if (mPopupWindowProgress != null
						&& mPopupWindowProgress.isShowing()) {
					mPopupWindowProgress.dismiss();
				}
				if (!isSecondDown) {
					if (!bProgress) {// 如果没有读条
						showTop();
						showBottom();
					}
				}
				isSecondDown = false;
			} else {
			}
			x1 = 0;
			x2 = 0;
			y1 = 0;
			y2 = 0;
			isDown = false;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			isSecondDown = true;
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			x2 = event.getX();
			y2 = event.getY();

			int midx = getWindowManager().getDefaultDisplay().getWidth() / 2;
			int midy = getWindowManager().getDefaultDisplay().getHeight() / 2;
			if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 0f) {
					float scale = newDist / oldDist;
					Log.d("scale", "scale:" + scale);
					if (scale <= 2.0f && scale >= 0.2f) {
						// zoomTo(originalScale * scale, midx, midy);
					}
				}
			}
		}

		return gt.onTouchEvent(event);
	}

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	float mMaxZoom = 2.0f;
	float mMinZoom = 0.3125f;
	float originalScale;
	float baseValue;
	protected Matrix mBaseMatrix = new Matrix();
	protected Matrix mSuppMatrix = new Matrix();
	private Matrix mDisplayMatrix = new Matrix();
	private final float[] mMatrixValues = new float[9];

	protected void zoomTo(float scale, float centerX, float centerY) {
		Log.d("zoomTo", "zoomTo scale:" + scale);
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		} else if (scale < mMinZoom) {
			scale = mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;
		Log.d("deltaScale", "deltaScale:" + deltaScale);
		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		videoViewStandard.setScaleType(ImageView.ScaleType.MATRIX);
		videoViewStandard.setImageMatrix(getImageViewMatrix());
	}

	protected Matrix getImageViewMatrix() {
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	protected float getScale() {
		return getScale(mSuppMatrix);
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}

	private float spacing(MotionEvent event) {
		try {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float)Math.sqrt(x * x + y * y);
		} catch (Exception e) {
		}
		return 0;
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d("tag", "onDown");
		return false;
	}

	private final int MINLEN = 80;
	private RelativeLayout topbg;
	private Animation showTopAnim;
	private Animation dismissTopAnim;
	private ImageButton ptzHoriMirror2;
	private ImageButton ptzVertMirror2;
	private ImageButton ptzHoriTour2;
	private ImageButton ptzVertTour2;
	private boolean isPTZPrompt;

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float x1 = e1.getX();
		float x2 = e2.getX();
		float y1 = e1.getY();
		float y2 = e2.getY();

		float xx = x1 > x2 ? x1 - x2 : x2 - x1;
		float yy = y1 > y2 ? y1 - y2 : y2 - y1;

		if (xx > yy) {
			if ((x1 > x2) && (xx > MINLEN)) {// left
				NativeCaller
						.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_RIGHT);
			} else if ((x1 < x2) && (xx > MINLEN)) {// right
				NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_LEFT);
			}

		} else {
			if ((y1 > y2) && (yy > MINLEN)) {// down
				NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_DOWN);
			} else if ((y1 < y2) && (yy > MINLEN)) {// up
				NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_UP);
			}

		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public void showSureDialogPlay() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.app);
		builder.setTitle(getResources().getString(R.string.exit_show));
		builder.setMessage(R.string.exit_play_show);
		builder.setPositiveButton(R.string.str_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						SmallPlayActivity.this.finish();
					}
				});
		builder.setNegativeButton(R.string.str_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						bManualExit = false;
					}
				});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.login_top_back:
			bManualExit = true;
			if (!bProgress) {// 如果是监控中
				if (isTakeVideo == true) {// 如果正在录像
					showToast(R.string.eixt_show_toast);
				} else {
					showSureDialogPlay();
				}
			}
			break;
		case R.id.imgup:
			NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_UP);
			Log.d("tag", "up");
			break;
		case R.id.imgdown:
			NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_DOWN);
			Log.d("tag", "down");
			break;
		case R.id.imgleft:
			NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_LEFT);
			Log.d("tag", "left");
			break;
		case R.id.imgright:
			NativeCaller.PPPPPTZControl(strDID, ContentCommon.CMD_PTZ_RIGHT);
			Log.d("tag", "right");
			break;
		case R.id.ptz_hori_mirror:
			if (isHorizontalMirror) {
				ptzHoriMirror2.setBackgroundColor(0x00ffffff);
				isHorizontalMirror = false;
				NativeCaller.PPPPCameraControl(strDID, 5,
						ContentCommon.CMD_PTZ_ORIGINAL);
				Log.d("tag", "水平镜像还原：" + ContentCommon.CMD_PTZ_ORIGINAL);
			} else {
				isHorizontalMirror = true;
				ptzHoriMirror2.setBackgroundColor(0xff0044aa);
				NativeCaller.PPPPCameraControl(strDID, 5,
						ContentCommon.CMD_PTZ_HORIZONAL_MIRROR);
				Log.d("tag", "水平镜像：" + ContentCommon.CMD_PTZ_HORIZONAL_MIRROR);
			}
			break;
		case R.id.ptz_vert_mirror:
			if (isVerticalMirror) {
				isVerticalMirror = false;
				ptzVertMirror2.setBackgroundColor(0x00ffffff);
				NativeCaller.PPPPCameraControl(strDID, 5,
						ContentCommon.CMD_PTZ_ORIGINAL);
				Log.d("tag", "垂直镜像还原：" + ContentCommon.CMD_PTZ_ORIGINAL);
			} else {
				isVerticalMirror = true;
				ptzVertMirror2.setBackgroundColor(0xff0044aa);
				NativeCaller.PPPPCameraControl(strDID, 5,
						ContentCommon.CMD_PTZ_VERTICAL_MIRROR);
				Log.d("tag", "垂直镜像：" + ContentCommon.CMD_PTZ_VERTICAL_MIRROR);
			}
			break;

		case R.id.ptz_hori_tour:
			if (isLeftRight) {
				ptzHoriTour2.setBackgroundColor(0x000044aa);
				isLeftRight = false;
				NativeCaller.PPPPPTZControl(strDID,
						ContentCommon.CMD_PTZ_LEFT_RIGHT_STOP);
				Log.d("tag", "水平巡视停止:" + ContentCommon.CMD_PTZ_LEFT_RIGHT_STOP);
			} else {
				ptzHoriTour2.setBackgroundColor(0xff0044aa);
				isLeftRight = true;
				NativeCaller.PPPPPTZControl(strDID,
						ContentCommon.CMD_PTZ_LEFT_RIGHT);
				Log.d("tag", "水平巡视开始:" + ContentCommon.CMD_PTZ_LEFT_RIGHT);
			}
			break;
		case R.id.ptz_vert_tour:
			if (isUpDown) {
				ptzVertTour2.setBackgroundColor(0x000044aa);
				isUpDown = false;
				NativeCaller.PPPPPTZControl(strDID,
						ContentCommon.CMD_PTZ_UP_DOWN_STOP);
				Log.d("tag", "垂直巡视停止:" + ContentCommon.CMD_PTZ_UP_DOWN_STOP);
			} else {
				ptzVertTour2.setBackgroundColor(0xff0044aa);
				isUpDown = true;
				NativeCaller.PPPPPTZControl(strDID,
						ContentCommon.CMD_PTZ_UP_DOWN);
				Log.d("tag", "垂直巡视开始:" + ContentCommon.CMD_PTZ_UP_DOWN);
			}
			break;
		case R.id.ptz_audio:
			dismissBrightAndContrastProgress();
			if (!isMcriophone) {
				if (bAudioStart) {
					Log.d("tag", "没有声音");
					bAudioStart = false;
					ptzAudio.setImageResource(R.drawable.ptz_audio_off);
					StopAudio();
				} else {
					Log.d("tag", "有声音");
					bAudioStart = true;
					ptzAudio.setImageResource(R.drawable.ptz_audio_on);
					StartAudio();
				}
			}
			break;
		case R.id.ptz_brightness:// 亮度
			if (mPopupWindowProgress != null
					&& mPopupWindowProgress.isShowing()) {
				mPopupWindowProgress.dismiss();
				mPopupWindowProgress = null;
			}
			setBrightOrContrast(BRIGHT);
			break;
		case R.id.ptz_contrast:// 对比度
			if (mPopupWindowProgress != null
					&& mPopupWindowProgress.isShowing()) {
				mPopupWindowProgress.dismiss();
				mPopupWindowProgress = null;
			}
			setBrightOrContrast(CONTRAST);
			break;
		case R.id.ptz_resoluti:// 720.vga等
			popupWindow_about.showAtLocation(button_back, Gravity.CENTER, 0, 0);
			break;
		case R.id.ptz_resolution_jpeg_qvga:
			ToastUtils.showToastWhendebug("没有使用？");
			dismissBrightAndContrastProgress();
			resolutionPopWindow.dismiss();
			nResolution = 1;
			setResolution(nResolution);
			Log.d("tag", "jpeg resolution:" + nResolution + " qvga");
			break;
		case R.id.ptz_resolution_jpeg_vga:
			ToastUtils.showToastWhendebug("没有使用？");
			dismissBrightAndContrastProgress();
			resolutionPopWindow.dismiss();
			nResolution = 0;
			setResolution(nResolution);
			Log.d("tag", "jpeg resolution:" + nResolution + " vga");
			break;
		case R.id.ptz_resolution_h264_qvga:
			ToastUtils.showToastWhendebug("没有使用？");
			dismissBrightAndContrastProgress();
			resolutionPopWindow.dismiss();
			nResolution = 1;
			setResolution(nResolution);// 设置分辨率
			Log.d("tag", "h264 resolution:" + nResolution + " qvga");
			break;
		case R.id.ptz_resolution_h264_vga:
			ToastUtils.showToastWhendebug("没有使用？");
			dismissBrightAndContrastProgress();
			resolutionPopWindow.dismiss();
			nResolution = 0;
			setResolution(nResolution);
			Log.d("tag", "h264 resolution:" + nResolution + " vga");
			break;
		case R.id.ptz_resolution_h264_720p:
			ToastUtils.showToastWhendebug("没有使用？");
			dismissBrightAndContrastProgress();
			resolutionPopWindow.dismiss();
			nResolution = 3;
			setResolution(nResolution);
			Log.d("tag", "h264 resolution:" + nResolution + " 720p");
			break;
		case R.id.ptz_playmode:// 切换视频样式？
			dismissBrightAndContrastProgress();
			switch (playmode) {
			case FULLSCREEN:
				ptzPlayMode.setImageResource(R.drawable.ptz_playmode_enlarge);
				ptzPlayMode
						.setBackgroundResource(R.drawable.ptz_takepic_selector);
				Log.d("tg", "magnify 1");
				playmode = STANDARD;
				break;
			case MAGNIFY:
				Log.d("tg", "STANDARD 2");
				playmode = FULLSCREEN;
				ptzPlayMode.setImageResource(R.drawable.ptz_playmode_standard);
				ptzPlayMode
						.setBackgroundResource(R.drawable.ptz_takepic_selector);
				break;
			case STANDARD:
				Log.d("tg", "FULLSCREEN 3");
				playmode = MAGNIFY;
				ptzPlayMode
						.setImageResource(R.drawable.ptz_playmode_fullscreen);
				ptzPlayMode
						.setBackgroundResource(R.drawable.ptz_takepic_selector);
				break;
			default:
				break;
			}

			break;
		case R.id.ptz_default_set:
			dismissBrightAndContrastProgress();
			defaultVideoParams();
			break;
		}
	}

	private void dismissBrightAndContrastProgress() {
		if (mPopupWindowProgress != null && mPopupWindowProgress.isShowing()) {
			mPopupWindowProgress.dismiss();
			mPopupWindowProgress = null;
		}
	}

	private void showBottom() {
		if (isUpDownPressed) {
			isUpDownPressed = false;
			ptzOtherSetAnimView.startAnimation(dismissAnim);
			ptzOtherSetAnimView.setVisibility(View.GONE);
		} else {
			isUpDownPressed = true;
			ptzOtherSetAnimView.startAnimation(showAnim);
			ptzOtherSetAnimView.setVisibility(View.VISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	private void setBrightOrContrast(final int type) {
		Log.i(LOG_TAG, "type:" + type + "  bInitCameraParam:"
				+ bInitCameraParam);
		if (!bInitCameraParam) {
			return;
		}
		int width = getWindowManager().getDefaultDisplay().getWidth();
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.brightprogress, null);
		SeekBar seekBar = (SeekBar) layout.findViewById(R.id.brightseekBar1);
		seekBar.setMax(255);
		switch (type) {
		case BRIGHT:
			seekBar.setProgress(nBrightness);
			break;
		case CONTRAST:
			seekBar.setProgress(nContrast);
			break;
		default:
			break;
		}
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				switch (type) {
				case BRIGHT:// 亮度
					nBrightness = progress;
					NativeCaller.PPPPCameraControl(strDID, BRIGHT, nBrightness);
					break;
				case CONTRAST:// 对比度
					nContrast = progress;
					NativeCaller.PPPPCameraControl(strDID, CONTRAST, nContrast);
					break;
				default:
					break;
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int progress,
					boolean arg2) {

			}
		});

		mPopupWindowProgress = new PopupWindow(layout, width / 2, 180);
		mPopupWindowProgress.showAtLocation(findViewById(R.id.play),
				Gravity.TOP, 0, 0);

	}

	private MyRender myRender = null;
	/** GLSurfaceView */
	// private GLSurfaceView myGlSurfaceView = null;
	
	/** 上下左右的控制键 */
	private LinearLayout ll_control;

	@Override
	protected void onDestroy() {
//		NativeCaller.StopPPPPLivestream(strDID);
		StopAudio();
		if (myRender != null) {
			myRender.destroyShaders();
		}
		if (brodCast != null) {
			unregisterReceiver(brodCast);
		}
		Log.d("tag", "PlayActivity onDestroy");

		super.onDestroy();
	}

	/***
	 * BridgeService callback
	 * 
	 * **/
	@Override
	public void callBackCameraParamNotify(String did, int resolution,
			int brightness, int contrast, int hue, int saturation, int flip) {
		Log.d("info", "CameraParamNotify...did:" + did + " brightness: "
				+ brightness + " resolution: " + resolution + " contrast: "
				+ contrast + " hue: " + hue + " saturation: " + saturation
				+ " flip: " + flip);
		Log.d("tag", "contrast:" + contrast + " brightness:" + brightness);
		nBrightness = brightness;
		nContrast = contrast;
		nResolution = resolution;
		Log.d("VGA", nResolution + "");
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (nResolution == 0) {
					// vga
					ptzResolutoin.setText("VGA");
				} else if (nResolution == 3) {
					// 720
					ptzResolutoin.setText("720P");
				} else if (nResolution == 1) {
					// 720
					ptzResolutoin.setText("QVGA");
				}
			}
		});
		Message msg = new Message();
		msg.what = 3;
		mHandler.sendMessage(msg);

		bInitCameraParam = true;
	}

	/***
	 * 收到视频数据后的回调
	 * 
	 * **/
	@Override
	public void callBaceVideoData(String did,byte[] videobuf, int h264Data, int len,
			int width, int height) {
		Log.d("info", "Call VideoData...h264Data: " + h264Data + " len: " + len
				+ " videobuf len: " + videobuf.length + "width==" + nVideoWidth
				+ "height==" + nVideoHeight);
		if (!bDisplayFinished) {
			Log.d("info", "return bDisplayFinished");
			return;
		}
		bDisplayFinished = false;

		videodata = videobuf;
		videoDataLen = len;
		nVideoWidths = width;// TODO log:传进来的数据就是720*1280
		nVideoHeights = height;
		Message msg = new Message();
		if (h264Data == 1) { // H264
			Log.i("info", "h264Data....");
			if (isTakepic) {
				ToastUtils.showToastWhendebug("takePic");
				isTakepic = false;
				byte[] rgb = new byte[width * height * 2];
				NativeCaller.YUV4202RGB565(videobuf, rgb, width, height);
				ByteBuffer buffer = ByteBuffer.wrap(rgb);
				mBmp = Bitmap
						.createBitmap(width, height, Bitmap.Config.RGB_565);
				mBmp.copyPixelsFromBuffer(buffer);
				// takePicture(mBmp);
			}
			msg.what = 1;
		} else { // MJPEG
			Log.i("info", "MJPEG....");
			msg.what = 2;
		}
		mHandler.sendMessage(msg);
	}

	/***
	 * BridgeService callback
	 * 
	 * **/
	@Override
	public void callBackMessageNotify(String did, int msgType, int param) {
		Log.d("tag", "MessageNotify did: " + did + " msgType: " + msgType
				+ " param: " + param);
		if (bManualExit)
			return;

		if (msgType == ContentCommon.PPPP_MSG_TYPE_STREAM) {
			nStreamCodecType = param;
			Message msgMessage = new Message();
			msgStreamCodecHandler.sendMessage(msgMessage);
			return;
		}

		if (msgType != ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS) {
			return;
		}

		if (!did.equals(strDID)) {
			return;
		}

		Message msg = new Message();
		msg.what = 1;
		// 断线了，关闭页面
		msgHandler.sendMessage(msg);
	}

	/***
	 * BridgeService callback
	 * 
	 * **/
	@Override
	public void callBackAudioData(byte[] pcm, int len) {
		Log.d(LOG_TAG, "AudioData: len :+ " + len);
		if (!audioPlayer.isAudioPlaying()) {
			return;
		}
		CustomBufferHead head = new CustomBufferHead();
		CustomBufferData data = new CustomBufferData();
		head.length = len;
		head.startcode = AUDIO_BUFFER_START_CODE;
		data.head = head;
		data.data = pcm;
		AudioBuffer.addData(data);
	}

	/***
	 * BridgeService callback
	 * 
	 * **/
	@Override
	public void callBackH264Data(byte[] h264, int type, int size) {
		Log.d("tag", "CallBack_H264Data" + " type:" + type + " size:" + size);
	}

}
