package com.ixhuiyunproject.huiyun.ixconfig.activity.scene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.FlowRadioGroup;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 场景设置
 * 
 * @author busy
 * 
 */
public class SceneSettingActivity extends SwipeBackActivity {
	private EditText inputName;
	private FlowRadioGroup btn_sence_choose;
	private Button btn9;
	private int icon = 0;
	private ImageView iv_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_scence_setting);
		initView();
	}

	private void initView() {
		inputName = (EditText) findViewById(R.id.input_name);
		BaseActivity.notAllowedWrap(inputName);
		btn_sence_choose = (FlowRadioGroup) findViewById(R.id.btn_sence_choose);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		btn9 = (Button) findViewById(R.id.btn9);
		btn_sence_choose.setOnCheckedChangeListener(onCheckedListener);
		iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inputName.getText().toString().trim().equals("")
						|| icon < 1) {
					ToastUtils.showToastReal("名称为空或场景未选择！");
				} else {
					// 跳转到下一个页面
					Intent intent = new Intent(SceneSettingActivity.this,
							SceneConfigActivity.class);
					intent.putExtra("name", inputName.getText().toString()
							.trim());
					intent.putExtra("icon", icon);
					startActivity(intent);
				}
			}
		});
	}

	private FlowRadioGroup.OnCheckedChangeListener onCheckedListener = new FlowRadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(FlowRadioGroup group, int checkedId) {
			for (int i = 0; i < group.getRadioButtonCount(); i++) {
				RadioButton rb = group.getRadioButton(i);
				if (checkedId == rb.getId()) {
					icon = i + 1;
					break;
				}
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		// 重新初始化
		inputName.setText("");
		btn_sence_choose.clearCheck();
		// for (int i = 0; i < btn_sence_choose.getRadioButtonCount(); i++) {
		// RadioButton rb = btn_sence_choose.getRadioButton(i);
		// rb.setChecked(false);
		// }
		icon = 0;
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
