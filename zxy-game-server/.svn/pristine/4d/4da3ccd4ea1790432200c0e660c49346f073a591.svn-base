package com.jtang.gameserver.module.notify.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.db.ErrorEntityBackup;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Notify;
import com.jtang.gameserver.module.notify.dao.FightVideoDao;
import com.jtang.gameserver.module.notify.dao.NotifyDao;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 
 * @author 0x737263
 *
 */
@Repository
public class NotifyDaoImpl implements NotifyDao, CacheListener {
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	@Autowired
	private FightVideoDao fightVideoDao;
	@Autowired
	private ErrorEntityBackup entityBackup;
	
	/**
	 * TODO 存在对List<Notify>并发修改的异常
	 * key:ownerActorId
	 * value:List<Notify>
	 */
	private static ConcurrentLinkedHashMap<Long, Notify> NOTIFY_MAP = new ConcurrentLinkedHashMap.Builder<Long, Notify>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	

	@Override
	public Notify get(long actorId) {
		if (NOTIFY_MAP.containsKey(actorId)) {
			return NOTIFY_MAP.get(actorId);
		}

		Notify notify = jdbc.get(Notify.class, actorId);
		if (notify == null) {
			notify = Notify.valueOf(actorId);
		}
		NOTIFY_MAP.put(actorId, notify);
		return notify;
	}
	
	@Override
	public AbstractNotifyVO getNotifyVO(long ownerActorId, long nId) {
		List<AbstractNotifyVO> sendList = getSendList(ownerActorId);
		for (AbstractNotifyVO vo : sendList) {
			if (vo.nId == nId) {
				return vo;
			}
		}
		
		List<AbstractNotifyVO> receiveList = getReceiveList(ownerActorId);
		for (AbstractNotifyVO vo : receiveList) {
			if (vo.nId == nId) {
				return vo;
			}
		}
		return null;
	}

	@Override
	public List<AbstractNotifyVO> getSendList(long actorId) {
		Notify notify = get(actorId);
		return notify.getSendNotifyList();
	}

	@Override
	public List<AbstractNotifyVO> getReceiveList(long actorId) {
		Notify notify = get(actorId);
		return notify.getReceiveNotifyList();
	}
	
	@Override
	public boolean update(Notify notify) {
		dbQueue.updateQueue(notify);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		// 清空录像缓存
		NOTIFY_MAP.remove(actorId);
		return NOTIFY_MAP.size();
	}
	
}
