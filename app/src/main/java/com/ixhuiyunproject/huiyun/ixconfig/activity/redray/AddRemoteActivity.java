package com.ixhuiyunproject.huiyun.ixconfig.activity.redray;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentFactory;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: InfraredSettingActivity.java
 * @Package com.huiyun.ixconfig.activity.setting
 * @Description: 遥控器设置
 * @author Yangshao
 * @date 2015年2月3日 上午11:06:39
 * @version V1.0
 */
public class AddRemoteActivity extends SwipeBackActivity {

	private LinearLayout setAir, setTv, setYx, setMi;
	private ImageView iv_left;
	private EditText inputName;
	private static int CHANGE_INFRA = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_hongwai_setting);
		initView();
	}

	private void initView() {
		inputName = (EditText) findViewById(R.id.input_inf_name);
		BaseActivity.notAllowedWrap(inputName);
		setAir = (LinearLayout) findViewById(R.id.set_air);
		setTv = (LinearLayout) findViewById(R.id.set_tv);
		setYx = (LinearLayout) findViewById(R.id.set_yx);
		setMi = (LinearLayout) findViewById(R.id.set_mi);
		ChangeInfraredRemote changeInfraredRemote = new ChangeInfraredRemote();
		setAir.setOnClickListener(changeInfraredRemote);
		setTv.setOnClickListener(changeInfraredRemote);
		setYx.setOnClickListener(changeInfraredRemote);
		setMi.setOnClickListener(changeInfraredRemote);

		iv_left = (ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 重置颜色
	 */
	protected void resetTextView() {
		setAir.setBackgroundResource(R.color.transparent);
		setTv.setBackgroundResource(R.color.transparent);
		setYx.setBackgroundResource(R.color.transparent);
		setMi.setBackgroundResource(R.color.transparent);
	}

	/**
	 * @Title: InfraredSettingActivity.java
	 * @Package com.huiyun.ixconfig.activity.setting
	 * @Description: 选择遥控器
	 * @author Yangshao
	 * @date 2015年2月3日 上午11:49:43
	 * @version V1.0
	 */
	public class ChangeInfraredRemote implements OnClickListener {
		@Override
		public void onClick(View v) {
			resetTextView();
			switch (v.getId()) {
			case R.id.set_air:
				CHANGE_INFRA = 2;
				setAir.setBackgroundResource(R.drawable.home_select);
				break;
			case R.id.set_tv:
				CHANGE_INFRA = 1;
				setTv.setBackgroundResource(R.drawable.home_select);
				break;
			case R.id.set_yx:
				CHANGE_INFRA = 3;
				setYx.setBackgroundResource(R.drawable.home_select);
				break;
			case R.id.set_mi:
				CHANGE_INFRA = 4;
				setMi.setBackgroundResource(R.drawable.home_select);
				break;
			default:
				break;
			}
		}
	}

	public void save(View v) {
		final String remoteName = inputName.getText().toString();
		final RedRay code = new RedRay();
		code.setR_name(remoteName);
		code.setPageType(CHANGE_INFRA);
		if (CHANGE_INFRA != 0 && !StringUtils.isEmpty(remoteName)) {
			if (FragmentContainer.remoteControllerList.contains(code)) {
				ToastUtils.showToastReal("遥控器已存在");
			} else {
				MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
				jsonobj.code = 17;
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("r_name", remoteName);
				maps.put("pageType", CHANGE_INFRA + "");
				maps.put("token", StaticValue.user.getToken());
				jsonobj.setData(maps);
				NetJsonUtil.getInstance().addCmdForSend(jsonobj);
			}

			JSONModuleManager.getInstance().result_18
					.setOnCmdReseivedListener(new OnResultListener<Object>() {
						@Override
						public void onResult(boolean isSecceed, Object obj) {
							if (isSecceed) {
								// 跳转界面
								FragmentContainer.remoteControllerList
										.add(code);
								ToastUtils.showToastReal("添加成功");
							} else {
								ToastUtils.showToastReal("遥控器添加失败");
							}
							FragmentFactory.redRayManager.flushData();// 刷新数据
							finish();
						}
					});
		} else {
			ToastUtils.showToastReal("请选择遥控器或输入遥控器名称");
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause
		MobclickAgent.onPause(this);
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		// TODO 自动生成的方法存根
		return null;
	}
}
