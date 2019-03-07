package com.jtang.gameserver.module.adventures.bable.facade;

import java.util.List;
import java.util.Map;

import com.jtang.gameserver.module.adventures.bable.model.BableRankVO;

public interface BableRankFacade {

	/**
	 * 获取所有排行榜
	 * @param bableId
	 * @return
	 */
	public Map<Integer,List<BableRankVO>> getRank();
	
	/**
	 * 是否在统计中
	 * @return
	 */
	public boolean isStatistics();
	
	/**
	 * 创建排行榜（昨天）
	 */
	void createRank();
	/**
	 * 发放昨日排行榜奖励
	 */
	void sendReward();
}
