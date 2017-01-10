package com.ixhuiyunproject.huiyun.ixconfig.fragment.redray;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 * 红外线的电视遥控界面
 * 
 * @author lzy_torah
 * 
 */
public abstract class RedRayTVFragment extends BaseRayFragment {

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
			RedRay controller = (RedRay) v.getTag();
			MyJsonObj1 myJsonObj = JsonUtil.getAJsonObj1ForMaster();
			myJsonObj.data = new HashMap<String, String>();
			if (isLearning()) {// 生成学习命令
				myJsonObj.code = 9;
				myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_TV+"");// 添加页面类型
			} else {// 生成控制命令
				myJsonObj.code = 11;// 控制的功能码是11
				myJsonObj.data.put("pageType", BaseRayFragment.PAGETYPE_AIR+"");// 添加页面类型  
			}
			myJsonObj.data.put("r_name", currentDevice);
			myJsonObj.data.put("btn_code", controller.getBtn_code()+"");
			myJsonObj.data.put("token", StaticValue.user.getToken());
			NetJsonUtil.getInstance().addCmdForSend(myJsonObj);// 添加要发送的命令

		}
	}

	@Override
	protected View getRootView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_tvcontroller, null);
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
		Button btn10 = (Button) view.findViewById(R.id.btn10);
		Button btn11 = (Button) view.findViewById(R.id.btn11);
		Button btn12 = (Button) view.findViewById(R.id.btn12);
		Button btn13 = (Button) view.findViewById(R.id.btn13);
		Button btn14 = (Button) view.findViewById(R.id.btn14);
		Button btn15 = (Button) view.findViewById(R.id.btn15);
		Button btn16 = (Button) view.findViewById(R.id.btn16);
		Button btn17 = (Button) view.findViewById(R.id.btn17);
		Button btn18 = (Button) view.findViewById(R.id.btn18);
		Button btn19 = (Button) view.findViewById(R.id.btn19);
		ImageButton btn20 = (ImageButton) view.findViewById(R.id.btn20);
		ImageButton btn21 = (ImageButton) view.findViewById(R.id.btn21);
		ImageButton btn22 = (ImageButton) view.findViewById(R.id.btn22);
		ImageButton btn23 = (ImageButton) view.findViewById(R.id.btn23);
		ImageButton btn24 = (ImageButton) view.findViewById(R.id.btn24);
		ImageButton btn25 = (ImageButton) view.findViewById(R.id.btn25);
		SparseArray<View> list = new SparseArray<View>();
		list.put(1, btn1);
		list.put(2, btn2);
		list.put(3, btn3);
		list.put(4, btn4);
		list.put(5, btn5);
		list.put(6, btn6);
		list.put(7, btn7);
		list.put(10, btn10);
		list.put(11, btn11);
		list.put(12, btn12);
		list.put(13, btn13);
		list.put(14, btn14);
		list.put(15, btn15);
		list.put(16, btn16);
		list.put(17, btn17);
		list.put(18, btn18);
		list.put(19, btn19);
		list.put(20, btn20);
		list.put(21, btn21);
		list.put(22, btn22);
		list.put(23, btn23);
		list.put(24, btn24);
		list.put(25, btn25);
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
