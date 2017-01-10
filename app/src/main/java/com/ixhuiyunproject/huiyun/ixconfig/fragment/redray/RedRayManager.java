package com.ixhuiyunproject.huiyun.ixconfig.fragment.redray;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.redray.AddRemoteActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.userlogin.LoginSucceedActtion;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentFactory;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;

/**
 * 红外页面的管理者
 * 
 * @author lzy_torah
 * 
 */
public class RedRayManager {

	private String[] device_discs;
	private boolean isLearning = false;
	public static RedRay currentController;
	BaseRayFragment current;
	private RedRayTVFragment remote;
	private RedRayAirFragment airFragment;
	private RedRayAudioFragment audioFragment;
	private RedRayMiFragment miFragment;

	public RedRayManager() {
		init();
	}

	/**
	 * 初始化 1.数据，2，交互，3页面
	 */
	public void init() {
		initActionBar();
	}

	/**
	 * 初始化界面
	 */
	private void inittPage() {
		if (FragmentContainer.remoteControllerList != null) {
			if (FragmentContainer.remoteControllerList.size() == 0) {
				LogUtils.e("没有遥控器数据");
				return;
			}
			device_discs = new String[FragmentContainer.remoteControllerList
					.size()];
			for (int i = 0; i < FragmentContainer.remoteControllerList.size(); i++) {
				device_discs[i] = FragmentContainer.remoteControllerList.get(i)
						.getR_name();
			}
			//
			currentController = FragmentContainer.remoteControllerList.get(0);
			HomeActivity.homeActivity.getTitleManager().setTitle(
					currentController.getR_name());
			HomeActivity.homeActivity.getTitleManager().setTitlePopMenu(true,
					device_discs, new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							HomeActivity.homeActivity.getTitleManager()
									.titlepopMenuDismiss();
							currentController = FragmentContainer.remoteControllerList
									.get(position);
							HomeActivity.homeActivity.getTitleManager()
									.setTitle(
											currentController.getR_name());
							HomeActivity.homeActivity
									.switchFrag(FragmentFactory.REDRAY);// 重新载入红外页面
						}
					});
		} else {
			LogUtils.e("主机没有返回");
		}
	}

	/**
	 * 刷新数据
	 * 
	 */
	public void flushData() {
		device_discs = new String[FragmentContainer.remoteControllerList.size()];
		for (int i = 0; i < FragmentContainer.remoteControllerList.size(); i++) {
			device_discs[i] = FragmentContainer.remoteControllerList.get(i)
					.getR_name();
		}
		currentController = null;
	}

	/**
	 * 初始化右键菜单
	 * 
	 * @param view
	 */
	private void initActionBar() {

		HomeActivity.homeActivity.getTitleManager().setRightPopMenu(
				new String[] { "切换" }, new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						HomeActivity.homeActivity.getTitleManager()
								.popMenuDismiss();
						switch (position) {
						case 0:// 切换学习，控制状态
							isLearning = !isLearning;

							RedRayAirFragment air = null;
							RedRayTVFragment tv = null;
							RedRayMiFragment mi = null;
							RedRayAudioFragment ad = null;
							TextView textView = null;

							if (current instanceof RedRayAirFragment) {
								air = (RedRayAirFragment) current;
								textView = (TextView) air.getActivity()
										.findViewById(R.id.tx_reminder);
							}
							if (current instanceof RedRayTVFragment) {
								tv = (RedRayTVFragment) current;
								textView = (TextView) tv.getActivity()
										.findViewById(R.id.tx_reminder);
							}
							if (current instanceof RedRayMiFragment) {
								mi = (RedRayMiFragment) current;
								textView = (TextView) mi.getActivity()
										.findViewById(R.id.tx_reminder);
							}
							if (current instanceof RedRayAudioFragment) {
								ad = (RedRayAudioFragment) current;
								textView = (TextView) ad.getActivity()
										.findViewById(R.id.tx_reminder);
							}
							if (!isLearning) {
								textView.setText("控制页面");
							} else {
								textView.setText("学习页面");
							}
							break;
						case 1:// 编辑遥控
							Intent intent = new Intent(
									HomeActivity.homeActivity,
									AddRemoteActivity.class);
							HomeActivity.homeActivity.startActivity(intent);
							break;
						case 2:// 回到主页
							break;

						}

					}
				});
	}

	/**
	 * 得到合适的页面，如果没数据就返回错误页面
	 * 
	 * @return
	 */
	public Fragment getFragment() {
		/*
		 * 该切换页面时才会调用此方法 1.错误到正确 2.正确时的不同页面
		 */
		if (currentController == null) {
			LogUtils.i("初始化遥控相关数据");
			inittPage();// 初始化页面交互
		}

		if (currentController == null) {// 没有数据
			return FragmentFactory.getErrorpage(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Message msg = Message.obtain();
					msg.what = 3;
					msg.obj = "正在重新载入";
					msg.arg1 = FragmentFactory.REDRAY;
					HomeActivity.homeActivity.dialogHandler.sendMessage(msg);
					LoginSucceedActtion.downloadRemoteController();// 重新开始下载区域
				}
			});
		}
		initTitleAction();
		if (currentController.getPageType() == BaseRayFragment.PAGETYPE_TV) {
			// current
			remote = new RedRayTVFragment() {
				@Override
				protected String getCurrentDevice() {
					return currentController.getR_name();
				}

				@Override
				public boolean isLearning() {
					return isLearning;
				}
			};
			current = remote;
			return remote;
		}
		if (currentController.getPageType() == BaseRayFragment.PAGETYPE_AIR) {
			airFragment = new RedRayAirFragment() {
				@Override
				protected String getCurrentDevice() {
					return currentController.getR_name();
				}

				@Override
				public boolean isLearning() {
					return isLearning;
				}
			};
			current = airFragment;
			return airFragment;
		}
		if (currentController.getPageType() == BaseRayFragment.PAGETYPE_AUDIO) {
			audioFragment = new RedRayAudioFragment() {
				@Override
				public boolean isLearning() {
					return isLearning;
				}

				@Override
				protected String getCurrentDevice() {
					return currentController.getR_name();
				}
			};
			current = audioFragment;
			return audioFragment;
		}
		if (currentController.getPageType() == BaseRayFragment.PAGETYPE_MI) {
			miFragment = new RedRayMiFragment() {
				@Override
				public boolean isLearning() {
					return isLearning;
				}

				@Override
				protected String getCurrentDevice() {
					return currentController.getR_name();
				}
			};
			current = miFragment;
			return miFragment;
		}
		throw new RuntimeException("fragment为空");
	}

	/**
	 * 初始化title的点击事件
	 */
	private void initTitleAction() {
		if (currentController == null)
			return;
		HomeActivity.homeActivity.getTitleManager().setTitle(
				currentController.getR_name());
		HomeActivity.homeActivity.getTitleManager().setTitlePopMenu(true,
				device_discs, new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						HomeActivity.homeActivity.getTitleManager()
								.titlepopMenuDismiss();
						currentController = FragmentContainer.remoteControllerList
								.get(position);
						HomeActivity.homeActivity.getTitleManager().setTitle(
								currentController.getR_name());
						HomeActivity.homeActivity
								.switchFrag(FragmentFactory.REDRAY);// 重新载入红外页面
					}
				});
		initActionBar();
	}
}
