package com.ixhuiyunproject.huiyun.ixconfig.activity.redray;//package com.huiyun.ixconfig.activity.redray;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.baoyz.swipemenulistview.SwipeMenu;
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//import com.baoyz.swipemenulistview.SwipeMenuListView;
//import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
//import com.huiyun.ix_configreconstitute.R;
//import com.huiyun.ixconfig.FinalValue;
//import com.huiyun.ixconfig.StaticValue;
//import com.huiyun.ixconfig.activity.back.SwipeBackActivity;
//import com.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
//import com.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2.Data;
//import com.huiyun.ixconfig.bean.RedRay;
//import com.huiyun.ixconfig.fragment.FragmentContainer;
//import com.huiyun.ixconfig.fragment.FragmentFactory;
//import com.huiyun.ixconfig.inter.OnResultListener;
//import com.huiyun.ixconfig.net.JSONModuleManager;
//import com.huiyun.ixconfig.net.NetJsonUtil;
//import com.huiyun.ixconfig.utils.RandomOfImageBg;
//import com.huiyun.ixconfig.utils.StringUtils;
//import com.huiyun.ixconfig.utils.ToastUtils;
//import com.huiyun.ixconfig.utils.UIUtils;
//import com.umeng.analytics.MobclickAgent;
//
///**
// * @Title: EareSettingActivity.java
// * @Package com.huiyun.ixconfig.activity.setting
// * @Description: 遥控器设置
// */
//public class RemoteSettingActivity extends SwipeBackActivity {
//	private SwipeMenuListView listview;
//	private ListViewAdapter adapter;
//	private Context context;
//	private List<RedRay> rayCodes = new ArrayList<RedRay>();
//
//	// 记录删除的记录
//	private List<String> delCodes = new ArrayList<String>();
//
//	/**
//	 * Function:添加 2015年1月19日 上午10:56:42
//	 */
//	private LinearLayout lay_add;
//	private TextView save;
//	private ImageView iv_left;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		context = this;
//		setContentView(R.layout.activity_remote_setting);
//		initView();
//
//	}
//
//	SwipeMenuCreator creator = new SwipeMenuCreator() {
//		@Override
//		public void create(SwipeMenu menu) {
//			SwipeMenuItem deleteItem = new SwipeMenuItem(
//					RemoteSettingActivity.this);
//			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F,
//					0x25)));
//			deleteItem.setWidth(UIUtils.dip2px(90));
//			deleteItem.setIcon(R.drawable.addedarea_del);
//			menu.addMenuItem(deleteItem);
//		}
//	};
//
//	
//	 /**
//	 *  Function:将主机下载的遥控器copy一份出来
//	 * 
//	 *  @author YangShao 2015年4月24日 上午10:43:32
//	 */
//	private void copyRemote() {
//		if (FragmentContainer.remoteControllerList.size() != 0) {
//			for (RedRay code : FragmentContainer.remoteControllerList) {
//				RedRay code2 = new RedRay();
//				code2.setBtn_code(code.getBtn_code());
//				System.out.println("空调名称：" + code.getR_name() + "页面样式"
//						+ code.getPageType());
//				code2.setR_name(code.getR_name());
//				code2.setPageType(code.getPageType());
//				if (!rayCodes.contains(code2)) {
//					rayCodes.add(code2);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * Function:初始化控件
//	 * 
//	 * @author Yangshao 2015年1月21日 下午2:00:38
//	 */
//	private void initView() {
//		iv_left = (ImageView) findViewById(R.id.iv_left);
//		iv_left.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				RemoteSettingActivity.this.finish();
//			}
//		});
//		listview = (SwipeMenuListView) findViewById(R.id.lsv_eare_set);
//		copyRemote();
//	//	setRayCodes(rayCodes);
//		adapter = new ListViewAdapter();
//		listview.setAdapter(adapter);
//		listview.setMenuCreator(creator);
//		listview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(int position, SwipeMenu menu,
//					int index) {
//				switch (index) {
//				case 0:// 删除
//					if (rayCodes.size() != 1) {
//						RedRay code = rayCodes.remove(position);
//						if (!delCodes.contains(code.getR_name())) {
//							delCodes.add(code.getR_name());
//						}
//						adapter.notifyDataSetChanged();
//					} else {
//						ToastUtils.showToastReal("至少剩余一个遥控器");
//					}
//					break;
//				}
//				return true;// always close menu
//			}
//		});
//		// 添加按钮
//		lay_add = (LinearLayout) findViewById(R.id.lay_add);
//		lay_add.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, AddRemoteActivity.class);
//				startActivity(intent);
//			}
//		});
//		save = (TextView) findViewById(R.id.remote_save);
//		save.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				uploadRemote();
//			}
//		});
//	}
//
//	/**
//	 * Function:跟新遥控器上传
//	 * 
//	 * @author YangShao 2015年4月24日 上午9:32:18
//	 */
//	private void uploadRemote() {
//		MyJsonObj2 jsonobj = new MyJsonObj2();
//		Map<String, String> map;
//		Data data = new Data();
//		jsonobj.code = 19;
//		jsonobj.obj = "master";
//		data.token = StaticValue.user.getToken();
//		if (delCodes.size() > 0) {
//			for (int i = 0; i < delCodes.size(); i++) {
//				if (!StringUtils.isEmpty(delCodes.get(i))) {
//					map = new HashMap<String, String>();
//					map.put("r_name", delCodes.get(i));
//					data.list.add(map);
//				}
//			}
//			jsonobj.setData(data);
//			NetJsonUtil.getInstance().addCmdForSend(jsonobj);
//			JSONModuleManager.getInstance().result_20
//					.setOnCmdReseivedListener(new OnResultListener<Object>() {
//						@Override
//						public void onResult(boolean isSecceed, Object obj) {
//							if (isSecceed) {
//								FragmentContainer.remoteControllerList.clear();
//								for (int i = 0; i < rayCodes.size(); i++) {
//									RedRay code = new RedRay();
//									code.setR_name(rayCodes.get(i).getR_name());
//									code.setPageType(rayCodes.get(i)
//											.getPageType());
//									FragmentContainer.remoteControllerList
//											.add(code);
//								}
//								ToastUtils.showToastReal("修改成功");
//							} else {
//								ToastUtils.showToastReal("修改失败");
//							}
//							FragmentFactory.redRayManager.flushData();// 刷新数据
//							finish();
//						}
//					});
//		}
//	}
//
//	class ListViewAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			return rayCodes.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return rayCodes.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				convertView = View.inflate(RemoteSettingActivity.this,
//						R.layout.item_remote_setting, null);
//				holder = new ViewHolder();
//				holder.textView = (TextView) convertView
//						.findViewById(R.id.tx_set_remote);
//				holder.imageView = (ImageView) convertView
//						.findViewById(R.id.im_set_remote);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.rayCode = rayCodes.get(position);
//			RedRay code = rayCodes.get(position);
//			holder.textView.setText(code.getR_name());
//			System.out.println("List：" + code.getR_name() + "页面样式"
//					+ code.getPageType());
//			holder.imageView.setImageResource(RandomOfImageBg
//					.getRemoteImage(code.getPageType()));
//			return convertView;
//		}
//
//		class ViewHolder {
//			ImageView imageView;
//			TextView textView;
//			RedRay rayCode;
//		}
//	}
//
//	// 点击屏幕 关闭输入弹出框
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		im.hideSoftInputFromWindow(getCurrentFocus()
//				.getApplicationWindowToken(),
//				InputMethodManager.HIDE_NOT_ALWAYS);
//		return super.onTouchEvent(event);
//	}
//
//	/**
//	 * @return rayCodes
//	 */
//	public List<RedRay> getRayCodes() {
//		return rayCodes;
//	}
//
//	/**
//	 * @param rayCodes
//	 *            要设置的 rayCodes
//	 */
//	public void setRayCodes(List<RedRay> rayCodes) {
//		this.rayCodes = rayCodes;
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		rayCodes.clear();
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		//copyRemote();
//		//adapter.notifyDataSetChanged();
//		MobclickAgent.onPageStart(FinalValue.mPageName);// 统计页面
//		MobclickAgent.onResume(this);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
//		MobclickAgent.onPageEnd(FinalValue.mPageName);
//		MobclickAgent.onPause(this); // 统计时长
//	}
//
//	@Override
//	public OnItemClickListener popmenuItemClickListener(
//			OnItemClickListener listener) {
//		return null;
//	}
//
//	@Override
//	public String[] setPopMenuName() {
//		return null;
//	}
//}