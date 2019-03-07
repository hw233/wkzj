package com.jtang.gameserver.module.equip.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Equips;
import com.jtang.gameserver.module.equip.dao.EquipDao;

/**
 * 装备库dao实现
 * @author liujian
 *
 */
@Repository
public class EquipDaoImpl implements EquipDao, CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipDaoImpl.class);
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 装备的缓存
	 * <pre>
	 * key:角色actorId  value:Equips
	 * </pre>
	 */
	private static ConcurrentLinkedHashMap<Long, Equips> EQUIP_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Equips>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();

	/**
	 * 获取 Equips
	 * @param actorId
	 * @return
	 */
	@Override
	public Equips get(long actorId) {
		if (EQUIP_MAPS.containsKey(actorId)) {
			return EQUIP_MAPS.get(actorId);
		}
		
		Equips equips = jdbc.get(Equips.class, actorId);
		if (equips == null){
			equips = Equips.valueOf(actorId);
		}
		
		EQUIP_MAPS.put(actorId, equips);
		return equips;
	}
	
	@Override
	public EquipVO get(long actorId, long uuid) {
		Equips equips = get(actorId);
		return equips.getEquipMap().get(uuid);
	}

	@Override
	public Collection<EquipVO> getList(long actorId) {
		Equips equips = get(actorId);
		Map<Long, EquipVO> maps = equips.getEquipMap();
		if (maps.isEmpty()) {
			return Collections.emptyList();
		}
		return maps.values();
	}
	
	@Override
	public int getCount(long actorId) {
		Equips equips = get(actorId);
		if(equips == null) {
			return 0;
		}
		return equips.getEquipMap().size();
	}

	@Override
	public boolean add(long actorId, EquipVO equipVo) {
		Equips equips = get(actorId);
		equips.addEquipVo(equipVo);
		dbQueue.updateQueue(equips);
		//jdbc.update(equips);
		return true;
	}

	@Override
	public boolean remove(long actorId, long uuid) {
		EquipVO vo = get(actorId, uuid);
		if (vo == null) {
			return false;
		}
		Equips equips = get(actorId);
		ChainLock lock = LockUtils.getLock(equips);
		try {
			lock.lock();
			equips.removeEquipVo(vo);
			dbQueue.updateQueue(equips);
			//jdbc.update(equips);
			return true;
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return false;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean update(long actorId, EquipVO equipVo) {
		Equips equips = get(actorId);
		equips.addEquipVo(equipVo);
		//jdbc.update(equips);
		dbQueue.updateQueue(equips);
		return true;
	}

	@Override
	public boolean canCompose(long actorId, int times) {
		Equips equips = get(actorId);
		if (equips.composeNum < times) {
			return true;
		}
		return false;
	}

	@Override
	public void recordCompose(long actorId) {
		Equips equips = get(actorId);
		equips.composeTime = TimeUtils.getNow();
		equips.composeNum += 1;
		// jdbc.update(equips);
		dbQueue.updateQueue(equips);
	}

	@Override
	public void chechAndResetCompose(long actorId) {
		Equips equips = get(actorId);
		if (TimeUtils.beforeTodayZero(equips.composeTime)) {
			equips.composeNum = 0;
		}
		dbQueue.updateQueue(equips);
	}

	@Override
	public int getComposeNum(long actorId) {
		Equips equips = get(actorId);
		return equips.composeNum;
	}

	@Override
	public int cleanCache(long actorId) {
		EQUIP_MAPS.remove(actorId);
		return EQUIP_MAPS.size();
	}

	@Override
	public void upResetNum(long actorId) {
		Equips equips = get(actorId);
		equips.resetNum += 1;
		equips.resetTime = TimeUtils.getNow();
		dbQueue.updateQueue(equips);
	}

	@Override
	public void update(Equips equips) {
		dbQueue.updateQueue(equips);
	}

}
