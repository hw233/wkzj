package com.jtang.gameserver.module.hole.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.hole.handler.response.HoleResponse;
import com.jtang.gameserver.module.hole.model.HoleVO;

public interface HoleFacade {

	/**
	 * 获取所有洞府信息
	 * @param actorId
	 * @return
	 */
	public HoleResponse getHoleResponse(long actorId);

	/**
	 * 获得洞府奖励
	 * @param id 洞府自增id
	 * @param actorId
	 * @return
	 */
	public TResult<List<RewardObject>> getReward(long id, long actorId);

	/**
	 * 盟友收到通知打开洞府
	 * @param actorId 发起人id
	 * @param allyActorId 接收人id
	 * @param id 洞府自增id
	 * @return
	 */
	public TResult<HoleVO> allyOpenHole(long actorId,long allyActorId,long id);

	/**
	 * 战斗
	 * @param id 洞府自增id
	 * @param actorId
	 * @param battleId 洞府战场id
	 * @return
	 */
	public Result holeFight(long id, long actorId, int battleId);

	/**
	 * 通知盟友
	 * @param id 洞府自增id
	 * @param actorid 
	 * @return
	 */
	public Result sendAlly(long id, long actorid);

	/**
	 * 获取盟友通关大礼包
	 * @param id 洞府自增id
	 * @param actorId
	 * @return
	 */
	public Result getPackageGift(long id, long actorId);
}
