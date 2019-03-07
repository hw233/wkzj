//package com.jtang.gameserver.module.refine.dao.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
//import com.jtang.core.cache.CacheListener;
//import com.jtang.core.cache.CacheRule;
//import com.jtang.core.db.DBQueue;
//import com.jtang.gameserver.dbproxy.IdTableJdbc;
//import com.jtang.gameserver.dbproxy.entity.Refine;
//import com.jtang.gameserver.module.refine.dao.RefineDao;
//
//@Repository
//public class RefineDaoImpl implements RefineDao, CacheListener {
//	@Autowired
//	private IdTableJdbc jdbc;
//	@Autowired
//	private DBQueue dbQueue;
//	
//	/**
//	 * 精炼室的缓存
//	 * <pre>
//	 * key:actorId  value:{@code Refine}
//	 * </pre>
//	 */
//	private static ConcurrentLinkedHashMap<Long, Refine> REFINE_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Refine>().maximumWeightedCapacity(
//			CacheRule.LRU_CACHE_SIZE).build();
//	
//	@Override
//	public Refine get(long actorId) {
//		if (REFINE_MAPS.containsKey(actorId)) {
//			return REFINE_MAPS.get(actorId);
//		}
//
//		Refine refine = jdbc.get(Refine.class, actorId);
//		if (refine == null) {
//			refine = Refine.valueOf(actorId);
//		}
//		REFINE_MAPS.put(actorId, refine);
//		return refine;
//	}
//
//	@Override
//	public boolean update(Refine refine) {
//		if (refine == null) {
//			return false;
//		}
//		dbQueue.updateQueue(refine);
//		return true;
//	}
//
//	@Override
//	public int cleanCache(long actorId) {
//		REFINE_MAPS.remove(actorId);
//		return REFINE_MAPS.size();
//	}
//
//}
