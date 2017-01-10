package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.GatewaySettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;

/** 网关列表的适配器
 * @author lzn
 *
 */
public class ChooseGatewayAdapter extends BaseAdapter {

	private List<OutDevice> allGateways;
	private Context mContext;
	
	public ChooseGatewayAdapter(Context context, List<OutDevice> allGateways){
		this.mContext = context;
		this.allGateways = allGateways;
	}
	
	@Override
	public int getCount() {
		return allGateways.size();
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
		convertView = View.inflate(mContext, R.layout.item_choose_gateway, null);
		
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);
		
		tvName.setText("网关" + (position+1));
		// 测试按钮
		ivCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 让目标设备闪烁
				MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
				jsonObj.code = 41;
				jsonObj.data = new HashMap<String, String>();
				jsonObj.data.put("token", StaticValue.user.getToken());
				jsonObj.data.put("address", String.valueOf(allGateways.get(position).getAddress()));
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
		// 选择网关后进入网关配置界面
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, GatewaySettingActivity.class);
				intent.putExtra("address", allGateways.get(position).getAddress());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	
}
