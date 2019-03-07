package com.jtang.gameserver.module.story.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.story.handler.response.StoryFightListResponse;
import com.jtang.gameserver.module.story.handler.response.StoryInfoResponse;

/**
 * 故事模块服务接口
 * @author vinceruan
 *
 */
public interface StoryFacade {
	/**
	 * 获取故事信息
	 * @param actorId
	 * @return
	 */
	public Stories get(long actorId);
	
	/**
	 * 进入副本战斗
	 * @param actorId		角色id
	 * @param battleId		战场id
	 * @param allyActorId	邀请的盟友id(有盟友id，则为合作关卡)
	 * @return
	 */
	public Result startBattle(long actorId, int battleId, long allyActorId);

	/**
	 * 领取故事通关奖励
	 * @param actorId
	 * @param storyId
	 * @param awardType 2星或3星奖励
	 */
	public Result clearStoryAward(long actorId, int storyId, int awardType);
	
	/**
	 * 指定关卡是否已经通关
	 * @param actorId
	 * @param battleId
	 * @return
	 */
	public TResult<Boolean> isBattlePassed(long actorId, int battleId);
	
	/**
	 * 指定故事是否已经通关(即主线关卡是已经全部通关)
	 * @param storyId
	 * @return
	 */
	public boolean isStoryPassed(long actorId, int storyId);

	/**
	 * 扫荡
	 * @param actorId
	 * @param fightNum
	 * @param battleId
	 * @return
	 */
	public TResult<StoryFightListResponse> storyFight(long actorId,long allyId, int fightNum,int battleId);

	/**
	 * 购买扫荡符
	 * @param actorId
	 * @return
	 */
	public Result buyFightGoods(long actorId);

	/**
	 * 获取扫荡信息
	 * @param actorId
	 * @return
	 */
	public TResult<StoryInfoResponse> getFightInfo(long actorId);
	
	/**
	 * 检车战场是否已解锁
	 */
	public Result checkBattle(long actorId, int battleId);
}
