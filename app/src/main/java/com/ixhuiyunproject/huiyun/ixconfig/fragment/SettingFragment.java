package com.ixhuiyunproject.huiyun.ixconfig.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.contrl.AddToAreaActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.AreaSettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.ChooseGatewayActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.Contrl_InfraredResult_12;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadAllEare_38;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.DownloadOutDevices_24;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.NotificationStudyResult_8;
import com.ixhuiyunproject.huiyun.ixconfig.net.jsonMod.Start_InfraredResult_10;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends Fragment {

	private NotificationStudyResult_8 result_8= JSONModuleManager.getInstance().result_8;
	private Start_InfraredResult_10 result_10=JSONModuleManager.getInstance().result_10;
	private Contrl_InfraredResult_12 result_12=JSONModuleManager.getInstance().result_12;
	private DownloadAllEare_38 result_38=JSONModuleManager.getInstance().result_38;
	private DownloadOutDevices_24 result_24=JSONModuleManager.getInstance().result_24;
	private NetJsonUtil netJsonUtil=NetJsonUtil.getInstance();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_setting, null);
		Button btn_setting = (Button) view.findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(), ChooseGatewayActivity.class);
				startActivity(intent);
			}
		});
		
		Button btn_send = (Button) view.findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyJsonObj1 jsonobj= JsonUtil.getAJsonObj1ForMaster();
				jsonobj.code=23;
				jsonobj.obj= FinalValue.OBJ_MASTER;
				Map<String, String> map=new HashMap<String, String>();
				map.put("token", StaticValue.user.getToken());
				jsonobj.setData(map);
				netJsonUtil.addCmdForSend(jsonobj);
				result_24.setOnCmdReseivedListener(new OnResultListener<List<OutDevice>>() {
					@Override
					public void onResult(boolean isSecceed, List<OutDevice> obj) {
						if(isSecceed){
							for(OutDevice d:obj){
								System.out.println(d);
							}
							ToastUtils.showToastReal("成功");
							LogUtils.i("成功");
						}
					}
				});
			}
		});
		
		
		Button btn_query = (Button) view.findViewById(R.id.btn_query);
		btn_query.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
			MyJsonObj1 jsonobj=JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code=7;
			jsonobj.obj=FinalValue.OBJ_MASTER;
			Map<String, String> map=new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			jsonobj.setData(map);
			netJsonUtil.addCmdForSend(jsonobj);
			result_8.setOnCmdReseivedListener(new OnResultListener<Object>() {
				@Override
				public void onResult(boolean isSecceed, Object obj) {
					if(isSecceed){
						ToastUtils.showToastReal("查询成功");
						LogUtils.i("成功");
					}
				}
			});}
		});
		
		
		
		//-------------------------------------------红外
		Button btn_start_red = (Button) view.findViewById(R.id.btn_start_red);
		btn_start_red.setOnClickListener(new OnClickListener() {
			String currentDevice = "电视";
			@Override
			public void onClick(View v) {	
			MyJsonObj1 jsonobj=JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code=9;
			jsonobj.obj=FinalValue.OBJ_MASTER;
			Map<String, String> map=new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			map.put("function_code", 1+"");
			map.put("device_disc",currentDevice);
			jsonobj.setData(map);
			netJsonUtil.addCmdForSend(jsonobj);
			result_10.setOnCmdReseivedListener(new OnResultListener<RedRay>() {
				@Override
				public void onResult(boolean isSecceed, RedRay obj) {
					if(isSecceed){
						ToastUtils.showToastReal("查询成功");
						LogUtils.i("成功");
					}
				}
			});}
		});
		
		
		Button btn_contrl_red = (Button) view.findViewById(R.id.btn_contrk_red);
		btn_contrl_red.setOnClickListener(new OnClickListener() {
			String currentDevice = "电视";
			@Override
			public void onClick(View v) {	
			MyJsonObj1 jsonobj=JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code=11;
			jsonobj.obj=FinalValue.OBJ_MASTER;
			Map<String, String> map=new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			map.put("function_code", 3+"");
			map.put("device_disc",currentDevice);
			jsonobj.setData(map);
			netJsonUtil.addCmdForSend(jsonobj);
			result_12.setOnCmdReseivedListener(new OnResultListener<RedRay>() {
				@Override
				public void onResult(boolean isSecceed, RedRay obj) {
					if(isSecceed){
						ToastUtils.showToastReal("查询成功");
						LogUtils.i("成功");
					}
				}
			});}
		});
		
		
		Button btn_contrls = (Button) view.findViewById(R.id.btn_contrls);
		btn_contrls.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent=new Intent(getActivity(), ContrlActivity.class);
				//startActivity(intent);
			}
			
		});
		
		Button btn_contrlsswitch = (Button) view.findViewById(R.id.btn_contrlsswitch);
		btn_contrlsswitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
			MyJsonObj1 jsonobj=JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code=27;
			jsonobj.obj=FinalValue.OBJ_MASTER;
			Map<String, String> map=new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			map.put("phoneCode", 3+"");
			map.put("state",1+"");
			jsonobj.setData(map);
			netJsonUtil.addCmdForSend(jsonobj);
			}
		});
		
		/**
		 * 读取状态
		 */
		Button btn_restata = (Button) view.findViewById(R.id.btn_readstat);
		btn_restata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
			MyJsonObj1 jsonobj=JsonUtil.getAJsonObj1ForMaster();
			jsonobj.code=29;
			jsonobj.obj=FinalValue.OBJ_MASTER;
			Map<String, String> map=new HashMap<String, String>();
			map.put("token", StaticValue.user.getToken());
			map.put("phoneCode", 3+"");
			map.put("state",1+"");
			jsonobj.setData(map);
			netJsonUtil.addCmdForSend(jsonobj);
			}
		});
		
		
		Button btn_eare = (Button) view.findViewById(R.id.btn_eare);
		btn_eare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent intent=new Intent(getActivity(), AreaSettingActivity.class);
				startActivity(intent);
			}
		});
		
		Button btn_addtoarea = (Button) view.findViewById(R.id.btn_addtoarea);
		btn_addtoarea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent intent=new Intent(getActivity(), AddToAreaActivity.class);
				startActivity(intent);
			}
		});
		
		
		/**
		 * 下载区域
		 */
		Button down_eare = (Button) view.findViewById(R.id.down_eare);
		down_eare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				MyJsonObj2 jsonobj=new MyJsonObj2();
				MyJsonObj2.Data sendData=new MyJsonObj2.Data();
				jsonobj.code=37;
				jsonobj.obj="master";
				sendData.token=StaticValue.user.getToken();
				jsonobj.setData(sendData);
				netJsonUtil.addCmdForSend(jsonobj);
				result_38.setOnCmdReseivedListener(new OnResultListener<List<String>>() {
					@Override
					public void onResult(boolean isSecceed, List<String> obj) {
						if(isSecceed){
							FragmentContainer.AREAS_LIST=obj;
							/*for(String s:obj){
								
								System.out.println("下载的区域"+s);
								.add(s);
							}*/
						}
					}
				});
				
			}
		});
		return view;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinalValue.mPageName);
	}

}
