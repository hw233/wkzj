package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;

@TableName(name="welkin", type = DBQueueType.IMPORTANT)
public class Welkin extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3765384299238714801L;

	/**
	 * 主键，玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 已使用免费次数
	 */
	@Column
	public int useCount;
	
	/**
	 * 已使用点券次数
	 */
	@Column
	public int ticketUseCount;
	
	/**
	 * 下次领取保底的次数
	 */
	@Column
	public int mastRewardCount;
	
	/**
	 * 操作时间
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
		Welkin welkin = new Welkin();
		welkin.actorId = rs.getLong("actorId");
		welkin.useCount = rs.getInt("useCount");
		welkin.ticketUseCount = rs.getInt("ticketUseCount");
		welkin.mastRewardCount = rs.getInt("mastRewardCount");
		welkin.operationTime = rs.getInt("operationTime");
		return welkin;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		value.add(this.actorId);
		value.add(this.useCount);
		value.add(this.ticketUseCount);
		value.add(this.mastRewardCount);
		value.add(this.operationTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	public static Welkin valueOf(long actorId) {
		Welkin welkin = new Welkin();
		welkin.actorId = actorId;
		welkin.useCount = 0;
		welkin.ticketUseCount = 0;
		welkin.operationTime = TimeUtils.getNow();
		return welkin;
	}

	public void reset() {
		this.useCount = 0;
		this.ticketUseCount = 0;
		this.operationTime = TimeUtils.getNow();
		this.mastRewardCount = 0;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
