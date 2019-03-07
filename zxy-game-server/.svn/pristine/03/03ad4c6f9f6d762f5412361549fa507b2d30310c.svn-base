package com.jtang.gameserver.module.user.dao;

import java.util.List;
import java.util.Map;

import com.jtang.gameserver.dbproxy.entity.Actor;

/**
 * 角色数据访问接口
 * @author 0x737263
 *
 */
public interface ActorDao {

	/**
	 * 获取角色Id
	 * @param platformType	平台id
	 * @param uid			第三方用户uid
	 * @param serverId		服务器id
	 * @return
	 */
	Actor getActorId(int platformType, String uid, int serverId,long actorId);
	
	/**
	 * 获取角色Id
	 * @param platformType	平台id
	 * @param uid			第三方用户uid
	 * @param serverId		服务器id
	 * @return
	 */
	List<Actor> getActorId(int platformType, String uid, int serverId);
	
	/**
	 * 根据角色名获取角色Id
	 * @param actorName		角色名
	 * @return
	 */
	long getActorId(String actorName);
	
	/**
	 * 获取角色信息
	 * @param actorId	角色id
	 * @return
	 */
	Actor getActor(long actorId);
	
	/**
	 * 获取角色id列表
	 * @param minLevel		最小等级
	 * @param maxLevel		最大等级
	 * @param recordNum		角色记录数
	 * @return
	 */
	List<Long> getActorIdList(int minLevel, int maxLevel, int recordNum);
	
	/**
	 * 创建角色(并且初始化各张db表)
	 * @param platformType
	 * @param uid
	 * @param channelId
	 * @param serverId
	 * @param heroId
	 * @param actorName
	 * @return
	 */
	long createActor(int platformType, String uid, int channelId, int serverId, int heroId, String actorName, String ip, String sim, String mac,
			String imei);
	
	/**
	 * 角色信息更新
	 * @param actor
	 */
	void updateActor(Actor actor);
	
	/**
	 * 直接db更新
	 * @param actor
	 */
	void dbUpdate(Actor actor);
	
	/**
	 * 根据登陆时间获取角色id，等级列表
	 * @param loginTime		上一次登陆时间
	 * @param num			查询记录数
	 * @return	Map<actorId,actorLevel>
	 */
	Map<Long,Integer> getLevelList(int loginTime, int num);

	/**
	 * 获取该服务器玩家最高等级
	 * @param serverId 服务器id
	 * @return
	 */
	int getMaxLevelActorId(int serverId);
	
}
