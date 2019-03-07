package com.jtang.gameserver.module.adventures.achievement.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Achievement;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;

public interface AchieveDao {
	
	/**
	 * 获取某一个成就
	 * @param actorId		角色id
	 * @param achieveId		成就id
	 * @param achieveType	成就类型
	 * @return
	 */
	AchieveVO getAchieveVO(long actorId, int achieveId, int achieveType);
	
	/**
	 * 获了成就列表
	 * @param actorId
	 * @return
	 */
	List<AchieveVO> getAchieveVOList(long actorId);
	
	/**
	 * 添加一样成就
	 * @param actorId
	 * @param achieveVO
	 */
	void addAchieveVO(long actorId, AchieveVO achieveVO);
	
	/**
	 * 更新成就系统
	 * @param actorId	角色id
	 */
	void update(long actorId);

	/**
	 * 删除某一条成就
	 * @param actorId 
	 * @param achieveId
	 * @param achieveType 
	 */
	void deleteAchieve(long actorId, int achieveId, int achieveType);
	
	/**
	 * 获取成就实体
	 * @param actorId
	 * @return
	 */
	Achievement get(long actorId);
}
