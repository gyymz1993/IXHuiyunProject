package com.ixhuiyunproject.huiyun.ixconfig.activity.setting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.FinalValue;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public abstract class BaseActivity extends Activity {

	private Context mContext;
	protected PopMenu popMenu;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		onMoblickAgent();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/**
	 * 页面统计
	 */
	public void onMoblickAgent() {
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		MobclickAgent.updateOnlineConfig(this);
	}

	public void showMenu(View v) {
		popMenu = new PopMenu(mContext);
		popMenu.addItems(setPopMenuName());
		// 菜单项点击监听器
		popMenu.setOnItemClickListener(popmenuItemClickListener(null));
		popMenu.showAsDropDown(v);
	}

	Handler handler = new Handler() {
		/**
		 * 方法一：setCanceledOnTouchOutside(false); 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
		 * 方法二： setCanceleable(false);调用这个方法时，按对话框以外的地方不起作用 按返回键也不起作用
		 */
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// 最多等待5秒的等待对话框
				pDialog = new ProgressDialog(mContext);
				pDialog.setMessage(msg.obj.toString());
				pDialog.setProgressStyle(TRIM_MEMORY_BACKGROUND);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(5000);
							pDialog.dismiss();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			} else if (msg.what == 2) {
				// 可能无限等待的等待对话框
				if (pDialog != null && pDialog.isShowing()) {
					pDialog.setMessage(msg.obj.toString());
				} else {
					pDialog = new ProgressDialog(mContext);
					pDialog.setMessage(msg.obj.toString());
					pDialog.setProgressStyle(TRIM_MEMORY_BACKGROUND);
					pDialog.setCanceledOnTouchOutside(false);
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(true);
					pDialog.show();
				}
			}
		};

	};

	protected void onDestroy() {
		super.onDestroy();
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	};

	/**
	 * 显示登录窗口
	 */
	public void showpDialog(String showpDialogMessage) {
		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = showpDialogMessage;
		handler.sendMessage(msg);
	}

	/**
	 * 显示有可能永久等待的登录窗口
	 */
	public void showpDialogEternity(String showpDialogMessage) {
		Message msg = Message.obtain();
		msg.what = 2;
		msg.obj = showpDialogMessage;
		handler.sendMessage(msg);
	}

	/**
	 * 关闭等待窗口
	 */
	public void dismissDialog() {
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
	}

	/**
	 * 
	 * 重写监听方法
	 * 
	 * @param listener
	 * @return
	 */
	public abstract OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener);

	// 设置自定义菜单接口
	public abstract String[] setPopMenuName();

	class PopMenu {
		private ArrayList<String> itemList;
		private Context context;
		private PopupWindow popupWindow;
		private ListView listView;

		@SuppressWarnings("deprecation")
		public PopMenu(Context context) {
			this.context = context;
			itemList = new ArrayList<String>(3);
			View view = LayoutInflater.from(context).inflate(R.layout.popmenu,
					null);
			// 设置 listview
			listView = (ListView) view.findViewById(R.id.listView);
			listView.setAdapter(new PopAdapter());
			listView.setFocusableInTouchMode(true);
			listView.setFocusable(true);
			popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
			popupWindow = new PopupWindow(view, context.getResources()
					.getDimensionPixelSize(R.dimen.popmenu_width),
					LayoutParams.WRAP_CONTENT);
			// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}

		// 设置菜单项点击监听器
		public void setOnItemClickListener(OnItemClickListener listener) {
			listView.setOnItemClickListener(listener);
		}

		// 批量添加菜单项
		public void addItems(String[] items) {
			for (String s : items)
				itemList.add(s);
		}

		// 单个添加菜单项
		public void addItem(String item) {
			itemList.add(item);
		}

		// 下拉式 弹出 pop菜单 parent 右下角
		public void showAsDropDown(View parent) {
			popupWindow.showAsDropDown(parent,
					10,
					// 保证尺寸是根据屏幕像素密度来的
					context.getResources().getDimensionPixelSize(
							R.dimen.popmenu_yoff));
			// 使其聚集
			popupWindow.setFocusable(true);
			// 设置允许在外点击消失
			popupWindow.setOutsideTouchable(true);
			// 刷新状态
			popupWindow.update();
		}

		// 隐藏菜单
		public void dismiss() {
			popupWindow.dismiss();
		}

		// 适配器
		private final class PopAdapter extends BaseAdapter {
			@Override
			public int getCount() {
				return itemList.size();
			}

			@Override
			public Object getItem(int position) {
				return itemList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(context).inflate(
							R.layout.pomenu_item, null);
					holder = new ViewHolder();
					convertView.setTag(holder);
					holder.groupItem = (TextView) convertView
							.findViewById(R.id.textView);

				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				holder.groupItem.setText(itemList.get(position));
				return convertView;
			}

			private final class ViewHolder {
				TextView groupItem;
			}
		}

	}

	/**
	 * 禁止换行
	 */
	public static void notAllowedWrap(EditText view) {
		view.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				try {
					return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
				} catch (Exception e) {
					e.printStackTrace();
					return true;
				}
			}
		});
	}

	// 点击屏幕 关闭输入弹出框
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() != null) {
			im.hideSoftInputFromWindow(getCurrentFocus()
					.getApplicationWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FinalValue.mPageName);// 统计页面
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd(FinalValue.mPageName);
		MobclickAgent.onPause(mContext); // 统计时长
	}

}
