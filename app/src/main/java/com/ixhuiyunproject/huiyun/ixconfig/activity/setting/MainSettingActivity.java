package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.redray.RedRaySettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.scene.SceneSettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.timing.AddTimerActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.timing.TimerTaskListActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.CommonRegistrationActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.voice.VoiceActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.wifi.WifiActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.ConfirmDialog;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.EnterDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.view.SettingItemRLView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MainSettingActivity extends SwipeBackActivity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏title
		setContentView(R.layout.frg_setdevice);
		mContext = this;
		initUser();
		initDevice();
		initGay();
		initArea();
		initScene();
		initComm();
		//initFind();
		initRemote();
		initSlave();
		initAlarm();
		initQueryAlarm();// 查询定时任务
		initVoice();// 语音
		//addDevice();
		setWifi();  // 主机wifi
		setWTRWifi(); // 中继器wifi
		setPankey();  // 主机key
	}

	@SuppressWarnings("unused")
	private void addDevice() {
//		SettingItemRLView addDevice = (SettingItemRLView) findViewById(R.id.add_device);
//		addDevice.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
//					jsonobj.code = 43;
//					jsonobj.obj = FinalValue.OBJ_MASTER;
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("token", StaticValue.user.getToken());
//					jsonobj.setData(map);
//					NetJsonUtil.getInstance().addCmdForSend(jsonobj);
//					JSONModuleManager.getInstance().result_44
//							.setOnCmdReseivedListener(new OnResultListener<Object>() {
//								@Override
//								public void onResult(boolean isSecceed,
//										Object obj) {
//									if (isSecceed) {
//										ToastUtils.showToastReal("请点击设备....");
//									}
//								}
//							});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		});
	}

	private void initQueryAlarm() {
		SettingItemRLView setAlarm = (SettingItemRLView) findViewById(R.id.alarm_query);
		setAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						TimerTaskListActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setWifi() {
		SettingItemRLView setAlarm = (SettingItemRLView) findViewById(R.id.set_wifi);
		setAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WifiActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setWTRWifi(){
		SettingItemRLView setAlarm = (SettingItemRLView) findViewById(R.id.set_wtr_wifi);
		setAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WTRWifiSettingActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void setPankey(){
		SettingItemRLView setKey = (SettingItemRLView) findViewById(R.id.set_key);
		setKey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出输入对话框提示输入要设置的PAN KEY
				EnterDialogUtils edu = new EnterDialogUtils(
						MainSettingActivity.this,"请输入要设置的KEY");
				edu.onCreateDialog();
				edu.setCallBack(new EnterDialogUtils.CallBack() {
					
					@Override
					public void isConfirm(String edString) {
						try {
							int key = Integer.parseInt(edString);
							key = key % 255; // key的取值范围在0~255
							
							MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
							jsonobj.code = 75;
							jsonobj.obj = "master";
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("token", StaticValue.user.getToken());
							maps.put("pankey", key + "");
							jsonobj.setData(maps);

							NetJsonUtil.getInstance().addCmdForSend(jsonobj);
							System.out.println("配置pan key,值为" + key);
							JSONModuleManager.getInstance().result_76.setOnCmdReseivedListener(
									new OnResultListener<Object>() {
								
								@Override
								public void onResult(boolean isSecceed, Object obj) {
									ToastUtils.showToastReal("设置成功！");
								}
							});
						} catch (NumberFormatException e) {
							ToastUtils.showToastReal("请输入数字！");
						}
					}
				});
			}
		});
	}
	
	private void initAlarm() {
		SettingItemRLView setAlarm = (SettingItemRLView) findViewById(R.id.alarm_test);
		setAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AddTimerActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 语音
	 */
	private void initVoice() {
		View setRemote = findViewById(R.id.voice_test);

		setRemote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, VoiceActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initRemote() {
		SettingItemRLView setRemote = (SettingItemRLView) findViewById(R.id.set_remote);

		setRemote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						RedRaySettingActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 注册普通用户 Function:
	 * 
	 * @author Howard DateTime 2015年1月24日 上午11:18:48
	 * @param view
	 */
	private void initUser() {
		SettingItemRLView setUser = (SettingItemRLView) findViewById(R.id.set_usera);
		setUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						CommonRegistrationActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Function: 组合
	 * 
	 * @author Howard DateTime 2015年1月29日 下午7:53:13
	 */
	private void initComm() {
		SettingItemRLView commDevice = (SettingItemRLView) findViewById(R.id.sett_com);
		commDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ComboConfigActivity.class);
				startActivity(intent);
			}
		});

	}

	public void setBack(View v) {
		this.finish();
	}

	/**
	 * 设备设置 Function:
	 * 
	 * @author Howard DateTime 2015年1月28日 上午9:23:40
	 * @param view
	 */
	private void initDevice() {
		SettingItemRLView setDevice = (SettingItemRLView) findViewById(R.id.set_device);

		setDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DeviceConfigActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Function: 网关设置
	 * 
	 * @author Howard DateTime 2015年1月24日 上午11:19:16
	 * @param view
	 */
	private void initGay() {
		SettingItemRLView setGay = (SettingItemRLView) findViewById(R.id.set_gaw);

		setGay.setOnClickListener(new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// 弹出提示框警告
				ConfirmDialog dlg = new ConfirmDialog(mContext, "警告",
						"该功能建议在详细阅读说明书之后，或者在安装人员的指导下使用！");
				dlg.setConfirmListener(new OnSucceedListener<Object>() {

					@Override
					public void onSucceed(Object obj) {
						Intent intent = new Intent(mContext,
								ChooseGatewayActivity.class);
						startActivity(intent);
					}
				});
				dlg.show(getFragmentManager(), "");
			}
		});
	}

	/**
	 * Function:区域设置
	 * 
	 * @author Howard DateTime 2015年1月24日 上午11:19:27
	 * @param view
	 */
	private void initArea() {
		SettingItemRLView setArea = (SettingItemRLView) findViewById(R.id.set_area);

		setArea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, AreaSettingActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Function: 场景设置
	 * 
	 * @author Howard DateTime 2015年1月24日 上午11:19:40
	 * @param view
	 */
	private void initScene() {
		SettingItemRLView setScene = (SettingItemRLView) findViewById(R.id.set_scence);

		setScene.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SceneSettingActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Function: 查找新设备
	 * 
	 * @author Howard DateTime 2015年1月24日 上午11:19:49
	 * @param view
	 */
	@SuppressWarnings("unused")
	private void initFind() {
//		SettingItemRLView setFind = (SettingItemRLView) findViewById(R.id.set_find);
//
//		setFind.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
//					jsonobj.code = 7;
//					jsonobj.obj = FinalValue.OBJ_MASTER;
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("token", StaticValue.user.getToken());
//					jsonobj.setData(map);
//					NetJsonUtil.getInstance().addCmdForSend(jsonobj);
//					JSONModuleManager.getInstance().result_8
//							.setOnCmdReseivedListener(new OnResultListener<Object>() {
//								@Override
//								public void onResult(boolean isSecceed,
//										Object obj) {
//									if (isSecceed) {
//										ToastUtils.showToastReal("通知主机成功");
//										LogUtils.i("成功");
//									}
//								}
//							});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * 查看所有从机
	 * 
	 * @author lzn
	 */
	private void initSlave() {
		SettingItemRLView setSlave = (SettingItemRLView) findViewById(R.id.set_slave);

		setSlave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SlaveListActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);// 统计页面
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd(FinalValue.mPageName);
		MobclickAgent.onPause(this); // 统计时长
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		// TODO 自动生成的方法存根
		return null;
	}
}
