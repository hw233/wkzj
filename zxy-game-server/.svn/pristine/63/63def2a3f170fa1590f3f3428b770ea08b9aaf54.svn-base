package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 玩家参与年兽活动信息
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author lig
 *
 */
@TableName(name="beast", type = DBQueueType.IMPORTANT)
public class Beast extends Entity<Long> {
	
	private static final long serialVersionUID = -3607188815207333715L;

	/**
	 * 角色id
	 */
	@Column(pk=true)
	private long actorId;
	
	/*
	 * 攻击年兽的次数
	 */
	@Column
	public int ackTimes;
	
	/*
	 * 对boss伤害
	 */
	@Column
	public int damage;
	
	/*
	 * 最后一次攻击年兽的时间
	 */
	@Column
	public int lastAckTime;
	
	/**
	 * 保底次数
	 * key:物品id
	 * value:保底次数
	 */
	private int leastNum = 0;
	
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
		Beast beast = new Beast();
		beast.actorId = rs.getLong("actorId");
		beast.ackTimes = rs.getInt("ackTimes");
		beast.damage = rs.getInt("damage");
		beast.lastAckTime = rs.getInt("lastAckTime");
		return beast;
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
		value.add(this.ackTimes);
		value.add(this.damage);
		value.add(this.lastAckTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	@Override
	protected void disposeBlob() {

	}
	
	public static Beast valueOf(long actorId) {
		Beast beast = new Beast();
		beast.actorId = actorId;
		beast.leastNum = 0;
		return beast;
	}
	
	
	/**
	 * 获取保底次数
	 * @return
	 */
	public int getLeastNum() {
		return leastNum;
	}
	
	/**
	 * 设置保底次数
	 * @param num
	 */
	public void setLeastNum(int num){
		this.leastNum = num;
	}

	public void increaLeastCount() {
		this.leastNum++;
	}
}
