package com.jtang.gameserver.module.user.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dbproxy.entity.UserDisabled;

public class UserDisabledResponse extends IoBufferSerializer {
	
	/**
	 * 封号类型
	 */
	public int disableType;
	
	/**
	 * 封号开始时间
	 */
	public String startTime;
	
	/**
	 * 封号至多久
	 */
	public String endTime;
	
	
	public UserDisabledResponse(UserDisabled userDisabled){
		this.disableType = userDisabled.disabledType;
		this.startTime = DateUtils.formatTime(userDisabled.beginTime);
		this.endTime = DateUtils.formatTime(userDisabled.endTime);
	}
	

	@Override
	public void write() {
		writeInt(disableType);
		writeString(startTime);
		writeString(endTime);
	}

}
