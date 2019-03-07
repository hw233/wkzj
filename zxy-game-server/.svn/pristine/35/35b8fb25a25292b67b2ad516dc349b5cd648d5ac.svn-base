package com.jtang.gameserver.module.extapp.monthcard.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.MonthCard;

public class MonthCardResponse extends IoBufferSerializer {

	/**
	 * 剩余领取天数
	 */
	public int day;
	
	/**
	 * 是否已领首次奖励
	 * 0.未领 1.可领 2.已领
	 */
	public byte isFirstReward;
	
	/**
	 * 今天奖励是否已领
	 * 0.不可领 1.可领 2.已领
	 */
	public byte isDayReward;
	
	/**
	 * 是否终身卡
	 * 0.不是 1.是
	 */
	public byte isLifelongCard;
	
	/**
	 * 终身卡奖励是否已领
	 * 0.不可领 1.可领 2.已领
	 */
	public byte lifelongDayReward;
	
	/**
	 * 首次充值奖励是否已领取
	 * 0.未领取 1.已领取
	 */
	public byte firstRecharge;
	
	/**
	 * 年卡剩余天数
	 */
	public int yearCardDay;
	
	/**
	 * 年卡奖励是否已领取
	 * 0.未领取 1.可领取 2.已领取
	 */
	public byte yearCardDayReward;
	
	/**
	 * 首冲奖励是否已发放
	 * 0.未发放 1.已领取
	 */
	public byte yearCardFrast;
	
	/**
	 * @param monthCard
	 * @param rechargeNum 充值次数
	 */
	public MonthCardResponse(MonthCard monthCard,int rechargeNum){
		this.day = monthCard.day;
		this.isFirstReward = monthCard.isFirsetReward;
		this.isDayReward = monthCard.isDayReward;
		this.isLifelongCard = monthCard.lifelongFirsetReward;
		this.lifelongDayReward = monthCard.lifelongDayReward;
		this.firstRecharge = (byte) (rechargeNum >= 1 ? 1:0);
		this.yearCardDay = monthCard.yearCardDay;
		this.yearCardDayReward = (byte) monthCard.yearCardDayReward;
		this.yearCardFrast = (byte) monthCard.yearCardFrast;
	}
	
	@Override
	public void write() {
		writeInt(day);
		writeByte(isFirstReward);
		writeByte(isDayReward);
		writeByte(isLifelongCard);
		writeByte(lifelongDayReward);
		writeByte(firstRecharge);
		writeInt(yearCardDay);
		writeByte(yearCardDayReward);
		writeByte(yearCardFrast);
	}
}
