package com.ixhuiyunproject.huiyun.ixconfig.activity.timing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.ixhuiyunproject.R;
import com.ixhuiyunproject.huiyun.ixconfig.StaticValue;
import com.ixhuiyunproject.huiyun.ixconfig.activity.back.SwipeBackActivity;
import com.ixhuiyunproject.huiyun.ixconfig.bean.BaseJsonObj.MyJsonObj1;
import com.ixhuiyunproject.huiyun.ixconfig.bean.SceneItem;
import com.ixhuiyunproject.huiyun.ixconfig.bean.TimerTask;
import com.ixhuiyunproject.huiyun.ixconfig.fragment.FragmentContainer;
import com.ixhuiyunproject.huiyun.ixconfig.inter.OnResultListener;
import com.ixhuiyunproject.huiyun.ixconfig.net.JSONModuleManager;
import com.ixhuiyunproject.huiyun.ixconfig.net.NetJsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.AlarmUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.DateTimePickDialogUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.JsonUtil;
import com.ixhuiyunproject.huiyun.ixconfig.utils.RadioButtonDialogUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.StringUtils;
import com.ixhuiyunproject.huiyun.ixconfig.utils.ToastUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddTimerActivity extends SwipeBackActivity {

	private TextView mSet, mDate, mScene, mxunhuan;
	private static String date;
	// 获取一个日历对象
	Calendar dateAndTime = Calendar.getInstance();
	// 当前选择的场景名称
	private String selectSceneName;
	// 是否循环
	private int isRepeat = 2;
	private TimePicker timepicker;
	private DatePicker datepicker;
	private String nowTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_main);
		mSet = (TextView) findViewById(R.id.mSet);
		mDate = (TextView) findViewById(R.id.mDate);
		mScene = (TextView) findViewById(R.id.tv_scenes);
		mxunhuan = (TextView) findViewById(R.id.tv_xunhuan);
		// setSpinner();
		setListener();

		ImageView ivReturn = (ImageView) findViewById(R.id.iv_title_left);
		ivReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void setListener() {
		mSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTime();
			}
		});

		mDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// setDate();
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						AddTimerActivity.this, nowTime);
				dateTimePicKDialog.onCreateDialog(new DateTimePickDialogUtil.SelectDateTimeCallBack() {
					@Override
					public void isConfirm(String edString) {
						date = edString;
						System.out.println("当前选择" + date);
						updateLabel();
					}
				});
			}
		});

		mScene.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectSceneShowDialog();
			}

		});

		mxunhuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectLoopShowDialog();
			}
		});

	}

	/**
	 * Function: 选择场景对话框
	 * 
	 * @author YangShao 2015年4月13日 上午9:22:56
	 */
	public void selectSceneShowDialog() {
		RadioButtonDialogUtils dialog = new RadioButtonDialogUtils(
				AddTimerActivity.this, "请选择场景") {
			@Override
			protected ArrayList<HashMap<String, String>> getListDate() {
				ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
				if (FragmentContainer.SCENE_LIST.size() != 0) {
					for (SceneItem item : FragmentContainer.SCENE_LIST) {
						HashMap<String, String> Item = new HashMap<String, String>();
						Item.put("test", item.getScene_name());
						listData.add(Item);
					}
				}
				return listData;
			}

			@Override
			protected int getDialogWidth() {
				return 590;
			}
		};

		dialog.onCreateDialog();
		dialog.setCallBack(new RadioButtonDialogUtils.SelectCallBack() {
			@Override
			public void isConfirm(String edString) {
				if (StringUtils.isEmpty(edString)) {
					Toast.makeText(AddTimerActivity.this, "不能为空！", 
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					selectSceneName = edString;
					// dialog.dismiss();
				}
			}
		});
	}

	/**
	 * Function: 选择循环对话框
	 * 
	 * @author YangShao 2015年4月13日 上午9:22:56
	 */
	public void selectLoopShowDialog() {

		RadioButtonDialogUtils dialog = new RadioButtonDialogUtils(
				AddTimerActivity.this, "是否每天执行") {
			@Override
			protected ArrayList<HashMap<String, String>> getListDate() {
				ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> Item = new HashMap<String, String>();
				Item.put("test", "是");
				listData.add(Item);
				Item = new HashMap<String, String>();
				Item.put("test", "否");
				listData.add(Item);
				return listData;
			}

			@Override
			protected int getDialogWidth() {
				return 590;
			}
		};

		dialog.onCreateDialog();
		dialog.setCallBack(new RadioButtonDialogUtils.SelectCallBack() {
			@Override
			public void isConfirm(String edString) {
				if (StringUtils.isEmpty(edString)) {
					Toast.makeText(AddTimerActivity.this, "不能为空！", 
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (edString.equals("是")) {
						isRepeat = 1;
					} else {
						isRepeat = 2;
					}
				}
			}
		});
	}

	/**
	 * 时间设置
	 */
	public void setTime() {
		int mHour = dateAndTime.get(Calendar.HOUR_OF_DAY);
		int mMinute = dateAndTime.get(Calendar.MINUTE);
		final int mSecode = dateAndTime.get(Calendar.SECOND);
		new TimePickerDialog(AddTimerActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						view.setBackgroundColor(Color.WHITE);
						dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
						dateAndTime.set(Calendar.MINUTE, minute);
						dateAndTime.set(Calendar.SECOND, mSecode);
						dateAndTime.set(Calendar.MILLISECOND, 0);
						/**
						 * 将任务存入数据库
						 */
						date = Long.toString(dateAndTime.getTimeInMillis());
						System.out.println("存入String" + date);
						System.out.println("存入Long"
								+ dateAndTime.getTimeInMillis());
						System.out.println("当前" + AlarmUtils.getNowTimeMinuties());
						// addTimer();
						// ObjectPool.mAlarmHelper.openAlarm(32, "开关", "状态",
						// dateAndTime.getTimeInMillis());
						updateLabel();
					}
				}, mHour, mMinute, true).show();

	}

	/**
	 * 年月日设置
	 */
	public void setDate() {
		int YEAR = dateAndTime.get(Calendar.YEAR);
		int MONTH = dateAndTime.get(Calendar.MONTH);
		int DAY_OF_MONTH = dateAndTime.get(Calendar.DAY_OF_MONTH);
		// 当点击DatePickerDialog控件的设置按钮时，调用该方法
		new DatePickerDialog(AddTimerActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// 修改日历控件的年，月，日
						// 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
						dateAndTime.set(Calendar.YEAR, year);
						dateAndTime.set(Calendar.MONTH, monthOfYear);
						dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						// 将页面TextView的显示更新为最新时间
						updateLabel();
					}
				}, YEAR, MONTH, DAY_OF_MONTH).show();
	}

	/**
	 * Function: 状态变更通知
	 */
	public void addTimer(View view) {
		JSONModuleManager.getInstance().result_68
				.setOnCmdReseivedListener(new OnResultListener<Integer>() {
					@Override
					public void onResult(boolean isSecceed, Integer obj) {
						if (isSecceed) {
							ToastUtils.showToastReal("定时成功");
							TimerTask timerTask = new TimerTask();
							timerTask.setIsRepeat(isRepeat);
							timerTask.setSceneName(selectSceneName);
							timerTask.setDate(date);
							if (!FragmentContainer.TASK_LIST
									.contains(timerTask)) {
								FragmentContainer.TASK_LIST.add(timerTask);
							}
						} else {
							ToastUtils.showToastReal("定时失败");
						}
					}
				});

		try {
			if (!StringUtils.isEmpty(date)
					&& !StringUtils.isEmpty(selectSceneName.toString())) {
				if (AlarmUtils.differSetTimeAndNowTime(Long.valueOf(date))) {
					MyJsonObj1 myJsonObj1 = JsonUtil.getAJsonObj1ForMaster();
					myJsonObj1.code = 67;
					myJsonObj1.data = new HashMap<String, String>();
					myJsonObj1.data.put("token", StaticValue.user.getToken());
					myJsonObj1.data.put("funcode", 2 + "");
					myJsonObj1.data.put("isRepeat", isRepeat + "");
					myJsonObj1.data.put("date", date);
					if (!StringUtils.isEmpty(selectSceneName)) {
						myJsonObj1.data.put("scene_name",
								selectSceneName.toString());
						NetJsonUtil.getInstance().addCmdForSend(myJsonObj1);
					} else {
						ToastUtils.showToastReal("请填写完整数据");
					}

				} else {
					ToastUtils.showToastReal("不能小于当前时间");
				}

			}
		} catch (Exception e) {
			ToastUtils.showToastReal("请填写完整数据");
		}

	}

	TextView textView;
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();

	public void setTimes() {
		timepicker = (TimePicker) findViewById(R.id.tp_time);
		datepicker = (DatePicker) findViewById(R.id.dp_date);

		int year = dateAndTime.get(Calendar.YEAR);
		int monthOfYear = dateAndTime.get(Calendar.MONTH);
		int dayOfMonth = dateAndTime.get(Calendar.DAY_OF_MONTH);
		// int mHour = dateAndTime.get(Calendar.HOUR_OF_DAY);
		// int mMinute = dateAndTime.get(Calendar.MINUTE);
		final int mSecode = dateAndTime.get(Calendar.SECOND);
		datepicker.init(year, monthOfYear, dayOfMonth,
				new OnDateChangedListener() {

					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
						dateAndTime.set(Calendar.YEAR, year);
						dateAndTime.set(Calendar.MONTH, monthOfYear);
						dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						// 将页面TextView的显示更新为最新时间
						updateLabel();
					}

				});

		timepicker.setIs24HourView(true); // 设置为24小时制显示
		timepicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				dateAndTime.set(Calendar.MINUTE, minute);
				dateAndTime.set(Calendar.SECOND, mSecode);
				dateAndTime.set(Calendar.MILLISECOND, 0);
				/**
				 * 将任务存入数据库
				 */
				date = Long.toString(dateAndTime.getTimeInMillis());
				System.out.println("存入String" + date);
				System.out.println("存入Long" + dateAndTime.getTimeInMillis());
				System.out.println("当前" + AlarmUtils.getNowTimeMinuties());
				updateLabel();
			}
		});
	}

	// 更新页面TextView的方法
	private void updateLabel() {
		// 获取日期格式器对象
		textView = (TextView) findViewById(R.id.date);
		String str = AlarmUtils.stringToData(date);
		textView.setText("您选择的时间：" + str + "你选择的场景：" + selectSceneName);
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