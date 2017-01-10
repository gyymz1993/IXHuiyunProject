package com.ixhuiyunproject.huiyun.ixconfig.activity.cell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;


public class DoorMasterMainActivity extends Activity{
	
	//输入门牌号   通话中  连接中
	RelativeLayout re_cell,re_cell_up,re_huang;
	//门牌号
	EditText doorNumber;
	//通話時間
	TextView tv_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.door_ms_main);
		
		initFindView();
		
		initOnclick();
		
	}
	private void initOnclick() {
		doorNumber.getText();
		tv_time.setText("");
	}
	private void initFindView() {
		re_cell=(RelativeLayout) findViewById(R.id.id_re_cell);
		re_cell_up=(RelativeLayout) findViewById(R.id.id_re_cell_up);
		re_huang=(RelativeLayout) findViewById(R.id.id_re_hung);
		doorNumber=(EditText) findViewById(R.id.id_ed_number);
		tv_time=(TextView) findViewById(R.id.id_tv_time);
		
	}
	
	private void goneView(View v){
		v.setVisibility(View.GONE);
	}
	private void visiView(View v){
		v.setVisibility(View.VISIBLE);
	}
	
	class DoorOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_re_cell:
				break;
			case R.id.id_re_cell_up:
				break;
			case R.id.id_re_hung:
				break;
			default:
				break;
			}
		}
	}
}
