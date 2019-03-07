package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.StringUtils;

/**
 * 聚宝盆
 * @author jianglf
 *
 */
@TableName(name = "basin", type = DBQueueType.IMPORTANT)
public class Basin extends Entity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5572169450949251821L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 奖励领取情况
	 */
	@Column
	public String reward;
	
	/**
	 * 累计充值元宝数
	 */
	@Column
	public int recharge;
	
	/**
	 * 操作时间
	 */
	@Column
	public int operationTime;
	
	/**
	 * key:充值元宝数
	 * value:是否完成(0.没完成1.可领取2.已领取)
	 */
	public Map<Integer,Integer> rewardMap = new ConcurrentHashMap<>();
	
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
		Basin basin = new Basin();
		basin.actorId = rs.getLong("actorId");
		basin.reward = rs.getString("reward");
		basin.recharge = rs.getInt("recharge");
		basin.operationTime = rs.getInt("operationTime");
		return basin;
	}

	@Override
	protected void hasReadEvent() {
		rewardMap = StringUtils.delimiterString2IntMap(reward);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(reward);
		values.add(recharge);
		values.add(operationTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		reward = StringUtils.numberMap2String(rewardMap);
	}

	@Override
	protected void disposeBlob() {
		this.reward = EMPTY_STRING;
	}
	
	public static Basin valueOf(long actorId){
		Basin basin = new Basin();
		basin.actorId = actorId;
		return basin;
	}
	
	public void reset() {
		rewardMap.clear();
		reward = "";
		recharge = 0;
		operationTime = 0;
	}

}
