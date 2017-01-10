package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.ChooseOutputListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;


public class ChooseOutputDevActivity extends BaseActivity{
	
	private ImageView ivReturn;
	private ExpandableListView elvDevice;
	private ChooseOutputListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 去掉默认的标题栏 */
		setContentView(R.layout.activity_choose_output);
		/* 从intent和bundle中提取需要的参数 */
		
		/* 标题栏 */
		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 退出该Activity
				finish();
			}
		});
		
		/* 折叠式列表 */
		elvDevice = (ExpandableListView) findViewById(R.id.elv_device);
		elvDevice.setGroupIndicator(null); // 去掉默认的箭头
		elvDevice.setDivider(null); // 去掉默认的分割线
		adapter = new ChooseOutputListAdapter(
				this, DeviceManager.outDeviceList, FragmentContainer.SCENE_LIST);
		elvDevice.setAdapter(adapter);
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
