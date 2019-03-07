package com.jtang.gameserver.module.battle.helper;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于性能统计
 * @author vinceruan
 *
 */
public class Clock {
	private static final Logger LOGGER = LoggerFactory.getLogger(Clock.class);
	private static Stack<ClockItem> stack = new Stack<>();
	
	static class ClockItem{
		public String name;
		public long startTime;
	}
	
	/**
	 * 开始记录一个操作
	 * @param name
	 */
	public static void start(String name) {
		ClockItem item = new ClockItem();
		item.name = name;
		item.startTime = System.currentTimeMillis();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(Thread.currentThread().getName()+"--> "+name + " 开始");	
		}
		stack.push(item);
	}
	
	/**
	 * 结束记录一个 操作
	 */
	public static void end() {
		long endTime = System.currentTimeMillis();
		ClockItem item = stack.pop();
		long interval = endTime - item.startTime;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(Thread.currentThread().getName() + "--> " + item.name + " 结束,耗时:" + interval);
		}
	}
}
