package com.jtang.gameserver.module.adventures.achievement.dao.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Achievement;
import com.jtang.gameserver.module.adventures.achievement.dao.AchieveDao;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;

@Repository
public class AchieveDaoImpl implements AchieveDao, CacheListener {	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * key:actorId value:Achievement
	 */
	private static ConcurrentMap<Long, Achievement> ACHIEVE_MAP = new ConcurrentLinkedHashMap.Builder<Long, Achievement>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Achievement get(long actorId) {
		if (ACHIEVE_MAP.containsKey(actorId)) {
			return ACHIEVE_MAP.get(actorId);
		}
		Achievement	achieve = jdbc.get(Achievement.class, actorId);
		if (achieve == null) {
			achieve = Achievement.valueOf(actorId);
		}
		ACHIEVE_MAP.put(actorId, achieve);
		return achieve;
	}
	
	@Override
	public AchieveVO getAchieveVO(long actorId, int achieveId, int achieveType) {
		Achievement achieve = get(actorId);
		return achieve.getAchievement(achieveId, achieveType);
	}
	
	@Override
	public List<AchieveVO> getAchieveVOList(long actorId) {
		Achievement achieve = get(actorId);
		return achieve.getAll();
	}

	@Override
	public void addAchieveVO(long actorId, AchieveVO achieveVO) {
		Achievement achieve = get(actorId);
		achieve.addAchievement(achieveVO);
		queueUpdate(achieve);
	}

	@Override
	public void update(long actorId) {
		Achievement achieve = get(actorId);
		queueUpdate(achieve);
	}

	@Override
	public void deleteAchieve(long actorId, int achieveId , int achieveType) {
		Achievement achieve = get(actorId);
		achieve.removeAchieveId(achieveId,achieveType);
		queueUpdate(achieve);
	}

	private void queueUpdate(Achievement entity) {
		dbQueue.updateQueue(entity);
	}

	@Override
	public int cleanCache(long actorId) {
		ACHIEVE_MAP.remove(actorId);
		return ACHIEVE_MAP.size();
	}

}
