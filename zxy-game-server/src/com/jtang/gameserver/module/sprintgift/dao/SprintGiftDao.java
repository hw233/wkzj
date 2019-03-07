package com.jtang.gameserver.module.sprintgift.dao;

import com.jtang.gameserver.dbproxy.entity.SprintGift;


/**
 * @author ligang
 *
 */
public interface SprintGiftDao {

	/**
	 * 根据actorId获取礼包领取状态对象
	 * @param actorId
	 * @return
	 */
	SprintGift get(long actorId);
	
	/**
	 * 更新gift
	 * @param gift
	 * @return
	 */
	boolean update(SprintGift gift);
}
