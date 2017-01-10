package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import java.util.HashMap;
import java.util.List;

/** “设备设置”的折叠列表的适配器
 * @author lzn
 *
 */
public class DeviceConfigListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<OutDevice> allDevices;
	private List<String> allDetails;
	
	public DeviceConfigListAdapter(Context context, List<OutDevice> allDevices){
		this.mContext = context;
		this.allDevices = allDevices;
		this.allDetails = DeviceManager.getAllDetails(allDevices);
	}
	
	/** 获得所有设备的信息
	 * @return
	 */
	public List<OutDevice> getAllDevices(){
		return allDevices;
	}

	/** 确认设备名是否存在为空的
	 * @return
	 */
	public boolean isDeviceNameEmpty(){
		for(OutDevice out:allDevices){
			if(out.getName().trim().equals("")){
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(isLastChild){
			convertView = View.inflate(mContext, R.layout.item_child_revise_name_last, null);
		} else {
			convertView = View.inflate(mContext, R.layout.item_child_revise_name, null);
		}
		
		// 该子项的数据在列表中的序号
		List<OutDevice> allDev = DeviceManager.getDeviceForDetails(
				allDetails.get(groupPosition), allDevices);
		final int position = allDevices.indexOf(allDev.get(childPosition));
		
		// 编辑框
		final EditText etName = (EditText) convertView.findViewById(R.id.et_name);
		etName.setText(allDevices.get(position).getName());
		etName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				allDevices.get(position).setName(s.toString());
			}
		});
		etName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etName.requestFocus();
			}
		});
		
		// 设备闪烁
		ImageView ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);
		ivCheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 用于显示的编号，由于网关以外的设备编号都是从0开始的，在显示上都是从1开始
				int numberShow = allDevices.get(position).getNumber();
				if(allDevices.get(position).getType() != OutDevice.TYPE_GATEWAY)
					numberShow ++;
				String show = "按键位于左数第" + numberShow + "个";
				// 吐司显示
				ToastUtils.showToastReal(show);
				// 让目标设备闪烁
				MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
				jsonObj.code = 41;
				jsonObj.data = new HashMap<String, String>();
				jsonObj.data.put("token", StaticValue.user.getToken());
				jsonObj.data.put("address", String.valueOf(allDevices.get(position).getAddress()));
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		List<OutDevice> allDev = DeviceManager.getDeviceForDetails(
				allDetails.get(groupPosition), allDevices);
		return allDev.size();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(isExpanded || groupPosition >= getGroupCount()-1){
			convertView = View.inflate(mContext, R.layout.item_parent_details_last, null);
		} else {
			convertView = View.inflate(mContext, R.layout.item_parent_details, null);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		tvName.setText(allDetails.get(groupPosition));
		return convertView;
	}
	
	@Override
	public int getGroupCount() {
		if(allDetails == null)
			return 0;
		else
			return allDetails.size();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

}
