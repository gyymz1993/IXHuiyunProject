package com.ixhuiyunproject.huiyun.ixconfig.fragment.redray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.bean.RedRay;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

/*
 * 1.下载或提前下载红外信息
 * 2.初始化各个按钮，把红外bean存入tag备用
 * 3.点击按钮，根据【学习 or 控制】和 标题 生成命令，发过去
 * 4.返回的结果提示。
 */
/**
 * 红外线的相关界面
 * 
 * @author lzy_torah
 * 
 */
public abstract class BaseRayFragment extends Fragment {

	public static final int PAGETYPE_TV = 1;
	public static final int PAGETYPE_AIR = 2;
	public static final int PAGETYPE_AUDIO = 3;
	public static final int PAGETYPE_MI = 4;

	protected String currentDevice;
	/** 按钮震动时间 */
	public final int VIBRATE_TIME = 60;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getRootView(inflater);
		currentDevice = getCurrentDevice();
		initButtons(view);// 初始化按键
		studySuccess();
		return view;
	}

	protected abstract String getCurrentDevice();

	/**
	 * 得到主界面
	 * 
	 * @return
	 */
	protected abstract View getRootView(LayoutInflater inflater);

	/**
	 * 初始化按钮们
	 * 
	 * @param view
	 */
	protected abstract void initButtons(View view);

	/**
	 * 当前是否是学习状态
	 * 
	 * @return
	 */
	public abstract boolean isLearning();

	public void studySuccess() {
		JSONModuleManager.getInstance().result_10
				.setOnCmdReseivedListener(new OnResultListener<RedRay>() {
					@Override
					public void onResult(boolean isSecceed, RedRay obj) {
						if (isSecceed) {
							ToastUtils.showToastReal(obj.getBtn_code()
									+ "学习成功");
						}
					}
				});
	}

	@Override
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
