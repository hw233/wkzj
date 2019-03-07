package com.jiatang.common.crossbattle.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class CrossBattleConfigW2G extends IoBufferSerializer {
	
	/**
	 * 开始日期 yyyy-MM-dd
	 */
	public String startDate;

	/**
	 * 结束日期 yyyy-MM-dd
	 */
	public String endDate;

	/**
	 * 每日比赛开始时间 HH:mm
	 */
	public String startTime;

	/**
	 * 每日报名时间 HH:mm
	 */
	public String signupTime;

	/**
	 * 每日比赛结束时间 HH:mm
	 */
	public String endTime;
	
	/**
	 * 对阵双方serverId
	 */
	public Map<Integer, Integer> serverIdMap;
	
	/**
	 *   上次奖励信息是否已读
	 *   0：未读
	 *   1：已读
	 */
	public byte readFlag;
	
	

	public CrossBattleConfigW2G(String startDate, String endDate,
			String startTime, String signupTime, String endTime, Map<Integer, Integer> serverIdMap, byte readFlag) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.signupTime = signupTime;
		this.endTime = endTime;
		this.serverIdMap = serverIdMap;
		this.readFlag = readFlag;
	}

	public CrossBattleConfigW2G(byte[] bytes) {
		super(bytes);
	}

	@Override
	public void write() {
		writeString(startDate);
		writeString(endDate);
		writeString(startTime);
		writeString(signupTime);
		writeString(endTime);
		this.writeIntMap(this.serverIdMap);
		this.writeByte(this.readFlag);
	}
	
	@Override
	protected void read() {
		this.startDate = readString();
		this.endDate = readString();
		this.startTime = readString();
		this.signupTime = readString();
		this.endTime = readString();
		this.serverIdMap = readIntMap();
		this.readFlag = readByte();
	}
	
	

}
