package com.ixhuiyunproject.huiyun.ixconfig.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;

@SuppressLint("ValidFragment")
public class UpdateDialog extends DialogFragment {

	private Context mContext;
	private OnResultListener<Object> listener;
	private String mTitle, mMsg;
	Handler mHandler;

	public UpdateDialog(Context context, String title, String msg) {
		mContext = context;
		mTitle = title;
		mMsg = msg;
	}

	public void setConfirmListener(OnResultListener<Object> listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View view = View.inflate(mContext, R.layout.dialog_confirm, null);
		final Dialog dlg = new Dialog(mContext, R.style.Theme_dialog);

		TextView tvTitle = (TextView) view.findViewById(R.id.tv_confirm_title);
		TextView tvMsg = (TextView) view.findViewById(R.id.tv_confirm);
		Button btnYes = (Button) view.findViewById(R.id.btn_yes);
		Button btnNo = (Button) view.findViewById(R.id.btn_no);

		tvTitle.setText(mTitle);
		tvMsg.setText(mMsg);
		btnNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onResult(false, null);
				}
				dlg.dismiss();
			}
		});
		btnYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onResult(true, null);
				}
				dlg.dismiss();
			}
		});
		dlg.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
		dlg.setContentView(view);
		return dlg;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
