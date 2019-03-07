package com.jtang.gameserver.module.snatch.facade;

import java.util.Set;

import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

/**
 * 抢夺真实玩家接口
 * @author 0x737263
 *
 */
public interface SnatchActorFacade {

	/**
	 * 随机获取抢夺角色id集合
	 * @param minLevel	最小等级
	 * @param maxLevel	最大等级
	 * @param num		获取记录数
	 * @param actorId	过滤的玩家id
	 * @return
	 */
	public Set<Long> randomActorIds(int minLevel, int maxLevel, int num, long actorId);
	
	/**
	 * 是否允许抢夺该角色的金币
	 * @param actorId
	 * @return
	 */
	public boolean isAllowSnatchGold(long actorId, SnatchEnemyType enemyType);
	
	/**
	 * 允许抢该角色金币金币数
	 * @param actorId	角色id	
	 * @return
	 */
	public int allowSnatchGoldNum(long actorId,int appendGold);
	
}
