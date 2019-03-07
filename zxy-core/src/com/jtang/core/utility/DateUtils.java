package com.jtang.core.utility;

//import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 * 
 * @author 0x737263
 * 
 */
public class DateUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
	/** 当String时间超过2038-01-19 11:14:07时,long(毫秒)转换为int(秒)会溢出.*/
	public static final long TIME_MAX_VALUE = Integer.MAX_VALUE * 1000L;

	/**
	 * 是不否为今天
	 * 
	 * @param second
	 *            utc转换忧的秒
	 * @return
	 */
	public static boolean isToday(int second) {
		Calendar today = Calendar.getInstance();
		Calendar compareday = Calendar.getInstance();
		compareday.setTimeInMillis(second * 1000L);
		
		if (today.get(Calendar.YEAR) == compareday.get(Calendar.YEAR)
				&& today.get(Calendar.DAY_OF_MONTH) == compareday.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		
		return false;
	}

	/**
	 * 获取今天指定时间的Date
	 * 
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @param millSenconds
	 * @return
	 */
	public static Calendar getSpecialTimeOfToday(int hour, int minute, int seconds, int millSenconds) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.MILLISECOND, millSenconds);
		return cal;
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param theDate
	 *            日期
	 * @param datePattern
	 *            {@code DatePattern}
	 * @return
	 */
	public static String date2String(Date theDate, String datePattern) {
		if (theDate == null) {
			return "";
		}

		DateFormat format = new SimpleDateFormat(datePattern);
		try {
			return format.format(theDate);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return "";
	}

	/**
	 * 字符串转换为Date对象
	 * 
	 * @param dateString
	 *            日期字符串
	 * @param datePattern
	 *            {@code DatePattern}
	 * @return
	 */
	public static Date string2Date(String dateString, String datePattern) {
		if ((dateString == null) || (dateString.trim().isEmpty())) {
			return null;
		}

		DateFormat format = new SimpleDateFormat(datePattern);
		try {
			Date date = format.parse(dateString);
			if(date.getTime() > TIME_MAX_VALUE){
				date.setTime(TIME_MAX_VALUE);
				return date;
			}
			return date;
		} catch (ParseException e) {
			LOGGER.error("ParseException in converting string to date: " + e.getMessage());
		}

		return null;
	}

	// /**
	// * long[]类型的秒 转换为总毫耗数
	// * @param seconds 秒数组
	// * @return
	// */
	// public static long toMillisSecond(long[] seconds) {
	// long millis = 0L;
	// if ((seconds != null) && (seconds.length > 0)) {
	// long[] arrayOfLong = seconds;
	// int j = seconds.length;
	// for (int i = 0; i < j; ++i) {
	// long time = arrayOfLong[i];
	// millis += time * 1000L;
	// }
	//
	// }
	// return millis;
	// }

	/**
	 * 毫秒转换为秒
	 * 
	 * @param millis
	 *            毫秒
	 * @return
	 */
	public static long toSecond(long... millis) {
		long second = 0L;
		if ((millis != null) && (millis.length > 0)) {
			long[] arrayOfLong = millis;
			int j = millis.length;
			for (int i = 0; i < j; ++i) {
				long time = arrayOfLong[i];
				second += time / 1000L;
			}

		}
		return second;
	}

	// /**
	// * 日期修改
	// * @param theDate 日期
	// * @param addDays 增加天数
	// * @param hour 设置小时
	// * @param minute 设置分
	// * @param second 设置秒
	// * @return
	// */
	// public static Date changeDateTime(Date theDate, int addDays, int hour,
	// int minute, int second) {
	// if (theDate == null) {
	// return null;
	// }
	//
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(theDate);
	//
	// cal.add(5, addDays);
	//
	// if ((hour >= 0) && (hour <= 24)) {
	// cal.set(11, hour);
	// }
	// if ((minute >= 0) && (minute <= 60)) {
	// cal.set(12, minute);
	// }
	// if ((second >= 0) && (second <= 60)) {
	// cal.set(13, second);
	// }
	//
	// return cal.getTime();
	// }

	/**
	 * 日期修改
	 * 
	 * @param theDate
	 *            日期
	 * @param addHours
	 *            增加天
	 * @param addMinutes
	 *            增加分
	 * @param addSecond
	 *            增加秒
	 * @return
	 */
	public static Date add(Date theDate, int addHours, int addMinutes, int addSecond) {
		if (theDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.add(11, addHours);
		cal.add(12, addMinutes);
		cal.add(13, addSecond);

		return cal.getTime();
	}

	// /**
	// * 是否为本周的日期
	// * @param theDate 日期
	// * @return
	// */
	// public static int dayOfWeek(Date theDate) {
	// if (theDate == null) {
	// return -1;
	// }
	//
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(theDate);
	//
	// return cal.get(7);
	// }

	// /**
	// * 日期是否为凌晨
	// * @param theDate
	// * @return
	// */
	// public static Date getDate0AM(Date theDate) {
	// if (theDate == null) {
	// return null;
	// }
	//
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(theDate);
	// return new GregorianCalendar(cal.get(1), cal.get(2),
	// cal.get(5)).getTime();
	// }

	/**
	 * 根据当前时间，获取下一天的零点
	 * 
	 * @return
	 */
	public static Date getNextDay0AM() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime() + 86400000L);
		return new GregorianCalendar(cal.get(1), cal.get(2), cal.get(5)).getTime();
	}

	// public static Date getThisDay2359PM(Date theDate) {
	// if (theDate == null) {
	// return null;
	// }
	//
	// Calendar cal = Calendar.getInstance();
	// long millis = theDate.getTime() + 86400000L - 1000L;
	// cal.setTimeInMillis(millis);
	// Date date = new GregorianCalendar(cal.get(1), cal.get(2),
	// cal.get(5)).getTime();
	// return new Date(date.getTime() - 1000L);
	// }

	// public static int calc2DateTDOADays(Date startDate, Date endDate) {
	// if ((startDate == null) || (endDate == null)) {
	// return 0;
	// }
	// Date startDate0AM = getDate0AM(startDate);
	// Date endDate0AM = getDate0AM(endDate);
	// long v1 = startDate0AM.getTime() - endDate0AM.getTime();
	// return Math.abs((int) divideAndRoundUp(v1, 86400000.0D, 0));
	// }

	// public static Date getNextMonday(Date date) {
	// if (date == null) {
	// return null;
	// }
	//
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(getDate0AM(date));
	// cal.set(7, 2);
	//
	// Calendar nextMondayCal = Calendar.getInstance();
	// nextMondayCal.setTimeInMillis(cal.getTimeInMillis() + 604800000L);
	// return nextMondayCal.getTime();
	// }

	// public static Date add(int addDay, boolean to0AM) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.add(5, addDay);
	// Date time = calendar.getTime();
	// return (to0AM) ? getDate0AM(time) : time;
	// }

	/**
	 * 当前时间转换为秒
	 * 
	 * @return
	 */
	public static long getCurrentSecond() {
		return System.currentTimeMillis() * 1000; // 转换为秒
	}

	// private static double divideAndRoundUp(double v1, double v2, int scale) {
	// if (scale < 0) {
	// throw new
	// IllegalArgumentException("The scale must be a positive integer or zero");
	// }
	//
	// BigDecimal bd1 = new BigDecimal(v1);
	// BigDecimal bd2 = new BigDecimal(v2);
	//
	// return bd1.divide(bd2, scale, 0).doubleValue();
	// }

	/**
	 * 判断到目前为止是否超过了指定的时间区间
	 * 
	 * @param startTime
	 * @param timeInterval
	 * @return
	 */
	public static boolean beyondTheTime(long startTime, int timeInterval) {
		return TimeUtils.getNow() - startTime >= timeInterval;
	}

	/**
	 * 判断是否在时间区间内
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isActiveTime(int startTime, int endTime) {
		int now = TimeUtils.getNow();
		return startTime < now && now < endTime;
	}

	/**
	 * 判断是否应该触发每日事件(例如次数重置事件)
	 * 
	 * @param hour
	 *            事件应该被触发的时间,即配置时间(24小时制)
	 * @param lastOccurTime
	 *            最近一次实际触发的时间,单位：秒
	 * @return
	 */
	public static boolean isTime4DailyEvent(int hour, int lastOccurTime) {
		long secondes = lastOccurTime;
		// 取得今天的触发时间
		Calendar checkDate = DateUtils.getSpecialTimeOfToday(hour, 0, 0, 0);
		Calendar now = Calendar.getInstance();

		// 如果今天的触发时间未到,则取过去最近一次的可触发时间
		if (now.before(checkDate)) {
			checkDate.add(Calendar.DAY_OF_YEAR, -1);
		}

		// 如果过去最近一次触发时间事件未被触发,则判断为可触发
		if (new Date(secondes * 1000).before(checkDate.getTime())) {
			return true;
		}
		return false;
	}

	public static int getNowInSecondes() {
		return (int) (new Date().getTime() / 1000);
	}

	/**
	 * @param delay
	 * @param timeUnit
	 * @return
	 */
	public static Date getDelayDate(int delay, TimeUnit timeUnit) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		switch (timeUnit) {
		case DAYS:
			c.add(Calendar.DAY_OF_YEAR, delay);
			break;
		case HOURS:
			c.add(Calendar.HOUR_OF_DAY, delay);
			break;
		case MILLISECONDS:
			c.add(Calendar.MILLISECOND, delay);
			break;
		case MINUTES:
			c.add(Calendar.MINUTE, delay);
			break;
		case SECONDS:
			c.add(Calendar.SECOND, delay);
			break;
		default:
			LOGGER.warn(String.format("IllegalArgumentException:{%s}", timeUnit.toString()));
			break;
		}
		return c.getTime();
	}
	
	/**
	 * 获取timeUnit字段指定的整数时间
	 * example : delay 指定延时周期, timeUnit为MINUTE 那么每分钟00秒
	 * @param delay 延时周期
	 * @param timeUnit 时间字段
	 * @return
	 */
	public static Date getRoundDelayDate(TimeUnit timeUnit) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.set(Calendar.MILLISECOND, 0);
		switch (timeUnit) {
		case HOURS:
			c.set(Calendar.HOUR_OF_DAY, 0);
			break;
		case MINUTES:
			c.add(Calendar.MINUTE, 1);
			c.set(Calendar.SECOND, 0);
			break;
		case SECONDS:
			c.set(Calendar.SECOND, 0);
			break;
		default:
			LOGGER.warn(String.format("IllegalArgumentException:{%s}", timeUnit.toString()));
			break;
		}
		return c.getTime();
	}

	/**
	 * 到零晨还剩余多少秒
	 * 
	 * @return
	 */
	public static int residueSecond2NextDay0AM() {
		return (int) (getNextDay0AM().getTime() - new Date().getTime()) / 1000;
	}

	/**
	 * 格式化date时间类型为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	/**
	 * 当前时间是否超过了指定时间
	 * 
	 * @param endTime
	 * @return
	 */
	public static boolean isExceedTime(Date endTime) {
		Date now = new Date();
		return endTime.getTime() < now.getTime();
	}
	

	/**
	 * 两个日期相差几天(按天数单位来比较)
	 * @param beginDate		开始日期
	 * @param endDate		结束日期
	 * @return
	 */
	public static int getRemainDays(Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);

		int days = endCalendar.get(Calendar.DAY_OF_YEAR) - beginCalendar.get(Calendar.DAY_OF_YEAR);

		int diffYears = endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR);
		if (diffYears > 0) {
			days += diffYears * 365;
		}
		return days;
	}
	
	public static void main(String[] args) {
		int millSeconds = 1;
		Date date = DateUtils.getRoundDelayDate(TimeUnit.MINUTES);
		System.out.println(DateUtils.formatTime(new Date()));
		System.out.println(DateUtils.formatTime(date));
	}

}