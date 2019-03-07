package com.jtang.gameserver.module.power.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.module.power.handler.response.PowerFlushResponse;
import com.jtang.gameserver.module.power.handler.response.PowerInfoResponse;
import com.jtang.gameserver.module.power.handler.response.PowerShopInfoResponse;
import com.jtang.gameserver.module.power.handler.response.RankListResponse;

public interface PowerFacade {

	/**
	 * 获取前x个名次
	 * @param maxRank	最大的排名
	 * @return
	 */
	List<Long> getRankList(int maxRank);
	
	
	/**
	 * 挑战某个势力排名
	 * @param actorId 			挑战者角色Id
	 * @param targetActorId 	被挑战的角色Id
	 * @return
	 */
	Result challenge(long actorId, long targetActorId);

	/**
	 * 获取自己的最新排名
	 * @param actorId
	 * @return
	 */
	Power getPower(long actorId);

	/**
	 * 获取排行榜信息
	 * @param actorId
	 * @return
	 */
	TResult<PowerInfoResponse> getInfo(long actorId);

	/**
	 * 补满排行挑战次数
	 * @param actorId
	 * @return
	 */
	TResult<PowerFlushResponse> buyFightNum(long actorId);

	/**
	 * 兑换物品
	 * @param actorId
	 * @param exchangeId
	 * @return
	 */
	Result exchange(long actorId, int exchangeId,int exchangeNum);
	
	/**
	 * 获取固定排行榜
	 */
	TResult<RankListResponse> getTopRank();


	/**
	 * 获取可挑战的对手列表
	 * @return
	 */
	TResult<RankListResponse> getFightRank(long actorId);

	/**
	 * 获取排行榜商店信息
	 * @param actorId
	 * @return
	 */
	TResult<PowerShopInfoResponse> getShopInfo(long actorId);

	/**
	 * 刷新商品列表
	 * @param actorId
	 * @return
	 */
	TResult<PowerShopInfoResponse> shopFlush(long actorId);
}
