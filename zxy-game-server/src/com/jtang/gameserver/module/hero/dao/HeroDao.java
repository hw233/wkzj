package com.jtang.gameserver.module.hero.dao;

import com.jtang.gameserver.dbproxy.entity.Heros;

/**
 * 仙人数据访问对象
 * @author 0x737263
 *
 */
public interface HeroDao {

	/**
	 * 获取Heros
	 * @param actorId
	 * @return
	 */
	Heros get(long actorId);
	
	/**
	 * 更新Goods
	 * @param goods
	 * @return
	 */
	boolean update(Heros heros);
	
	/**
	 * 设置合成数据
	 * @param actorId
	 * @param time 时间
	 */
	void recordCompose(long actorId);
	
	/**
	 * 检查是否可以合成
	 * @param actorId
	 * @param num 最大合成次数
	 * @return
	 */
	boolean canCompose(long actorId, int num);
	/**
	 * 检测并重置合成数据
	 * @param actorId 角色id
	 */
	void checkAndReset(long actorId);
	
}
