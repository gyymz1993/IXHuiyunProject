package com.ixhuiyunproject.huiyun.ixconfig.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.ixhuiyunproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateTimePickDialogUtil implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Dialog builder;
	private String dateTime;
	private String initDateTime;
	private Activity activity;

	public interface SelectDateTimeCallBack {
		public void isConfirm(String edString);
	}

	SelectDateTimeCallBack selectDateTimeCallBack;

	/**
	 * 
	 */
	public SelectDateTimeCallBack getSelectDateTimeCallBack() {
		return selectDateTimeCallBack;
	}

	public void setSelectDateTimeCallBack(
			SelectDateTimeCallBack selectDateTimeCallBack) {
		this.selectDateTimeCallBack = selectDateTimeCallBack;
	}

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DateTimePickDialogUtil(Activity activity, String initDateTime) {
		activity.setTheme(R.style.Theme_picker);
		this.activity = activity;
		this.initDateTime = initDateTime;

	}

	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}
		timePicker.setIs24HourView(true);
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public Dialog onCreateDialog(
			final SelectDateTimeCallBack selectDateTimeCallBack) {
		final View view = View
				.inflate(activity, R.layout.common_datetime, null);
		TextView title = (TextView) view.findViewById(R.id.tx_title);
		Button condirmBtn = (Button) view.findViewById(R.id.confirmU);
		datePicker = (DatePicker) view.findViewById(R.id.datepicker);
		timePicker = (TimePicker) view.findViewById(R.id.timepicker);
		init(datePicker, timePicker);
		timePicker.setIs24HourView(true);
		// 修改TimePicker字体的大小
		//setNumberPickerTextSize(timePicker);
		// 修改TimePicker中NumberPicker的大小
		//resizeTimerPicker(timePicker);
		timePicker.setOnTimeChangedListener(this);
		builder = new Dialog(activity, R.style.Theme_dialog);
		title.setText("请选择时间");
		builder.setContentView(view);
		setDialogLayoutParams(builder);
		builder.show();
		condirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectDateTimeCallBack != null && dateTime != null) {
					selectDateTimeCallBack.isConfirm(dateTime);
				}
				builder.dismiss();
			}
		});
		onDateChanged(null, 0, 0, 0);
		return builder;
	}

	private void setDialogLayoutParams(Dialog builder) {
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.BOTTOM);
		//lp.width = 1000; // 宽度
		// lp.alpha = 0.7f; // 透明度
		lp.width = LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		//String thisTime = sdf.format(calendar.getTime());
		// ad.setTitle(dateTime);
		dateTime = String.valueOf(calendar.getTimeInMillis());
		//builder.setTitle(thisTime);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
		String time = spliteString(initDateTime, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;

		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}

		return npList;
	}

	private EditText findEditText(NumberPicker np) {
		if (null != np) {
			for (int i = 0; i < np.getChildCount(); i++) {
				View child = np.getChildAt(i);

				if (child instanceof EditText) {
					return (EditText) child;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unused")
	private void setNumberPickerTextSize(ViewGroup viewGroup) {
		List<NumberPicker> npList = findNumberPicker(viewGroup);
		if (null != npList) {
			for (NumberPicker np : npList) {
				EditText et = findEditText(np);
				et.setFocusable(false);
				et.setGravity(Gravity.CENTER);
				et.setTextSize(10);

			}
		}
	}

	@SuppressWarnings("unused")
	private void resizeTimerPicker(TimePicker tp) {
		List<NumberPicker> npList = findNumberPicker(tp);

		for (NumberPicker np : npList) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);
			params.setMargins(10, 0, 10, 0);
			np.setLayoutParams(params);
		}
	}
}
