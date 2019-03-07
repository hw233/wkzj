package com.jtang.gameserver.module.recruit.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.recruit.handler.response.GetInfoResponse;

/**
 * 
 * @author ludd
 *
 */
public interface RecruitFacade {
	
	/**
	 * 初始化聚仙
	 * @param actorId
	 */
	public void init(long actorId);
	/**
	 * 获取聚仙阵信息
	 * @param actorId
	 * @return
	 */
	GetInfoResponse getInfo(long actorId);
	
//	/**
//	 * 一次聚仙
//	 * @param actorId
//	 * @param type
//	 * @return
//	 */
//	TResult<RewardObject> singleRecruit(long actorId, byte type);
//	/**
//	 * 小聚仙
//	 * @param actorId
//	 * @return
//	 */
//	TResult<List<RewardObject>> smallRecruit(long actorId, boolean useGoods, int num);
//	/**
//	 * 大聚仙
//	 * @param actorId
//	 * @return
//	 */
//	TResult<List<RewardObject>> bigRecruit(long actorId, byte type);
	
//	/**
//	 * 10连抽聚仙
//	 * @param actorId
//	 * @param type
//	 * @return
//	 */
//	TResult<List<RewardObject>> mutiRecruit(long actorId, byte type);
	
	TResult<List<RewardObject>> randRecruit(long actorId, byte recruitType, byte single);
	
}
