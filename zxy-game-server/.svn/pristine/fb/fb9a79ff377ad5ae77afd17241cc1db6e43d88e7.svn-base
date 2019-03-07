package com.jtang.gameserver.module.sysmail.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.db.ErrorEntityBackup;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Sysmail;
import com.jtang.gameserver.module.sysmail.dao.SysmailDao;

@Repository
public class SysmailDaoImpl implements SysmailDao,CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SysmailDaoImpl.class);

	@Autowired
	private IdTableJdbc jdbc;
	
	@Autowired
	private DBQueue dbQueue;
	
	
	@Autowired
	private ErrorEntityBackup entityBackup;
	
	private static ConcurrentLinkedHashMap<Long, List<Sysmail>> SYSMAIL_MAP = new ConcurrentLinkedHashMap.Builder<Long, List<Sysmail>>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Sysmail get(long actorId, long sysMailId) {
		List<Sysmail> list = getList(actorId, false);

		for (Sysmail mail : list) {
			if (mail.getPkId() == sysMailId) {
				return mail;
			}
		}

		return null;
	}

	@Override
	public List<Sysmail> getList(long actorId, boolean flushCache) {
		if (flushCache) {
			List<Sysmail> list = loadListFromDB(actorId);
			SYSMAIL_MAP.put(actorId, list);
			return list;
		}

		if (SYSMAIL_MAP.containsKey(actorId)) {
			return SYSMAIL_MAP.get(actorId);
		}

		List<Sysmail> list = loadListFromDB(actorId);
		SYSMAIL_MAP.put(actorId, list);
		return SYSMAIL_MAP.get(actorId);
	}
	
	/**
	 * 从db加载最新的系统邮件
	 * @param actorId
	 * @return
	 */
	private List<Sysmail> loadListFromDB(long actorId) {
		String sql = "SELECT * FROM sysmail WHERE ownerActorId = ? ORDER BY sendTime DESC";
		List<Sysmail> list = jdbc.getList(sql, new Object[] { actorId }, Sysmail.class);
		return list;
	}

	@Override
	public void remove(long actorId, long sysMailId) {
		List<Sysmail> list = getList(actorId, false);

		try {
			synchronized (list) {
				Iterator<Sysmail> iterator = list.iterator();
				while (iterator.hasNext()) {
					Sysmail sysmail = iterator.next();
					if (sysmail.getPkId() == sysMailId) {
						iterator.remove();
						jdbc.delete(sysmail); // 删除db
					}
				}
			}
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
	}

	@Override
	public Sysmail save(Sysmail sysmail) {
		List<Sysmail> list = getList(sysmail.ownerActorId, false);
		try {
			synchronized (list) {
				long sysmailId = jdbc.saveAndIncreasePK(sysmail);
				sysmail.setPkId(sysmailId);
				list.add(sysmail);
				dbQueue.insertQueue(sysmail);
			}
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
		return sysmail;
	}

	@Override
	public boolean update(Sysmail sysmail) {
		try{
			jdbc.update(sysmail);
		} catch (Exception e) {
			String tableName = "sysmail";
			LOGGER.error(String.format("save db error. actorId:%s, tableName:[%s], entity drop.", sysmail.ownerActorId, tableName), e);
			entityBackup.write(sysmail, tableName);
			return false;
		}
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		SYSMAIL_MAP.remove(actorId);
		return SYSMAIL_MAP.size();
	}

	@Override
	public List<Sysmail> loadFromDB(Long actorId) {
		String sql = "SELECT * FROM sysmail WHERE ownerActorId = ? ORDER BY sendTime DESC";
		List<Sysmail> list = jdbc.getList(sql, new Object[] { actorId }, Sysmail.class);
		return list;
	}

	@Override
	public void flushCache(Long actorId, Sysmail sysmail) {
		List<Sysmail> mailList = loadFromCache(actorId);
		mailList.add(sysmail);
	}

	@Override
	public List<Sysmail> loadFromCache(Long actorId) {
		if (SYSMAIL_MAP.containsKey(actorId)) {
			return SYSMAIL_MAP.get(actorId);
		}
		List<Sysmail> list = new ArrayList<>();
		SYSMAIL_MAP.put(actorId, list);
		return list;
	}

}
