package com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.BaseApplication;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.ContrlLoginResult_6;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.RegisterAdminResult_2;
import com.ixhuiyunproject.huiyun.ixconfig.push.MyReceiver;
import com.ixhuiyunproject.huiyun.ixconfig.utils.Injector;
import com.ixhuiyunproject.huiyun.ixconfig.utils.InjectorValue;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.NetConnectStateUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RegexUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ShowpDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/*
 * 1.先建立本地tcp连接
 */
/**
 * @author Yangshao
 * @version 创建时间：2015年1月5日 上午10:03:56 类说明
 */
public class LoginActivity extends BaseActivity {

	@InjectorValue(values = R.id.btn_login)
	private Button btn_Reg;
	@InjectorValue(values = R.id.ed_user)
	private EditText ed_User;
	@InjectorValue(values = R.id.ed_pwd)
	private EditText ed_Pwd;

	@InjectorValue(values = R.id.local_login)
	private TextView localText;

	@InjectorValue(values = R.id.remote_login)
	private TextView remoteText;

	@InjectorValue(values = R.id.btn_regin)
	private TextView is_btn_regin;

	private EditText et_familyId;

	private ContrlLoginResult_6 login_6 = JSONModuleManager.getInstance().login_6;

	private boolean isRemote = false;

	@InjectorValue(values = R.id.login_text)
	private TextView login_text_title;

	public static ShowpDialogUtils dialogUtils;
	private MyJsonObj1 jsonobj;

	@InjectorValue(values = R.id.rel_master)
	private RelativeLayout rel_master;

	@InjectorValue(values = R.id.lay_login)
	private LinearLayout chooseLogin;

	@InjectorValue(values = R.id.ed_master)
	private EditText ed_master;

	@InjectorValue(values = R.id.btn_text)
	private TextView btn_text;
	private RegisterAdminResult_2 admin_2 = JSONModuleManager.getInstance().admin_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StaticValue.setRemote(true);
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏title
		setContentView(R.layout.activity_login_register);
		Injector.get(this).inject();
		// 初始化找主机的对话框
		dialogUtils = new ShowpDialogUtils(this);

		initContrl();
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * Function: 自动登录
	 * 
	 * @author Yangshao 2015年2月10日 上午10:36:14
	 */
	public void automationLogin() {
		/* 从intent和bundle中提取需要的参数 */
		ed_User.setText(SpUtils.getString("userName"));
		ed_Pwd.setText(SpUtils.getString("password"));
		BaseActivity.notAllowedWrap(ed_User);
		BaseActivity.notAllowedWrap(ed_Pwd);
		/**
		 * 收到推送自动登录
		 */
		try {
			if (getIntent().getExtras().getBoolean("msg") || MyReceiver.isLogin) {
				login();
				MyReceiver.isLogin = false;
			}
		} catch (Exception e) {
		}
	}

