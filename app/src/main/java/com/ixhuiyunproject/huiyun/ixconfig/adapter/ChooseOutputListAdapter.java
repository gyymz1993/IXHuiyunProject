package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.ComboOutput;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.data.SettingTransit;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;

import java.util.List;

/** “选择输出设备”的折叠列表的适配器
 * 
 * @author lzn
 *
 */
public class ChooseOutputListAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private List<OutDevice> allDevices;
	private List<String> allDetails;
	private List<SceneItem> allScene;
	
	public ChooseOutputListAdapter(Activity activity, List<OutDevice> allDevices,
			List<SceneItem> allScene){
		this.activity = activity;
		this.allDevices = allDevices;
		this.allScene = allScene;
		this.allDetails = DeviceManager.getAllDetails(allDevices);
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(isLastChild){
			convertView = View.inflate(activity, R.layout.item_child_combo_output_last, null);
		} else {
			convertView = View.inflate(activity, R.layout.item_child_combo_output, null);
		}
		
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		
		if(groupPosition < getGroupCount()-2){ // 输出设备
			// 该子项的数据在列表中的序号
			List<OutDevice> allDev = DeviceManager.getDeviceForDetails(
					allDetails.get(groupPosition), allDevices);
			final int position = allDevices.indexOf(allDev.get(childPosition));
			String area = StringUtils.isEmpty(allDevices.get(position).getArea())?
					"未知":allDevices.get(position).getArea();
			tvName.setText(area + " " + allDevices.get(position).getName());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 把输出设备传回去
					ComboOutput co = new ComboOutput();
					co.type = ComboOutput.OUT_DEV;
					co.outDev = allDevices.get(position);
					SettingTransit.comboOutput = co;
					activity.finish();
				}
			});
		} else if(groupPosition == getGroupCount()-2) {  // 场景
			tvName.setText(allScene.get(childPosition).getScene_name());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 把输出设备传回去
					ComboOutput co = new ComboOutput();
					co.type = ComboOutput.SCENE;
					co.sceneName = allScene.get(childPosition).getScene_name();
					SettingTransit.comboOutput = co;
					activity.finish();
				}
			});
		} else if(groupPosition == getGroupCount()-1) {  // 其它
			switch(childPosition){
			case 0:
				tvName.setText("取消前次组合");
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 把输出设备传回去
						ComboOutput co = new ComboOutput();
						co.type = ComboOutput.NOLINK;
						SettingTransit.comboOutput = co;
						activity.finish();
					}
				});
				break;
			}
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition < getGroupCount()-2){ // 输出设备
			List<OutDevice> allDev = DeviceManager.getDeviceForDetails(
					allDetails.get(groupPosition), allDevices);
			return allDev.size();
		} else if(groupPosition == getGroupCount()-2) {  // 场景
			return allScene.size();
		} else if(groupPosition == getGroupCount()-1) {  // 其它
			return 1;
		}
		
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
	
	@Override
	public int getGroupCount() {
		if(allDetails == null)
			return 0;
		else
			return allDetails.size() + 2;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(isExpanded || groupPosition >= getGroupCount()-1){
			convertView = View.inflate(activity, R.layout.item_parent_details_last, null);
		} else {
			convertView = View.inflate(activity, R.layout.item_parent_details, null);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		if(groupPosition < getGroupCount()-2) {
			tvName.setText(allDetails.get(groupPosition));
		} else if(groupPosition == getGroupCount()-2) {
			tvName.setText("场景");
		} else if(groupPosition == getGroupCount()-1) {
			tvName.setText("其它");
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
