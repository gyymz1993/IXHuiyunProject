package com.ixhuiyunproject.huiyun.ixconfig.fragment.redray;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.VibrateHelp;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;

import java.util.HashMap;

/**
 * 音响
 * @author lzy_torah
 * 
 */
public abstract class RedRayAudioFragment extends BaseRayFragment {

	OnClickListener onButtonClickListener = new OnButtonClickListener();

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
			myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_AUDIO+"");// 添加页面类型
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
		View view = inflater.inflate(R.layout.frag_audiocontroller, null);
		TextView textView = (TextView) view.findViewById(R.id.tx_reminder);
		if (!isLearning()) {
			textView.setText("控制页面");
		} else {
			textView.setText("学习页面");
		}
		return view;
	}

	@Override
	protected void initButtons(View view) {
		ImageButton btn1 = (ImageButton) view.findViewById(R.id.btn1);
		ImageButton btn2 = (ImageButton) view.findViewById(R.id.btn2);
		ImageButton btn3 = (ImageButton) view.findViewById(R.id.btn3);
		ImageButton btn4 = (ImageButton) view.findViewById(R.id.btn4);
		ImageButton btn5 = (ImageButton) view.findViewById(R.id.btn5);
		ImageButton btn6 = (ImageButton) view.findViewById(R.id.btn6);
		ImageButton btn7 = (ImageButton) view.findViewById(R.id.btn7);
		ImageButton btn8 = (ImageButton) view.findViewById(R.id.btn8);
		ImageButton btn9 = (ImageButton) view.findViewById(R.id.btn9);
		ImageButton btn10 = (ImageButton) view.findViewById(R.id.btn10);
		ImageButton btn11 = (ImageButton) view.findViewById(R.id.btn11);
		ImageButton btn12 = (ImageButton) view.findViewById(R.id.btn12);
		ImageButton btn13 = (ImageButton) view.findViewById(R.id.btn13);
		SparseArray<View> list = new SparseArray<View>();
		list.put(1, btn1);
		list.put(2, btn2);
		list.put(3, btn3);
		list.put(4, btn4);
		list.put(5, btn5);
		list.put(6, btn6);
		list.put(7, btn7);
		list.put(8, btn8);
		list.put(9, btn9);
		list.put(10, btn10);
		list.put(11, btn11);
		list.put(12, btn12);
		list.put(13, btn13);
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
