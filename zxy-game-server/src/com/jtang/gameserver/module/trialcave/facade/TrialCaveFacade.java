package com.jtang.gameserver.module.trialcave.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.TrialCave;

/**
 * 试炼洞模块业务处理类
 * @author lig
 *
 */
public interface TrialCaveFacade {
	/**
	 * 加载试炼洞信息
	 * @param actorId
	 * @return
	 */
	public TResult<TrialCave> getTrialCaveInfo(long actorId);
	
	/**
	 * 开始试炼
	 * @param actorId
	 * @param entranceId 
	 * @return
	 */
	public Result trialBattle(long actorId, int entranceId);
	
	/**
	 * 重置试炼洞
	 * @param actorId
	 * @return
	 */
	public TResult<Integer> resetTrialCave(long actorId);

	/**
	 * 每日重置试炼洞
	 * @param actorId
	 * @return
	 */
	public void dailyResetTrialCave(long actorId);
}
