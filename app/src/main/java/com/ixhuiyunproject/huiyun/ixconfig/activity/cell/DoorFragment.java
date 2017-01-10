package com.ixhuiyunproject.huiyun.ixconfig.activity.cell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixhuiyunproject.R;


public class DoorFragment extends Fragment {
	// 挂断 接听电话 接听视频 开门
	LinearLayout LY_hangUp, LY_answer, LY_v_answer, LY_door_on;
   
	//
	LinearLayout Ly_oper_door,Ly_hungUp;
	
	
	RelativeLayout RL_answer,RL_cell,RL_v_answer;
	
	
	//通话时间
	TextView tv_time;
	//显示视频
	LinearLayout LY_show_video;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.door_main, null);
		initFindView(view);
		return view;
	}

	private void initFindView(View v) {
		//接听电话
		LY_answer = (LinearLayout) v.findViewById(R.id.id_ly_answer);
		//接听视频
		LY_v_answer = (LinearLayout) v.findViewById(R.id.id_ly_videoanswer);
		//挂断电话
		LY_hangUp = (LinearLayout) v.findViewById(R.id.id_ly_hangup);
		//关门
		LY_door_on = (LinearLayout) v.findViewById(R.id.id_ly_door_on);
		
		
		//开关门      接通电话显示界面
		Ly_oper_door = (LinearLayout) v.findViewById(R.id.id_open_door_ly);
		//拨打电话    显示界面
		Ly_hungUp = (LinearLayout) v.findViewById(R.id.id_on_cell);
		
		//拨号提示
		RL_answer = (RelativeLayout) v.findViewById(R.id.id_re_answer);
		
		//通话提示
		RL_cell = (RelativeLayout) v.findViewById(R.id.id_re_in_cell);
		tv_time = (TextView) v.findViewById(R.id.id_tv_time);
		
		//视频通话显示
		LY_show_video = (LinearLayout) v.findViewById(R.id.id_re_video);
		
	}

	class DoorOnclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_ly_answer:
				goneView(RL_answer);
				visiView(RL_cell);
				visiView(Ly_oper_door);
				break;
			case R.id.id_ly_videoanswer:
				goneView(RL_answer);
				visiView(LY_show_video);
				visiView(Ly_oper_door);
				break;
			case R.id.id_ly_hangup:
				
				break;
			case R.id.id_ly_door_on:
				
				break;
			default:
				break;
			}
		}
	}
	
	private void goneView(View v){
		v.setVisibility(View.GONE);
	}
	private void visiView(View v){
		v.setVisibility(View.VISIBLE);
	}
	
	 private synchronized Intent prepareIntent(boolean incoming) {
	        Intent intent = new Intent(getActivity(), CellService.class);
	        intent.putExtra("incoming", incoming);
	        return intent;
	    }

	    private synchronized void startAV(boolean incoming) {
	        Intent intent = prepareIntent(incoming);
	        getActivity().startService(intent);
	    }
	    
}
