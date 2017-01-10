package com.ixhuiyunproject.abtotest.voiptest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.ConnSupport;
import com.ixhuiyunproject.huiyun.ixconfig.net.SendCmdThread;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.Data;
import com.ixhuiyunproject.huiyun.ixconfig.net.cmd.JsonPack;
import com.ixhuiyunproject.huiyun.ixconfig.utils.BytesStringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

import org.abtollc.sdk.AbtoApplication;
import org.abtollc.sdk.AbtoPhone;
import org.abtollc.sdk.OnCallConnectedListener;
import org.abtollc.sdk.OnCallDisconcectedListener;
import org.abtollc.sdk.OnCallHeldListener;
import org.abtollc.sdk.OnRemoteAlertingListener;
import org.abtollc.sdk.OnToneReceiveListener;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenAV extends Activity implements OnCallConnectedListener,
		OnRemoteAlertingListener, OnCallDisconcectedListener,
		OnCallHeldListener, OnToneReceiveListener {

	protected static final String THIS_FILE = "ScreenAV";
	private static boolean isflag=true;
	public static final String CALL_TERMINATED = "Call terminated";
	public static final String SEND_VIDEO = "send_video";
	//public static final long MILLISECONDS_IN_SECONDS = 1000;
	public static final String POINT_TIME = "pointTime";
	public static final String TOTAL_TIME = "totalTime";
	public static final String CALL_ID = "call_id";

	private AbtoPhone phone;
	private String activeContact;
	private int activeCallId = AbtoPhone.INVALID_CALL_ID;

	private TextView status;
	private TextView name;
	private ImageView pickUpVideo;
	private LinearLayout allVideoLayout;
	private LinearLayout pickUpLayout;
	private static boolean sendingVideo;
	private boolean bIsIncoming;
	private WakeLock inCallWakeLock;
	private PowerManager powerManager;
	private Button btn_gone;
	
	Button send1,send2,send3,send4,send5,send6,
	send7,send8,send9,sendj,send0,sendx;
	SendOnClickListener clickListener;
	LinearLayout cellPhone,local_video;
	public static List<OutDevice> outDeviceList;
	public static String token="";
	public static String masterIp="";
	public static ScreenAV screenAV;
	private Socket localSocket;
	/**
	 * executes when activity have been created;
	 */
	LinearLayout  inParrent;
	public class SendOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.SEND_1:
				try {
					phone.sendTone('1');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_2:
				try {
					phone.sendTone('2');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_3:
				try {
					phone.sendTone('3');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_4:
				try {
					phone.sendTone('4');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_5:
				try {
					phone.sendTone('5');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_6:
				try {
					phone.sendTone('6');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_7:
				try {
					phone.sendTone('7');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_8:
				try {
					phone.sendTone('8');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_9:
				try {
					phone.sendTone('9');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_0:
				try {
					phone.sendTone('0');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_X:
				try {
					phone.sendTone('*');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			case R.id.SEND_J:
				try {
					phone.sendTone('#');
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
		
	}

	/**
	 * executes when activity have been created;
	 */
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onCreate(savedInstanceState);
		screenAV=this;
		conectTcp();
		powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (inCallWakeLock == null) {
			int flags = PowerManager.SCREEN_BRIGHT_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP;
			inCallWakeLock = powerManager.newWakeLock(flags,
					"org.abtollc.videoCall");
			inCallWakeLock.setReferenceCounted(false);
		}

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		phone = ((AbtoApplication) getApplication()).getAbtoPhone();
		activeCallId = getIntent().getIntExtra(CALL_ID,
				AbtoPhone.INVALID_CALL_ID);
		Log.d(THIS_FILE, "callId - " + activeCallId);
		token=getIntent().getStringExtra("token");
		masterIp=getIntent().getStringExtra("MasterIp");
		@SuppressWarnings("unchecked")
		List<OutDevice> serializableExtra = (List<OutDevice>) getIntent().getSerializableExtra("list");
		outDeviceList = serializableExtra;//获取list方式
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cell_screen_caller);

		name = (TextView) findViewById(R.id.caller_contact_name);
		activeContact = getIntent().getStringExtra(AbtoPhone.REMOTE_CONTACT);
		name.setText(activeContact);
		mTotalTime = getIntent().getLongExtra(TOTAL_TIME, 0);
		mPointTime = getIntent().getLongExtra(POINT_TIME, 0);
		if (mTotalTime != 0) {
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, 100);
		}
		status = (TextView) findViewById(R.id.caller_call_status);
		bIsIncoming = getIntent().getBooleanExtra("incoming", false);
		//sendingVideo = getIntent().getBooleanExtra(SEND_VIDEO, false);
		status.setText(bIsIncoming ? "Ringing" : "Calling");
		allVideoLayout = (LinearLayout) findViewById(R.id.all_video_layout);
		pickUpLayout = (LinearLayout) findViewById(R.id.caller_pick_up_layout);
		pickUpLayout.setVisibility(bIsIncoming ? View.VISIBLE : View.GONE);
		pickUpVideo = (ImageView) findViewById(R.id.caller_pick_up_video_button);
		pickUpVideo.setVisibility(phone.isVideoCall() ? View.VISIBLE
				: View.INVISIBLE);
		LinearLayout outParrent = (LinearLayout) ScreenAV.this
				.findViewById(R.id.local_video_parent);
		 inParrent = (LinearLayout) ScreenAV.this
				.findViewById(R.id.remote_video_parent);
		local_video=(LinearLayout) findViewById(R.id.local_video);
		//local_video.setVisibility(View.INVISIBLE);
		outParrent.setVisibility(View.INVISIBLE);
		phone.setVideoWindows(outParrent, inParrent);
		phone.setCallConnectedListener(this);
		phone.setCallDisconnectedListener(this);
		phone.setOnCallHeldListener(this);
		phone.setRemoteAlertingListener(this);
		phone.setToneReceiveListener(this);
		
		/**
		 * 发送数据
		 */
		send1 = (Button) findViewById(R.id.SEND_1);
		 send2 = (Button) findViewById(R.id.SEND_2);
		 send3 = (Button) findViewById(R.id.SEND_3);
		 send4 = (Button) findViewById(R.id.SEND_4);
		 send5 = (Button) findViewById(R.id.SEND_5);
		 send6 = (Button) findViewById(R.id.SEND_6);
		 send7 = (Button) findViewById(R.id.SEND_7);
		 send8 = (Button) findViewById(R.id.SEND_8);
		 send9 = (Button) findViewById(R.id.SEND_9);
		 sendj = (Button) findViewById(R.id.SEND_J);
		 send0 = (Button) findViewById(R.id.SEND_0);
		 sendx = (Button) findViewById(R.id.SEND_X);
		 clickListener=new SendOnClickListener();
		 
		 btn_gone=(Button) findViewById(R.id.video_gone);
		 cellPhone=(LinearLayout) findViewById(R.id.cell_phone);
		 btn_gone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isflag){
					isflag=false;
					//cellPhone.setVisibility(View.VISIBLE);
					cellPhone.setVisibility(View.GONE);
					btn_gone.setText("关锁");
					contrlDevice();
				}else{
					isflag=true;
					cellPhone.setVisibility(View.GONE);
					btn_gone.setText("开锁");
					contrlDevice();
				}
			}
		});
		 
		 try {
			 System.out.println(FinalValue.IP_SERVER);
			 System.out.println(FinalValue.mPageName);
			 System.out.println(DeviceManager.outDeviceList.size());
			 System.out.println(DeviceManager.inDeviceList);
			 System.out.println(StaticValue.user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onCallConnected(String remoteContact) {
		ScreenAV.this.pickUpLayout.setVisibility(View.GONE);
		bIsIncoming = false;
		if (mTotalTime == 0L) {
			mPointTime = System.currentTimeMillis();
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, 100);
		}

		if (phone.isVideoCall()) {
			showVideoWindows(phone.isVideoCall());
		} else {
			showVideoWindows(false);
		}
		
		send1.setOnClickListener(clickListener);
		send2.setOnClickListener(clickListener);
		send3.setOnClickListener(clickListener);
		send4.setOnClickListener(clickListener);
		send5.setOnClickListener(clickListener);
		send6.setOnClickListener(clickListener);
		send7.setOnClickListener(clickListener);
		send8.setOnClickListener(clickListener);
		send9.setOnClickListener(clickListener);
		sendj.setOnClickListener(clickListener);
		sendx.setOnClickListener(clickListener);
		send0.setOnClickListener(clickListener);

	}

	@Override
	public void onCallDisconcected(String remoteContact, int callId, int statusCode) {
		if (callId == phone.getAfterEndedCallId()) {
			finish();
			mTotalTime = 0;
		}
	}

	@Override
	public void onCallHeld(HoldState state) {
		if (state == HoldState.LOCAL_HOLD) {
			status.setText("Local Hold");
		} else if (state == HoldState.REMOTE_HOLD) {
			status.setText("Remote Hold");
		} else if (state == HoldState.ACTIVE) {
			status.setText("Active");
		}
	}

	@Override
	public void onRemoteAlerting(long accId, int statusCode) {
		String statusText = "";
		if (activeCallId == AbtoPhone.INVALID_CALL_ID) {
			activeCallId = phone.getActiveCallId();
		}

		switch (statusCode) {
		case TRYING:
			statusText = "Trying";
			break;
		case RINGING:
			statusText = bIsIncoming ? "Ringing" : "Calling";
			break;
		case SESSION_PROGRESS:
			statusText = "Session in progress";
			break;
		}

		status.setText(statusText);
	}

	/**
	 * 接收数字
	 */
	@Override
	public void onToneReceived(char tone) {
		Toast.makeText(ScreenAV.this, "DTMF tone received: " + tone,
				Toast.LENGTH_SHORT).show();
		contrlDevice();
	}
	/**
	 * @param holder
	 */
	static boolean iscontrl=true;
	public void contrlDevice() {
		List<OutDevice> devices = outDeviceList;
		OutDevice device = null;
		if(devices.size()!=0){
			for(int i=0;i<devices.size();i++){
				if(devices.get(i).getPhoneCode()==16){
					device=devices.get(i);
					System.out.println(device.getPhoneCode());
				}
			}
			//int number = new Random().nextInt(devices.size());
			//device = devices.get(number);
		}
		final BaseJsonObj.MyJsonObj1 jsonobj = JsonUtil.getAJsonObj1ForMaster();
		final Map<String, String> map = new HashMap<String, String>();
		jsonobj.code = 27;
		jsonobj.obj = FinalValue.OBJ_MASTER;
		map.put("token", token);
		// 窗帘
		//int state = device.getState();
		if(iscontrl){
			map.put("state", 1 + "");
			iscontrl=false;
		}else {
			map.put("state", 0 + "");
			iscontrl=true;
		}
		map.put("phoneCode", device.getPhoneCode() + "");
		jsonobj.setData(map);
		addCmdForSend(jsonobj);
		
	}
	private SendCmdThread sendCmdThread;
	
	public void conectTcp(){
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					localSocket=new Socket();
					InetSocketAddress socketAddress = new InetSocketAddress(masterIp, 48901);
					localSocket.connect(socketAddress, 5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		
		sendCmdThread = new SendCmdThread() {
			@Override
			protected void sendCMD(byte[] cmd) {
				System.out.println("sendCMDlocalSocket"+localSocket);
				ConnSupport.tcpSend(localSocket, cmd,
						new OnResultListener<byte[]>() {
							@Override
							public void onResult(boolean isSecceed,
									byte[] obj) {
								if (!isSecceed) {
									addCmdForSend(obj);// 重新添加到待发送
								}else{
									System.out.println("发送成功。。。。。");
								}
							}
						});
			}
		};// 初始化发送命令线程
		sendCmdThread.start();
	}
	/**
	 * 添加一个要通过tcp发送的jsonobj
	 * 
	 * @param list
	 */
	Gson gson = new Gson();
	public void addCmdForSend(BaseJsonObj myjsonObj) {
		if (myjsonObj != null) {
			String jsonstr = gson.toJson(myjsonObj);
			LogUtils.i("要发送的命令" + jsonstr);
			try {
				sendCmdThread.addCmdForSend(JsonPack.Data_Pack(
						Data.CUSTOM_CODE_RF, jsonstr.getBytes("utf-8")));
				System.out.println(BytesStringUtils.toStringShow(JsonPack.Data_Pack(
						Data.CUSTOM_CODE_RF, jsonstr.getBytes("utf-8")))+"打包");
			} catch (UnsupportedEncodingException e) {
				LogUtils.i("发送json失败，生成命令时错误");
				e.printStackTrace();
			}
		} else {
			LogUtils.i("发送json失败，生成命令时错误");
		}
	}
	

	
	 /**
		 *  Function:挂断
		 * 
		 *  @author YangShao 2015年6月4日 下午2:38:18
		 *  @param view
		 */
	public void hangUP(View view) {
		try {
			mHandler.removeCallbacks(mUpdateTimeTask);
			if (phone.getBeforeConfirmedCallId() == -1) {
				phone.hangUp();
			} else {
				phone.rejectCall();
			}
		} catch (RemoteException e) {
			Log.e(THIS_FILE, e.getMessage());
		}
	}

	 /**
		 *  Function:接听语音
		 * 
		 *  @author YangShao 2015年6月4日 下午2:37:44
		 *  @param view
		 */
	public void pickUp(View view) {
		sendingVideo = false;
		inParrent.setVisibility(View.INVISIBLE);
		try {
			phone.answerCall(200, false);
		} catch (RemoteException e) {
			Log.e(THIS_FILE, e.getMessage());
		}
	}


	 /**
	 *  Function:接听视频
	 * 
	 *  @author YangShao 2015年6月4日 下午2:38:00
	 *  @param view
	 */
	public void pickUpVideo(View view) {
		try {
			sendingVideo = true;
			phone.answerCall(200, true);
		} catch (RemoteException e) {
			Log.e(THIS_FILE, e.getMessage());
		}
	}

	private void showVideoWindows(boolean show) {
		if (show) {
			allVideoLayout.setVisibility(View.VISIBLE);
		} else {
			allVideoLayout.setVisibility(View.GONE);
		}
	}

	// ==========Timer==============
	private long mPointTime = 0;
	private long mTotalTime = 0;
	private Handler mHandler = new Handler();
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			mTotalTime += System.currentTimeMillis() - mPointTime;
			mPointTime = System.currentTimeMillis();
			int seconds = (int) (mTotalTime / 1000);
			int minutes = seconds / 60;
			seconds = seconds % 60;
			if (seconds < 10) {
				status.setText("" + minutes + ":0" + seconds);
			} else {
				status.setText("" + minutes + ":" + seconds);
			}

			mHandler.postDelayed(this, 1000);
		}
	};

	// =============================

	@Override
	protected void onPause() {
		if (inCallWakeLock != null && inCallWakeLock.isHeld()) {
			inCallWakeLock.release();
		}
		mHandler.removeCallbacks(mUpdateTimeTask);
		super.onPause();
	}

	@Override
	protected void onResume() {
		pickUpLayout.setVisibility(bIsIncoming ? View.VISIBLE : View.GONE);
		if (mTotalTime != 0L) {
			mHandler.removeCallbacks(mUpdateTimeTask);
			mHandler.postDelayed(mUpdateTimeTask, 100);
		}

		if (inCallWakeLock != null) {
			inCallWakeLock.acquire();
		}
		super.onResume();
		if (phone.getActiveCallIdInProgress() != -1) {
			phone.reinvite();
		}
	}

	/**
	 * executes when activity is destroyed;
	 */
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * overrides panel buttons keydown functionality;
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			try {
				phone.hangUp();
			} catch (RemoteException e) {
				Log.e(THIS_FILE, e.getMessage());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean isSendingVideo() {
		return sendingVideo;
	}
}
