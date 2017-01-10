package com.ixhuiyunproject.huiyun.ixconfig.activity.scene;

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
import com.ixhuiyunproject.huiyun.ixconfig.adapter.SceneConfigListAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ConfirmDialog;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.WarningDialog;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：对场景进行具体的配置
 * 
 * @author lzn
 * 
 */
public class SceneConfigActivity extends BaseActivity {

	private ImageView ivReturn;
	private TextView tvSave;
	private ExpandableListView elvDevice;
	private SceneConfigListAdapter adapter;

	private String sceneName; // 场景名
	private int sceneIcon; // 场景图标号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 去掉默认的标题栏 */
		setContentView(R.layout.activity_scene_config);

		/* 从intent和bundle中提取需要的参数 */
		sceneName = getIntent().getStringExtra("name");
		sceneIcon = getIntent().getIntExtra("icon", 1);

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
				// 从适配器中获得所需的信息
				final List<OutDevice> allDevices = adapter.getAllDevices();
				final boolean[] selected = adapter.getAllSelected();
				final int[] onoff = adapter.getAllOnOff();

				// 未进行任何设置时，弹出警告对话框
				boolean selectAny = false;
				if (selected != null) {
					for (boolean s : selected) {
						if (s) {
							selectAny = true;
						}
					}
				}
				if (allDevices.size() < 1 || !selectAny) {
					WarningDialog dlg = new WarningDialog(
							SceneConfigActivity.this, "提示", "请对场景的具体参数进行设置");
					dlg.show(getFragmentManager(), "");
				} else {
					// 弹出确认对话框
					ConfirmDialog dlg = new ConfirmDialog(
							SceneConfigActivity.this, "提示", "是否将该场景提交？");
					dlg.setConfirmListener(new OnSucceedListener<Object>() {

						@Override
						public void onSucceed(Object obj) {
							// 显示等待对话框
							showpDialogEternity("正在提交……");
							// 设置监听
							JSONModuleManager.getInstance().result_54
									.setOnCmdReseivedListener(new OnResultListener<Object>() {

										@Override
										public void onResult(boolean isSucceed,
												Object obj) {
											// 取消等待对话框
											dismissDialog();
											if (isSucceed) {
												/**
												 * 如果此场景名已存在忽略  但改变选择场景图片
												 * 否则将场景存入静态中
												 */
												SceneItem items = new SceneItem();
												items.setScene_name(sceneName);
												items.setImage_bg(sceneIcon);
												if (!FragmentContainer.SCENE_LIST.contains(items)) {
													FragmentContainer.SCENE_LIST
															.add(items);
												}else{
													SceneItem item=FragmentContainer.SCENE_LIST
															.get(FragmentContainer.SCENE_LIST.indexOf(items));
													item.setImage_bg(sceneIcon);
												}
												// 吐司显示
												ToastUtils
														.showToastReal("提交成功！");
												// 退出Activity
												finish();
											} else {
												// 吐司显示
												ToastUtils
														.showToastReal("提交失败，请检查原因");
											}
										}
									});
							// 根据参数生成json格式命令并发送出去
							BaseJsonObj.MyJsonObj3 jsonObj = JsonUtil
									.getAJsonObj3ForMaster();
							jsonObj.code = 53;

							BaseJsonObj.MyJsonObj3.Data3 data = new BaseJsonObj.MyJsonObj3.Data3();
							Map<String, String> info = new HashMap<String, String>();
							List<Map<String, String>> list = new ArrayList<Map<String, String>>();

							info.put("token", StaticValue.user.getToken());
							info.put("scene_name", sceneName);
							info.put("image_bg", String.valueOf(sceneIcon));
							for (int i = 0; i < allDevices.size(); i++) {
								if (selected[i]) {
									Map<String, String> map = new HashMap<String, String>();
									map.put("phoneCode", String
											.valueOf(allDevices.get(i)
													.getPhoneCode()));
									map.put("state",
											String.valueOf(onoff[i]));
									list.add(map);
								}
							}
							data.info = info;
							data.list = list;

							jsonObj.data = data;
							NetJsonUtil.getInstance().addCmdForSend(jsonObj);
						}
					});
					dlg.show(getFragmentManager(), "");
				}
			}
		});

		/* 折叠式列表 */
		elvDevice = (ExpandableListView) findViewById(R.id.elv_device);
		elvDevice.setGroupIndicator(null); // 去掉默认的箭头
		elvDevice.setDivider(null); // 去掉默认的分割线
		adapter = new SceneConfigListAdapter(this, DeviceManager.outDeviceList);
		elvDevice.setAdapter(adapter);
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
