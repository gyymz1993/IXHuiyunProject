package com.ixhuiyunproject.huiyun.ixconfig.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.about.AboutHuiyunActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.MainSettingActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.setting.SetSoftwareActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginActivity;
import com.ixhuiyunproject.huiyun.ixconfig.utils.SpUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.view.SettingItemRLView;
import com.umeng.analytics.MobclickAgent;

/**
 * @Title: SetFragment.java
 * @Package com.huiyun.ixconfig.fragment
 * @Description: 设置监听
 * @author Yangshao
 * @date 2015年1月23日 下午3:55:55
 * @version V1.0
 */
public class SetFragment extends Fragment {

	SettingItemRLView setPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_setting, null);
		initTitle(view);
		initlogout(view);
		initMe(view);
		initSet(view);
		initSoftware(view);
		return view;
	}

	private void initSoftware(View view) {
		SettingItemRLView setSoftware = (SettingItemRLView) view
				.findViewById(R.id.set_software);
		setSoftware.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						SetSoftwareActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * Function 注销
	 * 
	 * @author 2015年1月29日 下午5:57:32
	 * @param view
	 */
	private void initlogout(View view) {
		SettingItemRLView setLogout = (SettingItemRLView) view
				.findViewById(R.id.logout);
		setLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StaticValue.user = null;
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
//				NetJsonUtil.getInstance().closeAllSocket();//关闭所有socket
				HomeActivity.homeActivity.finish();
			}
		});
	}

	private void initSet(View view) {
		setPage = (SettingItemRLView) view.findViewById(R.id.settingPage);
		if (StaticValue.isRemote()) {
			setPage.setVisibility(View.GONE);
		}
		setPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StaticValue.user.getType() == 1) {
					Intent intent = new Intent(getActivity(),
							MainSettingActivity.class);
					startActivity(intent);
				} else {
					ToastUtils.showToastReal("权限不够");
				}

			}
		});
	}

	/**
	 * 
	 * Function: 表头
	 * 
	 * @author 2015年1月24日 上午11:18:33
	 * @param view
	 */
	private void initTitle(View view) {
		TextView familyId = (TextView) view.findViewById(R.id.set_fa);
		TextView userName = (TextView) view.findViewById(R.id.set_user);
		TextView userType = (TextView) view.findViewById(R.id.user_type);
		try {
			familyId.setText("家庭:" + SpUtils.getString("familyId"));
			userName.setText(StaticValue.user.getName());
			if (StaticValue.user.getType() == 1) {
				userType.setText("管理员");
			} else {
				userType.setText("普通用户");
				setPage.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Function: 关于我们
	 * 
	 * @author Yangshao 2015年1月23日 下午3:55:29
	 * @param view
	 */
	private void initMe(View view) {
		SettingItemRLView setMe = (SettingItemRLView) view
				.findViewById(R.id.set_guanyu);

		setMe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						AboutHuiyunActivity.class);
				startActivity(intent);
			}
		});
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
