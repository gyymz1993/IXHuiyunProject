package com.ixhuiyunproject.huiyun.ixconfig.activity.contrl;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.BaseActivity;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.AddToAreaListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.object.ChildItemObj;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.object.ParentItemObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 功能：把设备添加到区域
 * @author lzn
 *
 */
public class AddToAreaActivity extends BaseActivity {
	
	private ImageView ivReturn;
	private TextView tvSave;
	private ExpandableListView elvDevice;
	private AddToAreaListAdapter adapter;
	/* 全局的设备列表的副本 */
	private List<OutDevice> listDeviceTemp = new ArrayList<OutDevice>();
	private String area;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		/* 去掉默认的标题栏 */
		setContentView(R.layout.activity_add_to_area);
				
		/* 从intent和bundle中提取需要的参数 */
		area = getIntent().getExtras().getString("area");
		System.out.println("AddToAreaActivity"+area);
		/* 标题栏 */
		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		tvSave = (TextView) findViewById(R.id.tv_title_right);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 退出该Activity
				finish();
			}
		});
		tvSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 显示等待对话框
				showpDialog("正在保存……");
				// 将折叠列表用数据转换回设备列表数据
				final List<OutDevice> listUpdate = transferToDeviceData(adapter.getItemList());
				// 设置上传设备返回监听
				JSONModuleManager.getInstance().result_40.setOnCmdReseivedListener(
						new OnResultListener<Object>() {
					
					@Override
					public void onResult(boolean isSecceed, Object obj) {
						// 取消等待对话框
						dismissDialog();
						if(isSecceed){
							// 把更新后的设备列表数据直接覆盖全局静态的设备列表数据
							DeviceManager.outDeviceList = listUpdate;
							// 吐司显示
							ToastUtils.showToastReal("保存成功！");
							// 关闭该Activity
							finish();
						} else {
							// 吐司显示
							ToastUtils.showToastReal("保存失败，请检查下原因");
						}
					}
				});
				// 准备上传设备
				MyJsonObj2 myJsonObj2 = JsonUtil.getAJsonObj2ForMaster();
				myJsonObj2.code = 39;
				MyJsonObj2.Data data = new MyJsonObj2.Data();
				data.token = StaticValue.user.getToken();
				// 将设备列表数据转换成JSON用数据
				List<Map<String,String>> listJson = new ArrayList<Map<String, String>>();
				for(OutDevice dev:listUpdate){
					Map<String,String> map = new HashMap<String,String>();
					// 需要留意area属性可能为空的情况
					if(StringUtils.isEmpty(dev.getArea())){
						map.put("area", "");
					} else {
						map.put("area", dev.getArea());
					}
					map.put("name", dev.getName());
					map.put("detail", dev.getDetails());
					map.put("phoneCode", String.valueOf(dev.getPhoneCode()));
					map.put("type", String.valueOf(dev.getType()));
					map.put("address", String.valueOf(dev.getAddress()));
					map.put("number", String.valueOf(dev.getNumber()));
					listJson.add(map);
				}
				data.setList(listJson);
				myJsonObj2.data = data;
				// 将命令加入发送队列
				NetJsonUtil.getInstance().addCmdForSend(myJsonObj2);
			}
		});
		
		/* 获取设备列表的副本 */
		getTheCopyOfAllDevice();
		
		/* 获得适配器专用的列表 */
		List<ParentItemObj> allParent = transferToAdapterUsedData(listDeviceTemp);
		
		/* 折叠式列表 */
		elvDevice = (ExpandableListView) findViewById(R.id.elv_device);
		elvDevice.setGroupIndicator(null); // 去掉默认的箭头
		elvDevice.setDivider(null); // 去掉默认的分割线
		adapter = new AddToAreaListAdapter(this, allParent);
		elvDevice.setAdapter(adapter);
	}
	
	/**
	 * 获得所有设备的信息的副本
	 */
	private void getTheCopyOfAllDevice(){
		OutDevice devCopy;
		listDeviceTemp.clear();
		for(OutDevice dev:DeviceManager.outDeviceList){
			devCopy = new OutDevice();
			devCopy.setArea(dev.getArea());
			devCopy.setName(dev.getName());
			devCopy.setDetails(dev.getDetails());
			devCopy.setAddress(dev.getAddress());
			devCopy.setNumber(dev.getNumber());
			devCopy.setPhoneCode(dev.getPhoneCode());
			devCopy.setType(dev.getType());
			listDeviceTemp.add(devCopy);
		}
	}
	
	/**
	 * 将设备列表转换成折叠列表用数据
	 */
	private List<ParentItemObj> transferToAdapterUsedData(List<OutDevice> listDevice){
		List<ParentItemObj> listParent = new ArrayList<ParentItemObj>();
		
		// 获得所有类别
		List<String> allDetails = getAllDetails(listDevice);
		
		// 遍历所有类别
		for(String details:allDetails){
			List<ChildItemObj> listChild = new ArrayList<ChildItemObj>();
			// 遍历所有对应类别的设备
			for(OutDevice dev:listDevice){
				if(dev.getDetails().equals(details)){
					ChildItemObj child = new ChildItemObj();
					child.address = dev.getAddress();
					child.number = dev.getNumber();
					child.area = dev.getArea();
					child.name = dev.getName();
					child.phoneCode = dev.getPhoneCode();
					child.type = dev.getType();
					if(!StringUtils.isEmpty(dev.getArea()) && dev.getArea().equals(area)){
						child.checked = true;
					} else {
						child.checked = false;
					}
					listChild.add(child);
				}
			}
			// 生成父项
			ParentItemObj parent = new ParentItemObj(details, listChild);
			listParent.add(parent);
		}
		
		return listParent;
	}
	
	/**
	 * 将折叠列表用数据转换成设备列表
	 */
	private List<OutDevice> transferToDeviceData(List<ParentItemObj> listParent){
		List<OutDevice> listDevice = new ArrayList<OutDevice>();

		// 遍历所有父项
		for(ParentItemObj parent:listParent){
			// 遍历所有子项
			for(ChildItemObj child:parent.children){
				OutDevice dev = new OutDevice();
				dev.setDetails(parent.details);
				dev.setName(child.name);
				dev.setAddress(child.address);
				dev.setNumber(child.number);
				dev.setType(child.type);
				dev.setPhoneCode(child.phoneCode);
				if(child.checked){
					// 要添加到该区域的设备
					dev.setArea(area);
				} else if(!StringUtils.isEmpty(child.area) && area.equals(child.area)){
					// 从该区域移除的设备
					dev.setArea("");
				} else {
					// 保持区域值不变的设备
					dev.setArea(child.area);
				}
				listDevice.add(dev);
			}
		}
		
		return listDevice;
	}
	
	/**
	 * 获得所有设备的类别
	 */
	private List<String> getAllDetails(List<OutDevice> listDevice){
		boolean exists;
		List<String> allDetails = new ArrayList<String>();
		// 遍历所有设备
		for(OutDevice dev:listDevice){
			exists = false;
			// 遍历所有已存在的类别
			for(String details:allDetails){
				if(details.equals(dev.getDetails())){
					exists = true;
					continue;
				}
			}
			// 遍历后发现不存在，于是添加进去
			if(!exists){
				allDetails.add(dev.getDetails());
			}
		}
		return allDetails;
	}
	
	/**
	 * 测试用
	 */
	@SuppressWarnings("unused")
	private void test(){

//		/* 测试信息 */
//		List<ParentItemObj> listParent = new ArrayList<ParentItemObj>();
//		
//		List<ChildItemObj> listChild;
//		ParentItemObj parent;
//		ChildItemObj child;
//		
//		listChild = new ArrayList<ChildItemObj>();
//		child = new ChildItemObj(false, "客厅前门灯");
//		listChild.add(child);
//		child = new ChildItemObj(true, "客厅后门灯");
//		listChild.add(child);
//		child = new ChildItemObj(false, "中央灯");
//		listChild.add(child);
//		child = new ChildItemObj(false, "会议室灯");
//		listChild.add(child);
//		parent = new ParentItemObj("普通灯", listChild);
//		listParent.add(parent);
//		
//		listChild = new ArrayList<ChildItemObj>();
//		child = new ChildItemObj(false, "一楼窗帘");
//		listChild.add(child);
//		child = new ChildItemObj(false, "二楼201窗帘");
//		listChild.add(child);
//		child = new ChildItemObj(true, "二楼202窗帘");
//		listChild.add(child);
//		child = new ChildItemObj(false, "三楼总厅窗帘");
//		listChild.add(child);
//		child = new ChildItemObj(true, "三楼行政部窗帘");
//		listChild.add(child);
//		parent = new ParentItemObj("窗帘", listChild);
//		listParent.add(parent);
//		
//		listChild = new ArrayList<ChildItemObj>();
//		child = new ChildItemObj(false, "不明设备1");
//		listChild.add(child);
//		child = new ChildItemObj(false, "不明设备2");
//		listChild.add(child);
//		parent = new ParentItemObj("输出设备", listChild);
//		listParent.add(parent);
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
