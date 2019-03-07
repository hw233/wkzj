package com.jtang.gameserver.module.love.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterInfoResponse;

public interface LoveMonsterFacade {

	/**
	 * 获取合作信息
	 * @param actorId
	 * @return
	 */
	public TResult<LoveMonsterInfoResponse> getInfo(long actorId);

	/**
	 * 挑战boss
	 * @param actorId
	 * @param id
	 * @return
	 */
	public Result startFight(long actorId, int id);

	/**
	 * 解锁boss
	 * @param actorId
	 * @param id
	 * @return
	 */
	public Result unLockBoss(long actorId, int id);

	/**
	 * 购买boss攻击次数
	 * @param actorId
	 * @param id
	 * @return
	 */
	public Result flushFight(long actorId, int id);

	/**
	 * 领取奖励
	 * @return
	 */
	public Result getReward(long actorId, int id);
	
	/**
	 * 离婚调用接口
	 */
	public Result unMarry(long actorId);

}
