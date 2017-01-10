package com.ixhuiyunproject.huiyun.ixconfig.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.object.ChildItemObj;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.object.ParentItemObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

/** “添加设备到区域”的折叠列表的适配器
 * @author lzn
 *
 */
public class AddToAreaListAdapter extends BaseExpandableListAdapter {

	/**
	 * 用于保存列表所有子项和父项的信息
	 */
	private List<ParentItemObj> listItem;
	private Context mContext;
	
	public AddToAreaListAdapter(Context context, List<ParentItemObj> list){
		this.listItem = list;
		this.mContext = context;
	}
	
	public List<ParentItemObj> getItemList(){
		return listItem;
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
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(childPosition >= listItem.get(groupPosition).children.size() - 1){
			convertView = View.inflate(mContext, R.layout.item_child_addtoarea_last, null);
		} else {
			convertView = View.inflate(mContext, R.layout.item_child_addtoarea, null);
		}
		final CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb_selected);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
		ImageView iv = (ImageView) convertView.findViewById(R.id.iv_check);
		final ChildItemObj child = listItem.get(groupPosition).children.get(childPosition);
		
		// 复选框
		cb.setChecked(child.checked);
		
		// 区域 & 名称
		String area = "未知";
		if(!StringUtils.isEmpty(child.area)){
			area = child.area;
		}
		tv.setText(area + " " + listItem.get(groupPosition).children.get(childPosition).name);
		
		// 自身的监听
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 本身被点击后，改变复选框的状态
				if(cb.isChecked()){
					listItem.get(groupPosition).children.get(childPosition).checked = false;
				} else {
					listItem.get(groupPosition).children.get(childPosition).checked = true;
				}
				cb.setChecked(listItem.get(groupPosition).children.get(childPosition).checked);
			}
		});
		
		// 复选框的监听
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				listItem.get(groupPosition).children.get(childPosition).checked = isChecked;
			}
		});
		
		// 测试按钮的监听
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 用于显示的编号，由于网关以外的设备编号都是从0开始的，在显示上都是从1开始
				int numberShow = child.number;
				if(child.type != 12) numberShow ++;
				String show = "按键位于左数第" + numberShow + "个";
				// 吐司显示
				ToastUtils.showToastReal(show);
				// 让目标设备闪烁
				BaseJsonObj.MyJsonObj1 jsonObj = JsonUtil.getAJsonObj1ForMaster();
				jsonObj.code = 41;
				jsonObj.data = new HashMap<String, String>();
				jsonObj.data.put("token", StaticValue.user.getToken());
				jsonObj.data.put("address", String.valueOf(child.address));
				NetJsonUtil.getInstance().addCmdForSend(jsonObj);
			}
		});
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return listItem.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return listItem.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(isExpanded || groupPosition >= getGroupCount()-1){
			convertView = View.inflate(mContext, R.layout.item_parent_details_last, null);
		} else {
			convertView = View.inflate(mContext, R.layout.item_parent_details, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
		
		// 设备类别
		tv.setText(listItem.get(groupPosition).details);
		
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
