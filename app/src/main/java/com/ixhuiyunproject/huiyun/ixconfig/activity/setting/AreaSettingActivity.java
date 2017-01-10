package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenu;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuCreator;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuItem;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuListView;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.HomeActivity;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj;
import com.ixhuiyunproject.huiyun.ixconfig.dialog.EnterDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: EareSettingActivity.java
 * @Package com.huiyun.ixconfig.activity.setting
 * @Description: 区域设置
 */
public class AreaSettingActivity extends SwipeBackActivity {
	private SwipeMenuListView listview;
	private ListViewAdapter adapter;
	private Context context;
	private LinearLayout lay_add;
	private TextView save;
	private ImageView iv_left;
	private EditText et;
	/*** 真正的区域 */
	private List<String> realAreaNames = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_eare_setting);
		// 生成一个textview
		et = new EditText(this);
		et.setLayoutParams(new LayoutParams(-1, -2));
		BaseActivity.notAllowedWrap(et);
		initView();

		/**
		 * 初始化下载区域数据
		 */
		if (FragmentContainer.AREAS_LIST.size() != 0) {
			// 得到已有区域信息
			for (String name : FragmentContainer.AREAS_LIST) {
				if (!StringUtils.isEmpty(name)) {
					realAreaNames.add(name);
				}
			}
		}

	}

	/**
	 * 显示添加场景输入框
	 */
	private void showDialog() {
		EnterDialogUtils dialog = new EnterDialogUtils(
				AreaSettingActivity.this, "请输入区域名称");
		dialog.onCreateDialog();
		dialog.setCallBack(new EnterDialogUtils.CallBack() {
			@Override
			public void isConfirm(String edString) {
				if (StringUtils.isEmpty(edString)) {
					Toast.makeText(context, "不能为空！", Toast.LENGTH_LONG).show();
					return;
				} else {
					// 得到已有区域信息
					if (realAreaNames != null
							&& realAreaNames.contains(edString)) {
						Toast.makeText(context, "此区域已存在！", Toast.LENGTH_LONG).show();
					} else {
						realAreaNames.add(edString);
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	/**
	 * Function:初始化控件
	 * 
	 * @author Yangshao 2015年1月21日 下午2:00:38
	 */
	private void initView() {
		iv_left = (ImageView) findViewById(R.id.iv_left);
		/**
		 * 返回
		 */
		iv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AreaSettingActivity.this.finish();
			}
		});
		// 初始化listview
		listview = (SwipeMenuListView) findViewById(R.id.lsv_eare_set);
		adapter = new ListViewAdapter();
		listview.setAdapter(adapter);
		listview.setMenuCreator(creator);
		listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:// 删除
					realAreaNames.remove(position);
					adapter.notifyDataSetChanged();
					break;
				}
				return true;// always close menu
			}
		});
		// 添加按钮
		lay_add = (LinearLayout) findViewById(R.id.lay_add);
		lay_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		/**
		 * 添加区域 上传区域到服务器
		 */
		save = (TextView) findViewById(R.id.tv_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (realAreaNames.size() > 0) {
					updateArea();
				} else {
					ToastUtils.showToastReal("请至少添加一个区域名称");
				}

			}
		});

	}

	/**
	 * 
	 * Function: 上传服务器 更新区域
	 * 
	 * @author Yangshao 2015年2月10日 上午10:53:04
	 */
	public void updateArea() {

		try {
			MyJsonObj2 jsonobj = new MyJsonObj2();
			Map<String, String> map;
			BaseJsonObj.MyJsonObj2.Data data = new BaseJsonObj.MyJsonObj2.Data();
			jsonobj.code = 35;
			jsonobj.obj = "master";
			data.token = StaticValue.user.getToken();
			if (realAreaNames.size() > 0) {
				for (int i = 0; i < realAreaNames.size(); i++) {
					if (!StringUtils.isEmpty(realAreaNames.get(i))) {
						map = new HashMap<String, String>();
						map.put("area", realAreaNames.get(i));
						data.list.add(map);
					}
				}
				jsonobj.setData(data);
				NetJsonUtil.getInstance().addCmdForSend(jsonobj);
				JSONModuleManager.getInstance().result_36
						.setOnCmdReseivedListener(new OnResultListener<Object>() {
							@Override
							public void onResult(boolean isSecceed, Object obj) {
								if (isSecceed) {
									if (realAreaNames.size() > 0) {
										FragmentContainer.AREAS_LIST.clear();
										for (int i = 0; i < realAreaNames
												.size(); i++) {
											FragmentContainer.AREAS_LIST
													.add(realAreaNames.get(i));
										}
									}
									Intent intent = new Intent(context,
											HomeActivity.class);
									startActivity(intent);
									AreaSettingActivity.this.finish();
								}
							}
						});
			}
		} catch (Exception e) {
			ToastUtils.showToastReal("请填写区域名称");
			e.printStackTrace();
		}

	}

	class ListViewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return realAreaNames.size();
		}

		@Override
		public Object getItem(int position) {
			return realAreaNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(AreaSettingActivity.this,
						R.layout.item_eare_setting, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.tx_eare_set);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.area = realAreaNames.get(position);
			holder.textView.setText(holder.area);
			return convertView;
		}

		class ViewHolder {
			TextView textView;
			String area;
		}
	}

	/*
	 * // 点击屏幕 关闭输入弹出框
	 * 
	 * @Override public boolean onTouchEvent(MotionEvent event) {
	 * InputMethodManager im = (InputMethodManager)
	 * getSystemService(Context.INPUT_METHOD_SERVICE);
	 * im.hideSoftInputFromWindow(getCurrentFocus()
	 * .getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); return
	 * super.onTouchEvent(event); }
	 */
	SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			// create "delete" item
			SwipeMenuItem deleteItem = new SwipeMenuItem(
					AreaSettingActivity.this);
			// set item background
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F,
					0x25)));
			// set item width
			deleteItem.setWidth(UIUtils.dip2px(90));
			// set a icon
			deleteItem.setIcon(R.drawable.addedarea_del);
			// add to menu
			menu.addMenuItem(deleteItem);
		}
	};

	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("SplashScreen"); //统计页面
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("SplashScreen"); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息 
	    MobclickAgent.onPause(this);
	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		// TODO 自动生成的方法存根
		return null;
	}
}