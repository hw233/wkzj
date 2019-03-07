package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 最强势力排行表
 * --以下为db说明---------------------------
 * 主键为pk，总共会有50条记录，取决于策划配置
 * @author pengzy
 *
 */
@TableName(name = "power", type = DBQueueType.DEFAULT)
public class Power extends Entity<Long> {
	private static final long serialVersionUID = 7455186970809841098L;

	/**
	 * 主键
	 */
	@Column(pk = true)
	private long pk;
	
	/**
	 * 服务器Id
	 */
	@Column
	public int serverId;
	
	/**
	 * 名次
	 */
	@Column
	public int rank;
	
	/**
	 * 角色Id
	 */
	@Column
	public long actorId;
	
//	/**
//	 * 协战盟友Id
//	 */
//	@Column
//	public long allyActorId;
	
	@Override
	public Long getPkId() {
		return pk;
	}
	
	@Override
	public void setPkId(Long pk) {
		this.pk = pk;
	}
	
	public long getActorId() {
		return actorId;
	}
	
	public int getRank() {
		return rank;
	}
	
//	public long getAllyActorId() {
//		return allyActorId;
//	}
//	
//	public void setAllyActorId(long allyActorId) {
//		this.allyActorId = allyActorId;
//	}
	
	public static Power valueOf(long actorId, int rank, int serverId) {
		Power power = new Power();
		power.serverId = serverId;
		power.rank = rank;
		power.actorId = actorId;
//		power.allyActorId = 0;
		return power;
	}
	
	public void setActorId(long actorId) {
		this.actorId = actorId;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Power power = new Power();
		power.pk = rs.getLong("pk");
		power.serverId = rs.getInt("serverId");
		power.rank = rs.getInt("rank");
		power.actorId = rs.getLong("actorId");
//		power.allyActorId = rs.getLong("allyActorId");
		return power;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if (containsPK) {
			values.add(pk);
		}
		values.add(serverId);
		values.add(rank);
		values.add(actorId);
//		values.add(allyActorId);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}
	
}
