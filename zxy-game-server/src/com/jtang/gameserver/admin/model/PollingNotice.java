package com.jtang.gameserver.admin.model;

import java.util.List;

import com.jtang.core.utility.TimeUtils;

public class PollingNotice {
	/**
	 * 当前轮询次数
	 */
	private int currentPollingTimes;
	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 总共轮询次数
	 */
	private int pollingTimes;
	
	/**
	 * 间隔时间
	 */
	private int delayTime;
	
	/**
	 * 上次轮询时间
	 */
	private int lastPollingTime;
	
	/**
	 * 平台列表
	 */
	private List<Integer> channelIds;
	
	public PollingNotice() {
	}

	public PollingNotice(String message, int pollingTimes, int delayTime, List<Integer> channelIds) {
		super();
		this.message = message;
		this.pollingTimes = pollingTimes;
		this.delayTime = delayTime;
		this.channelIds = channelIds;
	}
	
	public int polling(){
		if (pollingTimes == -1){
			return -1;
		}
		currentPollingTimes++;
		int result = pollingTimes - currentPollingTimes;
		if (result > 0) {
			return result;
		} else {
			currentPollingTimes = pollingTimes;
			return 0;
		}
		
	}
	
	public String getMessage(){
		return this.message;
	}

	public int getPollingTimes() {
		return pollingTimes;
	}

	public int getDelayTime() {
		return delayTime;
	}
	
	public boolean isTimeOut(){
		return (lastPollingTime + delayTime - TimeUtils.getNow()) <= 0;
	}

	public void setLastPollingTime(int lastPollingTime) {
		this.lastPollingTime = lastPollingTime;
	}

	public List<Integer> getChannelIds() {
		return channelIds;
	}
}
