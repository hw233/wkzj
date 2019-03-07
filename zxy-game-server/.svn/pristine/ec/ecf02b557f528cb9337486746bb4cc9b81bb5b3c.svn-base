package com.jtang.gameserver.module.extapp.monthcard.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.MonthCard;
import com.jtang.gameserver.module.extapp.monthcard.dao.MonthCardDao;

@Component
public class MonthCardDaoImpl implements MonthCardDao,CacheListener {

	private static ConcurrentLinkedHashMap<Long, MonthCard> MONTH_CARD_MAP = new ConcurrentLinkedHashMap.Builder<Long, MonthCard>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	@Override
	public MonthCard get(long actorId) {
		if(MONTH_CARD_MAP.containsKey(actorId)){
			return MONTH_CARD_MAP.get(actorId);
		}
		MonthCard monthCard = jdbc.get(MonthCard.class, actorId);
		if(monthCard == null){
			monthCard = MonthCard.valueOf(actorId);
		}
		MONTH_CARD_MAP.put(actorId, monthCard);
		return monthCard;
	}

	@Override
	public void update(MonthCard monthCard) {
		dbQueue.updateQueue(monthCard);
	}

	@Override
	public int cleanCache(long actorId) {
		MONTH_CARD_MAP.remove(actorId);
		return MONTH_CARD_MAP.size();
	}

}
