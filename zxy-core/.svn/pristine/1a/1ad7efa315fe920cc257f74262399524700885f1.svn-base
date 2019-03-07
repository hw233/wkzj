package com.jtang.core.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeConstant;
import com.jtang.core.utility.TimeUtils;

/**
 * spring调度封装
 * @author 0x737263
 *
 */
@Component
public class Schedule {

	@Autowired
	TaskScheduler taskScheduler;
	
	/**
	 * 每x毫秒钟执行（如果时间已过立即执行一次）
	 * @param task 任务
	 * @param startSeconds 执行周期（毫秒）
	 */
	public void addEveryMillisecond(Runnable task, long startSeconds){
		taskScheduler.scheduleAtFixedRate(task, startSeconds);
	}
	
	/**
	 * 每x秒钟执行（如果时间已过立即执行一次）
	 * @param task 任务
	 * @param startSeconds 执行周期（秒）
	 */
	public void addEverySecond(Runnable task, int startSeconds){
		int millSeconds = startSeconds * TimeConstant.ONE_SECOND_MILLISECOND;
		Date startDate = DateUtils.getDelayDate(millSeconds, TimeUnit.MILLISECONDS);
		taskScheduler.scheduleWithFixedDelay(task, startDate, millSeconds);
	}
	
	/**
	 * 每x分钟执行 （如果时间已过立即执行一次）
	 * @param task			runnable对象
	 * @param startMinute	执行周期时间(分钟)
	 */
	public void addEveryMinute(Runnable task, int startMinute) {
		int millSeconds = startMinute * 60 * TimeConstant.ONE_SECOND_MILLISECOND;
		Date startDate = DateUtils.getDelayDate(millSeconds, TimeUnit.MILLISECONDS);
		taskScheduler.scheduleWithFixedDelay(task, startDate, millSeconds);
	}
	
	/**
	 * 每x分钟00秒执行 （如果时间已过立即执行一次）
	 * @param task			runnable对象
	 * @param startMinute	执行周期时间(分钟)
	 */
	public void addRoundMinute(Runnable task, int startMinute) {
		int millSeconds = startMinute * 60 * TimeConstant.ONE_SECOND_MILLISECOND;
		Date startDate = DateUtils.getRoundDelayDate(TimeUnit.MINUTES);
		taskScheduler.scheduleAtFixedRate(task, startDate, millSeconds);
	}
		
	/**
	 * 每小时整点触发(每天24次） 重复执行
	 * @param task	任务
	 */
	public void addEveryHour(Runnable task) {
		// TODO 延时增加500ms
		long oneHourMillisecond = TimeConstant.ONE_HOUR_MILLISECOND + 500;
		Date startDate = new Date(TimeUtils.getNextHourTime());
		taskScheduler.scheduleWithFixedDelay(task, startDate, oneHourMillisecond);
	}
	
	/**
	 * 
	 * 每天x点执行.(每天一次) （如果时间已过立即执行一次），然后延迟一天， 重复执行
	 * @param task
	 * @param hour  1-24 小时定时执行
	 */
	public void addFixedTime(Runnable task, int hour) {
		if (hour == 0) {
			hour = 24;
		}
		long oneDayMillisecond = TimeConstant.ONE_DAY_MILLISECOND + 500;
		Date startDate = new Date(TimeUtils.setFixTime(hour)); // 获取整点小时的时间
		taskScheduler.scheduleWithFixedDelay(task, startDate, oneDayMillisecond);
	}
	/**
	 * 每天x点执行.(每天一次) （如果时间已过立即执行一次），然后延迟一天， 重复执行
	 * @param task
	 * @param hour
	 * @param minutes
	 * @param seconds
	 */
	public void addFixedTime(Runnable task, int hour, int minutes, int seconds) {
		if (hour == 0) {
			hour = 24;
		}
		long oneDayMillisecond = TimeConstant.ONE_DAY_MILLISECOND;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minutes);
		c.set(Calendar.SECOND, seconds);
		Date startDate = c.getTime(); 
		taskScheduler.scheduleWithFixedDelay(task, startDate, oneDayMillisecond);
	}
	
	/**
	 * 延迟执行 
	 * @param task 任务
	 * @param seconds 延迟时间(秒)
	 */
	public void addDelaySeconds(Runnable task, int seconds){
		long millSeconds = TimeUnit.SECONDS.toMillis(seconds);
		taskScheduler.schedule(task, new Date(System.currentTimeMillis() + millSeconds));
		
	}
	
}
