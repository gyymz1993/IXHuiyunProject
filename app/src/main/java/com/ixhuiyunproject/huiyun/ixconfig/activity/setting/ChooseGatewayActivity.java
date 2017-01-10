package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.ChooseGatewayAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.umeng.analytics.MobclickAgent;

public class ChooseGatewayActivity extends SwipeBackActivity {
	
	private ListView lvGateway;
	private ChooseGatewayAdapter adapter;
	private ImageView ivReturn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_gateway);
		
		lvGateway = (ListView) findViewById(R.id.lv_gateway);
		adapter = new ChooseGatewayAdapter(this, DeviceManager.gatewayList);
		lvGateway.setAdapter(adapter);
		lvGateway.setDivider(null);
		
		/* 返回按钮 */
		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
