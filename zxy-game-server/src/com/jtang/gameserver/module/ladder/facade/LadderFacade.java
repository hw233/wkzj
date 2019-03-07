package com.jtang.gameserver.module.ladder.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.ladder.handler.response.BuyFightNumResponse;
import com.jtang.gameserver.module.ladder.handler.response.FightVideoResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderActorResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderFightInfoResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderRankResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderResponse;
import com.jtang.gameserver.module.ladder.handler.response.LadderRewardResponse;

public interface LadderFacade {

	/**
	 * 获取天梯信息
	 * @param actorId
	 * @return
	 */
	TResult<LadderResponse> getInfo(long actorId);

	/**
	 * 开始天梯战斗
	 * @param actorId
	 * @param targetActorId
	 * @return
	 */
	Result startFight(long actorId, long targetActorId);

	/**
	 * 领取天梯赛季奖励
	 * @param actorId
	 * @return
	 */
	TResult<LadderRewardResponse> getLadderReward(long actorId);

	/**
	 * 购买战斗次数
	 * @param actorId
	 * @return
	 */
	TResult<BuyFightNumResponse> buyFightNum(long actorId);

	/**
	 * 获取排行
	 * @param actorId
	 * @return
	 */
	TResult<LadderRankResponse> getRank(long actorId);

	/**
	 * 查看战斗记录
	 * @param actorId
	 * @return
	 */
	TResult<LadderFightInfoResponse> getFightInfo(long actorId);

	/**
	 * 刷新对手列表
	 * @param actorId
	 * @return
	 */
	TResult<LadderActorResponse> flushActor(long actorId);

	/**
	 * 获取战斗录像
	 * @param actorId
	 * @param fightVideoId
	 * @return
	 */
	TResult<FightVideoResponse> getFightVideo(long actorId, long fightVideoId);

}
