package com.ixhuiyunproject.huiyun.ixconfig.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginActivity;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentFactory;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.ContrlTestHandler;
import com.ixhuiyunproject.huiyun.ixconfig.push.ExampleUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.label.service.ToastService;
import com.ixhuiyunproject.ipcamer.demo.AddCameraActivity;
import com.ixhuiyunproject.ipcamer.demo.my.CameraCoreUtil;
import com.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 主页面
 * 
 * @author lzy_torah
 * 
 */
public class HomeActivity extends FragmentActivity {

	// public static final String showHome = "showHome";
	public static final String user = "user";
	public static final String token = "token";
	public static final String pwd = "pwd";
	public static final String type = "type";
	public static final int MENU_SLIDING_OUT_DELAY = 100; // 菜单滑动退出时的延迟毫秒数
	public static HomeActivity homeActivity;
	private long exitTime = 0;// 双击退出使用
	private FragmentManager fragManager;
	/** 侧滑菜单 */
	SlidingMenu menu;
	/** 管理标题的各种状态 */
	TitleManager titleManager;
	private int currentPage = -1;
	/** 切换页面的handler,在msg.arg1中储存要跳转的item号 */
	public Handler switchPageHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 != 0) {
				switchFrag(msg.arg1);
				titleManager.switchType(msg.arg1);
				currentPage = msg.arg1;
			} else {
				ToastUtils.showToastReal("要切换的页面错误");
			}
		};
	};

	/**
	 * 侧选项 菜单
	 */
	// SettingBarView selfCenter, switchControl, checkShow, scene, setting;
	LinearLayout selfCenter, switchControl, checkShow, scene, setting,
			homePage,test,michael;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		homeActivity = this;
		fragManager = getSupportFragmentManager();
		initSlidingMenu();
		titleManager = new TitleManager(this);
		switchToHomePage();

		/**
		 * 注册推送
		 */
		registerMessageReceiver();
	}

	/**
	 * 切换成主页状态
	 * 
	 */
	private void switchToHomePage() {
		Message msg = new Message();
		msg.arg1 = FragmentFactory.HOMEPAGE;
		switchPageHandler.sendMessage(msg);// 切换成主页状态
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		if(ToastService.showCamera){
			ToastService.closeCameraAndstartVoice();//关闭监控，开启语音
			CameraCoreUtil.recycleCameraThings(this);//回收监控资源
			//刷新悬浮窗
			Intent intent = new Intent(this,
					ToastService.class);
			intent.putExtra("flush", 1);
			startService(intent);
		}
		super.onDestroy();
	}

	private EditText msgText;
	public static boolean isForeground = false;

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg) {
		if (null != msgText) {
			msgText.setText(msg);
			msgText.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化侧滑菜单
	 */
	private void initSlidingMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		//menu.setMenu(R.layout.view_menu1);
		menu.setMenu(R.layout.view_menu);
		View menuView = menu.getRootView();
		initMenuButtonAction(menuView);// 初始化菜单的点击事件
	}

	/**
	 * 菜单的点击事件
	 * 
	 * @author torahs
	 * 
	 */
	class MenuItemClickListener implements OnClickListener {
		public void onClick(View v) {

			Message msg = switchPageHandler.obtainMessage();
			resetTextView();
			switch (v.getId()) {
			case R.id.camera_look:// 视频监控
				selfCenter.setBackgroundResource(R.drawable.home_select);
				Intent intent = new Intent(HomeActivity.this,
						AddCameraActivity.class);
				startActivity(intent);
				break;
			case R.id.switch_control:// 控制模块
				switchControl.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.CONTRL;
					switchPageHandler.sendMessage(msg);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.CONTRL, HomeActivity.this);
				}
				break;
			case R.id.check_show:// 红外模块
				checkShow.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.REDRAY;
					switchPageHandler.sendMessage(msg);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.REDRAY, HomeActivity.this);
				}
				break;
			case R.id.scene_show:// 场景模块
				scene.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.SCENE;
					switchPageHandler.sendMessage(msg);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.SCENE, HomeActivity.this);
				}
				break;
			case R.id.setting:// 设置
				setting.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.SETTING_MAIN;
					switchPageHandler.sendMessage(msg);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.SETTING_MAIN,
							HomeActivity.this);
				}
				break;
			case R.id.home_page: // 首页
				homePage.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.HOMEPAGE;
					switchPageHandler.sendMessage(msg);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.HOMEPAGE, HomeActivity.this);
				}
				break;
			case R.id.test: // 测试
				test.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					//ContrlHandler contrlHandler=new ContrlHandler();
					ContrlTestHandler.getIntance().sendMessage();
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.HOMEPAGE, HomeActivity.this);
				}
				break;
			case R.id.michael: // 首页
				michael.setBackgroundResource(R.drawable.home_select);
				if (judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.MICHAEL;
					switchPageHandler.sendMessage(msg);
					//Intent intent2=new Intent(HomeActivity.this, DoorActivity.class);
					//startActivity(intent2);
				} else {// 登陆
					jumpToLoginPage(FragmentFactory.HOMEPAGE, HomeActivity.this);
				}
				break;
			}
			// 关闭菜单
			new Thread() {
				public void run() {
					SystemClock.sleep(MENU_SLIDING_OUT_DELAY);
					Message msg = new Message();
					msg.arg1 = 1;
					menuHandler.sendMessage(msg);
				};
			}.start();
		}
	}

	private Handler menuHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				menu.toggle();
			}
		};
	};

	/**
	 * 重置颜色
	 */
	protected void resetTextView() {
		selfCenter.setBackgroundResource(R.color.transparent);
		switchControl.setBackgroundResource(R.color.transparent);
		checkShow.setBackgroundResource(R.color.transparent);
		scene.setBackgroundResource(R.color.transparent);
		setting.setBackgroundResource(R.color.transparent);
		homePage.setBackgroundResource(R.color.transparent);
		test.setBackgroundResource(R.color.transparent);
		michael.setBackgroundResource(R.color.transparent);
	}

	/**
	 * 跳转到登陆页面
	 * 
	 * @param homeActivity
	 * 
	 */
	public void jumpToLoginPage(int requestCode, HomeActivity homeActivity) {
		ToastUtils.showToastReal("请先登陆");
		Intent intentreds = new Intent(homeActivity, LoginActivity.class);
		homeActivity.startActivity(intentreds);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isForeground = true;
		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		isForeground = false;
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 判断登陆状态
	 */
	public static boolean judgeLoginState() {
		if (StaticValue.user == null) {
			return false;
		} else {
			if (StringUtils.isEmpty(StaticValue.user.getName())
					|| StringUtils.isEmpty(StaticValue.user.getToken())) {
				return false;
			}
			return true;
		}
	}

	/**
	 * 初始化menu的点击事件
	 * 
	 * @param menuView
	 */
	private void initMenuButtonAction(View menuView) {
		MenuItemClickListener listener = new MenuItemClickListener();
		/*
		 * selfCenter = (SettingBarView) findViewById(R.id.camera_look);
		 * switchControl = (SettingBarView) findViewById(R.id.switch_control);
		 * checkShow = (SettingBarView) findViewById(R.id.check_show); scene =
		 * (SettingBarView) findViewById(R.id.scene_show); setting =
		 * (SettingBarView) findViewById(R.id.setting);
		 */

		selfCenter = (LinearLayout) findViewById(R.id.camera_look);
		switchControl = (LinearLayout) findViewById(R.id.switch_control);
		checkShow = (LinearLayout) findViewById(R.id.check_show);
		scene = (LinearLayout) findViewById(R.id.scene_show);
		setting = (LinearLayout) findViewById(R.id.setting);
		homePage = (LinearLayout) findViewById(R.id.home_page);
		test = (LinearLayout) findViewById(R.id.test);
		michael = (LinearLayout) findViewById(R.id.michael);
		
		selfCenter.setOnClickListener(listener);
		switchControl.setOnClickListener(listener);
		checkShow.setOnClickListener(listener);
		scene.setOnClickListener(listener);
		setting.setOnClickListener(listener);
		homePage.setOnClickListener(listener);
		test.setOnClickListener(listener);
		michael.setOnClickListener(listener);
	}

	/**
	 * 切换页面
	 */
	public void switchFrag(int item) {
		fragManager.beginTransaction()
				.replace(R.id.ll_content, FragmentFactory.getFragment(item))
				.commit();
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();// 关闭菜单
		} else if (currentPage != FragmentFactory.HOMEPAGE) {
			switchToHomePage();
		} else
			doubleClickQuit();// 双击退出
	}

	/**
	 * 双击退出
	 */
	private void doubleClickQuit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtils.showToastReal("双击返回键退出");
			exitTime = System.currentTimeMillis();
		} else {
			StaticValue.setRunning(false);
			StaticValue.user = null;
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	public TitleManager getTitleManager() {
		return titleManager;
	}

	private ProgressDialog pDialog;
	/** 弹出等待框 */
	public Handler dialogHandler = new Handler() {
		/**
		 * 方法一：setCanceledOnTouchOutside(false); 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
		 * 方法二： setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用 按返回键也不起作用
		 */
		public void handleMessage(final Message msg) {
			pDialog = new ProgressDialog(homeActivity);
			pDialog.setMessage(msg.obj.toString());
			pDialog.setProgressStyle(TRIM_MEMORY_BACKGROUND);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			switch (msg.what) {
			case 1:
				// 最多等待5秒的等待对话框
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

				break;
			case 2:
				break;
			case 3:
				Thread t3 = new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(4000);
						pDialog.dismiss();
						switchPageHandler.sendMessage(msg);
					}
				});
				t3.start();
				break;
			default:
				break;
			}
		};

	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

}
