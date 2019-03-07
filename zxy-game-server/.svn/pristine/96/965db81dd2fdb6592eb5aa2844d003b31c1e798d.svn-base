package com.jtang.gameserver.module.sprintgift.facade;

import java.util.Map;

import com.jtang.core.result.Result;
import com.jtang.gameserver.dbproxy.entity.SprintGift;

/**
 * 冲级礼包
 * @author ligang
 *
 */
public interface SprintGiftFacade {

	/**
	 * 获取礼物信息
	 * @param actorId
	 * @return
	 */
	SprintGift get(long actorId);
	
	/**
	 * 获取冲级礼包领取状态列表
	 * @return
	 */
	Map<Integer, Integer> getSprintGiftStatusList(long actorId);
	
	/**
	 * 领取对应等级冲级礼包
	 * 
	 * @param actorId
	 * @param receiveLevel 等级
	 * @return Result
	 */
	Result receiveGift(long actorId, int receiveLevel);
	
	/**
	 * 冲级礼包是否全部领取(预留成就查询接口)
	 * @param actorId
	 * @return
	 */
	boolean isAllGiftReceived(long actorId);
}
