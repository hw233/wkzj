package com.jtang.gameserver.module.demon.model;

import java.util.Calendar;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 开放时间
 * @author ludd
 *
 */
public class OpenTime extends IoBufferSerializer {
	
	/**
	 * 开始小时
	 */
	public int startH;
	/**
	 * 开始分钟
	 */
	public int startM;
	/**
	 * 结束小时
	 */
	public int endH;
	/**
	 * 结束分钟
	 */
	public int endM;
	
	public OpenTime(String[] record) {
		String start = record[0] ;
		String end = record[1];
		
		String[] starts = StringUtils.split(start, Splitable.DELIMITER_ARGS);
		String[] ends = StringUtils.split(end, Splitable.DELIMITER_ARGS);
		startH = Integer.valueOf(starts[0]);
		startM = Integer.valueOf(starts[1]);
		endH = Integer.valueOf(ends[0]);
	    endM = Integer.valueOf(ends[1]);
	}
	
	
	/**
	 * 是否开始
	 * @return
	 */
	public boolean isStart() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		
		c.set(Calendar.HOUR_OF_DAY, startH);
		c.set(Calendar.MINUTE, startM);
		long start = c.getTimeInMillis();
		
		c.set(Calendar.HOUR_OF_DAY, endH);
		c.set(Calendar.MINUTE, endM);
		long end = c.getTimeInMillis();
		
		if (start >= end) {
			return false;
		}
		if (start <= now && now < end) {
			return true;
		}
		return false;
	}
	
	public int getStartSeconds() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, startH);
		c.set(Calendar.MINUTE, startM);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Long start = c.getTimeInMillis() / 1000;
		return start.intValue();
	}


	@Override
	public void write() {
		this.writeInt(startH);
		this.writeInt(startM);
		this.writeInt(endH);
		this.writeInt(endM);
	}
	
	
}