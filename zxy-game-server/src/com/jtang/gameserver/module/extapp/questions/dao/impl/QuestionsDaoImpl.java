package com.jtang.gameserver.module.extapp.questions.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Questions;
import com.jtang.gameserver.module.extapp.questions.dao.QuestionsDao;

@Component
public class QuestionsDaoImpl implements QuestionsDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Questions> QUESTIONS_MAP = new ConcurrentLinkedHashMap.Builder<Long, Questions>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Questions get(long actorId) {
		if(QUESTIONS_MAP.containsKey(actorId)){
			return QUESTIONS_MAP.get(actorId);
		}
		Questions questions = jdbc.get(Questions.class, actorId);
		if(questions == null){
			questions = Questions.valueOf(actorId);
		}
		QUESTIONS_MAP.put(actorId, questions);
		return questions;
	}

	@Override
	public int cleanCache(long actorId) {
		QUESTIONS_MAP.remove(actorId);
		return QUESTIONS_MAP.size();
	}

	@Override
	public void update(Questions questions) {
		questions.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(questions);
	}

}
