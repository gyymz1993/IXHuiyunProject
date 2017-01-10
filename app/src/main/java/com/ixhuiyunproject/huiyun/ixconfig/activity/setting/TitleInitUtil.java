package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ixhuiyunproject.R;

/**
 * 初始化title_2pic_green的工具
 * @author torah
 *
 */
public class TitleInitUtil {
	/**
	 * 初始化标题
	 * @param activity
	 * @param titleName
	 */
	public static  void initTitle(final Activity activity,String titleName){
		View iv_left = activity.findViewById(R.id.iv_left);
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		TextView tv_title = (TextView) activity.findViewById(R.id.tv_title);
		tv_title.setText(titleName);
	}
}
