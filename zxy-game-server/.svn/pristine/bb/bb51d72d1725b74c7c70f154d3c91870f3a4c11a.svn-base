package com.jtang.gameserver.module.lineup.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.lineup.handler.response.ViewLineupResponse;
import com.jtang.gameserver.module.lineup.model.LineupHero;

/**
 * 阵型模块对外接口
 * @author vinceruan
 *
 */
public interface LineupFacade {
	/**
	 * 获取阵型信息
	 * @param actorId
	 * @return
	 */
	Lineup getLineup(long actorId);
	
	/**
	 * 获取阵位和英雄信息
	 * @param actorId
	 * @return
	 */
	List<LineupHero> getLineupHeros(long actorId);
		
	/**
	 * 指派某人上阵
	 * @param actorId
	 * @param heroId
	 * @param gridIndex
	 */
	Result assignHero(long actorId, int heroId, int headIndex, int gridIndex);
	
	/**
	 * 将装备放到格子
	 * @param actorId
	 * @param equipId
	 * @param headIndex
	 */
	Result assignEquip(long actorId, long equipId, int headIndex);
	
	/**
	 * 将仙人脱离阵型
	 * @param actorId
	 * @param heroId
	 */
	Result unassignHero(long actorId, int heroId);
	
	/**
	 * 将装备从格子中取下
	 * @param actorId
	 * @param equipId
	 * @param isPush2Client 是否通知客户端
	 */
	Result unassignEquip(long actorId, long equipId, boolean isPush2Client);
	
	/**
	 * 改变仙人在阵型中的位置
	 * @param actorId
	 * @param heroId
	 * @param gridIndex
	 * @return
	 */
	Result changeHeroGrid(long actorId, int heroId, int gridIndex);
	
	/**
	 * 交换仙人在阵型中的位置
	 * @param actorId
	 * @param heroId1
	 * @param heroId2
	 * @return
	 */
	Result exChangeHeroGrid(long actorId, int heroId1, int heroId2);
	
	/**
	 * 手工解锁阵型
	 * @param actorId
	 * @return
	 */
	Result manualUnlockLineup(long actorId);
	
	/**
	 * 判断英雄是否上阵
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	boolean isHeroInLineup(long actorId, int heroId);
	
	/**
	 * 判断装备是否在格子上面
	 * 如果在,返回装备所在的格子的位置.
	 * 否则返回0
	 * @param actorId
	 * @param equipUuid
	 * @return
	 */
	int isEquipInLineup(long actorId, long equipUuid);
	
	/**
	 * 获取角色阵型信息
	 * @param actorId
	 * @return
	 */
	TResult<ViewLineupResponse> getLineupInfo(long actorId);

	/**
	 * 更改阵型
	 * @param actorId
	 * @param lineupIndex
	 * @return
	 */
	TResult<ViewLineupResponse> changeLineup(long actorId, int lineupIndex);
	
	/**
	 * 获取首个仙人id
	 */
	int getFirstHero(long actorId);
}
