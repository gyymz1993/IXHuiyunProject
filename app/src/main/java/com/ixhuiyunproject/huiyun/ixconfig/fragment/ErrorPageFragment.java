package com.ixhuiyunproject.huiyun.ixconfig.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.umeng.analytics.MobclickAgent;

/**
 * 错误页面处理
 * 
 * @author lzy_torah
 *
 */
public class ErrorPageFragment extends Fragment {

	/** 重试按钮 */
	private Button btn_error_retry;
	private View view;
	/** 点击事件 */
	private OnClickListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_page_error, null);
		btn_error_retry = (Button) view.findViewById(R.id.btn_error_retry);
		btn_error_retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null)
					ErrorPageFragment.this.listener.onClick(v);
			}
		});
		return view;
	}

	/**
	 * 设置错误页面的重试按钮点击事件
	 * 
	 * @param listener
	 */
	public void setOnButtonClickListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinalValue.mPageName);
	}


}
