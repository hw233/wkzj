package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
/**
 * 跨服战领取奖励实体
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="crossbattleactorrewardflag", type = DBQueueType.IMPORTANT)
public class CrossBattleActorRewardFlag extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8283010419427618213L;
	
	/**
	 * 玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 领取时间(毫秒)
	 */
	@Column
	public long getTime;

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
		CrossBattleActorRewardFlag reward = new CrossBattleActorRewardFlag();
		reward.actorId = rs.getLong("actorId");
		reward.getTime = rs.getLong("getTime");
		return reward;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		value.add(this.actorId);
		value.add(this.getTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	public static CrossBattleActorRewardFlag valueOf(long actorId) {
		CrossBattleActorRewardFlag actorRewardFlag = new CrossBattleActorRewardFlag();
		actorRewardFlag.actorId = actorId;
		actorRewardFlag.getTime = 0;
		return actorRewardFlag;
	}

	@Override
	protected void disposeBlob() {
		
	}
}
