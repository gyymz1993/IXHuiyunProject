package com.ixhuiyunproject.huiyun.ixconfig.activity.back;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;


/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，
 * 如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 * 
 *
 */
public abstract class SwipeBackActivity extends BaseActivity {
	protected SwipeBackLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		layout.attachToActivity(this);
	}
	
	
	@Override                                                                 
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		//这个函数有两个参数，一个参数是第一个activity进入时的动画，另外一个参数则是第二个
		//它必需紧挨着startActivity()或者finish()函数之后调用"
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}




	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}


}
