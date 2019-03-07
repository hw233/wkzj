package com.jtang.gameserver.module.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.user.dao.ActorDao;

/**
 * 角色数据访问实现类
 * @author 0x737263
 *
 */
@Repository
public class ActorDaoImpl implements ActorDao, CacheListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActorDaoImpl.class);
	@Autowired
	private IdTableJdbc jdbc;
	@Autowired
	private DBQueue dbQueue;
	
	/**
	 * 角色列表.key:actorId value:Actor
	 */
	private static ConcurrentLinkedHashMap<Long, Actor> ACTOR_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Actor>().maximumWeightedCapacity(
			CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Actor getActorId(int platformType, String uid, int serverId,long actorId) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("platformType", platformType);
		condition.put("uid", uid);
		condition.put("serverId", serverId);
		condition.put("actorId", actorId);

		Actor actor = jdbc.getFirst(Actor.class, condition);

		return actor;
	}
	
	@Override
	public long getActorId(String actorName) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("actorName", actorName);

		Actor actor = jdbc.getFirst(Actor.class, condition);
		return actor == null ? 0L : actor.getPkId();
	}

	@Override
	public Actor getActor(long actorId) {
		Actor entity = ACTOR_MAPS.get(actorId);
		if (entity == null) {
			entity = jdbc.get(Actor.class, actorId);
			if (entity != null) {
				ACTOR_MAPS.putIfAbsent(actorId, entity);
			}
		}
		return entity;
	}
	
	@Override
	public List<Long> getActorIdList(int minLevel, int maxLevel, int recordNum) {
		String sql = "select actorid from actor where level between ? and ? LIMIT ?";
		Object[] params = new Object[] { minLevel, maxLevel, recordNum };
		return jdbc.queryForList(sql, params, Long.class);
	}
	
	@Override
	public int getMaxLevelActorId(int serverId) {
		String sql = "SELECT DISTINCT MAX(LEVEL) FROM actor WHERE serverId = ?";
		Object[] params = new Object[] { serverId };
		return jdbc.queryForInt(sql, params);
	}

	@Override
	public long createActor(int platformType, String uid, int channelId, int serverId, int heroId, String actorName, String ip, String sim,
			String mac, String imei) {
		Actor actor = Actor.valueOf(platformType, uid, channelId, serverId, heroId, actorName, ip, sim, mac, imei);
		long actorId = 0;
		try {
			actorId = jdbc.save(actor);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		}
//		if (actorId > 0) {
//			List<Entity<Long>> list = new ArrayList<>();
//			list.add(Heros.valueOf(actorId, HeroService.get(heroId)));
//			list.add(Lineup.valueOf(actorId, heroId));
//			jdbc.save(list);
//			dbQueue.insertQueue(list);
//		}
		return actorId;
	}

	@Override
	public void updateActor(Actor actor) {
		dbQueue.updateQueue(actor);
	}

	@Override
	public void dbUpdate(Actor actor) {
		jdbc.update(actor);
	}

	@Override
	public Map<Long, Integer> getLevelList(int loginTime, int num) {
		String sql = "select actorid,level from actor where loginTime >= ? LIMIT ?";
		Object[] params = new Object[] { loginTime, num };

		final Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
		jdbc.query(sql, params, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				resultMap.put(rs.getLong("actorId"), rs.getInt("level"));
			}
		});
		return resultMap;
	}

	@Override
	public int cleanCache(long actorId) {
		ACTOR_MAPS.remove(actorId);
		return ACTOR_MAPS.size();
	}

	@Override
	public List<Actor> getActorId(int platformType, String uid, int serverId) {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("platformType", platformType);
		condition.put("uid", uid);
		condition.put("serverId", serverId);
		return jdbc.getList(Actor.class, condition);
	}
}
