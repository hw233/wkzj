package com.jtang.gameserver.module.user.facade;

import java.util.List;
//import java.util.List;
import java.util.Map;

//import com.jtang.sm2.dbproxy.entity.Actor;

/**
 * 角色相关查询接口
 * @author 0x737263
 *
 */
public interface ActorFetchFacade {
	
	/**
	 * 根据登陆时间获取角色id，等级列表
	 * @param loginTime		上一次登陆时间
	 * @param num			查询记录数
	 * @return	Map<actorId,actorLevel>
	 */
	Map<Long, Integer> getLevelList(int loginTime, int num);
	
	/**
	 * 获取在线角色id列表
	 * @param channelIdList 渠道id列表
	 * @return
	 */
	List<Long> getOnlineActorIds(List<Integer> channelIdList);

}
