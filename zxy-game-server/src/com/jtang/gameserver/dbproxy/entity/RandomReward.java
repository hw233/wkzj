package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.RandomRewardService;
import com.jtang.gameserver.module.extapp.randomreward.model.RewardVO;

/**
 * 主页面小人奖励刷新表
 * @author jianglf
 *
 */
@TableName(name="randomReward", type = DBQueueType.IMPORTANT)
public class RandomReward extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6374125433468513647L;

	/**
	 * 玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 奖励信息
	 * 人物id_下次刷新奖励时间|...
	 */
	@Column
	public String reward;
	
	/**
	 * 奖励信息列表
	 */
	public Map<Integer,RewardVO> rewardMap = new HashMap<>();
	
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
		RandomReward randomReward = new RandomReward();
		randomReward.actorId = rs.getLong("actorId");
		randomReward.reward = rs.getString("reward");
		return randomReward;
	}

	@Override
	protected void hasReadEvent() {
		Map<Integer,Integer> map = StringUtils.delimiterString2IntMap(reward);
		for(Entry<Integer,Integer> entry:map.entrySet()){
			RewardVO rewardVO = new RewardVO();
			rewardVO.id = entry.getKey();
			rewardVO.flushTime = entry.getValue();
			rewardMap.put(entry.getKey(), rewardVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(reward);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(RewardVO rewardVO:rewardMap.values()){
			sb.append(rewardVO.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		sb = sb.deleteCharAt(sb.lastIndexOf(Splitable.ELEMENT_DELIMITER));
		this.reward = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		this.reward = EMPTY_STRING;
	}

	public static RandomReward valueOf(long actorId) {
		RandomReward randomReward = new RandomReward();
		randomReward.actorId = actorId;
		randomReward.rewardMap = RandomRewardService.init();
		return randomReward;
	}

}
