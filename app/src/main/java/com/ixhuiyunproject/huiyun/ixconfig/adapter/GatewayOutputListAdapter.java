package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.bean.GatewayOutputInfo;

import java.util.List;


@Deprecated
public class GatewayOutputListAdapter extends BaseAdapter {

	private Context mContext;
	private List<GatewayOutputInfo> listGOI;
	
	public GatewayOutputListAdapter(Context context, List<GatewayOutputInfo> list){
		mContext = context;
		listGOI = list;
	}
	
	@Override
	public int getCount() {
		return listGOI.size();
	}

	@Override
	public Object getItem(int position) {
		return listGOI.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(mContext, R.layout.item_gateway_output, null);
		
		// view by id
		TextView tvArea = (TextView) convertView.findViewById(R.id.tv_area);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tvComm = (TextView) convertView.findViewById(R.id.tv_comm);
		TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
		TextView tvExec = (TextView) convertView.findViewById(R.id.tv_exec);
		TextView tvSwitch = (TextView) convertView.findViewById(R.id.tv_switch);
		
		// 显示内容
		tvArea.setText(listGOI.get(position).getArea());
		tvName.setText(listGOI.get(position).getName());
		switch(listGOI.get(position).getCommuType()){
		case GatewayOutputInfo.COMM_TYPE_232:
			tvComm.setText("232");
			break;
		case GatewayOutputInfo.COMM_TYPE_485:
			tvComm.setText("485");
			break;
		case GatewayOutputInfo.COMM_TYPE_KNX:
			tvComm.setText("KNX");
			break;
		default:
			tvComm.setText("ERR");
			break;	
		}
		switch(listGOI.get(position).getDetailType()){
		case GatewayOutputInfo.DETAIL_TYPE_EXECUTE_PPS:
			tvType.setText("开关\n(PPS)");
			break;
		default:
			tvType.setText("ERR");
			break;
		}
		tvExec.setText(String.valueOf(listGOI.get(position).getExecuteId()));
		tvSwitch.setText(String.valueOf(listGOI.get(position).getSwitchId()));
		
		return convertView;
	}

	/**
	 * 得到网关配置列表
	 */
	public List<GatewayOutputInfo> getGatewayOutputInfoList(){
		return listGOI;
	}
	
	/**
	 * 更新网关配置列表
	 */
	public void setGatewayOutputInfoList(List<GatewayOutputInfo> list){
		listGOI = list;
	}
}
