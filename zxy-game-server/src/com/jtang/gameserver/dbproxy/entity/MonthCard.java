package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;

@TableName(name="monthCard", type = DBQueueType.IMPORTANT)
public class MonthCard extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2323354800163496543L;
	
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 剩余领取天数
	 */
	@Column
	public int day;
	
	/**
	 * 首冲月卡奖励是否已发放
	 * 0.未充值
	 * 1.已发放
	 */
	@Column
	public byte isFirsetReward;
	
	/**
	 * 今天奖励是否已领取
	 * 0.不可领取
	 * 1.可领取
	 * 2.已领取
	 */
	@Column
	public byte isDayReward;
	
	/**
	 * 终身卡奖励是否已发放
	 * 0.未充值
	 * 1.已发放
	 */
	@Column
	public byte lifelongFirsetReward;
	
	/**
	 * 终身卡今天奖励是否已领取
	 * 0.不可领取
	 * 1.可领取
	 * 2.已领取
	 */
	@Column
	public byte lifelongDayReward;
	
	/**
	 * 领取时间
	 */
	@Column
	public int getTime;
	
	/**
	 * 终身卡领取时间
	 */
	@Column
	public int lifelongGetTime;
	
	/**
	 * 年卡剩余天数
	 */
	@Column
	public int yearCardDay;
	
	/**
	 * 年卡充值奖励是否已发放
	 * 0.未充值 1.已发放
	 */
	@Column
	public int yearCardFrast;
	
	/**
	 * 年卡当天奖励是否已领取
	 * 0.不可领取
	 * 1.可领取
	 * 2.已领取
	 */
	@Column
	public int yearCardDayReward;
	
	/**
	 * 年卡领取时间
	 */
	@Column
	public int yearCardTime;

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		MonthCard monthCard = new MonthCard();
		monthCard.actorId = rs.getLong("actorId");
		monthCard.day = rs.getInt("day");
		monthCard.isFirsetReward = rs.getByte("isFirsetReward");
		monthCard.isDayReward = rs.getByte("isDayReward");
		monthCard.lifelongFirsetReward = rs.getByte("lifelongFirsetReward");
		monthCard.lifelongDayReward = rs.getByte("lifelongDayReward");
		monthCard.getTime = rs.getInt("getTime");
		monthCard.lifelongGetTime = rs.getInt("lifelongGetTime");
		monthCard.yearCardDay = rs.getInt("yearCardDay");
		monthCard.yearCardFrast = rs.getInt("yearCardFrast");
		monthCard.yearCardDayReward = rs.getInt("yearCardDayReward");
		monthCard.yearCardTime = rs.getInt("yearCardTime");
		return monthCard;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(day);
		values.add(isFirsetReward);
		values.add(isDayReward);
		values.add(lifelongFirsetReward);
		values.add(lifelongDayReward);
		values.add(getTime);
		values.add(lifelongGetTime);
		values.add(yearCardDay);
		values.add(yearCardFrast);
		values.add(yearCardDayReward);
		values.add(yearCardTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	@Override
	protected void disposeBlob() {
		
	}

	public static MonthCard valueOf(long actorId) {
		MonthCard monthCard = new MonthCard();
		monthCard.actorId = actorId;
		monthCard.day = 0;
		monthCard.isDayReward = 0;
		monthCard.isFirsetReward = 0;
		monthCard.lifelongFirsetReward = 0;
		monthCard.lifelongDayReward = 0;
		monthCard.getTime = TimeUtils.getNow();
		monthCard.lifelongGetTime = TimeUtils.getNow();
		monthCard.yearCardDay = 0;
		monthCard.yearCardFrast = 0;
		monthCard.yearCardDayReward = 0;
		monthCard.yearCardTime = TimeUtils.getNow();
		return monthCard;
	}
	
	/**
	 * 月卡下一天
	 * @return
	 */
	public boolean nextDay() {
		if(DateUtils.isToday(getTime) == false){
			Date date = new Date();
			date.setTime(getTime * 1000L);
			int day = DateUtils.getRemainDays(date, new Date());
			if(this.day > 0){
				this.isDayReward = 1;
				if(this.day < day){
					this.day = 0;
				}else{
					this.day -= day;
				}
				this.getTime = TimeUtils.getNow();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 年卡下一天
	 * @return
	 */
	public boolean yearNextDay() {
		if(DateUtils.isToday(yearCardTime) == false){
			Date date = new Date();
			date.setTime(yearCardTime * 1000L);
			int day = DateUtils.getRemainDays(date, new Date());
			if(this.yearCardDay > 0){
				this.yearCardDayReward = 1;
				if(this.yearCardDay < day){
					this.yearCardDay = 0;
				}else{
					this.yearCardDay -= day;
				}
				this.yearCardTime = TimeUtils.getNow();
				return true;
			}
		}
		return false;
	}

}
