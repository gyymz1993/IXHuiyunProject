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


public class WindowsView extends LinearLayout {

	private int switch_logo;
	private String switch_name;
	/** 改变状态的命令 */
	private TextView tv_switch_name;
	@SuppressWarnings("unused")
	private Button bt_switch_on, bt_switch_off;
	private ImageView iv_image_logo;

	public WindowsView(Context context, AttributeSet attributeSet) {
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
		bt_switch_on = (Button) rootView.findViewById(R.id.switch_on);
		bt_switch_off = (Button) rootView.findViewById(R.id.switch_off);
		tv_switch_name = (TextView) rootView.findViewById(R.id.switch_name);
		iv_image_logo.setImageResource(switch_logo);
		tv_switch_name.setText(switch_name);

	}

	/**
	 * 将view加载
	 * 
	 * @return
	 */
	protected View loadRootView() {
		return View.inflate(getContext(), R.layout.new_window_view, this);// 将view添加。
	}

}
