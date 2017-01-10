package com.ixhuiyunproject.huiyun.ixconfig.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmUtils {

	public Date calendarToDate() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		return date;

	}

	/**
	 * 得到一个日期中取出对应时间和当前的日期
	 * 
	 * 得到今天的时间
	 * 
	 * @param timeLng
	 */
	public static String getTodayTime(Long timeLng) {
		Date date = new Date(timeLng);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		/*
		 * System.out.println(date); System.out.println(cal.get(Calendar.YEAR));
		 * System.out.println(cal.get(Calendar.MONTH) + 1);
		 * System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		 * System.out.println(cal.get(Calendar.HOUR));
		 * System.out.println(cal.get(Calendar.MINUTE));
		 * System.out.println(cal.get(Calendar.SECOND));
		 */

		Date nowDate = new Date(getNowTimeMinuties());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		System.out.println(nowCalendar.get(Calendar.YEAR));
		System.out.println(nowCalendar.get(Calendar.MONTH) + 1);
		System.out.println(nowCalendar.get(Calendar.DAY_OF_MONTH));
		nowCalendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		nowCalendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		nowCalendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		return String.valueOf(nowCalendar.getTimeInMillis());
	}

	/**
	 * 将日期变为Long值
	 * 
	 * @param calendar
	 * @return
	 */
	public static Long calendarToLong(Calendar calendar) {
		long time = calendar.getTimeInMillis();
		return time;

	}

	/**
	 * 将date变为日期
	 * 
	 * @param date
	 * @return
	 */
	public Calendar dateToCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String transferLongToDate(Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	public static long getNowTimeMinuties() {
		return System.currentTimeMillis();
	}

	public static boolean differSetTimeAndNowTime(long setTime) {
		if (setTime >= getNowTimeMinuties()) {
			return true;
		} else {
			return false;
		}
	}

	public static long getDifferMillis(int differDays) {
		return differDays * 24 * 60 * 60 * 1000;
	}

	// 返回String时间 MM/dd/yyyy HH:mm:ss
	public static String stringToData(String strDate) {
		long time = Long.valueOf(strDate);
		SimpleDateFormat sdf = new SimpleDateFormat("MM:dd:yyyy HH:mm:ss");
		System.out.println(sdf.format(new Date(time)));
		return sdf.format(new Date(time));
	}

	public static Long stringToLong(String strDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date data = sdf.parse(strDate);
		Long long1 = data.getTime();
		System.out.println(long1);
		return long1;
	}

	public static Date ConverToDate(String strDate) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return df.parse(strDate);
	}

	public static String getDate(String timer) {
		String year, month, day, hour, minute;
		year = Integer.valueOf(timer.split(" ")[0].split("-")[0]).toString();
		month = String
				.valueOf(Integer.valueOf(timer.split(" ")[0].split("-")[1]) - 1);
		day = Integer.valueOf(timer.split(" ")[0].split("-")[2]).toString();
		hour = Integer.valueOf(timer.split(" ")[1].split(":")[0]).toString();
		minute = Integer.valueOf(timer.split(" ")[1].split(":")[1]).toString();
		return year + month + day + hour + minute;
	}

	public static String stringToDateAndTime(String strDate) {
		long time = Long.valueOf(strDate);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println(sdf.format(new Date(time)));
		return sdf.format(new Date(time));
	}

	public static String stringToTime(String strDate) {
		long time = Long.valueOf(strDate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(new Date(time)));
		return sdf.format(new Date(time));
	}

	public static String stringToDate() {
		long time = Long.valueOf(getNowTimeMinuties());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date data = new Date(time);
		System.out.println(sdf.format(data));
		Long long1 = data.getTime();
		System.out.println(long1);
		return sdf.format(new Date(time));
	}

	public static Calendar longToCalendar(String strTime) {
		Calendar ca = Calendar.getInstance();
		Date d = ca.getTime();
		// long l = ca.getTimeInMillis();
		ca.setTime(d);
		ca.setTimeInMillis(Long.valueOf(strTime));
		return ca;
	}

	/**
	 * 得到一个日期中取出对应时间和当前的日期
	 * 
	 * 得到今天的时间
	 * 
	 * @param timeLng
	 */
	public static String getTodayTime(String timeLng) {
		Long longtime = Long.valueOf(timeLng);
		Date date = new Date(longtime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date nowDate = new Date(getNowTimeMinuties());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		nowCalendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		nowCalendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		return String.valueOf(nowCalendar.getTimeInMillis());
	}

	public static String getTomorrowTime(String timeLng) {
		Long longtime = Long.valueOf(timeLng);
		Date date = new Date(longtime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Date nowDate = new Date(getNowTimeMinuties());
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(nowDate);
		nowCalendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		nowCalendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		nowCalendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
		// 把日期往后增加一天.整数往后推,负数往前移动
		nowCalendar.add(Calendar.DATE, 1);//
		return String.valueOf(nowCalendar.getTimeInMillis());
	}

	/**
	 * Function: 传入得到long
	 * 
	 * @author Yangshao 2015年4月2日 下午1:34:18
	 * @param
	 * @return
	 */
	public static boolean compareNowTime(String time) {
		// 得到传入时间
		String getTime = AlarmUtils.getTimeforLong(time);
		// 得到当前时间
		String strnowtime = getTimeforLong(String.valueOf(getNowTimeMinuties()));
		return compareTime(getTime, strnowtime);
	}

	/**
	 * Function: 比较时间大小
	 * 
	 * @author Yangshao 2015年4月2日 上午11:51:22
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean compareTime(String time1, String time2) {
		if (stringToCalendar(time1) > stringToCalendar(time2)) {
			return true;
		}
		return false;
	}

	/**
	 * @param strTime
	 *            时间格式 HH:mm:ss 得到日期的通用Long类型
	 * @return 转 Calendar
	 */
	public static long stringToCalendar(String strTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date;
		Calendar calendar = Calendar.getInstance();
		try {
			date = sdf.parse(strTime);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}

	/**
	 * Function: 得到时间 HH:mm:ss 格式的时间
	 * 
	 * @author Yangshao 2015年4月2日 下午2:29:52
	 * @param strDate
	 * @return
	 */
	public static String getTimeforLong(String strDate) {
		long time = Long.valueOf(strDate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// System.out.println(sdf.format(new Date(time)));
		return sdf.format(new Date(time));
	}

}
