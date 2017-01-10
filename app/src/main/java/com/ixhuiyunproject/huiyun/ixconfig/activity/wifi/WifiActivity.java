package com.ixhuiyunproject.huiyun.ixconfig.activity.wifi;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.EnterDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RadioButtonDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: WifiActivity
 * @Description: TODO
 * @date 2012-11-9 上午11:47:51
 * 
 */
public class WifiActivity extends SwipeBackActivity implements OnClickListener {

	private Button scan_button;
	private TextView wifi_result_textview;
	private WifiManager wifiManager;
	private WifiInfo currentWifiInfo;// 当前所连接的wifi
	private List<ScanResult> wifiList;// wifi列表
	private String[] str;
	// private int wifiIndex;
	private String ssid;
	private ProgressDialog progressDialog;
	private WifiUtil mWifiAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_main);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		setupViews();
		initListener();
		mWifiAdmin = new WifiUtil(this);
		new ScanWifiThread().start();
		
		ImageView ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		// openWifi();
		currentWifiInfo = wifiManager.getConnectionInfo();
		wifi_result_textview.setText("当前网络：" + currentWifiInfo.getSSID()
				+ " ip:" + WifiUtil.intToIp(currentWifiInfo.getIpAddress()));

		super.onResume();
	}

	public void setupViews() {
		scan_button = (Button) findViewById(R.id.scan_button);
		wifi_result_textview = (TextView) findViewById(R.id.wifi_result_textview);
	}

	public void initListener() {
		scan_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scan_button:
			lookUpScan();
			break;
		}
	}

	/**
	 * 打开wifi
	 */
	public void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 扫描wifi线程
	 * 
	 * @author passing
	 * 
	 */
	class ScanWifiThread extends Thread {
		@Override
		public void run() {
			while (true) {
				currentWifiInfo = wifiManager.getConnectionInfo();
				startScan();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	/**
	 * 扫描wifi
	 */
	public void startScan() {
		wifiManager.startScan();
		// 获取扫描结果
		wifiList = wifiManager.getScanResults();
		str = new String[wifiList.size()];
		String tempStr = null;
		for (int i = 0; i < wifiList.size(); i++) {
			tempStr = wifiList.get(i).SSID;
			if (null != currentWifiInfo
					&& tempStr.equals(currentWifiInfo.getSSID())) {
				tempStr = tempStr + "(已连接)";
			}
			str[i] = tempStr;
		}
	}

	/**
	 * 弹出框 查看扫描结果
	 */
	public void lookUpScan() {
		// Builder builder = new Builder(WifiActivity.this);
		// builder.setTitle("wifi");
		// builder.setItems(str, new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// //wifiIndex = which;
		// //handler.sendEmptyMessage(3);
		// }
		// });
		// builder.show();
		selectWifiShowDialog();
	}

	/**
	 * Function: 选择循环对话框
	 * 
	 * @author YangShao 2015年4月13日 上午9:22:56
	 */
	public void selectWifiShowDialog() {

		RadioButtonDialogUtils dialog = new RadioButtonDialogUtils(
				WifiActivity.this, "请选择wifi") {
			@Override
			protected ArrayList<HashMap<String, String>> getListDate() {
				ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < str.length; i++) {
					HashMap<String, String> Item = new HashMap<String, String>();
					Item.put("test", str[i]);
					listData.add(Item);
				}
				return listData;
			}

			@Override
			protected int getDialogWidth() {
				return 1000;
			}
		};

		dialog.onCreateDialog();
		dialog.setCallBack(new RadioButtonDialogUtils.SelectCallBack() {
			@Override
			public void isConfirm(String edString) {
				if (StringUtils.isEmpty(edString)) {
					Toast.makeText(WifiActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
					return;
				} else {
					ssid = edString;
					System.out.println(edString);
					handler.sendEmptyMessage(3);
				}
			}
		});
	}

	/**
	 * Function: 删除任务
	 * 
	 * @author YangShao 2015年4月3日 下午2:35:51
	 */
	public void setWifi(String ssid, String pwd) {
		JSONModuleManager.getInstance().result_78
				.setOnCmdReseivedListener(new OnResultListener<Object>() {
					@Override
					public void onResult(boolean isSecceed, Object obj) {
						if (null != progressDialog) {
							progressDialog.dismiss();
						}
					}
				});
		MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj1.code = 77;
		myJsonObj1.data = new HashMap<String, String>();
		myJsonObj1.data.put("token", StaticValue.user.getToken());
		System.out.println(ssid);
		System.out.println(pwd);
		myJsonObj1.data.put("SSID", ssid);
		myJsonObj1.data.put("SSIDPWD", pwd);
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);

	}

	/**
	 * 获取网络ip地址
	 * 
	 * @author passing
	 * 
	 */
	class RefreshSsidThread extends Thread {
		@Override
		public void run() {
			boolean flag = true;
			while (flag) {
				currentWifiInfo = wifiManager.getConnectionInfo();
				if (null != currentWifiInfo.getSSID()
						&& 0 != currentWifiInfo.getIpAddress()) {
					flag = false;
				}
			}
			handler.sendEmptyMessage(4);
			super.run();
		}
	}

	/**
	 * 连接网络
	 * 
	 * @param password
	 */
	public void connetionConfiguration(final String ssid, final String password) {
		progressDialog = ProgressDialog.show(WifiActivity.this, "正在连接...",
				"请稍候...");
		setWifi(ssid, password);
		// new ConnectWifiThread().execute(index + "", password);
	}

	/**
	 * 连接wifi
	 * 
	 * @author passing
	 * 
	 */
	class ConnectWifiThread extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			int index = Integer.parseInt(params[0]);
			if (index > wifiList.size()) {
				return null;
			}
			// 连接配置好指定ID的网络
			WifiConfiguration config = mWifiAdmin.createWifiInfo(
					wifiList.get(index).SSID, params[1], 3);
			System.out.println("当前选择的网络" + wifiList.get(index).SSID);
			int networkId = wifiManager.addNetwork(config);
			if (null != config) {
				wifiManager.enableNetwork(networkId, true);
				return wifiList.get(index).SSID;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != progressDialog) {
				progressDialog.dismiss();
			}
			if (null != result) {
				handler.sendEmptyMessage(0);
			} else {
				handler.sendEmptyMessage(1);
			}
			super.onPostExecute(result);
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				wifi_result_textview.setText("正在获取ip地址...");
				new RefreshSsidThread().start();
				break;
			case 1:
				Toast.makeText(WifiActivity.this, "连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				// View layout = LayoutInflater.from(WifiActivity.this).inflate(
				// R.layout.wifi_dialog, null);
				// Builder builder = new Builder(WifiActivity.this);
				// builder.setTitle("请输入密码").setView(layout);
				// final EditText passowrdText = (EditText) layout
				// .findViewById(R.id.password_edittext);
				// builder.setPositiveButton("连接",
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// connetionConfiguration(wifiIndex, passowrdText
				// .getText().toString());
				// // setWifi(wifiIndex, passowrdText.getText()
				// // .toString());
				// }
				// }).show();

				showDialog();
				break;
			case 4:
				Toast.makeText(WifiActivity.this, "连接成功！", Toast.LENGTH_SHORT)
						.show();
				wifi_result_textview.setText("当前网络："
						+ currentWifiInfo.getSSID() + " ip:"
						+ WifiUtil.intToIp(currentWifiInfo.getIpAddress()));
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * Function: 显示输入密码对话框
	 * 
	 * @author YangShao 2015年4月13日 上午9:22:56
	 */
	private void showDialog() {
		EnterDialogUtils dialog = new EnterDialogUtils(WifiActivity.this,
				"请输入密码");
		dialog.onCreateDialog();
		dialog.setCallBack(new EnterDialogUtils.CallBack() {
			@Override
			public void isConfirm(String edString) {
				if (StringUtils.isEmpty(edString)) {
					Toast.makeText(WifiActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
					return;
				} else {
					connetionConfiguration(ssid, edString);
				}
			}
		});
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