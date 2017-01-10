package com.ixhuiyunproject.huiyun.ixconfig.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ixhuiyunproject.R;


@SuppressLint("ValidFragment")
public class WarningDialog extends DialogFragment {
	
	private Context mContext;
	private String mTitle, mMsg;
	
	public WarningDialog(Context context, String title, String msg){
		mContext = context;
		mTitle = title;
		mMsg = msg;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View view = View.inflate(mContext, R.layout.dialog_warning, null);
		final Dialog dlg = new Dialog(mContext, R.style.Theme_dialog);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_confirm_title);
		TextView tvMsg = (TextView) view.findViewById(R.id.tv_confirm);
		
		tvTitle.setText(mTitle);
		tvMsg.setText(mMsg);
		
		dlg.setContentView(view);
		
		return dlg;
	}
}
