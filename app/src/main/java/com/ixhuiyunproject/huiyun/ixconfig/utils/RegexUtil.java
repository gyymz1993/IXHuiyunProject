package com.ixhuiyunproject.huiyun.ixconfig.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author busy
 * 
 */
public class RegexUtil {

	public static boolean isEmail(String strEmail) {
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCellphone(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测数字，0个以上的数字
	 * @param number
	 * @return
	 */
	public static boolean checkNumber(String number) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(number);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isText(String str) {

		Pattern p = Pattern.compile("[a-zA-Z]");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			System.out.println("输入是字母");
		}
		@SuppressWarnings("unused")
		Pattern p1 = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m1 = p.matcher(str);
		if (m1.matches()) {
			System.out.println("输入是汉子");
		}
		return false;

	}

	// check email
	public static boolean checkEmail(String email) {
		String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
		return Pattern.matches(regex, email);
	}

	// check mobile
	public static boolean checkMobile(String mobile) {
		String regex = "(\\+\\d+)?1[3458]\\d{9}$";
		return Pattern.matches(regex, mobile);
	}

	// check phone
	public static boolean checkPhone(String phone) {
		String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
		return Pattern.matches(regex, phone);
	}

	// check Integer
	public static boolean checkInteger(String digit) {
		String regex = "\\-?[1-9]\\d+";
		return Pattern.matches(regex, digit);
	}

	// check null
	public static boolean checkBlackSpace(String blankSpace) {
		String regex = "\\s+";
		return Pattern.matches(regex, blankSpace);
	}

	// check chinese
	public static boolean checkChinese(String chinese) {
		String regex = "^[\u4E00-\u9FA5]+$";
		return Pattern.matches(regex, chinese);
	}

	// check date(year,month,day)
	public static boolean checkDate(String date) {
		String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
		return Pattern.matches(regex, date);
	}

	/** check user name(字母开头，允许3-7字符，允许字母数字下划线)
	 * @param name
	 * @return
	 */
	public static boolean checkUserName(String name) {
		String regex = "[a-zA-Z][a-zA-Z0-9_]{2,7}";
		return Pattern.matches(regex, name);
	}

	// check password(6-18字符，区分大小写)
	public static boolean checkPassword(String pswd) {
		String regex = "[a-zA-Z0-9_]{6,18}";
		return Pattern.matches(regex, pswd);
	}

	public static String checkPasswordStrong(String pswd) {
		String tip = "6-18字符，区分大小写";
		if (Pattern.matches("[0-9]{6,18}", pswd)) {
			return "密码强度：弱";
		}
		if (Pattern.matches("[a-zA-Z]{6,18}", pswd)) {
			return "密码强度：弱";
		}
		if (Pattern
				.matches("[~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]{6,18}", pswd)) {
			return "密码强度：弱";
		}
		if (Pattern.matches("[a-zA-Z0-9]{6,18}", pswd)) {
			return "密码强度：中";
		}
		if (Pattern.matches(
				"[a-zA-Z~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]{6,18}", pswd)) {
			return "密码强度：中";
		}
		if (Pattern.matches("[0-9~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]{6,18}",
				pswd)) {
			return "密码强度：中";
		}
		if (Pattern.matches(
				"[a-zA-Z0-9~!/@#$%^&*()-_=+\\|[{}];:\'\",<.>/?]{6,18}", pswd)) {
			return "密码强度：强";
		}
		return tip;

	}

}
