//package com.jtang.gameserver.module.enhanced.dao.impl;
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
//import com.jtang.gameserver.dbproxy.entity.Enhanced;
//import com.jtang.gameserver.module.enhanced.dao.EnhancedDao;
//
///**
// * 装备强化室的数据访问实现
// * 
// * @author pengzy
// * 
// */
//@Component
//public class EnhancedDaoImpl implements EnhancedDao, CacheListener {
//	private static final Logger LOGGER = LoggerFactory.getLogger(EnhancedDaoImpl.class);
//	
//	@Autowired
//	IdTableJdbc jdbc;
//	@Autowired
//	DBQueue dbQueue;
//
//	/**
//	 * 所有角色的装备强化室
//	 * 
//	 */
//	private static ConcurrentLinkedHashMap<Long, Enhanced> ENHANCED_MAP = new ConcurrentLinkedHashMap.Builder<Long, Enhanced>()
//			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
//
//	@Override
//	public Enhanced get(long actorId) {
//		if (ENHANCED_MAP.containsKey(actorId)) {
//			return ENHANCED_MAP.get(actorId);
//		}
//		Enhanced enhanced = jdbc.get(Enhanced.class, actorId);
//		if (enhanced == null) {
//			enhanced = Enhanced.valueOf(actorId);
//		} 
//		ENHANCED_MAP.put(actorId, enhanced);
//		return enhanced;
//	}
//
//	@Override
//	public boolean update(long actorId, int level) {
//		Enhanced enhanced = get(actorId);
//		if (enhanced != null) {
//			enhanced.level = level;
//			//jdbc.update(enhanced);
//			dbQueue.updateQueue(enhanced);
//			return true;
//		} else {
//			LOGGER.error(String.format("强化室更新时错误,缓存中没有对应的强化室数据 actorId:[%s],level: [%s]", actorId, level));
//		}
//		return false;
//	}
//
//	@Override
//	public int cleanCache(long actorId) {
//		ENHANCED_MAP.remove(actorId);
//		return ENHANCED_MAP.size();
//	}
//
//}
