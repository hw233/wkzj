package com.jtang.gameserver.module.battle.facade;

import com.jtang.gameserver.module.battle.model.BattleResult;

/**
 * 战斗结果回调接口
 * @author 0x737263
 *
 */
public interface BattleCallBack {
	/**
	 * 战斗回调接口
	 * @param result
	 */
	void execute(BattleResult result);
}
