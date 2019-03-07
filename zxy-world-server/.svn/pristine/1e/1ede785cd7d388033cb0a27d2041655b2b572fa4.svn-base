package com.jtang.worldserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 跨服战角色表
 * <pre>
 * (每日开赛前清除前一天的)
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name = "crossBattleActor", type = DBQueueType.IMPORTANT)
public class CrossBattleActor extends Entity<Long> {
	private static final long serialVersionUID = -204248448338066021L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 所在服务器id
	 */
	@Column
	public int serverId;

	/**
	 * 贡献点(可兑换物品,永久保留)
	 */
	@Column
	public int exchangePoint;

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
		CrossBattleActor crossBattleActor = new CrossBattleActor();
		crossBattleActor.actorId = rs.getLong("actorId");
		crossBattleActor.serverId = rs.getInt("serverId");
		crossBattleActor.exchangePoint = rs.getInt("exchangePoint");
		return crossBattleActor;
	}

	@Override
	protected void hasReadEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(actorId);
		list.add(serverId);
		list.add(exchangePoint);
		return list;
	}

	@Override
	protected void beforeWritingEvent() {
		// TODO Auto-generated method stub
		
	}

	public static CrossBattleActor valueOf(long actorId, int serverId) {
		CrossBattleActor crossBattleActor = new CrossBattleActor();
		crossBattleActor.actorId = actorId;
		crossBattleActor.serverId = serverId;
		crossBattleActor.exchangePoint = 0;
		return crossBattleActor;
	}
	
	public int costExchangePoint(int point){
		if(exchangePoint < point){
			return -1;
		}
		exchangePoint -= point;
		return exchangePoint;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
