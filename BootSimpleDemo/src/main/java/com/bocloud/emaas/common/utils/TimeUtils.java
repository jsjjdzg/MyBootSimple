package com.bocloud.emaas.common.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * date：2015年8月12日 下午4:42:17 project name：sgcc-devops-common
 * 时间工具类
 * @author langzi
 * @version 1.0
 * @since JDK 1.7.0_21 file name：TimeUtils.java description：
 */
public final class TimeUtils {

	private static Logger logger = Logger.getLogger(TimeUtils.class);

	/**
	 * 格式化日期和时间
	 * 
	 * @author langzi
	 * @param dateTime
	 * @param formatString
	 * @return 2015年8月12日
	 */
	public static String formatTime2String(Date dateTime, String formatString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.format(dateTime);
	}

	/**
	 * 获取前n月的第一天
	 * 
	 * @author langzi
	 * @param n
	 * @return 2015年8月12日
	 */
	public static Date getFirstDayOfMonth(int n) {
		// 获取前月的第一天
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.add(Calendar.MONTH, -n);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal_1.set(Calendar.HOUR_OF_DAY, 0);
		cal_1.set(Calendar.MINUTE, 0);
		cal_1.set(Calendar.SECOND, 0);
		return cal_1.getTime();
	}

	/**
	 * 将时间戳转换成日期和时间
	 * 
	 * @author langzi
	 * @param timestamp
	 * @version 1.0
	 * @return 2015年8月12日
	 */
	public static String Timestamp2DateTime(long timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = format.format(timestamp);
		return dateTime;
	}

	/**
	 * 将日期和时间转换成时间戳
	 * 
	 * @author langzi
	 * @param dateTime
	 * @return
	 * @throws Exception
	 * @version 1.0 2015年8月12日
	 */
	public static long DateTime2Timestamp(String dateTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(dateTime);
		return date.getTime();
	}

	/**
	 * 将日期和时间转换成时间戳
	 * 
	 * @author langzi
	 * @param timestamp
	 * @return
	 * @throws ParseException
	 * @version 1.0 2015年8月12日
	 */
	public static Date Timestamp2Date(long timestamp) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(Timestamp2DateTime(timestamp));
		return date;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @author langzi
	 * @param dateString
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date stringToDate(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			if (dateString != null && dateString.length() != 0) {
				date = format.parse(dateString);
			}
		} catch (ParseException e) {
			logger.error(e);
		}
		return date;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @author langzi
	 * @param dateString
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date stringToDateTime(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			if (dateString != null && dateString.length() != 0) {
				date = format.parse(dateString);
			}
		} catch (ParseException e) {
			logger.error(e);
		}
		return date;
	}

	/**
	 * 格式化日期和时间（特定日期格式）
	 * 
	 * @author langzi
	 * @param dateTime
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static String formatTime(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dateTime);
	}

	/**
	 * 月份转换
	 * 
	 * @author langzi
	 * @param month
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date String2Month(String month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = format.parse(month);
		} catch (ParseException e) {
			logger.error(e);
		}
		return date;
	}

	/**
	 * 保留小数点后两位的小数
	 * 
	 * @author langzi
	 * @param value
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static double Round2(double value) {
		return (double) (Math.round(value * 100) / 100.0);
	}

	/**
	 * 增加几分钟
	 * 
	 * @author langzi
	 * @param date
	 * @param minute
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date AddMinuteForDate(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * 增加几个月
	 * 
	 * @author langzi
	 * @param date
	 * @param month
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date AddMonthForDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * md5加密算法
	 * 
	 * @author langzi
	 * @param pwd
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public final static String MD5(String pwd) {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
			byte[] btInput = pwd.getBytes();

			// 获得指定摘要算法的 MessageDigest对象，此处为MD5
			// MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
			mdInst.update(btInput);

			// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) { // i = 0
				byte byte0 = md[i]; // 95
				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
				str[k++] = md5String[byte0 & 0xf]; // F
			}

			// 返回经过加密后的字符串
			return new String(str);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	/**
	 * 计算使用时间
	 * 
	 * @author langzi
	 * @param createDate
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static String dateToUsed(Date createDate) {
		Date current = new Date();
		long elapse = current.getTime() - createDate.getTime();
		long day = elapse / (24 * 60 * 60 * 1000);
		long hour = (elapse / (60 * 60 * 1000) - day * 24);
		long min = ((elapse / (60 * 1000)) - day * 24 * 60 - hour * 60);
		if (day > 0) {
			return day + " 天";
		} else if (hour > 0) {
			return hour + " 小时";
		} else if (min > 0) {
			return min + " 分钟";
		} else {
			return "<１分钟";
		}
	}

	/**
	 * 计算使用时间
	 * 
	 * @author langzi
	 * @param createDate
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static String timeToUsed(Date createDate) {
		Date currnt = new Date();
		double elapse = currnt.getTime() - createDate.getTime();
		return (long) Math.ceil(elapse / (60 * 1000 * 60 * 24)) + "天";
	}

	/**
	 * 计算使用时间
	 * 
	 * @author langzi
	 * @param createDate
	 * @param endDate
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static String timeToUsed(Date createDate, Date endDate) {
		double elapse = endDate.getTime() - createDate.getTime();
		return (long) Math.ceil(elapse / (60 * 1000 * 60 * 24)) + "天";
	}

	/**
	 * 计算时间间隔
	 * 
	 * @author langzi
	 * @param startTime
	 * @param endTime
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static int timeElapse(Date startTime, Date endTime) {
		long elapse = endTime.getTime() - startTime.getTime();
		int elapseInt = 0;
		if (elapse > 0) {
			elapseInt = (int) elapse / 1000;
		}
		return elapseInt;
	}

	/**
	 * 使用多长时间（分钟）
	 * 
	 * @author langzi
	 * @param createDate
	 * @param endDate
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static long timeToUsedMin(Date createDate, Date endDate) {
		long elapse = endDate.getTime() - createDate.getTime();
		return elapse / (60 * 1000);
	}

	/**
	 * 使用多少天
	 * 
	 * @author langzi
	 * @param createDate
	 * @param endDate
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static long timeToUsedDay(Date createDate, Date endDate) {
		double elapse = endDate.getTime() - createDate.getTime();
		return (long) Math.ceil(elapse / (60 * 1000 * 60 * 24));
	}

	/**
	 * 日期加和
	 * 
	 * @author langzi
	 * @param currentDay
	 * @param day
	 * @return
	 * @version 1.0 2015年8月12日
	 */
	public static Date daySub(int currentDay, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(currentDay, day);
		Date startDate = calendar.getTime();
		return startDate;
	}

	/**
	 * 日期差
	 * 
	 * @param date1
	 *            <Date>
	 * @param date2
	 *            <Date>
	 * @param type
	 *            <int> 0月 1年
	 * @return
	 */
	public static int getDateSpace(Date date1, Date date2, int type) {
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		if (type == 0) {
			result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH)
					+ (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;
		} else if (type == 1) {
			result = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		}
		return result == 0 ? 1 : Math.abs(result);
	}

	/**
	 * 日期加天
	 * 
	 * @param date
	 *            <Date>
	 * @param day
	 *            <int>
	 * @return
	 */
	public static Date AddDayForDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static String stickyToInfo(String message) {
		// TODO Auto-generated method stub
		return null;
	}

}
