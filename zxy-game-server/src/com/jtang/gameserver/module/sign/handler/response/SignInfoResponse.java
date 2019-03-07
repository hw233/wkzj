package com.jtang.gameserver.module.sign.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.entity.Sign;

public class SignInfoResponse extends IoBufferSerializer {

	/**
	 * 普通签到累计天数
	 */
	public byte sign;
	
	/**
	 * 是否已签到
	 * 0.未签到
	 * 1.已签到
	 */
	public byte isSign;
	
	/**
	 * vip签到天数
	 */
	public byte vipSign;
	
	/**
	 * vip签到状态
	 * 0.不可签到
	 * 1.可签到
	 * 2.已签到 
	 */
	public byte vipSignState;
	
	/**
	 * 月份
	 */
	public byte month;
	
	/**
	 * 本月天数
	 */
	public byte monthOfDay;
	
	public SignInfoResponse(Sign sign){
		this.sign = sign.day;
		this.isSign = (byte) sign.isSign;
		this.vipSign = sign.vipDay;
		this.vipSignState = (byte) sign.vipSignState;
		this.month = TimeUtils.getMonth();
		this.monthOfDay = TimeUtils.getMonthOfDay();
	}
	
	@Override
	public void write() {
		writeByte(sign);
		writeByte(isSign);
		writeByte(vipSign);
		writeByte(vipSignState);
		writeByte(month);
		writeByte(monthOfDay);
	}
}
