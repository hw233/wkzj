package com.jtang.gameserver.module.snatch.result;

import com.jtang.core.result.Result;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.snatch.model.SnatchVO;
import com.jtang.gameserver.module.snatch.type.SnatchEnemyType;

/**
 * 抢夺结果接口
 * @author 0x737263
 *
 */
public interface SnatchResult {
	
	/**
	 * 战前抢夺条件检查
	 * @param actorId			角色id
	 * @param targetActorId		抢夺目标角色id
	 * @param enemyType			抢夺目标敌人类型
	 * @param config			抢夺配置
	 * @param exType				当前抢夺类型
	 * @param cfgId				抢夺配置id
	 * @param notifyId			通知Id
	 * @return
	 */
	Result checkSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, SnatchConfig config);
		
	/**
	 * 计算抢夺结果
	 * @param actorId
	 * @param targetActorId
	 * @param enemyType
	 * @param winLevel
	 * @param config
	 * @return
	 */
	SnatchVO calculateSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, WinLevel winLevel,
			SnatchConfig config);
	
	/**
	 * 奖励处理
	 * @param actorId
	 * @param targetActorId
	 * @param enemyType
	 * @param winLevel
	 * @param snatchVo
	 */
	void rewardSnatch(long actorId, long targetActorId, SnatchEnemyType enemyType, WinLevel winLevel, SnatchVO snatchVo, SnatchConfig config);
	
}
