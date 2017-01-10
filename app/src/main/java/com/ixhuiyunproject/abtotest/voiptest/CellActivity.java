package com.ixhuiyunproject.abtotest.voiptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.DeviceManager;

import org.abtollc.sdk.AbtoApplication;
import org.abtollc.sdk.AbtoPhone;
import org.abtollc.sdk.OnIncomingCallListener;
import org.abtollc.sdk.OnInitializeListener;
import org.abtollc.sdk.OnRegistrationListener;
import org.abtollc.utils.codec.Codec;

import java.io.Serializable;
import java.util.ArrayList;


public class CellActivity extends Activity{

    private boolean registered = false;
    private boolean isCaling = false;
    private String activeRemoteContact;
    private String domain;
    private long accId;
    private AbtoPhone abtoPhone;
    private Button mainButton;
    private EditText callUri;
    private int callId;
    private ProgressDialog registrationWaiting;
    private RelativeLayout cell;
    private LinearLayout register;
    
    private Button btnCancel, btnSend, btnDial,speaker;
	private EditText edtNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cell_main);
        abtoPhone = ((AbtoApplication)getApplication()).getAbtoPhone();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        cell=(RelativeLayout) findViewById(R.id.cell);
        register=(LinearLayout) findViewById(R.id.register);
        if (abtoPhone.getActiveCallId() != AbtoPhone.INVALID_CALL_ID) {
            startAV(false);
        }
        abtoPhone.setInitializeListener(new OnInitializeListener() {
            @Override
            public void onInitializeState(OnInitializeListener.InitializeState state, String message) {
                switch (state) {
                case START:
                    if(!dialog.isShowing())
                        dialog.show();
                    break;
                case INFO:
                case WARNING:
                    break;
                case FAIL:
                    dialog.dismiss();
                    new AlertDialog.Builder(CellActivity.this)
                        .setTitle("Error")
                        .setMessage(message)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }).create().show();
                    break;
                case SUCCESS:
                   dialog.dismiss();
                    break;

                default:
                    break;
                }
            }
        });

        abtoPhone.getConfig().setCodecPriority(Codec.G729, (short)250);
        abtoPhone.getConfig().setCodecPriority(Codec.PCMU, (short)200);
        abtoPhone.getConfig().setCodecPriority(Codec.GSM, (short)150);
        abtoPhone.getConfig().setCodecPriority(Codec.PCMA, (short)100);
        abtoPhone.getConfig().setCodecPriority(Codec.speex_16000, (short)50);
        
        // Start initialize
        abtoPhone.initialize();
        if (!abtoPhone.isActive()) {
            dialog.show();
        }// initialization
        
        mainButton = (Button) findViewById(R.id.main_button);
        callUri = (EditText) findViewById(R.id.sip_number);
        
        registrationWaiting = new ProgressDialog(this);
        registrationWaiting.setMessage("Registration in progress");
        registrationWaiting.setCancelable(false);
        
        final RegisterDialog regDialog = new RegisterDialog(this);
        
        if(!registered){
            mainButton.setText("Register");
            callUri.setEnabled(false);
        }
        
        if(isCaling){
            mainButton.setText("Hangup");
            callUri.setEnabled(false);
        }
        
        // Set the action depending on registered or calling
        mainButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(!registered){
                    //Show registration dialog
                    regDialog.show();
                }else if(!isCaling){
                    String sipNumber = callUri.getText().toString();
                    if(sipNumber != null && !sipNumber.equals("")){
                     // Start Call
                        try {
                            abtoPhone.startVideoCall(sipNumber, accId);
                            callId = abtoPhone.getActiveCallId();
                            startAV(false);
                            if(!sipNumber.contains("@")){
                                activeRemoteContact = String.format("<sip:%1$s@%2$s>", sipNumber, domain);
                            }else{
                                activeRemoteContact = String.format("<sip:%1$s>", sipNumber);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }                
                
            }
        });// mainButton onClicListener
        
        // Set registration event
        abtoPhone.setRegistrationStateListener(new OnRegistrationListener() {
            public void onRegistrationFailed(long accId, int statusCode, String statusText) {
                registered = false; 
                domain = null;
                registrationWaiting.dismiss();
                AlertDialog.Builder fail = new AlertDialog.Builder(CellActivity.this);
                fail.setTitle("Registration Faild");
                fail.setMessage(statusCode + " - " + statusText);
                fail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();                                             
                    }
                });
                fail.show();
            }
            
            public void onRegistered(long accId) {
              //mainButton.setText("Call");
              register.setVisibility(View.GONE);
              cell.setVisibility(View.VISIBLE);
              callUri.setEnabled(true);
              registered = true;
              registrationWaiting.dismiss();
            }

            @Override
            public void onUnRegistered(long arg0) {
                
            }
        }); //registration listener
        
        // Set on Incoming call listener
        abtoPhone.setIncomingCallListener(new OnIncomingCallListener() {
            public void OnIncomingCall(String remoteContact, long accountId) {
            	activeRemoteContact = remoteContact;
            	callId = abtoPhone.getActiveCallId();
            	startAV(true);
            }
        }); //incoming call listener
        initView();
    }

    @Override
    protected void onResume() {
    	if (abtoPhone.getActiveCallId() != AbtoPhone.INVALID_CALL_ID) {
            startAV(false);
        }
    	super.onResume();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    private class RegisterDialog extends Dialog{
        public RegisterDialog(Context context){
            super(context);
            setContentView(R.layout.cell_register_dialog);
            Button regButton = (Button) findViewById(R.id.register_button);
            final EditText user = (EditText) findViewById(R.id.login);
            final EditText pass = (EditText) findViewById(R.id.password);
            final EditText domain = (EditText) findViewById(R.id.domain);
            
            regButton.setOnClickListener(new View.OnClickListener() {
                
                public void onClick(View v) {
                    // Add account
                    accId = abtoPhone.getConfig().addAccount(domain.getText().toString(), user.getText().toString(), pass.getText().toString(), null, user.getText().toString(), 300, true);
                    //And register added account
                    CellActivity.this.domain = domain.getText().toString();
                    try {
                        abtoPhone.register();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if(registrationWaiting != null){
                        registrationWaiting.show();
                    }
                    RegisterDialog.this.dismiss();
                }
            });
        }
    }
    private synchronized Intent prepareIntent(boolean incoming) {
        Intent intent = new Intent(this, StartAVActivityService.class);
        intent.putExtra("incoming", incoming);
        System.out.println("prepareIntent"+ StaticValue.user.getToken());
        System.out.println("FinalValue.MasterIp"+ FinalValue.MasterIp);
        Bundle bundle=new Bundle();
        bundle.putSerializable("list",(Serializable) DeviceManager.outDeviceList);//序列化,要注意转化(Serializable)
        bundle.putString("token", StaticValue.user.getToken());
        bundle.putString("MasterIp", FinalValue.MasterIp);
        intent.putParcelableArrayListExtra("token", (ArrayList<? extends Parcelable>) DeviceManager.outDeviceList);
        intent.putExtra(ScreenAV.CALL_ID, callId);
        intent.putExtra(AbtoPhone.REMOTE_CONTACT, activeRemoteContact);
        intent.putExtras(bundle);//发送数据
        return intent;
    }

    private synchronized void startAV(boolean incoming) {
        Intent intent = prepareIntent(incoming);
        startService(intent);
    }
    
    private void initView() {
		edtNumber = (EditText) findViewById(R.id.edtNumber);
		edtNumber.setInputType(InputType.TYPE_NULL);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnDial = (Button) findViewById(R.id.btnOut);
		speaker = (Button) findViewById(R.id.btn_sound);
		Onclick onclick=new Onclick();
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sipNumber = edtNumber.getText().toString();
                if(sipNumber != null && !sipNumber.equals("")){
                 // Start Call
                    try {
                        abtoPhone.startVideoCall(sipNumber, accId);
                        callId = abtoPhone.getActiveCallId();
                        startAV(false);
                        if(!sipNumber.contains("@")){
                            activeRemoteContact = String.format("<sip:%1$s@%2$s>", sipNumber, domain);
                        }else{
                            activeRemoteContact = String.format("<sip:%1$s>", sipNumber);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
			}
		});
		btnCancel.setOnClickListener(onclick);
		btnSend.setOnClickListener(onclick);
		btnDial.setOnClickListener(onclick);
		speaker.setOnClickListener(onclick);
		findViewById(R.id.btn_0).setOnClickListener(onclick);
		findViewById(R.id.btn_1).setOnClickListener(onclick);
		findViewById(R.id.btn_2).setOnClickListener(onclick);
		findViewById(R.id.btn_3).setOnClickListener(onclick);
		findViewById(R.id.btn_4).setOnClickListener(onclick);
		findViewById(R.id.btn_5).setOnClickListener(onclick);
		findViewById(R.id.btn_6).setOnClickListener(onclick);
		findViewById(R.id.btn_7).setOnClickListener(onclick);
		findViewById(R.id.btn_8).setOnClickListener(onclick);
		findViewById(R.id.btn_9).setOnClickListener(onclick);
		findViewById(R.id.btn_10).setOnClickListener(onclick);
		findViewById(R.id.btn_sound).setOnClickListener(onclick);
	}
    
    public class Onclick implements OnClickListener{

		@Override
		public void onClick(View v) {

			int id = v.getId();
			switch (id) {
			case R.id.btnSend:{
                String sipNumber = callUri.getText().toString();
                if(sipNumber != null && !sipNumber.equals("")){
                 // Start Call
                    try {
                        abtoPhone.startVideoCall(sipNumber, accId);
                        callId = abtoPhone.getActiveCallId();
                        startAV(false);
                        if(!sipNumber.contains("@")){
                            activeRemoteContact = String.format("<sip:%1$s@%2$s>", sipNumber, domain);
                        }else{
                            activeRemoteContact = String.format("<sip:%1$s>", sipNumber);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            
			}

				break;
			case R.id.btnOut:
				String sipNumber = edtNumber.getText().toString();
                if(sipNumber != null && !sipNumber.equals("")){
                 // Start Call
                    try {
                        abtoPhone.startVideoCall(sipNumber, accId);
                        callId = abtoPhone.getActiveCallId();
                        startAV(false);
                        if(!sipNumber.contains("@")){
                            activeRemoteContact = String.format("<sip:%1$s@%2$s>", sipNumber, domain);
                        }else{
                            activeRemoteContact = String.format("<sip:%1$s>", sipNumber);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
				break;
			case R.id.btnCancel:
				setEditValue();
				break;
			default :
				Button btn = (Button)v;
				String text = btn.getText().toString();
				System.out.println(text);
				if (null == text || text.equals("")) return;
				Editable edit = edtNumber.getText();
				if (edit.length() > 0) {
					edit.insert(edit.length(), text);
				} else {
					edit.insert(0, text);
				}
				break;
			}
		
		}
    }
    

	private void setEditValue() {
		int start = edtNumber.getSelectionStart();
		if (start > 0) {
			edtNumber.getText().delete(start - 1, start);
		}
	}

}
