package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.ixhuiyunproject.R;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RadioButtonDialogUtils {

	static DialogFragment dialog = null;
	private Context context;
	private static String configStr;

	private String titleMsg;
	SelectCallBack callBack;

	public SelectCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(SelectCallBack callBack) {
		this.callBack = callBack;
	}

	public interface SelectCallBack {
		public void isConfirm(String edString);
	}

	public RadioButtonDialogUtils(Context mcontext, String mmsg) {
		this.titleMsg = mmsg;
		this.context = mcontext;
	}

	private void setDialogLayoutParams(Dialog builder) {
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		if (getDialogWidth() != 0) {
			lp.height = getDialogWidth(); // 高度
		} else {
			lp.width = 590; // 宽度
		}
		// lp.alpha = 0.7f; // 透明度
		lp.width = LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
	}

	public Dialog onCreateDialog() {
		final View view = View.inflate(context, R.layout.dialog_radioswitch,
				null);
		TextView title = (TextView) view.findViewById(R.id.tx_title);
		Button condirmBtn = (Button) view.findViewById(R.id.confirmU);
		ListView listView = (ListView) view.findViewById(R.id.lv_dialog);
		dialogListview(listView);
		final Dialog builder = new Dialog(context, R.style.Theme_dialog);
		title.setText(titleMsg);
		builder.setContentView(view);
		setDialogLayoutParams(builder);
		builder.show();
		condirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callBack != null && configStr != "") {
					callBack.isConfirm(configStr);
				}
				builder.dismiss();
			}
		});
		return builder;
	}

	private void dialogListview(ListView listView) {
		if (getListDate() != null && getListDate().size() != 0) {
			SimpleAdapter sim = new SimpleAdapter(context, getListDate(),
					R.layout.listview_item, new String[] { "test" },
					new int[] { android.R.id.text1 });
			listView.setFocusable(true);
			listView.setFocusableInTouchMode(true);
			listView.setAdapter(sim);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View convertView,
						int position, long arg3) {
					// configStr = str[position];
					configStr = getListDate().get(position).get("test");
				}
			});

		}

	}

	/**
	 * Function:listView数据集
	 * 
	 * @author YangShao 2015年4月15日 下午1:33:15
	 * @return
	 */
	protected abstract ArrayList<HashMap<String, String>> getListDate();

	protected abstract int getDialogWidth();

}