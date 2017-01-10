package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.bean.ColorState;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ColorPickerDialog;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;

import java.util.List;

/** “场景配置”的折叠列表的适配器
 * @author lzn
 *
 */
public class SceneConfigListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<OutDevice> allDevices;
	private List<String> allDetails;
	private boolean[] selected;
	private int[] onoff;
	
	public SceneConfigListAdapter(Context context, List<OutDevice> allDevices){
		this.mContext = context;
		this.allDevices = allDevices;
		this.allDetails = DeviceManager.getAllDetails(allDevices);
		if(allDevices.size() > 0){
			selected = new boolean[allDevices.size()];
			onoff = new int[allDevices.size()];
		}
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
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(isLastChild){
			convertView = View.inflate(mContext, R.layout.item_child_scene_config_last, null);
		} else {
			convertView = View.inflate(mContext, R.layout.item_child_scene_config, null);
		}
		
		// 该子项的数据在列表中的序号
		List<OutDevice> allDev = DeviceManager.getDeviceForDetails(
				allDetails.get(groupPosition), allDevices);
		final int position = allDevices.indexOf(allDev.get(childPosition));
		
		final CheckBox cbSelect = (CheckBox) convertView.findViewById(R.id.cb_selected);
		final CheckBox cbOnOff = (CheckBox) convertView.findViewById(R.id.cb_switch);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		
		String area = StringUtils.isEmpty(allDevices.get(position).getArea())?
				"未知":allDevices.get(position).getArea();
		tvName.setText(area + " " + allDevices.get(position).getName());
		cbOnOff.setChecked(onoff[position]!=0?true:false);
		cbSelect.setChecked(selected[position]);
		cbOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				onoff[position] = isChecked?1:0;
				// 如果该设备是调光灯，并且准备从关的状态切换到开的状态时
				if(allDevices.get(position).getType() == OutDevice.TYPE_COLOR_LAMP &&
						isChecked == true){
					// 弹出调色对话框
					ColorPickerDialog cpdlg = new ColorPickerDialog(
							mContext, ColorState.getARGBfromBRG(onoff[position]),
							"请选择颜色", new ColorPickerDialog.OnColorChangedListener() {
						
						@Override
						public void colorChanged(int color) {
							
							onoff[position] = ColorState.getBRGfromARGB(color);
							cbOnOff.setChecked(onoff[position]!=0?true:false);
						}
					});
					cpdlg.enableCenterClose(true);
					cpdlg.show();
				}
			}
		});
		cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selected[position] = isChecked;
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selected[position]){
					selected[position] = false;
					cbSelect.setChecked(false);
				} else {
					selected[position] = true;
					cbSelect.setChecked(true);
				}
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
			return allDetails.size();
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
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/** 返回设备列表
	 * @return
	 */
	public List<OutDevice> getAllDevices(){
		return allDevices;
	}
	
	/** 用以确认设备是否被选进场景，序号与设备列表对应
	 * @return
	 */
	public boolean[] getAllSelected(){
		return selected;
	}
	
	/** 用以确认设备是开还是关，序号与设备列表对应
	 * @return
	 */
	public int[] getAllOnOff(){
		return onoff;
	}
}
