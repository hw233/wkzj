package com.jtang.gameserver.module.snatch.facade;

import java.util.List;

import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;

/**
 * 抢夺敌人接口
 * @author 0x737263
 *
 */
public interface SnatchEnemyFacade {

	/**
	 * 获取敌人列表
	 * @param actorId		角色id
	 * @param cleanCache	是否清除缓存
	 * @return
	 */
	List<SnatchEnemyVO> getEnemyList(long actorId, boolean cleanCache);
	
	/**
	 * 获取敌人
	 * @param actorId		角色id
	 * @param enemyActorId	对手列表中的角色id
	 * @return
	 */
	SnatchEnemyVO getEnemy(long actorId, long enemyActorId);
	
	/**
	 * 清除敌人列表
	 * @param actorId
	 * @param snatchType
	 */
	void refreshEnemyList(long actorId);
}
