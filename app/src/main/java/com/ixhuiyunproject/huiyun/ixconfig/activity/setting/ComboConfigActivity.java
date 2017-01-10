package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.ComboOutput;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.SettingTransit;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.CombinationListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.DialogMsg;
import com.ixhuiyunproject.huiyun.ixconfig.bean.InDevice;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.DialogUtil;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合设置
 * @author lzn
 *
 */
public class ComboConfigActivity extends SwipeBackActivity {
	
	private ImageView tvReturn;
	private Button btnCommit;
	private ListView lvCombination;
	private CombinationListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combination_setting);
		
		/* 返回 */
		tvReturn = (ImageView) findViewById(R.id.iv_title_left);
		tvReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		/* 列表 */
		lvCombination = (ListView) findViewById(R.id.lv_combination);
		adapter = new CombinationListAdapter(this, DeviceManager.inDeviceList);
		lvCombination.setAdapter(adapter);
		lvCombination.setDivider(null);
		
		/* 提交 */
		btnCommit = (Button) findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 显示等待对话框
				DialogMsg msg = new DialogMsg();
				msg.setMessage("正在提交……");
				msg.setTimeMills(0);
				DialogUtil.showProgressDialog(ComboConfigActivity.this, msg);
				
				// 建立监听
				JSONModuleManager.getInstance().result_26.setOnCmdReseivedListener(
						new OnResultListener<Object>() {
					
					@Override
					public void onResult(boolean isSucceed, Object obj) {
						// 取消等待对话框
						DialogUtil.dismissProgressDialog();
						if(isSucceed){
							// 吐司显示
							ToastUtils.showToastReal("提交成功！");
						} else {
							// 吐司显示
							ToastUtils.showToastReal("提交失败，请检查原因");
						}
					}
				});
				
				// 生成并发送json命令
				MyJsonObj2 jsonObj = JsonUtil.getAJsonObj2ForMaster();
				jsonObj.code = 25;
				MyJsonObj2.Data data = new MyJsonObj2.Data();
				data.token = StaticValue.user.getToken();
				data.list = new ArrayList<Map<String,String>>();
				
				List<InDevice> allIn = adapter.getAllInDevice();
				ComboOutput[] allOut = adapter.getAllOutput();
				for(int i=0; i<allIn.size(); i++){
					if(allOut[i] != null){
						Map<String,String> map = new HashMap<String,String>();
						map.put("inAddr", String.valueOf(allIn.get(i).getAddress()));
						map.put("inNum", String.valueOf(allIn.get(i).getNumber()));
						switch(allOut[i].type){
						case ComboOutput.OUT_DEV:
							map.put("type", 1+"");
							map.put("outAddr", String.valueOf(allOut[i].outDev.getAddress()));
							map.put("outNum", String.valueOf(allOut[i].outDev.getNumber()));
							break;
						case ComboOutput.SCENE:
							map.put("type", 2+"");
							map.put("sceneName", allOut[i].sceneName);
							break;
						case ComboOutput.NOLINK:
							map.put("type", 0+"");
							break;
						}
						
						data.list.add(map);
					}
				}
				jsonObj.data = data;
				
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// 从中转站获取数据，更新适配器
		if(SettingTransit.combConfigIndex >= 0 && SettingTransit.comboOutput != null){
			adapter.chooseOutput(
					SettingTransit.combConfigIndex, SettingTransit.comboOutput);
		}
		adapter.notifyDataSetChanged();
		// 重置中转站里的数据
		SettingTransit.combConfigIndex = -1;
		SettingTransit.comboOutput = null;
		
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
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		return null;
	}
}
