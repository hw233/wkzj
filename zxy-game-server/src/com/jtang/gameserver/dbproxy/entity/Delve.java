package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 潜修室实体
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="delve", type = DBQueueType.IMPORTANT)
public class Delve extends Entity<Long> {
	private static final long serialVersionUID = -8864346846751581377L;

	/**
	 * 角色ID
	 */
	@Column (pk = true)
	private long actorId;
	
	/**
	 * 等级
	 */
	@Column
	public int level;
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public static Delve valueOf(long actorId) {
		Delve delve = new Delve();
		delve.actorId = actorId;
		delve.level = 1;
		return delve;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Delve delve = new Delve();
		delve.actorId = rs.getLong("actorId");
		delve.level = rs.getShort("level");
		return delve;
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
		value.add(this.level);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}
	
	public void reset() {
		this.level = 1;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

}
