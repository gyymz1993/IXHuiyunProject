  package com.ixhuiyunproject.ipcamer.demo;

  import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.CameraDevice;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.label.service.ToastService;
import com.ixhuiyunproject.ipcamer.demo.my.CameraCoreUtil;
import com.ixhuiyunproject.ipcamer.demo.my.NetUtilForCamera;
import com.ixhuiyunproject.vstc2.nativecaller.NativeCaller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * 每次切换摄像头要重新连接一遍StartPPPP+StartPPPPLivestream
 */
/**
 * 播放视频的类
 * 实现了查找摄像机，连接摄像机，播放摄像机状态的接口
 * @author lzy_torah
 *
 */
public class AddCameraActivity extends Activity implements BridgeService.AddCameraInterface,
		BridgeService.IpcamClientInterface {
	/** 要展示的当前的camera */
	private CameraDevice current = new CameraDevice();
	/** 搜索时间 */
	private static final int SEARCH_TIME = 2000;
	/** 是否已经找到设备 */
	private boolean isSearched = false;
	private static final String STR_DID = "did";
	private static final String STR_MSG_PARAM = "msgparam";
	/** 当前的要改名的 */
	CameraDevice currentRename;

	/**
	 * 初始化视频搜索
	 */
	private void initSearchCamera() {
		new Thread() {
			@Override
			public void run() {
				try {
					// 是局域网才找摄像头
					if (!StaticValue.isRemote()) {
						showWaitingDialog();// 显示等待框
						searchCamera();// 搜索摄像头
					} else {
						ToastUtils.showToastReal("本地登录才能搜索");
					}
				} catch (Exception e) {
					LogUtils.e("搜索设备错误");
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera_choose);
		initGesture();// 初始化手势
		initView();// 初始化各个组件
		initShowView();// 初始化展示视频的控件

		if (!ToastService.showCamera) {
			LogUtils.w("开始初始化视频相关");
			// 初始化数据回调service
			Intent intent = new Intent();
			intent.setClass(this, BridgeService.class);
			startService(intent);
			cameraNativeInit();// 初始化视频相关
		} else {
			current.setDid(SystemValue.deviceId);
			current.setName(SystemValue.deviceName);
			current.setPsw(SystemValue.devicePass);
			current.setTag(3);//设为正在播放
		}
		// 初始化回调接口
		BridgeService.setAddCameraInterface(this);// 查找摄像头的回调
		BridgeService.setIpcamClientInterface(this); // 连接摄像头的回调
		BridgeService.setPlayInterface(CameraCoreUtil.getInstance());// 设置开始播放的回调
		imgDataListener = new ImgDataListener();
		CameraCoreUtil.getInstance().addVideoPiclistener(imgDataListener);// 视频图像的回调接口

		getCameras();// 从主机得到摄像头的信息
	}

	/**
	 * 初始化视频
	 */
	private void cameraNativeInit() {
		new Thread() {
			@Override
			public void run() {
				try {
					NativeCaller.Init(); // 为播放视频初始化
					NativeCaller // EBGBEMBMKGJMGAJPEIGIFKEGHBMCHMJHCKBMBHGFBJNOLCOLCIEBHFOCCHKKJIKPBNMHLHCPPFMFADDFIINOIABFMH
							.PPPPInitialOther("EBGBEMBMKGJMGAJPEIGIFKEGHBMCHMJHCKBMBHGFBJNOLCOLCIEBHFOCCHKKJIKPBNMHLHCPPFMFADDFIINOIABFMH");// 初始化默认服务器,如无定制服务器则不需要修改
					NativeCaller.PPPPNetworkDetect();
					LogUtils.w("初始化视频成功");
				} catch (Exception e) {
					LogUtils.e("初始化视频错误");
					e.printStackTrace();
				}

			}
		}.start();
	}

	/**
	 * 初始化手势
	 */
	private void initGesture() {
		gestureDetector = new GestureDetector(this, new OnGestureListener() {
			private final int MINLEN = 30;

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				System.out.println("onSingleTapUp");
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				System.out.println("onShowPress");
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				System.out.println("onScroll");
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				System.out.println("onLongPress");
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				float x1 = e1.getX();
				float x2 = e2.getX();
				float y1 = e1.getY();
				float y2 = e2.getY();
				float xx = x1 > x2 ? x1 - x2 : x2 - x1;
				float yy = y1 > y2 ? y1 - y2 : y2 - y1;
				if (xx > MINLEN || yy > MINLEN) {
					LogUtils.e("触屏手势");
				}
				if (xx > yy) {
					if ((x1 > x2) && (xx > MINLEN)) {// left
						NativeCaller.PPPPPTZControl(current.getDid(),
								ContentCommon.CMD_PTZ_RIGHT);
					} else if ((x1 < x2) && (xx > MINLEN)) {// right
						NativeCaller.PPPPPTZControl(current.getDid(),
								ContentCommon.CMD_PTZ_LEFT);
					}

				} else {
					if ((y1 > y2) && (yy > MINLEN)) {// down
						NativeCaller.PPPPPTZControl(current.getDid(),
								ContentCommon.CMD_PTZ_DOWN);
					} else if ((y1 < y2) && (yy > MINLEN)) {// up
						NativeCaller.PPPPPTZControl(current.getDid(),
								ContentCommon.CMD_PTZ_UP);
					}
				}
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				System.out.println("onDown");
				return false;
			}
		});
	}

	/**
	 * 下载摄像头信息
	 */
	private void getCameras() {
		NetUtilForCamera
				.downLoadCamera(new OnResultListener<List<CameraDevice>>() {

					@Override
					public void onResult(boolean isSecceed,
							List<CameraDevice> obj) {
						if (isSecceed) {
							cameraList = obj;
							showHandler.sendEmptyMessage(-1);
						}
					}
				});
	}

	/**
	 * 初始化视频的展示相关
	 */
	private void initShowView() {
		videoViewStandard = (ImageView) findViewById(R.id.vedioview_standard);
		InitViewParams();
	}

	/** 显示视频的控件 */
	private ImageView videoViewStandard;

	// 视频的长宽
	public int nVideoWidths = 0;
	public int nVideoHeights = 0;
	/** 显示当前的分辨率 */
	private int nResolution = 0;
	/** 流的类型? */
	private int streamType = ContentCommon.MJPEG_SUB_STREAM;
	/** 屏幕的高度 */
	private int screenHeight;
	/** 屏幕的宽度 */
	private int screenWidth;
	// 显示视频的大小
	private int vedioPicHeight;
	private int vedioPicWidth;
	/**
	 * 等待框
	 */
	private ProgressDialog progressdlg = null;
	/**
	 * 修改显示视频的大小
	 */
	private void InitViewParams() {
		rl_vidioClip = findViewById(R.id.rl_vidioClip);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		vedioPicHeight = dm.widthPixels * 9 / 16;
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		vedioPicWidth = dm.widthPixels;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				vedioPicWidth, vedioPicHeight);
		rl_vidioClip.setLayoutParams(lp);// 修改成16:9
		rl_vidioClip.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	/**
	 * 显示改名dialog
	 */
	void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		final AlertDialog dialog = builder.setTitle("请输入要改的名字").setView(input)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						String value = input.getText().toString();
						if (!StringUtils.isEmpty(value)) {
							currentRename.setName(value);
							adapter.notifyDataSetChanged();
							dialog.dismiss();
							uploadNames();
						}
					}
				}).setNegativeButton("取消", null).create();
		dialog.show();
	}

	/**
	 * 上传名字
	 */
	protected void uploadNames() {
		MyJsonObj2 jsonobj2 = JsonUtil.getAJsonObj2ForMaster();
		jsonobj2.code = 61;
		for (CameraDevice d : cameraList) {
			String did = d.getDid();
			String name = d.getName();
			Map<String, String> map = new HashMap<String, String>();
			map.put("did", did);
			map.put("area", name);
			jsonobj2.data.list.add(map);
		}
		jsonobj2.data.token = StaticValue.user.getToken();
		JSONModuleManager.getInstance().result_62
				.setOnCmdReseivedListener(new OnResultListener<Object>() {

					@Override
					public void onResult(boolean isSecceed, Object obj) {
						if (isSecceed) {
							ToastUtils.showToastReal("修改区域成功");
							JSONModuleManager.getInstance().result_62
									.setOnCmdReseivedListener(null);
						}
					}
				});
		NetJsonUtil.getInstance().addCmdForSend(jsonobj2);
	}

	/**
	 * 初始化view的方法
	 */
	private void initView() {
		// 初始化标题
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText("监控");
		// 退出按钮
		ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		rl_actionbar = findViewById(R.id.rl_actionbar);
		View iv_right = findViewById(R.id.iv_right);
		iv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openOptionsMenu();
			}
		});
		// 动画控件
		iv_animation = (ImageView) findViewById(R.id.iv_animation);
		//iv_animation.setImageResource(R.anim.gif_camera_no_signal);
		// listview
		ListView lv_cameras = (ListView) findViewById(R.id.lv_cameras);
		if (adapter == null)
			adapter = new ChooseCameraAdapter();
		lv_cameras.setAdapter(adapter);
		// 选择摄像头，开始监控画面
		lv_cameras.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtils.e("点击了第 "+position);
				// startCamGifAnimation();//动画效果
				if (current.equals(cameraList.get(position))
						&& (current.getTag() == 1 || current.getTag() == 3)) {
					ToastUtils.showToastReal("已是该摄像机");
					return;
				}
				// 停止旧的
				if (current.getTag() == 1 || current.getTag() == 3) {
					current.setTag(1);
					NativeCaller.StopPPPPLivestream(current.getDid());
				}
				LogUtils.e("点击了第 "+position+" 生效");
				// 初始化current
				current = cameraList.get(position);
				// 连接设备
				initCameraConnected();
				// 每隔一秒判断一下状态。
				Message msg = Message.obtain();
				msg.what = 1;
				showHandler.sendMessageDelayed(msg, 1000);
			}

		});
		lv_cameras.setOnItemLongClickListener(new OnItemLongClickListener() {
			// 长按改名
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentRename = cameraList.get(position);
				showDialog();
				return true;
			}
		});
		// 本地查找摄像机
		btn_lookforcamera = (Button) findViewById(R.id.btn_lookforcamera);
		btn_lookforcamera.setVisibility(View.GONE);
		iv_enterFullScreen = findViewById(R.id.iv_enterFullScreen);
		iv_enterFullScreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (current != null) {
					if (current.getTag() == 1 || current.getTag() == 3) {
						enterFullScreen();
					} else {
						ToastUtils.showToastReal("当前的摄像机未连接");
					}
				}
			}
		});
		if (progressdlg == null) {
			progressdlg = new ProgressDialog(AddCameraActivity.this);
			progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressdlg.setMessage(getString(R.string.searching_tip));
		}
	}

	/**
	 * 进入全屏显示
	 * 
	 */
	protected void enterFullScreen() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 重设current的tag。adapter刷新 2:连接成功 1：切换摄像头
	 * 
	 * */
	Handler showHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1 || msg.what == 2) {// 收到callBaceVideoData回调后发送此信息
				getCameraParams();// 用于实时监控摄像头的状态
			}
			switch (msg.what) {
			case 1:// 循环更新状态
				if (current.getTag() == CameraDevice.tag_conncted) {// 连接成功
					switchCamera();
					showHandler.sendEmptyMessageDelayed(2, 1000);
				} else {
					showHandler.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			case 2:
				if (current.getTag() == CameraDevice.tag_conncted) {// 连接成功
					current.setTag(CameraDevice.tag_playing);
				}
				break;

			default:
				break;
			}
			adapter.notifyDataSetChanged();
		};
	};

	/**
	 * 显示camera的listadapter
	 * 
	 * @author lzy_torah
	 *
	 */
	public class ChooseCameraAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cameraList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				convertView = View.inflate(AddCameraActivity.this,
						R.layout.item_camera_choose, null);
				holder = new Holder();
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_state = (TextView) convertView
						.findViewById(R.id.tv_state);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.camera = cameraList.get(position);
			switch (holder.camera.getTag()) {
			/*
			 * 0:没有连接成功 tag_notConn = 0; 1:连接成功 tag_conncted = 1; 2:连接中
			 * tag_conning = 2; 3:播放中 tag_playing = 3;
			 */
			case 3:
				holder.tv_state.setText("监控中");
				holder.tv_state.setTextColor(0xffdddd00);
				break;
			case 2:
				holder.tv_state.setText("连接中");
				holder.tv_state.setTextColor(0xff888888);
				break;
			case 1:
				holder.tv_state.setText("连接成功");
				holder.tv_state.setTextColor(0xff00dd00);
				break;
			case 0:
				holder.tv_state.setText("未连接");
				holder.tv_state.setTextColor(0xffdd0000);
				break;
			default:
				break;
			}
			holder.tv_name.setText(holder.camera.getName());
			return convertView;
		}

		class Holder {
			/** 名字 */
			TextView tv_name;
			/** 状态 */
			TextView tv_state;
			CameraDevice camera;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "监控悬浮");
		menu.add(0, 1, 0, "切换悬浮窗大小");
		menu.add(0, 2, 0, "本地搜索");
		menu.add(0, 3, 0, "关闭悬浮");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ToastUtils.showToastWhendebug(item.getTitle());
		switch (item.getItemId()) {
		case 0:
			if (!ToastService.showCamera)
				startCamera();// 开启悬浮监控
			break;
		case 1:
			if (ToastService.showCamera)
				ToastService.changeCameraSize();
			else {
				startCamera();// 开启悬浮监控
			}
			break;
		case 2:
			initSearchCamera();// 初始化搜索摄像头准备
			break;
		case 3:
			ToastService.closeCameraAndstartVoice();
			// 刷新悬浮窗
			Intent intent = new Intent(this, ToastService.class);
			intent.putExtra("flush", 1);
			startService(intent);
			break;
		default:
			break;
		}
		return true;

	}

	/**
	 * 开启悬浮监控
	 */
	private void startCamera() {
		ToastService.startCamera();
		// 刷新悬浮窗
		Intent intent = new Intent(this, ToastService.class);
		intent.putExtra("flush", 1);
		startService(intent);
	}

	@Override
	public void onBackPressed() {
		if (isScreenLANDSCAPE()) {// 横屏
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtils.e("屏幕尺寸变化了");
		if (isScreenLANDSCAPE()) {// 横屏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			vedioPicHeight = screenWidth;
			vedioPicWidth = screenHeight;
			rl_actionbar.setVisibility(View.GONE);// 标题
			iv_enterFullScreen.setVisibility(View.GONE);
			reinitVedioSize();
		} else {// 竖屏
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			vedioPicHeight = screenWidth * 9 / 16;
			vedioPicWidth = screenWidth;
			rl_actionbar.setVisibility(View.VISIBLE);
			iv_enterFullScreen.setVisibility(View.VISIBLE);
			reinitVedioSize();
		}
	}

	/**
	 * 重新设置video的尺寸
	 */
	void reinitVedioSize() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				vedioPicWidth, vedioPicHeight);
		rl_vidioClip.setLayoutParams(lp);
	}

	public boolean isScreenLANDSCAPE() {
		Configuration mConfiguration = this.getResources().getConfiguration(); // 获取设置的配置信息
		int ori = mConfiguration.orientation; // 获取屏幕方向
		if (ori == Configuration.ORIENTATION_LANDSCAPE) {
			// 横屏
			LogUtils.w("屏幕当前方向横向");
			return true;
		} else if (ori == Configuration.ORIENTATION_PORTRAIT) {
			// 竖屏
			LogUtils.w("屏幕当前方向纵向");
			return false;
		}
		return false;
	}

	@Override
	protected void onStop() {
		progressDlgHandler.sendEmptyMessage(2);// 消失等待框
//		try {
//			NativeCaller.StopSearch(); // 停止寻找
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogUtils.w("AddCameraActivity被回收了");
		// 取消观察者
		CameraCoreUtil.getInstance().removeVideoPiclistener(imgDataListener);
		if (!ToastService.showCamera) {
			CameraCoreUtil.recycleCameraThings(this);// 回收监控资源
		}
		super.onDestroy();
	}

	/** 停止查找，消失等待框 */
	Runnable updateThread = new Runnable() {
		public void run() {
			NativeCaller.StopSearch();// 停止查找
			progressDlgHandler.sendEmptyMessage(2);
			updateListHandler.sendEmptyMessage(1);
		}
	};
	Handler updateListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				if (cameraList.size() == 0) {
					isSearched = false;
					ToastUtils.showToastReal("没有搜索到，请重试");
				}
				break;
			case 3:// 如果是本地则上传摄像头信息
				if (!StaticValue.isRemote())
					uploadCamera();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 开始局域网搜索后调用
	 */
	private void startSearch() {
		new SearchThread().start();// 开始查找
		// 3秒后停止查找
		updateListHandler.postDelayed(updateThread, SEARCH_TIME);
		updateListHandler.sendEmptyMessageDelayed(3, SEARCH_TIME);// 上传搜索到的摄像头信息
	}

	/**
	 * 显示等待框
	 */
	private void showWaitingDialog() {
		progressDlgHandler.sendEmptyMessage(1);// 等待框
	}

	/**
	 * progressDlg的handler
	 * 1:显示Dialog
	 * 2:消失Dialog
	 */
	Handler progressDlgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (progressdlg != null && (!progressdlg.isShowing()))
					progressdlg.show();
				break;
			case 2:
				if (progressdlg != null && progressdlg.isShowing()) {
					progressdlg.dismiss();
				}
				break;
			default:
				break;
			}

		};
	};

	/**
	 * 搜索摄像头的线程
	 * 
	 * @author torah
	 *
	 */
	private class SearchThread extends Thread {
		@Override
		public void run() {
			Log.d("tag", "startSearch");
			NativeCaller.StartSearch();
		}
	}

	/**
	 * 局域网搜索模式，开始局域网搜索
	 */
	private void searchCamera() {
		if (!isSearched) {
			isSearched = true;
			startSearch();
		} else {
			ToastUtils.showToastReal("已经搜索到设备");
		}
	}

	/**
	 * 开始连接，连接按钮的核心方法
	 */
	private void initCameraConnected() {
		String strUser = current.getUser();
		String strPwd = current.getPsw();
		String strDID = current.getDid();
		LogUtils.i("准备连接监控的摄像机：" + current);
		// 同步到全局变量中
		SystemValue.deviceName = strUser;
		SystemValue.deviceId = strDID;
		SystemValue.devicePass = strPwd;
		CameraCoreUtil.startPPPP();// 开始视频连接
	}

	/**
	 * 局域网查找摄像机的回调
	 * **/
	@Override
	public void callBackSearchResultData(int cameraType, String strMac,
			String strName, String strDeviceID, String strIpAddr, int port) {
		// 将设备添加到list中
		CameraDevice camera = new CameraDevice();
		camera.setDid(strDeviceID);
		camera.setMac(strMac);
		if (!cameraList.contains(camera)) {
			cameraList.add(camera);
			updateListHandler.sendEmptyMessage(1);// 刷新界面
		}
	}

	/**上传摄像头信息
	 * 
	 */
	private void uploadCamera() {
		if (cameraList.size() < 0) {
			LogUtils.e("没有查找到摄像头，不提交摄像头信息");
			return;
		}
		NetUtilForCamera.uploadCamera(cameraList,
				new OnResultListener<Object>() {

					@Override
					public void onResult(boolean isSecceed, Object obj) {
						if (isSecceed) {
							LogUtils.w("上传摄像头信息成功");
						} else {
							LogUtils.w("上传摄像头信息失败");
						}
					}
				});
	}

	/** 储存所有搜到的camera */
	List<CameraDevice> cameraList = new ArrayList<CameraDevice>();

	/** 1.处理相机的状态变化信息 */
	private Handler PPPPMsgHandler = new Handler() {
		public void handleMessage(Message msg) {
			// ToastUtils.showToastReal("状态变化");
			Bundle bd = msg.getData();
			int msgParam = bd.getInt(STR_MSG_PARAM);
			int msgType = msg.what;
			String did = bd.getString(STR_DID);
			// 生成一个假的cam
			CameraDevice cam = new CameraDevice();
			cam.setDid(did);
			// 真的cameraDevice
			CameraDevice cameraDevice = cameraList.get(cameraList.indexOf(cam));
			if (cameraDevice != null) {
				switch (msgType) {
				case ContentCommon.PPPP_MSG_TYPE_PPPP_STATUS:// 摄像机状态变化
					int resid;// 提示信息
					switch (msgParam) {
					case ContentCommon.PPPP_STATUS_CONNECTING:// 0：正在连接
						resid = R.string.pppp_status_connecting;
						cameraDevice.setTag(2);// 连接中
						break;
					case ContentCommon.PPPP_STATUS_CONNECT_FAILED:// 3：连接失败
						resid = R.string.pppp_status_connect_failed;
						cameraDevice.setTag(0);
						break;
					case ContentCommon.PPPP_STATUS_DISCONNECT:// 4：断线
						resid = R.string.pppp_status_disconnect;
						cameraDevice.setTag(0);
						break;
					case ContentCommon.PPPP_STATUS_INITIALING:// 1：已连接，正在初始化
						resid = R.string.pppp_status_initialing;
						cameraDevice.setTag(2);
						break;
					case ContentCommon.PPPP_STATUS_INVALID_ID:// 5：id号无效
						resid = R.string.pppp_status_invalid_id;
						cameraDevice.setTag(0);
						break;
					case ContentCommon.PPPP_STATUS_ON_LINE:// 2：在线
						resid = R.string.pppp_status_online;
						cameraDevice.setTag(1);// 连接成功
						break;
					case ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE:// 6：摄像机不在线
						resid = R.string.device_not_on_line;
						cameraDevice.setTag(0);
						break;
					case ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT:// 7：连接超时
						resid = R.string.pppp_status_connect_timeout;
						cameraDevice.setTag(0);
						break;
					case ContentCommon.PPPP_STATUS_CONNECT_ERRER:// 8：密码错误
						resid = R.string.pppp_status_pwd_error;
						cameraDevice.setTag(0);
						break;
					default:
						LogUtils.i("未知状态");
						resid = R.string.pppp_status_unknown;
					}
					LogUtils.w("相机状态变化 " + did + " " + getString(resid));
					adapter.notifyDataSetChanged();
					if (msgParam == ContentCommon.PPPP_STATUS_ON_LINE) {
						NativeCaller.PPPPGetSystemParams(did,
								ContentCommon.MSG_TYPE_GET_PARAMS);
					} else if (msgParam == ContentCommon.PPPP_STATUS_INVALID_ID
							|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_FAILED
							|| msgParam == ContentCommon.PPPP_STATUS_DEVICE_NOT_ON_LINE
							|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_TIMEOUT
							|| msgParam == ContentCommon.PPPP_STATUS_CONNECT_ERRER) {
						NativeCaller.StopPPPP(did);
						cameraDevice.setTag(CameraDevice.tag_notConn);
						LogUtils.e("停止了连接,连接出错");
						// ToastUtils.showToastWhendebug("停止了连接,连接出错");
					}
					break;
				case ContentCommon.PPPP_MSG_TYPE_PPPP_MODE:
					LogUtils.i("收到了另一种消息类型：" + "PPPP_MSG_TYPE_PPPP_MODE");
					break;
				}
			} else {
				LogUtils.e("有一个未知摄像头返回了状态");
			}
		}
	};

	/**
	 * 跳转监控界面
	 * 
	 */
	private void switchCamera() {
		LogUtils.i("重新初始化当前摄像头" + current);
		getCameraParams();
		// 初始化视频录像
		streamType = 10;
		NativeCaller.StartPPPPLivestream(current.getDid(), streamType);
	}

	/** 选择摄像机的adapter */
	public ChooseCameraAdapter adapter;
	/** 查找所有的摄像机 */
	private Button btn_lookforcamera;

	/**
	 * 视频连接流程相关参数说明:
	 *  did:摄像机序列号 
	 *  type：0 摄像机状态返回，1 当前连接为转发模式
	 *   param：type== 0：
	 * 0;//连接中 
	 * 1;//已连接，正在初始化
	 *  2;//在线 
	 *  3;// 连接失败
	 *   4;// 连接已关闭
	 *    5;//无效UID 
	 *    6;//不在线
	 * 7;//连接超时
	 *  8;//密码错误.. 
	 *  9;// 密码错误. 
	 *  10;// 密码错误.
	 */
	@Override
	public void BSMsgNotifyData(String did, int type, int param) {
		LogUtils.w("相机状态变化：" + did + "type:" + type + " param:" + param);
		Bundle bd = new Bundle();
		Message msg = PPPPMsgHandler.obtainMessage();
		msg.what = type;
		bd.putInt(STR_MSG_PARAM, param);
		bd.putString(STR_DID, did);
		msg.setData(bd);
		PPPPMsgHandler.sendMessage(msg);
	}

	@Override
	public void BSSnapshotNotify(String did, byte[] bImage, int len) {// IpcamClientInterface:截图后回调？
		Log.i("ip", "BSSnapshotNotify---len" + len);
	}

	@Override
	public void callBackUserParams(String did, String user1, String pwd1,
			String user2, String pwd2, String user3, String pwd3) {// IpcamClientInterface

	}

	@Override
	public void CameraStatus(String did, int status) {// IpcamClientInterface

	}

	/**
	 * 得到设备的参数
	 */
	private void getCameraParams() {
		NativeCaller.PPPPGetSystemParams(current.getDid(),
				ContentCommon.MSG_TYPE_GET_CAMERA_PARAMS);
	}

	/**
	 * 监控视频图像的回调
	 * @author torah
	 *
	 */
	class ImgDataListener implements OnResultListener<Bitmap> {

		@Override
		public void onResult(boolean isSecceed, Bitmap obj) {
			if (isSecceed) {
				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = obj;
				mHandler.sendMessage(msg);
			}
		}
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:
				Bitmap bmp = (Bitmap) msg.obj;
				Bitmap bitmap = Bitmap.createScaledBitmap(bmp, vedioPicWidth,
						vedioPicHeight, true);
				// 核心的显示方法
				videoViewStandard.setImageBitmap(bitmap);
				videoViewStandard.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 判断当前的分辨率
	 */

	/** 动画 */
	private ImageView iv_animation;
	private View rl_vidioClip;
	/** 标题 */
	private View rl_actionbar;
	/** 手势识别 */
	private GestureDetector gestureDetector;
	/** 全屏按钮 */
	private View iv_enterFullScreen;
	private ImgDataListener imgDataListener;

}
