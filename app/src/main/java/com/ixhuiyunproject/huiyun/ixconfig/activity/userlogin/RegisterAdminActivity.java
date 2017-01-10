package com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.BaseApplication;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.RegisterAdminResult_2;
import com.ixhuiyunproject.huiyun.ixconfig.utils.Injector;
import com.ixhuiyunproject.huiyun.ixconfig.utils.InjectorValue;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RegexUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yangshao
 * @version 创建时间：2015年1月5日 上午10:03:56 类说明
 */
@Deprecated
public class RegisterAdminActivity extends BaseActivity {

	@InjectorValue(values = R.id.btn_login)
	private Button btn_Reg;
	@InjectorValue(values = R.id.ed_user)
	private EditText ed_User;
	@InjectorValue(values = R.id.ed_pwd)
	private EditText ed_Pwd;
	@InjectorValue(values = R.id.ed_master)
	private EditText ed_MasterId;
	@InjectorValue(values = R.id.btn_regin)
	private TextView registerAdmin;

	private RegisterAdminResult_2 admin_2 = JSONModuleManager.getInstance().admin_2;
	private NetJsonUtil netJsonUtil = NetJsonUtil.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏title
		setContentView(R.layout.activity_login_register);
		StaticValue.setRemote(false);
		NetJsonUtil.getInstance().createLocalTCPConnection(
				new OnResultListener<Integer>() {
					@Override
					public void onResult(boolean isSecceed, Integer obj) {
						if (isSecceed) {// TODO 创建本地tcp连接成功
						} else {// 进行中
						}
					}
				});// 创建本地tcp连接
		Injector.get(this).inject();
		initContrl();
		MobclickAgent.openActivityDurationTrack(false);
	}

	public void back(View v) {
		this.finish();
	}

	/**
	 * @author Yangshao 功能： 按钮 的 控制
	 */
	private void initContrl() {

		BaseActivity.notAllowedWrap(ed_User);
		BaseActivity.notAllowedWrap(ed_Pwd);

		btn_Reg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(ed_User.getText().toString())
						&& !StringUtils.isEmpty(ed_Pwd.getText().toString())) {
					if (!RegexUtil.checkUserName(ed_User.getText().toString())) {
						ToastUtils.showToastReal("用户名长度3-7个字符");
					} else {
						String user = ed_User.getText().toString();
						String psw = ed_Pwd.getText().toString();
						showpDialog("正在为您注册...");
						MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
						jsonobj.code = 1;
						Map<String, String> maps = new HashMap<String, String>();
						maps.put("masterId", ed_MasterId.getText().toString());
						maps.put("user", user);
						maps.put("psw", psw);
						// 存入静态变量
						if (StaticValue.user == null) {
							StaticValue.user = new User();
						}
						StaticValue.user.setName(user);
						StaticValue.user.setPassword(psw);

						jsonobj.setData(maps);
						netJsonUtil.addCmdForSend(jsonobj);
						admin_2.setOnCmdReseivedListener(new OnResultListener<User>() {
							@Override
							public void onResult(boolean isSecceed, User obj) {
								if (isSecceed) {
									dismissDialog();
									LoginSucceedActtion.getDataFromMaster();// 从主机获得数据
									LogUtils.i("注册成功...");
									ToastUtils.showToastReal("注册成功");

									admin_2.setOnCmdReseivedListener(null);
									// 跳转界面
									Intent intent = new Intent(
											RegisterAdminActivity.this,
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
					}

				} else {
					ToastUtils.showToastReal("请输入完整数据");
				}

			}
		});

		registerAdmin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterAdminActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
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

	// 点击屏幕 关闭输入弹出框
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getCurrentFocus()
				.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
