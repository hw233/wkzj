package com.jtang.core.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 * @author 0x737263
 *
 */
public class TimeUtils {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);
	/**
	 * 获取次日凌晨时间(秒)
	 * @return
	 */
	public static int getEarlyMorning() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		return (int)(now.getTimeInMillis() / 1000);
	}
	
	/**
	 * 是否是今日零点以前
	 * @param time
	 * @return
	 */
	public static boolean beforeTodayZero(int time){
		Calendar specifyTime = Calendar.getInstance();		
		specifyTime.set(Calendar.HOUR_OF_DAY, 0);
		specifyTime.set(Calendar.MINUTE, 0);
		specifyTime.set(Calendar.SECOND, 0);
		specifyTime.set(Calendar.MILLISECOND, 0);//今日零点时间，昨日的终点时间
		if(specifyTime.getTimeInMillis() / 1000 < time){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 获取当前时间( UTC 1970  秒)
	 * @return
	 */
	public static int getNow() {
		return (int)(System.currentTimeMillis() / 1000);
	}
	/**
	 * 获取当前时间( UTC 1970  秒)数组
	 * @return
	 */
	public static byte[] getNowBytes() {
		int now = getNow();
		IoBuffer buf = BufferFactory.getIoBuffer(4, false);
		buf.putInt(now);
		return buf.array();
	}

	/**
	 * 是否在当月时间范围内
	 * 这个月第一天的0:00:00至下个月第一天的0:00:00
	 * @param time  utc时间转换为秒
	 * @return
	 */
	public static boolean inMonth(int time) {
		Calendar zeroTime = Calendar.getInstance();// 本月初零点
		zeroTime.set(Calendar.DAY_OF_MONTH, 1);
		zeroTime.set(Calendar.HOUR_OF_DAY, 0);
		zeroTime.set(Calendar.MINUTE, 0);
		zeroTime.set(Calendar.SECOND, 0);
		zeroTime.set(Calendar.MILLISECOND, 0);
		int monthStartTime = (int) (zeroTime.getTimeInMillis() / 1000);
		zeroTime.add(Calendar.MONTH, 1);// 本月末零点
		int monthEndTime = (int) (zeroTime.getTimeInMillis() / 1000);
		if (monthStartTime < time && time < monthEndTime) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否在昨日时间范围内
	 * @param time	utc时间转换为秒
	 * @return
	 */
	public static boolean inYesterday(int time) {

		Calendar yesterday = Calendar.getInstance();
		yesterday.set(Calendar.DATE, yesterday.get(Calendar.DATE) - 1);
		Calendar compareday = Calendar.getInstance();
		compareday.setTimeInMillis(time * 1000L);
		
		if (yesterday.get(Calendar.YEAR) == compareday.get(Calendar.YEAR)
				&& yesterday.get(Calendar.DAY_OF_MONTH) == compareday.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		
		return false;

	}
	
	/**
	 * 根据当前时间获取一个整点时间
	 * @return	返回utc时间毫秒
	 */
	public static long getNextHourTime() {
		Calendar nowDay = Calendar.getInstance();
		int minute = nowDay.get(Calendar.MINUTE);
		int second = nowDay.get(Calendar.SECOND);
		int millSecond = nowDay.get(Calendar.MILLISECOND);
		if(minute == 0 && second == 0 && millSecond == 0) {
			return nowDay.getTimeInMillis();
		}
		
		nowDay.add(Calendar.HOUR_OF_DAY, 1);
		nowDay.set(Calendar.MINUTE, 0);
		nowDay.set(Calendar.SECOND, 0);
		nowDay.set(Calendar.MILLISECOND, 0);
		
		return nowDay.getTimeInMillis();
	}
	
	/**
	 * 当前几点(24小时制)
	 * @return
	 */
	public static int getHour() {
		Calendar nowDay = Calendar.getInstance();
		return nowDay.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 获取整点时间
	 * @param hour	设置整点(0-23)
	 * @return 返回utc时间毫秒
	 */
	public static long setFixTime(int hour) {
		Calendar currentTime = Calendar.getInstance();
		int curentHour = currentTime.get(Calendar.HOUR_OF_DAY);
		if (curentHour >= hour){
			hour += 24; 
		}
		Calendar nowDay = Calendar.getInstance();
		nowDay.set(Calendar.HOUR_OF_DAY, hour);
		nowDay.set(Calendar.MINUTE, 0);
		nowDay.set(Calendar.SECOND, 0);
		nowDay.set(Calendar.MILLISECOND, 0);
		
		return nowDay.getTimeInMillis();
	}
	
	/**
	 * 获取整点时间
	 * @param hour	设置整点(0-23)
	 * @return 返回utc时间毫秒
	 */
	public static long getTodayFixTime(int hour) {
		Calendar nowDay = Calendar.getInstance();
		nowDay.set(Calendar.HOUR_OF_DAY, hour);
		nowDay.set(Calendar.MINUTE, 0);
		nowDay.set(Calendar.SECOND, 0);
		nowDay.set(Calendar.MILLISECOND, 0);
		
		return nowDay.getTimeInMillis();
	}
	/**
	 * 获取两个时间差值（小时）
	 * @param startSeconds		开始时间
	 * @param endSeconds		结束时间
	 * @return 小时
	 */
	public static int getBetweenHour(int startSeconds, int endSeconds){
		int result = (endSeconds - startSeconds) / 3600;
		return result ;
	}
	
	
	/**
	 * 获取今天的零点
	 */
	public static int getTodayZero(){
		Calendar zeroTime = Calendar.getInstance();
		zeroTime.set(Calendar.HOUR_OF_DAY, 0);
		zeroTime.set(Calendar.MINUTE, 0);
		zeroTime.set(Calendar.SECOND, 0);
		zeroTime.set(Calendar.MILLISECOND, 0);
		return (int) (zeroTime.getTimeInMillis() / 1000);
	}
	/**
	 * 获取昨天的零点
	 */
	public static int getYesterDayZero(){
		Calendar zeroTime = Calendar.getInstance();
		zeroTime.add(Calendar.DAY_OF_MONTH, -1);
		zeroTime.set(Calendar.HOUR_OF_DAY, 0);
		zeroTime.set(Calendar.MINUTE, 0);
		zeroTime.set(Calendar.SECOND, 0);
		zeroTime.set(Calendar.MILLISECOND, 0);
		return (int) (zeroTime.getTimeInMillis() / 1000);
	}

	/**
	 * 获取两个时间间隔多少天
	 * @param now
	 * @param end
	 * @return
	 */
	public static int getBetweenDay(int now, int sendTime) {
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(now * 1000L);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(sendTime * 1000L);
		return Math.abs(start.get(Calendar.DAY_OF_YEAR) - end.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * 获取当前是几月份
	 * @return
	 */
	public static byte getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getNow() * 1000L);
		return (byte) (calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 获取本月有多少天
	 * @return
	 */
	public static byte getMonthOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getNow() * 1000L);
		return (byte) calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 字符串转换为时间(例如8:00)
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static Date string2TodyTime(String source) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = simpleDateFormat.parse(source);
			Calendar sourceDate = Calendar.getInstance();
			sourceDate.setTime(date);
			Calendar nowDate = Calendar.getInstance();
			nowDate.set(Calendar.HOUR_OF_DAY, sourceDate.get(Calendar.HOUR_OF_DAY));
			nowDate.set(Calendar.MINUTE, sourceDate.get(Calendar.MINUTE));
			nowDate.set(Calendar.SECOND, 0);
			nowDate.set(Calendar.MILLISECOND, 0);
			date = nowDate.getTime();
		} catch (ParseException e) {
			LOGGER.error(String.format("时间格式化错误:source:[%s]",  source));
		}
		return date;
	}
	
	/**
	 * 获取一周中某天的开始时间 
	 * @param dayOfWeek 一周中的第几天 [1,7]
	 * @return TimeMillis
	 */
	public static long getThisWeekOnedayStart(int dayOfWeek) {
		if (Calendar.SUNDAY > dayOfWeek || Calendar.SATURDAY < dayOfWeek) {
			return 0L;
		}
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.DAY_OF_WEEK, TimeUtils.getModifyDayOfWeek(dayOfWeek));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒",Locale.CHINA);
//		System.out.println(format.format(cal.getTime()));
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取一周中某天的结束时间 (23:59:59)
	 * @param dayOfWeek 一周中的第几天 [1,7]
	 * @return TimeMillis
	 */
	public static long getThisWeekOnedayEnd(int dayOfWeek) {
		if (Calendar.SUNDAY > dayOfWeek || Calendar.SATURDAY < dayOfWeek) {
			return 0L;
		}
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.DAY_OF_WEEK, TimeUtils.getModifyDayOfWeek(dayOfWeek));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒",Locale.CHINA);
//		System.out.println(format.format(cal.getTime()));
		return cal.getTimeInMillis();
	}
	
	/**
	 *  dayOfWeek按照习惯星期一为1 星期天为7 但是calendar中星期天为1 星期六为7 实际上相当于落后一天.
	 * @param dayOfWeek
	 * @return dayOfWeek % 7 + 1
	 */
	private static int getModifyDayOfWeek(int dayOfWeek) {
		return dayOfWeek % 7 + 1;
	}
	
	
	public static void main(String[] args) {
//		int time = getNow() - 24 *60 *60 * 2;
//		System.out.println(inYesterday(time));
//		
//		int today = getTodayDayOfWeek();
//		int modify = getModifyDayOfWeek(today);
		
		
//		List<Integer> dateList = getTrialCaveGlobalConfig().openDate;
//		List<Integer> dateList = new ArrayList<Integer>();
//		dateList.add(1);
//		dateList.add(7);
//		
//		int today = TimeUtils.getTodayDayOfWeek();
		
//		Calendar cal = Calendar.getInstance(Locale.CHINA);
//		cal.setFirstDayOfWeek(Calendar.MONDAY);
//		int orginDay = cal.get(Calendar.DAY_OF_WEEK);
		
//		int endDay = -1;
//		for (int i = today; i <= 7; i++) {
//			if (dateList.contains(i)) {
//				endDay = i;
//				break;
//			}
//		}
//		long time = TimeUtils.getThisWeekOnedayEnd(endDay);
//		
//		System.out.println(today + "---------" + endDay + "--------" + time);
		
		long fixTime = getTodayFixTime(11);
		System.out.println(DateUtils.date2String(new Date(fixTime), "yyyy-MM-dd HH:mm:ss"));
	}
	

	public static int getTodayDayOfWeek() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek > 1) {
			return dayOfWeek - 1;
		} else {
			return 7;
		}
	}
	/**
	 * 检测指定的时间是否在起始时间和结束时间的时间段内
	 * @param measureTime 	指定的时间点
	 * @param startTime		起始时间
	 * @param endTime		结束时间
	 * @return
	 */
	public static boolean isInSpecifiedPeriodOfTime(long measureTime, long startTime, long endTime) {
		if (startTime < measureTime && measureTime < endTime) {
			return true;
		}
		return false;
	}
	
}
