package com.ixhuiyunproject.huiyun.ixconfig.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.bean.GatewayOutputInfo;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;


@SuppressLint("ValidFragment")
@Deprecated
public class GatewayOutputInfoDialog extends DialogFragment {
	
	private Context mContext;
	private OnSucceedListener<GatewayOutputInfo> listener;
	private GatewayOutputInfo outputInfo;
	
	public GatewayOutputInfoDialog(Context context, GatewayOutputInfo info){
		mContext = context;
		outputInfo = info;
	}
	
	public void setOnYesListener(OnSucceedListener<GatewayOutputInfo> listener){
		this.listener = listener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View view = View.inflate(mContext, R.layout.dialog_gateway_output, null);
		final Dialog dlg = new Dialog(mContext, R.style.Theme_dialog);
		
		// get view
		final EditText etArea = (EditText) view.findViewById(R.id.et_area);
		final EditText etName = (EditText) view.findViewById(R.id.et_name);
		final EditText etComm = (EditText) view.findViewById(R.id.et_comm);
		final EditText etType = (EditText) view.findViewById(R.id.et_type);
		final EditText etExec = (EditText) view.findViewById(R.id.et_exec);
		final EditText etSwitch = (EditText) view.findViewById(R.id.et_switch);
		Button btnYes = (Button) view.findViewById(R.id.btn_yes);
		Button btnNo = (Button) view.findViewById(R.id.btn_no);
		
		// set text
		etArea.setText(outputInfo.getArea());
		etName.setText(outputInfo.getName());
		switch(outputInfo.getCommuType()){
		case GatewayOutputInfo.COMM_TYPE_232:
			etComm.setText("232");
			break;
		case GatewayOutputInfo.COMM_TYPE_485:
			etComm.setText("485");
			break;
		case GatewayOutputInfo.COMM_TYPE_KNX:
			etComm.setText("KNX");
			break;
		default:
			etComm.setText("ERR");
			break;	
		}
		switch(outputInfo.getDetailType()){
		case GatewayOutputInfo.DETAIL_TYPE_EXECUTE_PPS:
			etType.setText("开关\n(PPS)");
			break;
		default:
			etType.setText("ERR");
			break;
		}
		etExec.setText(String.valueOf(outputInfo.getExecuteId()));
		etSwitch.setText(String.valueOf(outputInfo.getSwitchId()));
		
		// listener
		btnNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		btnYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 把修改后的配置信息回调
				GatewayOutputInfo info = new GatewayOutputInfo();
				info.setArea(etArea.getText().toString());
				info.setName(etName.getText().toString());
				if(etComm.getText().toString().equals("232")){
					info.setCommuType(GatewayOutputInfo.COMM_TYPE_232);
				} else if(etComm.getText().toString().equals("485")){
					info.setCommuType(GatewayOutputInfo.COMM_TYPE_485);
				} else if(etComm.getText().toString().equals("KNX")){
					info.setCommuType(GatewayOutputInfo.COMM_TYPE_KNX);
				} else {
					info.setCommuType(GatewayOutputInfo.COMM_TYPE_485);
				}
				if(etType.getText().toString().equals("开关\n(PPS)")){
					info.setDetailType(GatewayOutputInfo.DETAIL_TYPE_EXECUTE_PPS);
				} else {
					info.setDetailType(GatewayOutputInfo.DETAIL_TYPE_EXECUTE_PPS);
				}
				info.setExecuteId(Integer.valueOf(etExec.getText().toString()));
				info.setSwitchId(Integer.valueOf(etSwitch.getText().toString()));
				
				if(listener != null){
					listener.onSucceed(info);
				}
				dlg.dismiss();
			}
		});
		
		dlg.setContentView(view);
		
		return dlg;
	}
}
