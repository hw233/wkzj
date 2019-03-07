package com.jtang.gameserver.module.notice.dao.impl;

import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.module.notice.constant.NoticeRule;
import com.jtang.gameserver.module.notice.dao.NoticeDao;

@Repository
public class NoticeDaoImpl implements NoticeDao {
	
	/**
	 * 格式是: ConcurrentMap<actorId, NoticeSendRecord>
	 */
	private static ConcurrentLinkedHashMap<Long, NoticeSendRecord> RECORD_MAP = new ConcurrentLinkedHashMap.Builder<Long, NoticeSendRecord>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	@Override
	public boolean ableBroadcast(long actorId) {
		NoticeSendRecord record = get(actorId);
		if (!DateUtils.isToday(record.time)) {
			record.time = DateUtils.getNowInSecondes();
			record.num = NoticeRule.NOTICE_SEND_NUM_LIMIT;
		}
		if (record.num >= NoticeRule.NOTICE_SEND_NUM_LIMIT) {
			return false;
		}
		return true;
	}
	
	@Override
	public void broadcastSuccess(long actorId) {
		NoticeSendRecord record = get(actorId);
		record.num = record.num + 1;
	}
	
	private NoticeSendRecord get(long actorId) {
		if (RECORD_MAP.containsKey(actorId)) {
			return RECORD_MAP.get(actorId);
		}

		NoticeSendRecord record = new NoticeSendRecord();
		record.time = DateUtils.getNowInSecondes();
		record.num = 0;
		RECORD_MAP.put(actorId, record);
		return record;
	}

	private class NoticeSendRecord {

		/**
		 * 发送次数
		 */
		private int num;

		/**
		 * 最后一次发送时间
		 */
		private int time;
	}
}
