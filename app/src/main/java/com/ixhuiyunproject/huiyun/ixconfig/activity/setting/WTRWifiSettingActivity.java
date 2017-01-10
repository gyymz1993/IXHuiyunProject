package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.lsd.smartconfig.lib.ConfigStatus;
import com.lsd.smartconfig.lib.SmartConfigActivity;

/**
 * 中继器的wifi设置页面
 * @author lzn
 *
 */
public class WTRWifiSettingActivity extends SmartConfigActivity {

	private WTRWifiSettingActivity me = this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 
	 * 可以在此自定义布局文件、更改提示文字。
	 */
	@Override
	protected void renderView(Bundle savedInstanceState) {
		//超时设置
		this.SMART_CONFIG_TIMEOUT = 30000;
		//提示信息设置
		this.TIP_CONFIGURING_DEVICE = me.getString(R.string.tip_configuring_device);
		this.TIP_DEVICE_CONFIG_SUCCESS= me.getString(R.string.tip_device_config_success);
		this.TIP_WIFI_NOT_CONNECTED=me.getString(R.string.tip_wifi_not_connected);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.smart_config);
		//ssidEt,pwdEt,showPwd,deviceCountGroup为约定的控件实例变量，不可更改。
		connectBtn = (Button)findViewById(R.id.connect);
		ssidEt = (TextView)findViewById(R.id.ssid);
		pwdEt = (EditText)findViewById(R.id.pwd);
		showPwd = (CheckBox)findViewById(R.id.showPwd);
		deviceCountGroup = (RadioGroup)findViewById(R.id.deviceCountGroup);
		deviceCountGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.deviceCountSingle){
					me.deviceCountMode = SmartConfigActivity.DEVICE_COUNT_ONE;
				}else{
					me.deviceCountMode = SmartConfigActivity.DEVICE_COUNT_MULTIPLE;
				}
			}
		});
		
		ImageView ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 单个配置成功。
	 * 多个配置返回。
	 * 
	 */
	@Override
	protected void onSuccess(ConfigStatus configStatus) {
		if(me.deviceCountMode == DEVICE_COUNT_ONE){
			Toast.makeText(me, String.format(me.getString(R.string.tip_device_config_success), configStatus.mac),Toast.LENGTH_SHORT).show();
		}else if(me.deviceCountMode == DEVICE_COUNT_MULTIPLE){
			Toast.makeText(me, me.getString(R.string.tip_timeout),Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onSingleConfigTimeout() {
		Toast.makeText(me, me.getString(R.string.tip_timeout),Toast.LENGTH_LONG).show();
	}
}
