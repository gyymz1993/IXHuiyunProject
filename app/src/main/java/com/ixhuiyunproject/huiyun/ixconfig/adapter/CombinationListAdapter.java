package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.ChooseOutputDevActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.ComboOutput;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.SettingTransit;
import com.ixhuiyunproject.huiyun.ixconfig.bean.InDevice;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;

/** 组合设置界面用的适配器
 * @author lzn
 *
 */
public class CombinationListAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<InDevice> allInDevice;
	private ComboOutput[] allOutput;
	
	public CombinationListAdapter(Context context, List<InDevice> allInDevice){
		mContext = context;
		this.allInDevice = allInDevice;
		if(allInDevice.size() > 0){
			allOutput = new ComboOutput[allInDevice.size()];
		}
	}
	
	public void chooseOutput(int pos, ComboOutput out){
		if(pos >= 0 && pos < allInDevice.size()){
			allOutput[pos] = out;
		}
	}
	
	/** 获得所有输入
	 * @return
	 */
	public List<InDevice> getAllInDevice(){
		return allInDevice;
	}
	
	/** 获得所有输出
	 * @return
	 */
	public ComboOutput[] getAllOutput(){
		return allOutput;
	}
	
	@Override
	public int getCount() {
		return allInDevice.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContext, R.layout.item_combination_setting, null);
		
		// 输入
		TextView tvInput = (TextView) convertView.findViewById(R.id.tv_input);
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.iv_input);
		tvInput.setText(allInDevice.get(position).getName());
		ivCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 用于显示的编号，由于网关以外的设备编号都是从0开始的，在显示上都是从1开始
				int numberShow = allInDevice.get(position).getNumber() + 1;
				String show = "按键位于左数第" + numberShow + "个";
				// 吐司显示
				ToastUtils.showToastReal(show);
				// 让目标设备闪烁
				MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
				jsonObj.code = 41;
				jsonObj.data = new HashMap<String, String>();
				jsonObj.data.put("token", StaticValue.user.getToken());
				jsonObj.data.put("address", String.valueOf(
						allInDevice.get(position).getAddress()));
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
		
		// 输出
		TextView tvOutput = (TextView) convertView.findViewById(R.id.tv_output);
		if(allOutput[position] == null){
			tvOutput.setText("请选择设备");
		} else {
			switch(allOutput[position].type){
			case ComboOutput.OUT_DEV:  // 设备
				String area = "未知";
				if(!StringUtils.isEmpty(allOutput[position].outDev.getArea())){
					area = allOutput[position].outDev.getArea();
				}
				tvOutput.setText(area + " " + allOutput[position].outDev.getName());
				break;
			case ComboOutput.SCENE:  // 场景
				tvOutput.setText("场景 " + allOutput[position].sceneName);
				break;
			case ComboOutput.NOLINK:  // 取消
				tvOutput.setText("取消前次组合");
				break;
			}
		}
		RelativeLayout rlOutput = (RelativeLayout) convertView.findViewById(R.id.rl_output);
		rlOutput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 进入选择输出设备的界面
				SettingTransit.combConfigIndex = position;
				Intent intent = new Intent(mContext, ChooseOutputDevActivity.class);
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}

}
