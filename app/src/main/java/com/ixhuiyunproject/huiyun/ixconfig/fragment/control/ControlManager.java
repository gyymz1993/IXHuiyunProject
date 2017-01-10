package com.ixhuiyunproject.huiyun.ixconfig.fragment.control;//package com.huiyun.ixconfig.fragment.control;
//
//import android.content.Intent;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.BaseAdapter;
//
//import com.huiyun.ixconfig.activity.HomeActivity;
//import com.huiyun.ixconfig.activity.setting.AreaSettingActivity;
//import com.huiyun.ixconfig.activity.setting.SceneSettingActivity;
//import com.huiyun.ixconfig.activity.userlogin.LoginSucceedActtion;
//import com.huiyun.ixconfig.fragment.FragmentContainer;
//import com.huiyun.ixconfig.fragment.FragmentFactory;
//import com.huiyun.ixconfig.utils.UIUtils;
//
//public class ControlManager {
//
//	public static BaseAdapter baseAdapter;
//	public static FragmentPagerAdapter fragmentPagerAdapter;
//	/**  */
//	public static ContrlFragment contrlFragment = null;
//
//	/**
//	 * 判断是否有数据，没有数据就跳出错误页面
//	 * 
//	 * @return
//	 */
//	public static Fragment getFragment() {
//		if (FragmentContainer.AREAS_LIST.size() == 0) {
//			return  FragmentFactory.getErrorpage(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Message msg = Message.obtain();
//						msg.what = 3;// 该选项表示：4秒后重新载入页面
//						msg.obj = "正在重新载入";
//						msg.arg1 = FragmentFactory.CONTRL;
//						
//						HomeActivity.homeActivity.dialogHandler
//								.sendMessage(msg);
//						LoginSucceedActtion.downloadArea();// 重新开始下载区域
//					}
//				});
//		} else {
//			if (contrlFragment == null) {
//				contrlFragment = new ContrlFragment();
//			}
//			return contrlFragment;
//		}
//		
//	}
//
//	public static void notifyDataSetChanged() {
//		try {
//			if (baseAdapter != null && fragmentPagerAdapter != null) {
//				baseAdapter.notifyDataSetChanged();
//				fragmentPagerAdapter.notifyDataSetChanged();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
