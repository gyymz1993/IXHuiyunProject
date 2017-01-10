package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.SlaveListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Slave;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlaveListActivity extends SwipeBackActivity {

	private ImageView ivReturn;
	private ListView lvSlave;
	private SlaveListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_all_slave);

		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		lvSlave = (ListView) findViewById(R.id.lv_slave);

		ivReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 获得从机信息并显示
		downloadSlave();
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

	
	/**
	 * 下载从机信息
	 */
	public void downloadSlave() {
		// 发送下载输出设备的信息
		JSONModuleManager.getInstance().result_58
				.setOnCmdReseivedListener(new OnResultListener<List<Slave>>() {

					@Override
					public void onResult(boolean isSucceed, final List<Slave> obj) {
						if (isSucceed) {
							LogUtils.i("下载从机：" + obj.size() + "个");
							// 保存到本地
							DeviceManager.slaveList = obj;
							for (Slave slave : DeviceManager.slaveList) {
								System.out.println("从机信息：" + slave.getAddr()
										+ "," + slave.getType());
							}

							// adapter
							UIUtils.runInMainThread(new Runnable() {
								
								@Override
								public void run() {
									adapter = new SlaveListAdapter(obj, SlaveListActivity.this);
									lvSlave.setAdapter(adapter);
									lvSlave.setDivider(null);
								}
							});
							
							// 把监听设置为空
							JSONModuleManager.getInstance().result_58
									.setOnCmdReseivedListener(null);
						}
					}
				});
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 57;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				switch(pos){
				case 0:  // 搜索设备
					searchSlave();
					break;
				case 1:  // 手动添加
					manualAdd();
					break;
				}
				popMenu.dismiss();
			}
		};
		return listener;
	}

	@Override
	public String[] setPopMenuName() {
		return new String[]{"搜索设备", "手动添加"};
	}
	
	/**
	 * 搜索设备
	 */
	private void searchSlave(){
		try {
			MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code = 7;
			jsonobj.obj = FinalValue.OBJ_MASTER;
			Map<String, String> map = new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			jsonobj.setData(map);
			NetJsonUtil.getInstance().addCmdForSend(jsonobj);
			JSONModuleManager.getInstance().result_8
					.setOnCmdReseivedListener(new OnResultListener<Object>() {
						@Override
						public void onResult(boolean isSecceed,
								Object obj) {
							if (isSecceed) {
								ToastUtils.showToastReal("通知主机成功");
								LogUtils.i("成功");
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 手动添加
	 */
	private void manualAdd(){
		try {
			MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code = 43;
			jsonobj.obj = FinalValue.OBJ_MASTER;
			Map<String, String> map = new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			jsonobj.setData(map);
			NetJsonUtil.getInstance().addCmdForSend(jsonobj);
			JSONModuleManager.getInstance().result_44
					.setOnCmdReseivedListener(new OnResultListener<Object>() {
						@Override
						public void onResult(boolean isSecceed,
								Object obj) {
							if (isSecceed) {
								ToastUtils.showToastReal("请点击设备....");
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
