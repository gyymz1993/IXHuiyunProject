package com.ixhuiyunproject.huiyun.ixconfig.fragment;

import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View.OnClickListener;

import com.ixhuiyunproject.huiyun.ixconfig.activity.cell.DoorFragment;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.NewContrlFragment;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.homepage.HomePageFragment;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.redray.RedRayManager;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.scene.NewSceneFragment;
import com.ixhuiyunproject.huiyun.ixconfig.utils.LogUtils;


/**
 * 管理所有的Fragment
 * 
 * @author torahs
 *
 */
public class FragmentFactory {
	private static ErrorPageFragment errorpage = new ErrorPageFragment();

	public static final int SETTING_MAIN = 3;
	public static final int REDRAY = 2;
	public static final int CONTRL = 1;
	public static final int SCENE = 4;
	public static final int HOMEPAGE = 5;
	public static final int MICHAEL = 6;
	/** 测试专用 */
	public static final int TEST = -1;
	public static RedRayManager redRayManager = new RedRayManager();
	/** 记录所有的fragment，防止重复创建 */
	private static SparseArray<Fragment> mFragmentMap = new SparseArray<Fragment>();

	/** 采用工厂类进行创建Fragment，便于扩展，已经创建的Fragment不再创建 */
	public static Fragment getFragment(int index) {
		Fragment fragment = mFragmentMap.get(index);
		if (fragment == null) {
			switch (index) {
			case TEST: // 设置页面
				break;
			case SETTING_MAIN: // 设置页面
				fragment = new SetFragment();
				break;
			case CONTRL:
				fragment = new NewContrlFragment();
				break;
			case REDRAY:// TODO
				fragment = redRayManager.getFragment();
				break;
			case SCENE:// TODO
				fragment=new NewSceneFragment();
				break;
			case HOMEPAGE: // 主页
				fragment = new HomePageFragment();
				break;
			case MICHAEL: 
				fragment = new DoorFragment();
				break;
			}
			if (index != REDRAY && index != CONTRL)
				mFragmentMap.put(index, fragment);
		}
		if(fragment==null){
			LogUtils.i("fragment为空");
		}
		return fragment;
	}

	public static ErrorPageFragment getErrorpage(OnClickListener listener) {
		errorpage.setOnButtonClickListener(listener);
		return errorpage;
	}
}
