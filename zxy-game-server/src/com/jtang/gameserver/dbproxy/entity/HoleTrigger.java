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
 * 上古洞府触发次数记录表
 * --以下为db说明--------------------------- 
 * 主键为actorId,每个角色一行记录
 * @author jianglf
 *
 */
@TableName(name = "holeTrigger", type = DBQueueType.IMPORTANT)
public class HoleTrigger extends Entity<Long> {
	private static final long serialVersionUID = -8810780050919989313L;

	/**
	 * actorId
	 */
	@Column(pk = true)
	private long actorId;

	/**
	 * 自己触发的次数
	 */
	@Column
	public int selfCount;

	/**
	 * 盟友邀请的次数
	 */
	@Column
	public int allyCount;

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
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		HoleTrigger holeTrigger = new HoleTrigger();
		holeTrigger.actorId = rs.getLong("actorId");
		holeTrigger.selfCount = rs.getInt("selfCount");
		holeTrigger.allyCount = rs.getInt("allyCount");
		holeTrigger.operationTime = rs.getInt("operationTime");
		return holeTrigger;
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
		value.add(this.selfCount);
		value.add(this.allyCount);
		value.add(this.operationTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static HoleTrigger valueOf(long actorId) {
		HoleTrigger holeTrigger = new HoleTrigger();
		holeTrigger.actorId = actorId;
		holeTrigger.selfCount = 0;
		holeTrigger.allyCount = 0;
		holeTrigger.operationTime = TimeUtils.getNow();
		return holeTrigger;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}
}