	private void initContrl() {
		/* 从intent和bundle中提取需要的参数 */
		btn_Reg.setText("登录");
		automationLogin();
		localText.setOnClickListener(new OnClickListenerImpl());
		remoteText.setOnClickListener(new OnClickListenerImpl());
		is_btn_regin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(LoginActivity.this,
				// RegisterAdminActivity.class);
				// startActivity(intent);
				if (is_btn_regin.getText().equals("去注册！")) {
					is_btn_regin.setText("去登录！");
					rel_master.setVisibility(View.VISIBLE);
					btn_Reg.setText("注册");
					chooseLogin.setVisibility(View.INVISIBLE);
				} else {
					is_btn_regin.setText("去注册！");
					rel_master.setVisibility(View.INVISIBLE);
					btn_Reg.setText("登录");
					chooseLogin.setVisibility(View.VISIBLE);
				}
			}
		});
		btn_Reg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println(btn_Reg.getText());
				if (btn_Reg.getText().equals("登录")) {
					login();
				} else {
					comRegister();
				}

			}
		});
	}

	public void comRegister() {
		StaticValue.setRemote(false);
		if (!StringUtils.isEmpty(ed_User.getText().toString())
				&& !StringUtils.isEmpty(ed_Pwd.getText().toString())) {
			if (!RegexUtil.checkUserName(ed_User.getText().toString())) {
				ToastUtils.showToastReal("用户名长度3-7个字符");
			} else {
				String user = ed_User.getText().toString();
				String psw = ed_Pwd.getText().toString();
				showpDialog("正在为您注册...");
				jsonobj = JsonUtil.getAJsonObj1ForMaster();
				jsonobj.code = 1;
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("masterId", ed_master.getText().toString());
				maps.put("user", user);
				maps.put("psw", psw);
				// 存入静态变量
				if (StaticValue.user == null) {
					StaticValue.user = new User();
				}
				StaticValue.user.setName(user);
				StaticValue.user.setPassword(psw);
				jsonobj.setData(maps);
				admin_2.setOnCmdReseivedListener(new OnResultListener<User>() {
					@Override
					public void onResult(boolean isSecceed, User obj) {
						dismissDialog();
						if (isSecceed) {

							dismissDialog();
							LoginSucceedActtion.getDataFromMaster();// 从主机获得数据
							LogUtils.i("注册成功...");
							ToastUtils.showToastReal("注册成功");
							admin_2.setOnCmdReseivedListener(null);
							// 跳转界面
							Intent intent = new Intent(LoginActivity.this,
									HomeActivity.class);
							startActivity(intent);

							finish();
							UIUtils.runInMainThread(new Runnable() {
								@Override
								public void run() {
									BaseApplication.initPush(SpUtils
											.getString("familyId"));
								}
							});
						} else {
							ToastUtils.showToastReal("注册失败");
						}
					}
				});
				creatTcpAndSendJsonobj();
			}

		} else {
			ToastUtils.showToastReal("请输入完整数据");
		}

	}

	public class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			resetTextColor();
			switch (v.getId()) {
			case R.id.local_login:
				localText.setTextColor(Color.rgb(78, 204, 202));
				login_text_title.setText("本地登录");
				isRemote = false;
				break;
			case R.id.remote_login:
				// 01bbb8
				remoteText.setTextColor(Color.rgb(78, 204, 202));
				login_text_title.setText("远程登录");
				isRemote = true;
				break;
			default:
				break;
			}
		}
	}

	protected void resetTextColor() {
		localText.setTextColor(Color.BLACK);
		remoteText.setTextColor(Color.BLACK);
	}

	/**
	 * 登录按钮的点击事件
	 */
	public void login() {
		// 不登陆测试使用：
		String llllll = ed_User.getText().toString();
		if(StringUtils.isEmpty(llllll)){
			Intent tempIntent = new Intent(LoginActivity.this,
					HomeActivity.class);
			startActivity(tempIntent);
			finish();
		}
		if ("666666".equals(llllll)) {
			Intent tempIntent = new Intent(LoginActivity.this,
					HomeActivity.class);
			startActivity(tempIntent);
			finish();
		}
		// 正常的逻辑
		/*
		 * 1.判断填写是否正确 2.判断网络连接状态 3.弹等待框，进行登录
		 */
		String userName = ed_User.getText().toString();
		String password = ed_Pwd.getText().toString();
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			ToastUtils.showToastReal("用户名密码不能为空");
			return;
		}
		if (!RegexUtil.checkUserName(userName)) {
			ToastUtils.showToastReal("请输入以字母开头，允许3-7字节，允许字母数字下划线");
			return;
		}
		if (!isRemote) {
			// 本地连接：检查连接WIFI状态
			if (!NetConnectStateUtil.isWifi(LoginActivity.this)) {
				ToastUtils.showToastReal("无WIFI连接,无法登录");
				return;
			}
		} else {// 远程连接，检查网络连接
			if (!NetConnectStateUtil.isNetWorkConnect(LoginActivity.this)) {
				ToastUtils.showToastReal("无网络连接");
				return;
			}
		}

		jsonobj = JsonUtil.getAJsonObj1ForMaster();
		jsonobj.code = 5;
		final Map<String, String> maps = new HashMap<String, String>();// 储存信息
		maps.put("user", ed_User.getText().toString());
		maps.put("psw", ed_Pwd.getText().toString());
		jsonobj.setData(maps);
		// 设置监听
		login_6.setOnCmdReseivedListener(new OnResultListener<User>() {
			@Override
			public void onResult(boolean isSecceed, User obj) {
				if (isSecceed) {
					dismissDialog();
					// 记住用户名和密码
					LoginSucceedActtion.saveLoginInfo(ed_User.getText()
							.toString(), ed_Pwd.getText().toString());
					loginSucceedAndJump();
				} else {
					dismissDialog();
					ToastUtils.showToastReal("用户名或者密码错误");
				}
			}
		});
		// 同步全局
		if (StaticValue.user == null) {
			StaticValue.user = new User();
		}
		StaticValue.user.setName(ed_User.getText().toString());
		StaticValue.user.setPassword(ed_Pwd.getText().toString());
		StaticValue.setRemote(isRemote); // 同步到全局变量
		// 登录
		if (!isRemote) {
			creatTcpAndSendJsonobj();// 局域网登陆
		} else {
			// 远程
			jsonobj.obj = FinalValue.OBJ_SERVER;
			// 获得familyId,发送
			String familyId = SpUtils.getString("familyId");
			if (!StringUtils.isEmpty(familyId)) {// 有familyId
				maps.put("familyId", familyId);
				remoteLogin();// 远程登录
			} else {// 没有familyId
				new AlertDialog.Builder(LoginActivity.this)
						.setTitle("请输入家庭账号")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et_familyId = new EditText(LoginActivity.this))
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										String ed_familyId = et_familyId
												.getText().toString();
										boolean isavaliable = (!StringUtils
												.isEmpty(ed_familyId))
												&& (ed_familyId.length() > 4)
												&& RegexUtil
														.checkNumber(ed_familyId);// 检测
										if (isavaliable) {
											maps.put("familyId", ed_familyId);
											remoteLogin();// 远程登录
										} else {
											ToastUtils
													.showToastReal("家庭编号格式不正确");
										}
									}
								}).setNegativeButton("取消", null).show();

			}

		}

	}

	/**
	 * 远程登录
	 */
	private void remoteLogin() {
		// 初始化主机ip
		InetAddress serverAddr = null;
		try {
			serverAddr = InetAddress.getByName(FinalValue.IP_SERVER);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("主机ip错误");
		}
		NetJsonUtil.getInstance().createServerTcpConn(serverAddr, 8989,
				new OnResultListener<Integer>() {

					@Override
					public void onResult(boolean isSecceed, Integer obj) {
						if (isSecceed) {// 创建本地tcp连接成功
							// 发送
							showpDialogEternity("验证中...");
							NetJsonUtil.getInstance().addCmdForSend(jsonobj);
						} else {// 进行中
							switch (obj) {
							case 1:
								showpDialogEternity("建立连接...");
								break;

							default:
								break;
							}

						}
					}
				});
	}

	/**
	 * 建立tcp连接，并发送初始化好的jsonobj
	 */
	private void creatTcpAndSendJsonobj() {
		// 本地登录
		jsonobj.obj = FinalValue.OBJ_MASTER;
		// 建立连接，弹等待框
		NetJsonUtil.getInstance().createLocalTCPConnection(
				new OnResultListener<Integer>() {

					@Override
					public void onResult(boolean isSecceed, Integer obj) {
						if (isSecceed) {// 创建本地tcp连接成功
							// 发送
							NetJsonUtil.getInstance().addCmdForSend(jsonobj);

						} else {// 进行中
							switch (obj) {
							case 0:
								showpDialogEternity("寻找主机...");
								break;
							case 1:
								showpDialogEternity("建立连接...");
								break;

							default:
								break;
							}

						}
					}
				});// 创建本地tcp连接
	}

	/**
	 * 登录成功，准备跳转
	 */
	private void loginSucceedAndJump() {

		dismissDialog();
		LoginSucceedActtion.getDataFromMaster();// 初始化参数
		ToastUtils.showToastReal("登陆成功");
		LogUtils.i("登陆成功");

		login_6.setOnCmdReseivedListener(null);
		// 跳转主页
		Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				/**
				 * 初始化推送是否开启
				 */
				if (SpUtils.getBoolean(Sp_Key.Logo_Push, true)) {
					BaseApplication.initPush(SpUtils.getString("familyId"));
				} else {
					BaseApplication.initPush("false");
				}

			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		automationLogin();
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		return null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
