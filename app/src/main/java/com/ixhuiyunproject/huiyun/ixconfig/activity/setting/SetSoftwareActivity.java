package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.huiyunUtil.Sp_Key;
import com.ixhuiyunproject.huiyun.ixconfig.push.SettingTag;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.label.service.ToastService;
import com.umeng.analytics.MobclickAgent;

/**
 * 软件设置
 * 
 * @author lzy_torah
 * 
 */
public class SetSoftwareActivity extends SwipeBackActivity {
	ImageView iv_message;
	ImageView iv_vibration;
	ImageView iv_memory;
	SettingTag settingTag = new SettingTag();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 去掉默认的标题栏 */
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frg_set_software);
		initSoftware();
	}

	/**
	 * 
	 * Function:返回
	 * 
	 * @author Yangshao 2015年2月6日 下午1:41:45
	 * @param v
	 */
	public void setBack(View v) {
		this.finish();
	}

	private void initSoftware() {
		RelativeLayout sendMessage = (RelativeLayout) findViewById(R.id.set_message);
		iv_message = (ImageView) findViewById(R.id.iv_message);

		RelativeLayout sendVibration = (RelativeLayout) findViewById(R.id.set_vibration);
		iv_vibration = (ImageView) findViewById(R.id.iv_vibration);

		RelativeLayout sendMemory = (RelativeLayout) findViewById(R.id.set_Memory);
		iv_memory = (ImageView) findViewById(R.id.iv_memory);

		/**
		 * 初始化推送是否开启
		 */
		if (SpUtils.getBoolean(Sp_Key.Logo_Push, true)) {
			iv_message.setImageResource(R.drawable.switch_on);
			settingTag.setTag(SpUtils.getString("familyId"));
		} else {
			iv_message.setImageResource(R.drawable.switch_off);
			settingTag.setTag("false");
		}

		/**
		 * 初始化震动是否开启
		 */
		if (SpUtils.getBoolean(Sp_Key.Logo_Vibrate, true)) {
			iv_vibration.setImageResource(R.drawable.switch_on);
		} else {
			iv_vibration.setImageResource(R.drawable.switch_off);
		}

		if (SpUtils.getBoolean(Sp_Key.B_WILL_STRAT, true)) {
			iv_memory.setImageResource(R.drawable.switch_on);
		} else {
			iv_memory.setImageResource(R.drawable.switch_off);
		}

		/**
		 * 开启和关闭监听
		 */
		sendMessage.setOnClickListener(new ContrlOnclickListener(iv_message));
		sendVibration
				.setOnClickListener(new ContrlOnclickListener(iv_vibration));
		sendMemory.setOnClickListener(new ContrlOnclickListener(iv_memory));
	}

	public class ContrlOnclickListener implements OnClickListener {
		private ImageView view;

		public ContrlOnclickListener(ImageView iv_contrl) {
			this.view = iv_contrl;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.set_message:
				if (SpUtils.getBoolean(Sp_Key.Logo_Push, true)) {
					view.setImageResource(R.drawable.switch_off);
					SpUtils.saveBoolean(Sp_Key.Logo_Push, false);
					settingTag.setTag("false");
				} else {
					view.setImageResource(R.drawable.switch_on);
					SpUtils.saveBoolean(Sp_Key.Logo_Push, true);
					settingTag.setTag(SpUtils.getString("familyId"));
				}
				break;
			case R.id.set_vibration:
				if (SpUtils.getBoolean(Sp_Key.Logo_Vibrate, true)) {
					view.setImageResource(R.drawable.switch_off);
					SpUtils.saveBoolean(Sp_Key.Logo_Vibrate, false);
				} else {
					view.setImageResource(R.drawable.switch_on);
					SpUtils.saveBoolean(Sp_Key.Logo_Vibrate, true);
				}
				break;
			case R.id.set_Memory:
				if (SpUtils.getBoolean(Sp_Key.B_WILL_STRAT, true)) {
					view.setImageResource(R.drawable.switch_off);
					SpUtils.saveBoolean(Sp_Key.B_WILL_STRAT, false);
				} else {
					view.setImageResource(R.drawable.switch_on);
					SpUtils.saveBoolean(Sp_Key.B_WILL_STRAT, true);
				}
				Intent intent = new Intent(SetSoftwareActivity.this,
						ToastService.class);
				intent.putExtra("flush", 1);
				startService(intent);
				break;
			default:
				break;

			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);// 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd(FinalValue.mPageName);
		MobclickAgent.onPause(this); // 统计时长
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
