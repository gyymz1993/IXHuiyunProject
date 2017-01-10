package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginSucceedActtion;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ConfirmDialog;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.WarningDialog;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;

public class GatewaySettingActivity extends BaseActivity {
	
	private EditText etDetails, etName, etCommType, etDetailsType,
				etExecId, etSwitchId;
	private Button btnAdd, btnClean;
	private ImageView ivReturn;
	
	private int address;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gateway_setting);
		
		/* 从intent中获取数据 */
		address = getIntent().getIntExtra("address", 0);
		
		/* 编辑框 */ 
		etDetails = (EditText) findViewById(R.id.et_details);
		etName = (EditText) findViewById(R.id.et_name);
		etCommType = (EditText) findViewById(R.id.et_comm_type);
		etDetailsType = (EditText) findViewById(R.id.et_details_type);
		etExecId = (EditText) findViewById(R.id.et_exec_id);
		etSwitchId = (EditText) findViewById(R.id.et_switch_id);
		etDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 选项对话框
				AlertDialog.Builder builder = new AlertDialog.Builder(GatewaySettingActivity.this);
                builder.setTitle("选择一个类别");
                //    指定下拉列表的显示数据
                final String[] category = {"灯","日光灯","灯带","筒灯","台灯","吊灯","落地灯","调光膜","风机"};
                //    设置一个下拉的列表选择项
                builder.setItems(category, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    	etDetails.setText(category[which]);
                    }
                });
                builder.show();
			}
		});
		
		
		/* 按钮 */
		btnAdd = (Button) findViewById(R.id.btn_commit);
		btnClean = (Button) findViewById(R.id.btn_clean);
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 添加配置
				if(etDetails.getText().toString().trim().equals("") ||
					etName.getText().toString().trim().equals("") ||
					etCommType.getText().toString().trim().equals("") ||
					etDetailsType.getText().toString().trim().equals("") ||
					etExecId.getText().toString().trim().equals("") ||
					etSwitchId.getText().toString().trim().equals("")){
					WarningDialog dlg = new WarningDialog(
							GatewaySettingActivity.this, "提示", "填写项不能为空");
					dlg.show(getFragmentManager(), "");
				} else {
					// 显示等待对话框
					showpDialogEternity("正在添加配置……");
					
					String details = etDetails.getText().toString().trim();
					String name = etName.getText().toString().trim();
					String strCommType = etCommType.getText().toString().trim();
					String strDetailsType = etDetailsType.getText().toString().trim();
					int execId = Integer.valueOf(etExecId.getText().toString().trim());
					int switchId = Integer.valueOf(etSwitchId.getText().toString().trim());
					int commType,detailsType;
					
					if(strCommType.equals("485")){
						commType = 1;
					} else {
						commType = 1;
					}
					
					if(strDetailsType.equals("开关(PPS)")){
						detailsType = 2;
					} else {
						detailsType = 2;
					}
					
					// 发送json命令
					JSONModuleManager.getInstance().result_34.setOnCmdReseivedListener(
							new OnResultListener<Object>() {
						
						@Override
						public void onResult(boolean isSecceed, Object obj) {
							if(isSecceed){
								// 取消等待对话框
								dismissDialog();
								
								// 显示吐司
								ToastUtils.showToastReal("添加配置成功！");
							}
						}
					});
					MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
					myJsonObj1.code = 33;
					myJsonObj1.data = new HashMap<String, String>();
					myJsonObj1.data.put("token", StaticValue.user.getToken());
					myJsonObj1.data.put("details", details);
					myJsonObj1.data.put("name", name);
					myJsonObj1.data.put("address", String.valueOf(address));
					myJsonObj1.data.put("comm_type", String.valueOf(commType));
					myJsonObj1.data.put("details_type", String.valueOf(detailsType));
					myJsonObj1.data.put("exec_id", String.valueOf(execId));
					myJsonObj1.data.put("switch_id", String.valueOf(switchId));
					NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
				}
			}
		});
		btnClean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 清空填写的数据
				etDetails.setText("");
				etName.setText("");
				etExecId.setText("");
				etSwitchId.setText("");
			}
		});
		
		/* 返回按钮 */
		ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// 重新下载设备信息
		LoginSucceedActtion.downloadOutputDevice();
	}
	
	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				switch(pos){
				case 0:
					// 清空网关配置
					ConfirmDialog dlg = new ConfirmDialog(
							GatewaySettingActivity.this, "提示", "确认是否清空网关配置？");
					dlg.setConfirmListener(new OnSucceedListener<Object>() {
						
						@Override
						public void onSucceed(Object obj) {
							// 显示等待对话框
							showpDialogEternity("正在清空配置……");
							
							// 发送json命令
							JSONModuleManager.getInstance().result_34.setOnCmdReseivedListener(
									new OnResultListener<Object>() {
								
								@Override
								public void onResult(boolean isSecceed, Object obj) {
									if(isSecceed){
										// 取消等待对话框
										dismissDialog();
										
										// 显示吐司
										ToastUtils.showToastReal("清空配置成功！");
									}
								}
							});
							MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
							myJsonObj1.code = 33;
							myJsonObj1.data = new HashMap<String, String>();
							myJsonObj1.data.put("token", StaticValue.user.getToken());
							myJsonObj1.data.put("area", "");
							myJsonObj1.data.put("name", "");
							myJsonObj1.data.put("address", String.valueOf(address));
							myJsonObj1.data.put("comm_type", "0");
							myJsonObj1.data.put("details_type", "0");
							myJsonObj1.data.put("exec_id", "0");
							myJsonObj1.data.put("switch_id", "0");
							NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
						}
					});
					dlg.show(getFragmentManager(), "");
					break;
				}
				popMenu.dismiss();
			}
		};
		return listener;
	}

	@Override
	public String[] setPopMenuName() {
		return new String[]{"清空配置"};
	}
}
