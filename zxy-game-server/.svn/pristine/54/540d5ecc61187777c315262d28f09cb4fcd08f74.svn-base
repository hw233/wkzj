package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;

/**
 * 角色签到表实体，有每日连续签到及其对应的奖励；有月累积签到及其对应的奖励。
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author pengzy
 * 
 */
@TableName(name = "sign", type = DBQueueType.IMPORTANT)
public class Sign extends Entity<Long> {
	private static final long serialVersionUID = -7993974832806914475L;

	@Column(pk = true)
	private long actorId;
	
	/**
	 * 普通累计签到天数
	 */
	@Column
	public byte day;
	
	/**
	 * 今日是否已签到
	 * 0.未签到 1.已签到
	 */
	@Column
	public byte isSign;
	
	/**
	 * vip累计签到天数
	 */
	@Column
	public byte vipDay;
	
	/**
	 * 今日充值金额
	 */
	@Column
	public int rechageNum;
	
	/**
	 * vip签到状态
	 * 0.不可签到
	 * 1.可签到
	 * 2.已签到
	 */
	@Column
	public byte vipSignState;
	
	/**
	 * 签到时间
	 */
	@Column
	public int operationTime;
	
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
		Sign sign = new Sign();
		sign.actorId = rs.getLong("actorId");
		sign.day = rs.getByte("day");
		sign.isSign = rs.getByte("isSign");
		sign.vipDay = rs.getByte("vipDay");
		sign.rechageNum = rs.getInt("rechageNum");
		sign.vipSignState = rs.getByte("vipSignState");
		sign.operationTime = rs.getInt("operationTime");
		return sign;
	}


	@Override
	protected void hasReadEvent() {
		
	}


	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.actorId);
		}
		values.add(day);
		values.add(isSign);
		values.add(vipDay);
		values.add(rechageNum);
		values.add(vipSignState);
		values.add(operationTime);
		return values;
	}


	@Override
	protected void beforeWritingEvent() {
		
	}
	
	@Override
	protected void disposeBlob() {
	}


	public static Sign valueOf(long actorId) {
		Sign sign = new Sign();
		sign.actorId = actorId;
		sign.day = 0;
		sign.isSign = 0;
		sign.vipDay = 0;
		sign.rechageNum = 0;
		sign.vipSignState = 0;
		sign.operationTime = 0;
		return sign;
	}


	public void dayClean() {
		isSign = 0;
		rechageNum = 0;
		vipSignState = 0;
	}


	public void monthClean() {
		if(TimeUtils.inMonth(operationTime) == false){
			day = 0;
			isSign = 0;
			vipDay = 0;
			rechageNum = 0;
			vipSignState = 0;
		}
	}
}
