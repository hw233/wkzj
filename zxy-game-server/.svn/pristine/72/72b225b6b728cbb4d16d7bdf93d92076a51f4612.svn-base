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
 * 
 * 在线礼物信息表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ligang
 *
 */
@TableName(name = "onlineGifts", type = DBQueueType.IMPORTANT)
public class OnlineGifts extends Entity<Long> {

	private static final long serialVersionUID = 6605826806390878173L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 最后领取的时间后累计在线时间
	 */
	@Column
	public int elapseTime;
	
	/**
	 * 最后领取的礼包id
	 */
	@Column
	public int lastRecIndex;
	
	/**
	 * 最后领取的时间
	 */
	@Column
	public int lastRecTime;
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		OnlineGifts gifts = new OnlineGifts();
		gifts.actorId = rs.getLong("actorId");
		gifts.elapseTime = rs.getInt("elapseTime");
		gifts.lastRecIndex = rs.getInt("lastRecIndex");
		gifts.lastRecTime = rs.getInt("lastRecTime");
		return gifts;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
		    value.add(this.actorId);
		}
		value.add(this.elapseTime);
	    value.add(this.lastRecIndex);
	    value.add(this.lastRecTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	@Override
	protected void disposeBlob() {
		
	}
	
	public void receiveAddition() {
		this.elapseTime = 0;
		this.lastRecIndex++;
		this.lastRecTime = TimeUtils.getNow();
	}
	
	public boolean isAllReceived(int total) {
		return this.lastRecIndex >= total;
	}
	public static OnlineGifts valueOf(long actorId) {
		OnlineGifts gifts = new OnlineGifts();
		gifts.actorId = actorId;
		gifts.elapseTime = 0;
		gifts.lastRecIndex = 0;
		gifts.lastRecTime = 0;
		return gifts; 
	}

}
