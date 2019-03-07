package com.jtang.gameserver.server.session;

import java.util.UUID;

import com.jtang.core.utility.TimeConstant;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;

public class UserReconnectData {
	
	/**
	 * 7天
	 */
	public static final int TIME_OUT = TimeConstant.ONE_MINUTE_SECOND * 60 * 24 * 7;
	
	/**
	 * 重连id
	 */
	private String reconnectId;
	
	/**
	 * 上次登录时间
	 */
	private int time;
	
	/**
	 * 平台id
	 */
	private int platformId;
	
	private PlatformLoginResult platformLoginResult;
	public UserReconnectData(int platformId, PlatformLoginResult platformLoginResult) {
		super();
		this.time = TimeUtils.getNow();
		this.platformId = platformId;
		this.platformLoginResult = platformLoginResult;
		
		this.reconnectId = UUID.randomUUID().toString();
	}
	public int getTime() {
		return time;
	}
	
	public String getReconnectId() {
		return reconnectId;
	}
	public int getPlatformId() {
		return platformId;
	}
	public String getUid() {
		return platformLoginResult.uid;
	}
	
	public boolean isTimeOut() {
		return TimeUtils.getNow() - this.time > TIME_OUT;
	}
	
	public PlatformLoginResult getPlatformLoginResult() {
		return platformLoginResult;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	
	
	
}
