//package com.jtang.gameserver.module.vampiir.dao.impl;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
//import com.jtang.core.cache.CacheListener;
//import com.jtang.core.cache.CacheRule;
//import com.jtang.core.db.DBQueue;
//import com.jtang.gameserver.dbproxy.IdTableJdbc;
//import com.jtang.gameserver.dbproxy.entity.Vampiir;
//import com.jtang.gameserver.module.vampiir.dao.VampiirDao;
//
///**
// * 吸灵室dao实现
// * 
// * @author ludd
// * 
// */
//@Component
//public class VampiirDaoImpl implements VampiirDao, CacheListener {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(VampiirDaoImpl.class);
//	@Autowired
//	private IdTableJdbc jdbc;
//	@Autowired
//	private DBQueue dbQueue;
//	
//	/**
//	 * 所有角色的吸灵室
//	 * 
//	 */
//	private static ConcurrentLinkedHashMap<Long, Vampiir> VAMPIIR_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Vampiir>()
//			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
//
//	@Override
//	public Vampiir get(long actorId) {
//		if (VAMPIIR_MAPS.containsKey(actorId)) {
//			return VAMPIIR_MAPS.get(actorId);
//		}
//
//		Vampiir vampiir = jdbc.get(Vampiir.class, actorId);
//		if (vampiir == null) {
//			vampiir = Vampiir.valueOf(actorId);
//		}
//		VAMPIIR_MAPS.putIfAbsent(actorId, vampiir);
//		return vampiir;
//	}
//
//	@Override
//	public boolean update(long actorId, int level) {
//		Vampiir vampiir = get(actorId);
//		if (vampiir != null) {
//			vampiir.level = level;
//			dbQueue.updateQueue(vampiir);
//			//jdbc.update(vampiir);
//			return true;
//		} else {
//			LOGGER.error(String.format("吸灵室更新错误,缓存中没有该角色对应的数据 actorId:[%s],level: [%s]", actorId, level));
//		}
//		return false;
//	}
//
//	@Override
//	public int cleanCache(long actorId) {
//		VAMPIIR_MAPS.remove(actorId);
//		return VAMPIIR_MAPS.size();
//	}
//}
