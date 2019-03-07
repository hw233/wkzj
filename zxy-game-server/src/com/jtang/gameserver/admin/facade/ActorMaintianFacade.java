package com.jtang.gameserver.admin.facade;

import java.util.List;
import java.util.Map;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;

public interface ActorMaintianFacade{

	/**
	 * 修改角色属性
	 * @param uid
	 * @param serverId
	 * @param platformId
	 * @param modifyFields
	 * @return
	 */
	 TResult<List<ActorAttributeKey>> modify(long actorId, Map<ActorAttributeKey, String> modifyFields);
	
	/**
	 * 加声望
	 * @param actorId
	 * @param reputationValue
	 * @return
	 */
	 Result addReputation(long actorId, int reputationValue);
	
	/**
	 * 添加金币
	 * @param actorId
	 * @param giveNum
	 * @return
	 */
	 Result addGold(long actorId, int giveNum);
	
	/**
	 * 扣除金币
	 * @param actorId
	 * @param decreaseNum
	 * @return
	 */
	 Result decreaseGold(long actorId, int decreaseNum);
	
	/**
	 * 赠送vip等级
	 * @param actorId
	 * @param level
	 * @return
	 */
	 Result modifyVipLevel(long actorId, int level);

	 /**
	  * 删除角色所有活动数据
	  * @param actorId
	  * @return
	  */
	Result deleteActorActive(long actorId, long appId);
	
	/**
	 * 修改角色的帐号归属
	 * @param actorId			当前角色id
	 * @param newPlatformId		新的平台id
	 * @param newUid			新的uid
	 * @param newChannelId		新的渠道id
	 * @return
	 */
	Result changeActorUid(long actorId, int newPlatformId, String newUid, int newChannelId);
}
