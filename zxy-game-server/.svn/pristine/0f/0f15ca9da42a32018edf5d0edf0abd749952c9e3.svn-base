package com.jtang.gameserver.module.ladder.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import com.jtang.gameserver.dbproxy.entity.Ladder;
import com.jtang.gameserver.dbproxy.entity.LadderGlobal;

public interface LadderDao {

	/**
	 * 获取天梯数据
	 * @param actorId
	 * @return
	 */
	public Ladder get(long actorId);

	/**
	 * 更新天梯数据
	 * @param ladder
	 */
	public void update(Ladder ladder);

	/**
	 * 获取发奖排行数据
	 * @param rewardRank 
	 * @return
	 */
	public List<Long> getRank(long sportId,int rewardRank);
	
	/**
	 * 获取赛季开始时间
	 */
	public LadderGlobal getLadderGlobal();

	/**
	 * 更新赛季开始时间
	 * @param now
	 */
	public long save(int now);

	/**
	 * 获取本赛季所有排行数据
	 * @param rewardRank 
	 * @return
	 */
	SortedMap<Integer, ArrayList<Long>> getAllRank(long sportId);

}
