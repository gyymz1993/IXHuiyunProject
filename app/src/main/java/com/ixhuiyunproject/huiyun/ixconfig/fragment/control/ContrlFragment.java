package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.TitleManager;
import com.ixhuiyunproject.huiyun.ixconfig.activity.contrl.AddToAreaActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.OutDevice;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.viewpagerindicator.TabPageIndicator;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: ContrlDeviceFragment.java
 * @Package com.huiyun.ixconfig.fragment.control
 * @Description: 控制页面
 * @author Yangshao
 * @date 2015年1月21日 上午11:23:08
 * @version V1.0
 */
@Deprecated
public class ContrlFragment extends Fragment {
	private List<String> TITLE = new ArrayList<String>();
	private TitleManager fragManager;
	private static String current = ""; // 当前选择的区域名称
	/**
	 * 状态改变刷新
	 */
	static SparseArray<ContrlDeviceFragment> fragMap = new SparseArray<ContrlDeviceFragment>();

	/**
	 * 启动控制页面时刷新
	 */
	public FragmentPagerAdapter titleAdapter;
	public TabPageIndicator indicator;
	public static ContrlFragment contrlFragment;

	@Override
	public void onResume() {
		// flushDevice();
		super.onResume();
		Message msg = Message.obtain();
		msg.what = 2;
		ContrlFragment.sendMessage(msg);
		MobclickAgent.onPageStart(FinalValue.mPageName);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FinalValue.mPageName);
	}


	/** 侧滑菜单 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/**
		 * 设置主题
		 */
		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), R.style.StyledIndicators);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);
		View view = localInflater.inflate(R.layout.activity_contrl_main,
				container, false);
		contrlFragment = this;
		// System.out.println("contrlFragment" + contrlFragment);
		initTitleFragment(view);
		initActionBar();
		return view;
	}

	/**
	 * 
	 * Function:刷新界面表头
	 * 
	 * @author Howard DateTime 2015年1月30日 下午3:30:24
	 * @param items
	 */
	public static void sendMessage(Message msg) {
		if (contrlFragment != null) {
			contrlFragment.handler.sendMessage(msg);
		}
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.what == 1 && titleAdapter != null) {
				/*
				 * for (String area : (List<String>) msg.obj) { TITLE.add(area);
				 * }
				 */
				contrlFragment.setTITLE((List<String>) msg.obj);
				titleAdapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
			}
			if (msg.what == 2 && titleAdapter != null) {
				titleAdapter.notifyDataSetChanged();
				indicator.notifyDataSetChanged();
				flushDevice();
			}
		};
	};

	/**
	 * 刷新状态 如果个数改变了，刷新页面 Function:
	 */
	public static void refreshState() {
		try {
			for (int i = 0; i < fragMap.size(); i++) {
				ContrlDeviceFragment frag = fragMap.valueAt(i);
				frag.adpater.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择设备后刷新
	 * 
	 * @author 2015年1月23日 下午5:42:37
	 */
	public void flushDevice() {
		try {
			for (int i = 0; i < fragMap.size(); i++) {
				ContrlDeviceFragment frag = fragMap.valueAt(i);
				List<OutDevice> devices = DeviceManager.getDeviceForArea(TITLE
						.get(i));
				if (devices != null) {
					frag.setDevices(devices);
					frag.adpater.notifyDataSetChanged();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化标题
	 * 
	 * @param view
	 */
	private void initActionBar() {
		fragManager = HomeActivity.homeActivity.getTitleManager();
		fragManager.setRightPopMenu(new String[] { "添加" },
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long id) {
						fragManager.popMenuDismiss();
						switch (position) {
						case 0:
							// System.out.println("传入区域" + current);
							Bundle bundle = new Bundle();
							bundle.putString("area", current);
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									AddToAreaActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
							break;
						case 1:
							break;
						}
					}
				});
		fragManager.setTitlePopMenu(false, null, null);
	}

	/**
	 * 
	 * Function: 初始化标题栏
	 * 
	 * @author Howard DateTime 2015年1月21日 上午11:23:33
	 * @param view
	 */
	private void initTitleFragment(View view) {
		if (FragmentContainer.AREAS_LIST.size() != 0) {
			for (String area : FragmentContainer.AREAS_LIST) {
				TITLE.add(area);
			}
		}
		titleAdapter = new TabPageIndicatorAdapter(getChildFragmentManager());
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(titleAdapter);
		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		// indicator.setFillViewport(true);
		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				current = TITLE.get(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	/**
	 * ViewPager适配器
	 */
	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// 新建一个Fragment来展示ViewPager item的内容，并传递参数
			List<OutDevice> devices = DeviceManager.getDeviceForArea(TITLE
					.get(position));
			ContrlDeviceFragment fragment = new ContrlDeviceFragment();
			fragment.setDevices(devices);
			fragMap.put(position, fragment);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE.get(position % TITLE.size());
		}

		@Override
		public int getCount() {
			return TITLE.size();
		}
	}

	/**
	 * 避免下次进入报错
	 */
	public void onDetach() {
		super.onDetach();
		try {
			// TITLE.clear();
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null)
			this.getView()
					.setVisibility(menuVisible ? View.VISIBLE : View.GONE);
	}

	public List<String> getTITLE() {
		return TITLE;
	}

	public void setTITLE(List<String> tITLE) {
		TITLE = tITLE;
	}
	

}
