package com.jtang.gameserver.module.adventures.bable.dao;

import java.util.List;
import java.util.Map;

import com.jtang.gameserver.dbproxy.entity.BableRecord;

public interface BableRecordDao {
	/**
	 * 获取登天塔记录
	 * @param actorId
	 * @param bableId
	 * @return
	 */
	BableRecord get(long actorId, int bableId);
	
	/**
	 * 获取登天塔排行榜
	 * @param bableId
	 * @return
	 */
	List<BableRecord> getRank(int bableId);
	
	/**
	 * 更新登天塔记录
	 * @param bableRecord
	 * @return
	 */
	int update(BableRecord bableRecord);
	
	/**
	 * 创建排行
	 */
	void createRank();
	
	/**
	 * 获取排行榜里所有记录
	 * @return
	 */
	 Map<Integer, List<BableRecord>> allRankRecords();
}
