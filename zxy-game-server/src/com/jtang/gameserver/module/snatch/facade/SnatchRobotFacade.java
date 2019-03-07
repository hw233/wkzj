package com.jtang.gameserver.module.snatch.facade;

import java.util.List;
import java.util.Map;

import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;

/**
 * 抢夺模块机器人玩家管理类
 * @author vinceruan
 *
 */
public interface SnatchRobotFacade {
	
	/**
	 * 随机获取机器人列表
	 * @param minLevel
	 * @param maxLevel
	 * @param num
	 * @return
	 */
	List<SnatchEnemyVO> randomRobotList(int minLevel, int maxLevel, int num);
	
	/**
	 * 获取机器人的阵型
	 * @param actorId
	 * @return
	 */
	Map<Integer, HeroVO> getRobotLineup(long actorId);
	
	/**
	 * 获取抢夺对象(机器人)
	 * @param actorId
	 * @return
	 */
	SnatchEnemyVO getSnatchEnemy(long actorId);
}
