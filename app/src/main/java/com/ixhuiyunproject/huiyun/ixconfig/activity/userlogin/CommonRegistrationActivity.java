package com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.User;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RegexUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: CommonRegistrationActivity.java
 * @Package com.huiyun.ixconfig.activity.userlogin
 * @Description: 普通用户注册
 * @author Yangshao
 * @date 2015年1月27日 下午7:11:10
 * @version V1.0
 */
public class CommonRegistrationActivity extends SwipeBackActivity {

	private EditText ed_user;
	private EditText ed_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_register);
		initView();

	}

	public void re_back(View v) {
		this.finish();
	}

	private void initView() {
		RelativeLayout lay_text = (RelativeLayout) findViewById(R.id.lay_text);
		lay_text.setVisibility(View.INVISIBLE);
		RelativeLayout rel_master = (RelativeLayout) findViewById(R.id.rel_master);
		rel_master.setVisibility(View.INVISIBLE);
		Button btn_Reg = (Button) findViewById(R.id.btn_login);
		btn_Reg.setText("注册");
		LinearLayout chooseLogin = (LinearLayout) findViewById(R.id.lay_login);
		chooseLogin.setVisibility(View.INVISIBLE);

		TextView login_text = (TextView) findViewById(R.id.login_text);
		login_text.setVisibility(View.INVISIBLE);
		ed_user = (EditText) findViewById(R.id.ed_user);
		ed_pwd = (EditText) findViewById(R.id.ed_pwd);

		BaseActivity.notAllowedWrap(ed_user);
		BaseActivity.notAllowedWrap(ed_pwd);

		btn_Reg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(ed_user.getText().toString())
						&& !StringUtils.isEmpty(ed_pwd.getText().toString())) {
					if (!RegexUtil.checkUserName(ed_user.getText().toString())) {
						ToastUtils.showToastReal("用户名长度3-7个字符");
					} else {
						comRegister();
					}
				}
			}

		});
	}

	public void comRegister() {
		String user = ed_user.getText().toString();
		String psw = ed_pwd.getText().toString();
		showpDialog("正在为您注册...");
		MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		jsonobj.code = 3;
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("user", user);
		maps.put("psw", psw);
		maps.put("token", StaticValue.user.getToken());
		jsonobj.setData(maps);
		NetJsonUtil.getInstance().addCmdForSend(jsonobj);
		JSONModuleManager.getInstance().user_4
				.setOnCmdReseivedListener(new OnResultListener<User>() {
					@Override
					public void onResult(boolean isSecceed, User obj) {
						if (isSecceed) {
							dismissDialog();
							LoginSucceedActtion.getDataFromMaster();// 从主机获得数据
							System.out.println("注册成功...");
							ToastUtils.showToastReal("注册成功");
							JSONModuleManager.getInstance().user_4
									.setOnCmdReseivedListener(null);
							// 跳转界面
							finish();
						}
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

}
