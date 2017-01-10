package com.ixhuiyunproject.huiyun.ixconfig.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;


public class ContrlView extends LinearLayout {

	private int switch_logo;
	private String switch_name;
	/** 改变状态的命令 */
	private TextView tv_switch_state, tv_switch_name;
	private Button bt_switch_btn;
	private ImageView iv_image_logo;

	public interface OnStateChangedListener {
		/**
		 * 切换状态后通知
		 * 
		 * @param state
		 * @param view
		 *            根View
		 */
		void hasSwitchTo(boolean state, View view);
	}

	private OnStateChangedListener switchBtnListener;

	public ContrlView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init(attributeSet);
	}

	private void init(AttributeSet attributeSet) {
		if (attributeSet != null) {
			TypedArray ta = getContext().obtainStyledAttributes(attributeSet,
					R.styleable.ControlItemView);
			switch_name = ta
					.getString(R.styleable.ControlItemView_device_names);
			switch_logo = ta.getResourceId(
					R.styleable.ControlItemView_logo_srcs,
					R.drawable.ic_launcher);
		}
		View rootView = loadRootView();
		iv_image_logo = (ImageView) rootView.findViewById(R.id.switch_logo);
		bt_switch_btn = (Button) rootView.findViewById(R.id.switch_press);
		tv_switch_state = (TextView) rootView.findViewById(R.id.switch_state);
		tv_switch_name = (TextView) rootView.findViewById(R.id.switch_name);
		iv_image_logo.setImageResource(switch_logo);
		tv_switch_name.setText(switch_name);
		/*bt_switch_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean enabled = "开".equals(tv_switch_state.getText());
				updateStateShow(enabled);
			}
		});*/
	}

	/**
	 * 将view加载
	 * 
	 * @return
	 */
	protected View loadRootView() {
		return View.inflate(getContext(), R.layout.new_contrl_view, this);// 将view添加。
	}

	/**
	 * 更新界面状态
	 * 
	 * @param state
	 */
	private void updateStateShow(boolean state) {
		if (state) {
			bt_switch_btn.setBackgroundResource(R.drawable.button_off);
			tv_switch_state.setText("关");
		} else {
			bt_switch_btn.setBackgroundResource(R.drawable.button_on);
			tv_switch_state.setText("开");
		}
		if (switchBtnListener != null) {
			switchBtnListener.hasSwitchTo(state, ContrlView.this);
		}
	}

	/**
	 * 设置设备开关
	 * 
	 * @param state
	 */
	public void setState(boolean state) {
		updateStateShow(state);
	}

	/**
	 * 设置开关状态切换监听
	 * 
	 * @param switchBtnlistener
	 */
	public void setOnSwitchBtnClickListener(
			OnStateChangedListener switchBtnlistener) {
		this.switchBtnListener = switchBtnlistener;
	}

}
