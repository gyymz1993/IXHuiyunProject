package com.ixhuiyunproject.huiyun.ixconfig.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.adapter.HomePagerAdapter;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentFactory;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.control.ContrlHandler;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnSucceedListener;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.ixhuiyunproject.ipcamer.demo.AddCameraActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页的fragment
 * @author lzn
 *
 */
public class HomePageFragment extends Fragment {
	
	// 滑动图片相关
	private ViewPager vpAdvert;
	private HomePagerAdapter hpadapter;
	private Timer timerAdvertSwitch;
	private final int INTERVAL_ADVERT = 5000;
	private FrameLayout flDot;
	private ManyDots manyDots;
	private static boolean first = true;
	private static List<HomePagerAdapter.ImageRes> listImageRes
		= new ArrayList<HomePagerAdapter.ImageRes>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, 
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_home_page, null, false);
		
		// 滑动图片栏
		initAdvert(view);
		// 主页/设置选项卡栏
		initTab(view);
		// 正中间的选项模块
		initMainBody(view);
		
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		// 停止计时器
		if(timerAdvertSwitch != null){
			timerAdvertSwitch.cancel();
			timerAdvertSwitch = null;
		}
		
	}
	
	/**
	 * 滑动图片栏
	 * @param view
	 */
	private void initAdvert(View view){
		vpAdvert = (ViewPager) view.findViewById(R.id.vp_advert);
		hpadapter = new HomePagerAdapter();
		
		// 更新适配器
		if(first){
			listImageRes.add(new HomePagerAdapter.ImageRes(R.drawable.home_adpic1, null));
		}
		hpadapter.loadImageResList(listImageRes);
		
		vpAdvert.setAdapter(hpadapter);
		if(vpAdvert.getAdapter().getCount() > 1){
			// 滑动监听
			vpAdvert.setOnPageChangeListener(new MyHomePageAdChangeListener(vpAdvert));
			// 定时滑动任务
			timerAdvertSwitch = new Timer();
			timerAdvertSwitch.schedule(
					new HomePageAdTimeTask(vpAdvert,
						(vpAdvert.getCurrentItem()+1)%vpAdvert.getAdapter().getCount()), INTERVAL_ADVERT);
		}
		
		// 该FrameLayout用于加载小圆点，只能在OnPreDrawListener中获取正确的控件高度和宽度
		flDot = (FrameLayout) view.findViewById(R.id.fl_dot);
		flDot.getViewTreeObserver().
			addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				
				if (vpAdvert.getAdapter().getCount() > 1) {
					// 小圆点集合
					manyDots = new ManyDots(flDot, vpAdvert.getAdapter()
							.getCount(), flDot.getMeasuredWidth(),
							flDot.getMeasuredHeight());
					manyDots.choose(vpAdvert.getCurrentItem());
					manyDots.setOnDotClickListener(new OnSucceedListener<Integer>() {

						@Override
						public void onSucceed(Integer obj) {
							// 小圆点被点击后，跳转到指定图片
							vpAdvert.setCurrentItem(obj, true);
						}
					});
				}
				// 该监听被执行过一次后直接移除
				flDot.getViewTreeObserver().
					removeOnPreDrawListener(this);
				return true;
			}
		});
		
		// 通过http从服务器请求要加载的图片信息
		if(first){
			requestAdInfo();
			first = false;
		}
	}
	
	/**
	 * 收到POST的结果后，刷新广告栏
	 * @param jsonObj
	 */
	private void freshAdvert(AdvertJsonObj jsonObj){
		// 更新适配器
		listImageRes.clear();
		for(int i=0; i<jsonObj.data.size(); i++){
			listImageRes.add(new HomePagerAdapter.ImageRes(
					jsonObj.data.get(i).icon, jsonObj.data.get(i).url));
		}
		hpadapter.loadImageResList(listImageRes);
		
		vpAdvert.setAdapter(hpadapter);
		if(vpAdvert.getAdapter().getCount() > 1){
			// 滑动监听
			vpAdvert.setOnPageChangeListener(new MyHomePageAdChangeListener(vpAdvert));
			// 定时滑动任务
			timerAdvertSwitch = new Timer();
			timerAdvertSwitch.schedule(
					new HomePageAdTimeTask(vpAdvert,
						(vpAdvert.getCurrentItem()+1)%vpAdvert.getAdapter().getCount()), INTERVAL_ADVERT);
		}
		
		// 该FrameLayout用于加载小圆点，只能在OnPreDrawListener中获取正确的控件高度和宽度
		// 需要注意的是，需要先移除掉原本的小圆点
		flDot.getViewTreeObserver().
			addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				// 清理旧的小圆点
				if(manyDots != null){
					manyDots.clean(flDot);
					manyDots = null;
				}
				
				if (vpAdvert.getAdapter().getCount() > 1) {
					// 小圆点集合
					manyDots = new ManyDots(flDot, vpAdvert.getAdapter()
							.getCount(), flDot.getMeasuredWidth(),
							flDot.getMeasuredHeight());
					manyDots.choose(vpAdvert.getCurrentItem());
					manyDots.setOnDotClickListener(new OnSucceedListener<Integer>() {

						@Override
						public void onSucceed(Integer obj) {
							// 小圆点被点击后，跳转到指定图片
							vpAdvert.setCurrentItem(obj, true);
						}
					});
				}
				// 该监听被执行过一次后直接移除
				flDot.getViewTreeObserver().
					removeOnPreDrawListener(this);
				return true;
			}
		});
		
	}
	
	/**
	 * 通过http从服务器请求要加载的图片信息
	 * 将加载到的图片信息显示在广告栏上
	 */
	private void requestAdInfo(){
		try {
			// POST请求
			RequestParams params = new RequestParams();
			HttpEntity entity = new StringEntity("{\"currentDate\":\"2015-04-21_1\"}");
			params.setBodyEntity(entity);
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.send(HttpMethod.POST, FinalValue.WEBSITE_ADVERT, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException exception, String s) {
							// do nothing
							System.out.println("httpUtils.send.onFailure");
						}

						@Override
						public void onSuccess(ResponseInfo<String> response) {
							// POST请求的返回结果
							String result = response.result;
							System.out.println("httpUtils.send.onSuccess");
							System.out.println("http结果：" + result);

							// 将JOSON格式的结果转换
							Gson gson = new Gson();
							try {
								AdvertJsonObj jsonObj = gson.fromJson(result,
										AdvertJsonObj.class);
								System.out.println(jsonObj);
								if(jsonObj != null){
									if (jsonObj.data != null) {
										// 对反馈的结果进行处理
										freshAdvert(jsonObj);
									}
								}
							} catch (JsonSyntaxException e) {
								e.printStackTrace();
							}
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 主页/设置选项卡栏
	 * @param view
	 */
	private void initTab(View view){
		Button btnHomePage,btnSetting;
		btnHomePage = (Button) view.findViewById(R.id.btn_homepage);
		btnSetting = (Button) view.findViewById(R.id.btn_setting);
		btnHomePage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到主页
				System.out.println("btnHomePage");
			}
		});
		btnSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到设置界面
				System.out.println("btnSetting");
				Message msg = HomeActivity.homeActivity.
						switchPageHandler.obtainMessage();
				if (HomeActivity.judgeLoginState()) { // 先登陆才能使用
					msg.arg1 = FragmentFactory.SETTING_MAIN;
					HomeActivity.homeActivity.switchPageHandler.sendMessage(msg);
				} else {// 登陆
					HomeActivity.homeActivity.jumpToLoginPage(
							FragmentFactory.SETTING_MAIN, (HomeActivity) getActivity());
				}
			}
		});
	}
	
	/**
	 * 正中间的选项模块
	 * @param view
	 */
	private void initMainBody(View view){
		Button btnMonitor,btnSwitch,btnRemote,btnScene,btnAxtion;
		btnMonitor = (Button) view.findViewById(R.id.irbtn_hp_1);
		btnSwitch = (Button) view.findViewById(R.id.irbtn_hp_2);
		btnRemote = (Button) view.findViewById(R.id.irbtn_hp_3);
		btnScene = (Button) view.findViewById(R.id.irbtn_hp_4);
		btnAxtion = (Button) view.findViewById(R.id.irbtn_hp_center);
		btnMonitor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到监控界面
				Intent intent = new Intent(getActivity(),
						AddCameraActivity.class);
				startActivity(intent);
				ContrlHandler contrlHandler=new ContrlHandler();
				contrlHandler.sendMessage();
			}
		});
		btnSwitch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到开关界面
				Message msg = HomeActivity.homeActivity.
						switchPageHandler.obtainMessage();
				if (HomeActivity.judgeLoginState()) { // 先登陆才能使用
					msg.arg1 = FragmentFactory.CONTRL;
					HomeActivity.homeActivity.switchPageHandler.sendMessage(msg);
				} else {// 登陆
					HomeActivity.homeActivity.jumpToLoginPage(
							FragmentFactory.CONTRL, (HomeActivity) getActivity());
				}
			}
		});
		btnRemote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到遥控界面
				Message msg = HomeActivity.homeActivity.
						switchPageHandler.obtainMessage();
				if (HomeActivity.judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.REDRAY;
					HomeActivity.homeActivity.switchPageHandler.sendMessage(msg);
				} else {// 登陆
					HomeActivity.homeActivity.jumpToLoginPage(
							FragmentFactory.REDRAY, (HomeActivity) getActivity());
				}
			}
		});
		btnScene.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 跳转到场景界面
				Message msg = HomeActivity.homeActivity.
						switchPageHandler.obtainMessage();
				if (HomeActivity.judgeLoginState()) {// 先登陆才能使用
					msg.arg1 = FragmentFactory.SCENE;
					HomeActivity.homeActivity.switchPageHandler.sendMessage(msg);
				} else {// 登陆
					HomeActivity.homeActivity.jumpToLoginPage(
							FragmentFactory.SCENE, (HomeActivity) getActivity());
				}
			}
		});
		btnAxtion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	/**
	 * 最上方的滑动图片栏的监听器
	 * @author lzn
	 *
	 */
	private class MyHomePageAdChangeListener implements OnPageChangeListener {
		
		private ViewPager viewPager;
		
		MyHomePageAdChangeListener(ViewPager viewPager){
			this.viewPager = viewPager;
		}
		
		@Override
		public void onPageSelected(int pos) {
			// 被选中的圆点有所改变
			if(manyDots == null){
				return;
			}
			manyDots.choose(pos);
			// 重新开启延迟执行的自动跳页任务
			if(timerAdvertSwitch != null){
				timerAdvertSwitch.cancel();
			}
			timerAdvertSwitch = new Timer();
			// 跳到下一页，如果是最后一页则跳回第一页
			timerAdvertSwitch.schedule(new HomePageAdTimeTask(viewPager,
				(pos+1)%viewPager.getAdapter().getCount()), INTERVAL_ADVERT);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int pos) {
		}
		
	}
	
	/**
	 * 最上方的滑动图片栏的定时任务
	 * @author lzn
	 *
	 */
	private class HomePageAdTimeTask extends TimerTask{

		private ViewPager viewPager;
		private int pos;
		
		HomePageAdTimeTask(ViewPager viewPager, int pos){
			this.viewPager = viewPager;
			this.pos = pos;
		}
		
		@Override
		public void run() {
			// 需要在UI主线程下执行
			UIUtils.runInMainThread(new Runnable() {
				
				@Override
				public void run() {
					// 切换图片
					viewPager.setCurrentItem(pos, true);
				}
			});
		}
	}
	
	/**
	 * 用于显示广告栏的小圆点集合
	 * @author lzn
	 *
	 */
	private class ManyDots {
		
		// 用于监听圆点被点击
		private OnSucceedListener<Integer> listener;
		// 所有view的集合
		private List<ImageView> allDotsView;
		
		/**
		 * @param fl 用于装载圆点的Layout
		 * @param total 圆点数量
		 * @param width Layout的宽度
		 * @param height Layout的高度
		 */
		public ManyDots(FrameLayout fl, int total, int width, int height){
			allDotsView = new ArrayList<ImageView>();
			
			final float ZOOM = 2.4f;
			int widthDot = 25; // 圆点宽度
			int heightDot = 25; // 圆点长度
			float yForAll =  height * (10.0f/12.0f);
			float xForFirst = (width - ((total-1.0f)*ZOOM+1.0f) * widthDot)/2.0f;
			
			// 逐个生成圆点控件
			for(int i=0; i<total; i++){
				final int listener_i = i;
				ImageView iv = new ImageView(getActivity());
				iv.setBackgroundResource(R.drawable.home_ad_dot_2);
				iv.setX(xForFirst + ZOOM*i*widthDot);
				iv.setY(yForAll);
				iv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// 圆点被点击时回调
						if(listener != null){
							listener.onSucceed(listener_i);
						}
					}
				});
				flDot.addView(iv, widthDot, heightDot);
				allDotsView.add(iv);
			}
		}
		
		/**
		 * 让指定的圆点处于被选中状态
		 * @param num
		 */
		public void choose(int num){
			for(int i=0; i<allDotsView.size(); i++){
				if(i == num){
					// 被选中的
					allDotsView.get(i).setBackgroundResource(R.drawable.home_ad_dot_1);
				} else {
					// 未被选中的
					allDotsView.get(i).setBackgroundResource(R.drawable.home_ad_dot_2);
				}
			}
		}
		
		/**
		 * 将所有圆点从视图上移除
		 */
		public void clean(FrameLayout fl){
			for(int i=0; i<allDotsView.size(); i++){
				fl.removeView(allDotsView.get(i));
			}
		}
		
		/** 设置圆点被点击的监听
		 * @param listener
		 */
		public void setOnDotClickListener(OnSucceedListener<Integer> listener){
			this.listener = listener;
		}
	}
	
	/**
	 * 广告栏POST反馈信息的JSON格式
	 * @author lzn
	 *
	 */
	private class AdvertJsonObj {
		public int result;
		public String date;
		public List<AdvertJsonData> data;
		
		public class AdvertJsonData {
			public int adid;
			public String url;
			public String icon;
			public String date;
			public int version;
			public int type;
			@Override
			public String toString() {
				return "AdvertJsonData [adid=" + adid
						+ ", url=" + url + ", icon=" + icon + ", date=" + date
						+ ", version=" + version + ", type=" + type + "]";
			}
		}

		@Override
		public String toString() {
			return "AdvertJsonObj [result=" + result + ", date=" + date
					+ ", data=" + data + "]";
		}
	}
}
