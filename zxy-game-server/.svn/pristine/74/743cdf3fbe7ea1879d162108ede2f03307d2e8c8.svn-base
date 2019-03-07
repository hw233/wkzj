package com.jtang.gameserver.module.adventures.achievement.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;

public interface AchieveFacade {
	
	/**
	 * 获得所有成就信息
	 * @param actorId
	 * @return
	 */
	List<AchieveVO> getAchieve(long actorId);
	
	/**
	 * 获取成就奖励
	 * @param actorId
	 * @param achieveId
	 * @param achieveLevel
	 * @return
	 */
	Result getReward(long actorId, int achieveId);

}
