package com.ixhuiyunproject.huiyun.ixconfig.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;


/**
 * 自定义view，控制里面的条目
 * 
 * @author torahs
 *
 */
public class SettingItemRLView extends RelativeLayout {
	private int logo_srcId;
	private String item_name;

	/**
	 * xml生成使用的
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingItemRLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	/**
	 * 初始化
	 * 
	 * @param attrs
	 */
	private void init(AttributeSet attrs) {
		if (attrs != null) {// 得到自定义属性
			TypedArray ta = getContext().obtainStyledAttributes(attrs,
					R.styleable.settingItem);
			item_name = ta.getString(R.styleable.settingItem_item_name);
			logo_srcId = ta.getResourceId(R.styleable.settingItem_logo_src, -1);
			ta.recycle();
		}
		View rootView = View.inflate(getContext(), R.layout.view_setting_item, this);// 将view添加。

		TextView tv_describe = (TextView) rootView.findViewById(R.id.tv_title);
		ImageView iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
		tv_describe.setText(item_name);
		if (logo_srcId == -1) {
			iv_icon.setVisibility(View.INVISIBLE);
		} else
			iv_icon.setImageResource(logo_srcId);
	}

}
