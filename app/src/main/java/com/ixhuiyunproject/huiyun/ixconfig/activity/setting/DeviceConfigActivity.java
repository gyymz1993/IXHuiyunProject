package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginSucceedActtion;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.DeviceConfigListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.WarningDialog;
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


/**
 * 设备设置
 * @author lzn
 *
 */
public class DeviceConfigActivity extends SwipeBackActivity {

	private ImageView ivReturn;
	private TextView tvCommit;
	private ExpandableListView elvDevice;
	private DeviceConfigListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 去掉默认的标题栏 */
		setContentView(R.layout.activity_device_config);

		/* 从intent和bundle中提取需要的参数 */

		/* 标题栏 */
		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 退出该Activity
				finish();
			}
		});

		/* 提交按钮 */
		tvCommit = (TextView) findViewById(R.id.tv_title_right);
		tvCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 设备名不能为空
				if (adapter.isDeviceNameEmpty()) {
					WarningDialog dlg = new WarningDialog(
							DeviceConfigActivity.this, "提示", "设备名字不能为空！");
					dlg.show(getFragmentManager(), "");
					return;
				}

				// 显示等待对话框
				showpDialog("正在提交……");
				// 将折叠列表用数据转换回设备列表数据
				final List<OutDevice> listUpdate = adapter.getAllDevices();
				// 设置上传设备返回监听
				JSONModuleManager.getInstance().result_40
						.setOnCmdReseivedListener(new OnResultListener<Object>() {

							@Override
							public void onResult(boolean isSecceed, Object obj) {
								// 取消等待对话框
								dismissDialog();
								if (isSecceed) {
									// 把更新后的设备列表数据直接覆盖全局静态的设备列表数据
									DeviceManager.outDeviceList = listUpdate;
									// 吐司显示
									ToastUtils.showToastReal("提交成功！");
									// 更新输入设备的数据
									LoginSucceedActtion.downloadInputDevice();
									// 关闭该Activity
									finish();
								} else {
									// 吐司显示
									ToastUtils.showToastReal("提交失败，请检查下原因");
								}
							}
						});
				// 准备上传设备
				MyJsonObj2 myJsonObj2 = JsonUtil.getAJsonObj2ForMaster();
				myJsonObj2.code = 39;
				MyJsonObj2.Data data = new MyJsonObj2.Data();
				data.token = StaticValue.user.getToken();
				// 将设备列表数据转换成JSON用数据
				List<Map<String, String>> listJson = new ArrayList<Map<String, String>>();
				for (OutDevice dev : listUpdate) {
					Map<String, String> map = new HashMap<String, String>();
					// 需要留意area属性可能为空的情况
					if (StringUtils.isEmpty(dev.getArea())) {
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

		/* 折叠式列表 */
		elvDevice = (ExpandableListView) findViewById(R.id.elv_device);
		elvDevice.setGroupIndicator(null); // 去掉默认的箭头
		elvDevice.setDivider(null); // 去掉默认的分割线
		adapter = new DeviceConfigListAdapter(this,
				getTheCopyOfAllDevice(DeviceManager.outDeviceList));
		elvDevice.setAdapter(adapter);
	}

	/**
	 * 获得所有设备的信息的副本
	 */
	private List<OutDevice> getTheCopyOfAllDevice(List<OutDevice> listDev) {
		OutDevice devCopy;
		List<OutDevice> listDevCopy = new ArrayList<OutDevice>();
		for (OutDevice dev : DeviceManager.outDeviceList) {
			devCopy = new OutDevice();
			devCopy.setArea(dev.getArea());
			devCopy.setName(dev.getName());
			devCopy.setDetails(dev.getDetails());
			devCopy.setAddress(dev.getAddress());
			devCopy.setNumber(dev.getNumber());
			devCopy.setPhoneCode(dev.getPhoneCode());
			devCopy.setType(dev.getType());
			listDevCopy.add(devCopy);
		}

		return listDevCopy;
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
