package com.jtang.gameserver.module.refine.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.refine.model.RefineResult;

public interface RefineFacade {

//	/**
//	 * 获取精炼室信息
//	 * @param actorId
//	 * @return
//	 */
//	Refine get(long actorId);
	
//	/**
//	 * 精炼室升级
//	 * @param actorId
//	 */
//	TResult<Integer> upgrade(long actorId);
	
	/**
	 * 精炼装备
	 * @param actorId	角色id
	 * @param uuid		装备uuid
	 * @param refineType 精炼类型
	 * @param refineNum 精炼次数
	 * @return
	 */
	TResult<RefineResult> refineEquip(long actorId, long uuid, int refineType,int refineNum);
	/**
	 * 刷新装备的最大精炼次数
	 * @param actorId
	 * @param uuid
	 * @param upLevel
	 */
	void flushMaxRefineNum(long actorId, long uuid, int upLevel);
	
}
