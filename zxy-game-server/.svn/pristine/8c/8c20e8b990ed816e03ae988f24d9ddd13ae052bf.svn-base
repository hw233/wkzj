package com.jtang.gameserver.module.notify.dao.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.FightVideo;
import com.jtang.gameserver.module.notify.dao.FightVideoDao;
import com.jtang.gameserver.module.notify.type.FightVideoRemoveType;

/**
 * 战斗录像数据访问实现类
 * @author 0x737263
 *
 */
@Repository
public class FightVideoDaoImpl implements FightVideoDao, CacheListener {
	@Autowired
	IdTableJdbc jdbc;
	
	/**
	 * key:notifyId value:录象
	 */
	private static ConcurrentLinkedHashMap<Long, Map<Long, FightVideo>> FIGHT_VIDEO_MAP = new ConcurrentLinkedHashMap.Builder<Long, Map<Long, FightVideo>>()
			.maximumWeightedCapacity(Byte.MAX_VALUE).build();

	
	@Override
	public FightVideo get(long actorId, long notifyId) {
		if (actorId < 1 || notifyId < 1) {
			return null;
		}

		if (FIGHT_VIDEO_MAP.containsKey(actorId)) {
			Map<Long, FightVideo> maps = FIGHT_VIDEO_MAP.get(actorId);
			if (maps != null && maps.containsKey(notifyId)) {
				return maps.get(notifyId);
			}
		}
		
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("actorId", actorId);
		condition.put("notifyId", notifyId);
		
		FightVideo video = jdbc.getFirst(FightVideo.class, condition);
		if (video != null) {
			Map<Long, FightVideo> maps = FIGHT_VIDEO_MAP.get(actorId);
			if (maps == null) {
				maps = new ConcurrentHashMap<>();
				FIGHT_VIDEO_MAP.put(actorId, maps);
			}
			maps.put(notifyId, video);
		}
		return video;
	}
	
	@Override
	public long create(long actorId, long notifyId, byte[] videoData) {
		if (actorId > 0 && notifyId > 0) {
			FightVideo entity = FightVideo.valueOf(actorId, notifyId, videoData);
			return jdbc.saveAndIncreasePK(entity);
		}
		return 0;
	}

	@Override
	public void remove(long actorId, long notifyId,FightVideoRemoveType type) {
		if(type == FightVideoRemoveType.TYPE1){
			Map<Long,FightVideo> map = FIGHT_VIDEO_MAP.get(actorId);
			if(map == null){
				return;
			}
			long id = 0;
			for(Entry<Long,FightVideo> entry : map.entrySet()){
				if(entry.getValue().getPkId() == notifyId){
					id = entry.getKey();
				}
			}
			if(id != 0){
				map.remove(id);
			}
		}else{
			FightVideo entity = get(actorId, notifyId);
			if (entity != null) {
				Map<Long, FightVideo> maps = FIGHT_VIDEO_MAP.get(notifyId);
				if (maps != null) {
					maps.remove(notifyId);
				}
				jdbc.delete(entity);
			}
		}
	}
	
	@Override
	public int cleanCache(long actorId) {
		FIGHT_VIDEO_MAP.remove(actorId);
		return FIGHT_VIDEO_MAP.size();
	}

	@Override
	public byte[] get(long fightVideoId) {
		return jdbc.get(FightVideo.class, fightVideoId).videoData;
	}

	
}
