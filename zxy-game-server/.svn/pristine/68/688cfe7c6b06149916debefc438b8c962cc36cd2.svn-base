package com.jtang.gameserver.module.ally.facade;

import java.util.Collection;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.ally.handler.response.GetActorsResponse;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.ally.model.CoordinateVO;

/**
 * 
 * @author pengzy
 * 
 */
public interface AllyFacade {
	/**
	 * 获取角色的盟友
	 * 
	 * @param actorId
	 * @return
	 */
	Collection<AllyVO> getAlly(long actorId);

	/**
	 * 判断对方是否已经是盟友,是则返回 ;判断对方盟友数量是否已经达到上限,是则返回失败;双方各添加为盟友,并同步数据.
	 * 
	 * @param actorId
	 * @param allyActorId
	 *            盟友Id
	 * @return
	 */
	Result addAlly(long actorId, long allyActorId);

	/**
	 * 
	 * @param actorId
	 * @param allyActorId
	 *            盟友角色Id
	 * 
	 * @return
	 */
	Result removeAlly(long actorId, long allyActorId);

	/**
	 * 返回该角色的所有盟友的等级之和
	 * @param actorId
	 * @return
	 */
	int getAllyLevel(long actorId);
	
	/**
	 * 判断两个角色是否为盟友
	 * @param actorId
	 * @param allyActorId
	 * @return
	 */
	boolean isAlly(long actorId, long allyActorId);

	/**
	 * 切磋
	 * @param actorId
	 * @param allyActorId
	 * @return
	 */
	Result fight(long actorId, long allyActorId);
	
	int getDayFightCount(long actorId);

	/**
	 * 返回可切磋次数重置的倒计时
	 * @param actorId
	 * @return
	 */
	int getCountDown(long actorId);
	
	/**
	 * 角色升级后通知在线盟友的处理
	 * @param actorId
	 * @param level
	 */
	void actorLevelup(long actorId, int level);
	
	/**
	 * 更新角色坐标
	 * @param actorId
	 * @param longitude
	 * @param latitude
	 * @param height
	 */
	void updateCoordinate(long actorId, double longitude, double latitude, double height);
	
	/**
	 * 获取角色坐标
	 * @param actorId
	 * @return
	 */
	TResult<CoordinateVO> getCoordinate(long actorId);
	

	/**
	 * 获取一批陌生人
	 * @param actorId
	 * @return
	 */
	TResult<GetActorsResponse> getActors(long actorId);

	/**
	 * 获取机器人
	 * @param actorId
	 * @return
	 */
	TResult<GetActorsResponse> getRobot(long actorId);
	
}
