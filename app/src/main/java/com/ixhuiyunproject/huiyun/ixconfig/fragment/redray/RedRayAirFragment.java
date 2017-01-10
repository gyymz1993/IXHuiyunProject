package com.ixhuiyunproject.huiyun.ixconfig.fragment.redray;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.VibrateHelp;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;

/*
 * 1.下载或提前下载红外信息
 * 2.初始化各个按钮，把红外bean存入tag备用
 * 3.点击按钮，根据【学习 or 控制】和 标题 生成命令，发过去
 * 4.返回的结果提示。
 */
/**
 * 红外线的空调相关界面
 * 
 * @author lzy_torah
 * 
 */
public abstract class RedRayAirFragment extends BaseRayFragment {
	OnClickListener onButtonClickListener = new OnButtonClickListener();
	private SeekBar seekBar;
	private TextView tv_temperature;
	private RelativeLayout layout;
	private static int temeTemerature = 16;

	

	/**
	 * 温度改变需要的
	 */
	private SeekClickListener<Integer> listener;

	public SeekClickListener<Integer> getListener() {
		return listener;
	}

	public void setListener(SeekClickListener<Integer> listener) {
		this.listener = listener;
	}

	public interface SeekClickListener<T> {
		public void seekClick(T obj);
	}

	/**
	 * 按钮的点击事件
	 * 
	 * @author lzy_torah
	 * 
	 */
	class OnButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			VibrateHelp.vSimple(v.getContext(), VIBRATE_TIME);
			studyOrContrl(v);
		}
	}

	/**
	 * Function: 学习或者控制
	 * 
	 * @author 2015年1月29日 下午5:11:47
	 * @param v
	 */
	public void studyOrContrl(View v) {
		RedRay controller = (RedRay) v.getTag();
		MyJsonObj1 myJsonObj = JsonUtil.getAJsonObj1ForMaster();
		myJsonObj.data = new HashMap<String, String>();
		if (isLearning()) {// 生成学习命令
			myJsonObj.code = 9;
			myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_AIR+"");// 添加页面类型
		} else {// 生成控制命令
			myJsonObj.code = 11;// 控制的功能码是11
			myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_AIR+"");// 添加页面类型
		}
		myJsonObj.data.put("r_name", currentDevice);
		myJsonObj.data.put("btn_code", controller.getBtn_code()+"");
		myJsonObj.data.put("token", StaticValue.user.getToken());
		NetJsonUtil.getInstance().addCmdForSend(myJsonObj);// 添加要发送的命令

	}
	

	@Override
	protected View getRootView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_air, null);
		TextView textView = (TextView) view.findViewById(R.id.tx_reminder);
		if (!isLearning()) {
			textView.setText("控制页面");
		} else {
			textView.setText("学习页面");
		}
		return view;

	}

	private OnSeekBarChangeListener seekbarChangeListener = new OnSeekBarChangeListener() {
		// 停止拖动时执行
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// textView_two.setText("停止拖动了！");
		}

		// 在进度开始改变时执行
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// textView_two.setText("进度开始改变");
		}

		// 当进度发生改变时执行
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Message message = new Message();
			Bundle bundle = new Bundle();// 存放数据
			float pro = seekBar.getProgress();// 0-20
			// float num = seekBar.getMax(); // 20
			float result = pro + 10;
			// bundle.putFloat("key", result);
			int i = (int) (result + 0.5); // i=4
			bundle.putInt("key", i);
			message.setData(bundle);
			message.what = 0;
			handler.sendMessage(message);

			if (listener != null) {
				listener.seekClick(i);
			}

		}
	};

	/**
	 * 用Handler来更新UI
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			tv_temperature.setText(msg.getData().getInt("key") + "");
			temeTemerature = msg.getData().getInt("key");
			// seekBar.setProgress(temeTemerature);
			//System.out.println("当前温度" + 100+temeTemerature);
			seekBar.setTag(new RedRay(100+temeTemerature));
		}

	};

	@Override
	protected void initButtons(View view) {
		ImageButton btn1 = (ImageButton) view.findViewById(R.id.btn1);
		ImageButton btn2 = (ImageButton) view.findViewById(R.id.btn2);
		/**
		 * 制冷
		 */
		LinearLayout btn_refrigeration = (LinearLayout) view
				.findViewById(R.id.btn_refrigeration);
		// 制热
		LinearLayout btn_heating = (LinearLayout) view
				.findViewById(R.id.btn_heating);
		// 除湿
		LinearLayout btn_dehumidifier = (LinearLayout) view
				.findViewById(R.id.btn_dehumidifier);
		// 风向
		LinearLayout btn_winddirection = (LinearLayout) view
				.findViewById(R.id.btn_winddirection);

		LinearLayout btn_wind_speed = (LinearLayout) view
				.findViewById(R.id.btn_wind_speed);
		// 定时
		LinearLayout btn_timing = (LinearLayout) view
				.findViewById(R.id.btn_timing);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar01);
		seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
		tv_temperature = (TextView) view.findViewById(R.id.temperature);
		layout = (RelativeLayout) view.findViewById(R.id.center_relative);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VibrateHelp.vSimple(v.getContext(), VIBRATE_TIME);
				if (Integer.valueOf(layout.getTag().toString()) == 0) {
					seekBar.setVisibility(View.VISIBLE);
					layout.setTag(1);
				} else {
					seekBar.setVisibility(View.INVISIBLE);
					layout.setTag(0);
					studyOrContrl(seekBar);
				}
			}
		});

		/**
		 * 设定初始化
		 */
		seekBar.setProgress(temeTemerature);
		tv_temperature.setText(String.valueOf(temeTemerature));
		SparseArray<View> list = new SparseArray<View>();
		list.put(1, btn1);
		list.put(2, btn2);
		list.put(3, btn_refrigeration);
		list.put(4, btn_heating);
		list.put(5, btn_dehumidifier);
		list.put(6, btn_winddirection);
		list.put(7, btn_wind_speed);
		list.put(8, btn_timing);
		// list.put(temeTemerature, seekBar);
		for (int i = 0; i < list.size(); i++) {
			int key = list.keyAt(i);
			View btn = list.get(key);
			btn.setOnClickListener(onButtonClickListener);// 设置点击事件
			btn.setTag(new RedRay(key));
			// 每个按钮 new 一个 RedRayCode对象，存入tag.
			// RedRayCode的funtion_code设置为按钮的按键码
		}
	}

}
