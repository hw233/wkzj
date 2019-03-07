package com.jtang.worldserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 跨服战服务器信息表
 * @author 0x737263
 *
 */
@TableName(name="crossBattle", type = DBQueueType.DEFAULT)
public class CrossBattle extends Entity<Integer> {
	private static final long serialVersionUID = 8558314366806307411L;
	
	/**
	 * 服务器配置id
	 */
	@Column(pk = true)
	public Integer serverId;
	
	/**
	 * 最强角色id(每天报名后更新)
	 */
	@Column
	public long bestActorId;
	
	/**
	 * 本服积分
	 */
	@Column
	public int score;
	
	/**
	 * 最近一次本服是否获胜(每天开赛后会清空该数据)
	 * 0.无记录 1.失败  2.平 2.获胜 
	 */
	@Column
	public int dayIsWin;
	
	/**
	 * 最强角色名
	 */
	@Column
	public String bestActorName = "";
	
	/**
	 * 分组id
	 */
	@Column
	public int groupId;
	
	/**
	 * 排名
	 */
	public int rank;

	@Override
	public Integer getPkId() {
		return serverId;
	}

	@Override
	public void setPkId(Integer pk) {
		this.serverId = pk;
	}

	@Override
	protected Entity<Integer> readData(ResultSet rs, int rowNum) throws SQLException {
		CrossBattle crossBattle = new CrossBattle();
		crossBattle.serverId = rs.getInt("serverId");
		crossBattle.bestActorId = rs.getLong("bestActorId");
		crossBattle.score = rs.getInt("score");
		crossBattle.dayIsWin = rs.getInt("dayIsWin");
		crossBattle.bestActorName = rs.getString("bestActorName");
		crossBattle.groupId = rs.getInt("groupId");
		return crossBattle;
	}

	@Override
	protected void hasReadEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> list = new ArrayList<>();
		if(containsPK){
			list.add(serverId);
		}
		list.add(bestActorId);
		list.add(score);
		list.add(dayIsWin);
		list.add(bestActorName);
		list.add(groupId);
		return list;
	}

	@Override
	protected void beforeWritingEvent() {
		// TODO Auto-generated method stub
		
	}

	public static CrossBattle valueOf(int serverId) {
		CrossBattle crossBattle = new CrossBattle();
		crossBattle.serverId = serverId;
		crossBattle.bestActorId = 0;
		crossBattle.score = 0;
		crossBattle.dayIsWin = 0;
		crossBattle.bestActorName = "";
		crossBattle.groupId = 0;
		return crossBattle;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}



}
