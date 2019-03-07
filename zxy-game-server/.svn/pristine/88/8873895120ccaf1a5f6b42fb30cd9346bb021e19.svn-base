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
 * 迷宫寻宝
 * @author jianglf
 *--以下为db说明--------------------------- 
 * 主键为actorId,每个角色一行数据
 */
@TableName(name = "treasure", type = DBQueueType.IMPORTANT)
public class Treasure extends Entity<Long> {
	private static final long serialVersionUID = -5352224081233278366L;

	/**
	 * actorId
	 */
	@Column(pk = true)
	public long actorId;

	/**
	 * 玩家已使用次数
	 */
	@Column
	public int useNum;
	
	/**
	 * 是否第一次使用迷宫
	 * 0.不是 1.是
	 */
	@Column
	public int isFirstUse;
	
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
		Treasure treasure = new Treasure();
		treasure.actorId = rs.getLong("actorId");
		treasure.useNum = rs.getInt("useNum");
		treasure.isFirstUse = rs.getInt("isFirstUse");
		treasure.operationTime = rs.getInt("operationTime");
		return treasure;
	}

	@Override
	protected void hasReadEvent() {

	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<Object>();
		if(containsPK){
			value.add(actorId);
		}
		value.add(useNum);
		value.add(isFirstUse);
		value.add(operationTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static Treasure valueOf(long actorId) {
		Treasure treasure = new Treasure();
		treasure.actorId = actorId;
		treasure.isFirstUse = 1;
		treasure.operationTime = TimeUtils.getNow();
		return treasure;
	}

	public void reset() {
		useNum = 0;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

	public boolean isFirstUse() {
		return this.isFirstUse == 1;
	}

}
