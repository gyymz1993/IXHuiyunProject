package com.ixhuiyunproject.huiyun.ixconfig.activity.timing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenu;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuCreator;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuItem;
import com.ixhuiyunproject.baoyz.swipemenulistview.SwipeMenuListView;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.TimerTask;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.AlarmUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.UIUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Function:
 * 
 * @author YangShao 2015年4月3日 下午2:35:26
 * @version 1.0
 */
public class TimerTaskListActivity extends SwipeBackActivity {

	// private ListView listView;
	SwipeMenuListView listView;
	private Context mContext;
	private ListViewAdapter adapter;
	private List<TimerTask> tasks;

	private Button addBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetask_main);
		mContext = this;
		addBtn = (Button) findViewById(R.id.add_timer);
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TimerTaskListActivity.this,
						AddTimerActivity.class);
				startActivity(intent);
			}
		});
		initView();
		
		ImageView ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem deleteItem = new SwipeMenuItem(
					TimerTaskListActivity.this);
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F,
					0x25)));
			deleteItem.setWidth(UIUtils.dip2px(90));
			deleteItem.setIcon(R.drawable.addedarea_del);
			menu.addMenuItem(deleteItem);
		}
	};

	/**
	 * 初始化View
	 */
	private void initView() {
		listView = (SwipeMenuListView) findViewById(R.id.id_lv_timertask);
		if (FragmentContainer.TASK_LIST.size() != 0) {
			String time;
			tasks = FragmentContainer.TASK_LIST;
			System.out.println("当前时间："+ AlarmUtils.stringToData(String.valueOf(AlarmUtils.getNowTimeMinuties())));
			for (TimerTask task : tasks) {
				if (AlarmUtils.compareNowTime(task
						.getDate())) {
					System.out.println("设置今天循环");
					time = AlarmUtils.getTodayTime(task.getDate());
				} else {
					System.out.println("设置明天循环");
					time = AlarmUtils.getTomorrowTime(task.getDate());
				}
				task.setDate(time);
			}
			adapter = new ListViewAdapter();
			listView.setAdapter(adapter);
			listView.setMenuCreator(creator);
			listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(int position, SwipeMenu menu,
						int index) {
					switch (index) {
					case 0:// 删除
						deleteTimer(tasks.get(index));
						tasks.remove(index);
						setTasks(tasks);
						adapter.notifyDataSetChanged();
						break;
					}
					return true;// always close menu
				}
			});

		}

	}

	public class ListViewAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return tasks.size();
		}

		@Override
		public Object getItem(int position) {
			return tasks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parentn) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_time, null);
				holder.dateview = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.isLoop = (Button) convertView
						.findViewById(R.id.bt_xunhuan);
				holder.sceneview = (TextView) convertView
						.findViewById(R.id.tv_scene);
				holder.state = (TextView) convertView
						.findViewById(R.id.tv_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.task = tasks.get(position);
			String time = AlarmUtils.stringToDateAndTime(holder.task.getDate());
			holder.dateview.setText(time);
			holder.sceneview.setText(holder.task.getSceneName() + "");
			if (holder.task.getIsRepeat() == 1) {
				holder.isLoop.setBackgroundResource(R.drawable.button_on);
				holder.state.setText("每天");
			} else {
				holder.isLoop.setBackgroundResource(R.drawable.button_off);
				holder.state.setText("今天");
			}

			return convertView;
		}

		public class ViewHolder {
			TimerTask task;
			TextView dateview;
			TextView sceneview;
			TextView state;
			Button isLoop;
		}
	}

	public List<TimerTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<TimerTask> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Function: 删除任务
	 * 
	 * @author YangShao 2015年4月3日 下午2:35:51
	 * @param task
	 */
	public void deleteTimer(TimerTask task) {
		JSONModuleManager.getInstance().result_74
				.setOnCmdReseivedListener(new OnResultListener<Integer>() {
					@Override
					public void onResult(boolean isSecceed, Integer obj) {
						if (isSecceed) {
							ToastUtils.showToastReal("删除任务成功");
						} else {
							ToastUtils.showToastReal("删除任务");
						}
					}
				});
		if (!StringUtils.isEmpty(task.getDate())) {
			MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
			myJsonObj1.code = 73;
			myJsonObj1.data = new HashMap<String, String>();
			myJsonObj1.data.put("token", StaticValue.user.getToken());
			myJsonObj1.data.put("date", task.getDate() + "");
			myJsonObj1.data.put("scene_name", task.getSceneName());
			myJsonObj1.data.put("isRepeat", task.getIsRepeat() + "");
			NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
		}

	}

	@Override
	public OnItemClickListener popmenuItemClickListener(
			OnItemClickListener listener) {
		return null;
	}

	@Override
	public String[] setPopMenuName() {
		return null;
	}

}