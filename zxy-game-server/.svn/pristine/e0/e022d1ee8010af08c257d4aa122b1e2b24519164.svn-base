package com.jtang.gameserver.module.snatch.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.snatch.handler.response.SnatchNumResponse;

public interface SnatchFacade {

	/**
	 * 获取抢夺信息
	 * @param actorId
	 * @return
	 */
	Snatch get(long actorId);
	
	/**
	 * 开始抢夺
	 * @param actorId			抢夺者的角色id
	 * @param targetActorId		被抢夺者的角色id
	 * @param snatchType	            抢夺类型   {@code SnatchType}
	 * @param cfgId				魂魄或碎片的配置id
	 * @param notifyId			通知自增id
	 * @return
	 */
	Result startSnatch(long actorId, long targetActorId, long notifyId);
	
	/**
	 * 商店兑换物品
	 * @param actorId
	 * @param type		ExchangeType
	 * @param cfgId
	 * @param num
	 * @return
	 */
	Result exchange(long actorId, int type, int cfgId,int num);

	/**
	 * 购买抢夺次数
	 * @param actorId
	 * @return
	 */
	TResult<SnatchNumResponse> buySnatchNum(long actorId);
	
	/**
	 * 系统补满次数
	 */
	Result fullSnatchNum(long actorId);

//	/**
//	 * 获取商店兑换列表
//	 * @param actorId
//	 * @return
//	 */
//	TResult<ExchangeListResponse> getExchangeList(long actorId);

//	/**
//	 * 刷新兑换列表
//	 * @param actorId
//	 * @return
//	 */
//	TResult<ReflushExchangeResponse> flushExchange(long actorId);

}
