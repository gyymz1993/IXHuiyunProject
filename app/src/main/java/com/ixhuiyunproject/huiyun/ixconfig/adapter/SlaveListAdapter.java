package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.Slave;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;
import java.util.List;

public class SlaveListAdapter extends BaseAdapter {

	private List<Slave> listSlave;
	private Context mContext;
	
	public SlaveListAdapter(List<Slave> listSlave, Context context){
		this.listSlave = listSlave;
		mContext = context;
	}
	
	public void setSlaveList(List<Slave> listSlave){
		this.listSlave = listSlave;
	}
	
	@Override
	public int getCount() {
		return listSlave.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContext, R.layout.item_slave, null);
		TextView tvType,tvAddr;
		tvType = (TextView) convertView.findViewById(R.id.tv_type);
		tvAddr = (TextView) convertView.findViewById(R.id.tv_addr);
		ImageView tvCheck = (ImageView) convertView.findViewById(R.id.iv_check);
		
		final Slave slave = listSlave.get(position);
		tvAddr.setText("地址：" + slave.getAddr());
		switch(slave.getType()){
		case OutDevice.TYPE_COLOR_LAMP:
			tvType.setText("调色灯");
			break;
		case OutDevice.TYPE_GATEWAY:
			tvType.setText("网关");
			break;
		case OutDevice.TYPE_IN:
			tvType.setText("面板");
			break;
		case OutDevice.TYPE_OUT:
			tvType.setText("面板(带继电器)");
			break;
		case OutDevice.TYPE_RAYSENDER:
			tvType.setText("旧红外转发器");
			break;
		case OutDevice.TYPE_BODY_INDUCTION:
			tvType.setText("热释红外人体感应");
			break;
		case OutDevice.TYPE_OUT_WITH_IN:
			tvType.setText("面板(带继电器)");
			break;
		case OutDevice.TYPE_WINDOW:
			tvType.setText("窗帘");
			break;
		case OutDevice.TYPE_RED_RAY:
			tvType.setText("红外转发器");
			break;
		default:
			tvType.setText("其它设备");
			break;
		}
		tvCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 让目标设备闪烁
				MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
				jsonObj.code = 41;
				jsonObj.data = new HashMap<String, String>();
				jsonObj.data.put("token", StaticValue.user.getToken());
				jsonObj.data.put("address", String.valueOf(slave.getAddr()));
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
		
		return convertView;
	}

}
