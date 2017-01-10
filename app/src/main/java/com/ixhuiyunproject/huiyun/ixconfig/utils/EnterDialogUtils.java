package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ixhuiyunproject.R;


public class EnterDialogUtils {

	static DialogFragment dialog = null;
	private Activity activity;
	private View view;
	private Context context;
	private Button condirmBtn;
	private EditText ed_text;

	private String titleMsg;

	CallBack callBack;

	public CallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {
		public void isConfirm(String edString);
	}

	public EnterDialogUtils(Context mcontext, Activity mactivity, View mview,
			Button mcondirmBtn, EditText med_text) {
		this.activity = mactivity;
		this.context = mcontext;
		this.view = mview;
		this.condirmBtn = mcondirmBtn;
		this.ed_text = med_text;
	}

	public EnterDialogUtils(Context mcontext, String mmsg) {
		this.titleMsg = mmsg;
		this.context = mcontext;
	}

	public Dialog createDialog() {
		final Dialog builder = new Dialog(context, R.style.Theme_dialog);
		builder.setContentView(view);
		setDialogLayoutParams(builder);
		builder.show();
		condirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.isConfirm(ed_text.getText().toString());
				}
				builder.dismiss();
			}
		});
		return builder;
	}

	private void setDialogLayoutParams(Dialog builder) {
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		@SuppressWarnings("unused")
		int height = metric.heightPixels; // 屏幕高度（像素）
		lp.width = width - 150;
		dialogWindow.setAttributes(lp);
	}

	public Dialog onCreateDialog() {
		final View view = View.inflate(context, R.layout.dialog_eare_add, null);

		TextView title = (TextView) view.findViewById(R.id.tx_title);
		Button condirmBtn = (Button) view.findViewById(R.id.confirmU);
		final EditText input = (EditText) view.findViewById(R.id.add_area);
		final Dialog builder = new Dialog(context, R.style.Theme_dialog);
		title.setText(titleMsg);
		builder.setContentView(view);
		builder.show();
		condirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callBack != null && input.getText().toString() != "") {
					callBack.isConfirm(input.getText().toString());
				}
				builder.dismiss();
			}
		});
		return builder;
	}

}