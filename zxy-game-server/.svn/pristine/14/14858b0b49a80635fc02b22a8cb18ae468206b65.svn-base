package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;
/**
 * 跨服战奖励实体
 * @author ludd
 *
 */
@TableName(name="crossbattlereward", type = DBQueueType.NONE)
public class CrossBattleReward extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4491289463116615730L;

	/**
	 * id
	 */
	@Column(pk = true)
	public long id;
	
	/**
	 * 奖励列表
	 * type_id_num|...
	 */
	@Column
	public String reward;
	
	/**
	 * 发奖时间
	 */
	@Column
	public long rewardTime;
	
	/**
	 * 奖励列表解析
	 */
	private List<ExprRewardObject> rewardList = new ArrayList<>();
	
	@Override
	public Long getPkId() {
		return id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		CrossBattleReward reward = new CrossBattleReward();
		reward.id = rs.getLong("id");
		reward.reward = rs.getString("reward");
		reward.rewardTime = rs.getLong("rewardTime");
		return reward;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str : list){
			ExprRewardObject reward = ExprRewardObject.valueOf(str);
			rewardList.add(reward);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if(containsPK){
			value.add(this.id);
		}
		value.add(reward);
		value.add(rewardTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	public static CrossBattleReward valueOf(String rewardObjects, long time) {
		CrossBattleReward crossBattleReward = new CrossBattleReward();
		crossBattleReward.reward = rewardObjects;
		crossBattleReward.rewardTime = time;
		return crossBattleReward;
	}
	
	public List<RewardObject> getRewardList(int level,String rewardObject){
		List<String[]> array = StringUtils.delimiterString2Array(rewardObject);
		List<RewardObject> list = new ArrayList<>();
		for(String[] str : array){
			ExprRewardObject reward = ExprRewardObject.valueOf(str);
			list.add(reward.clone(level));
		}
		return list;
	}
	@Override
	protected void disposeBlob() {
		this.reward = EMPTY_STRING;
	}
}
