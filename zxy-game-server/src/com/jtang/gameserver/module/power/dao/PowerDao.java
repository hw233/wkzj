package com.jtang.gameserver.module.power.dao;

import java.util.Collection;
import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Power;

public interface PowerDao {
	
	/**
	 * 获取topx排名 key:名次,value:势力对象
	 * @return
	 */
	Collection<Power> getTopRanks(int minRank,int maxRank);
	
	/**
	 * 查询角色的势力对象
	 * @param actorId
	 * @return
	 */
	Power getPower(long actorId);
	
	/**
	 * 添加新势力
	 * @param actorId
	 * @param serverId
	 */
	void add(long actorId, int serverId);
	
	/**
	 * 交换排名
	 * @param power
	 * @param targetPower
	 */
	boolean changeRank(Power power, Power targetPower);
	
	/**
	 * 根据排名获取角色id
	 * @param rank
	 * @return
	 */
	Long getActorId(int rank);
	
	/**
	 * 获取排行列表
	 * @param mustRank
	 * @param actorRank
	 * @param upRank
	 * @param downRank
	 * @param viewUp
	 * @return
	 */
	Collection<Power> getPowerList(int mustRank,int actorRank,int upRank,int downRank,List<Integer> viewUp);
	
	/**
	 * 获取周围高于当前排名的列表
	 */
	List<Power> getUpList(int actorRank,int upRank);
	
	/**
	 * 获取周围低于当前排名的列表
	 */
	List<Power> getDownList(int actorRank,int downRank);
	
	/**
	 * 获取区间可挑战排行列表
	 */
	List<Power> getFightList(List<Integer> viewUp);
	
}
