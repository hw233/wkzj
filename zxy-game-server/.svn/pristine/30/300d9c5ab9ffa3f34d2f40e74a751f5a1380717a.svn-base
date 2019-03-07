package com.jtang.gameserver.module.power.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Power;
import com.jtang.gameserver.module.power.constant.PowerRule;
import com.jtang.gameserver.module.power.dao.PowerDao;

/**
 * 最强势力排行dao
 * 
 * 同步：
 * 1、共享状态
 * a、POWER_RANK_MAP：对POWER_RANK_MAP的操作只有添加，没有删除，
 * 	   且POWER_RANK_MAP的大小有上限，上限取决于配置；
 * b、Power中的actorId：
 * @author pengzy
 *
 */
@Repository
public class PowerDaoImpl implements PowerDao {
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
//	/**
//	 * topx排名集合  key:排名,value:Power
//	 */
//	private static ConcurrentLinkedHashMap<Integer, Power> TOP_RANKS_MAP = new ConcurrentLinkedHashMap.Builder<Integer, Power>()
//			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
//	/**
//	 * 角色power集合 key:actorid,value:power
//	 */
//	private static ConcurrentLinkedHashMap<Long, Power> ACTOR_POWER_MAP = new ConcurrentLinkedHashMap.Builder<Long, Power>().maximumWeightedCapacity(
//			CacheRule.LRU_CACHE_SIZE).build();
//	
//	/**
//	 * 排名集合 key:名次,value:角色id
//	 */
//	private static ConcurrentLinkedHashMap<Integer, Long> POWER_RANK_MAP = new ConcurrentLinkedHashMap.Builder<Integer, Long>()
//			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();

	private int SERVER_ID = Game.getServerId();
	
	/**
	 * 服务器启动时获取最大的排名
	 */
	private AtomicInteger maxRank = new AtomicInteger();
	
	/**
	 * 1、读取配置中的前 PowerRule.POWER_MAX_NUM_LIMIT 缓存到 TOP_RANKS_MAP中
	 * 2、每个玩家获取自己当前的排名缓存在 ACTOR_POWER_MAP中 3、当每一次挑战成功时，需要看当前两个挑战者的排名是否为在
	 * PowerRule.POWER_MAX_NUM_LIMIT之内，如果在这之内则触发一次top ranks 排序.
	 */
	@PostConstruct
	public void init() {
		String sql = "SELECT * FROM power WHERE serverid=? ORDER BY rank LIMIT ?";
		final List<Power> topList = new ArrayList<>();
		jdbc.query(sql, new Object[] { SERVER_ID, PowerRule.POWER_MAX_NUM_LIMIT }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Power power = new Power();
				power.setPkId(rs.getLong("pk"));
				power.serverId = rs.getInt("serverid");
				power.rank = rs.getInt("rank");
				power.actorId = rs.getLong("actorId");
//				power.allyActorId = rs.getLong("allyActorId");
				topList.add(power);
			}
		});

//		for (Power entity : topList) {
//			TOP_RANKS_MAP.put(entity.rank, entity);
//			ACTOR_POWER_MAP.put(entity.actorId, entity);
//			POWER_RANK_MAP.put(entity.rank, entity.actorId);
//		}
		
		//缓存最大排名
		maxRank.set(maxRank());
	}
	
	@Override
	public Collection<Power> getTopRanks(int minRank,int maxRank) {
		String sql = "select * from power where serverId = ? and rank >= ? and rank <= ?";
		final List<Power> list = new ArrayList<>();
		jdbc.query(sql.toString(), new Object[] { SERVER_ID, minRank,maxRank }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Power power = new Power();
				power.setPkId(arg0.getLong("pk"));
				power.serverId = arg0.getInt("serverId");
				power.rank = arg0.getInt("rank");
				power.actorId = arg0.getLong("actorId");
				list.add(power);
			}
		});
		return list;
	}
	
	@Override
	public Power getPower(long actorId) {
		if(actorId == 0){
			return null;
		}
//		if (ACTOR_POWER_MAP.containsKey(actorId)) {
//			return ACTOR_POWER_MAP.get(actorId);
//		}

		LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("actorId", actorId);
		Power power = jdbc.getFirst(Power.class, condition);
		if (power == null) {
			power = Power.valueOf(actorId, maxRank.incrementAndGet(), SERVER_ID);
			jdbc.save(power);
		}
//		ACTOR_POWER_MAP.put(actorId, power);
//		POWER_RANK_MAP.put(power.rank, actorId);

		return power;
	}
	
	/**
	 * 获取最大排名
	 * @return
	 */
	private int maxRank() {
		try {
			String sql = "SELECT max(rank) FROM power WHERE serverid = ? ";
			return jdbc.queryForInt(sql, new Object[] { SERVER_ID });
		} catch (Exception ex) {
		}
		return 0;
	}

	@Override
	public void add(long actorId, int serverId) {
		if (serverId != SERVER_ID) {
			return;
		}
		
		// 如果top_rank_map没满人，则添加该人到map中
		getPower(actorId);
//			synchronized (power) {
//				TOP_RANKS_MAP.put(power.rank, power);
//			}
	}

	@Override
	public boolean changeRank(Power power, Power targetPower) {
//		synchronized (CHANGE_RANK_LOCK) {
			
			//互换名次
			int powerRank = power.getRank(); 
			int targetPowerRank = targetPower.getRank();
			power.rank = targetPowerRank;
			targetPower.rank = powerRank;

//			
//			ACTOR_POWER_MAP.put(power.actorId, power);
//			ACTOR_POWER_MAP.put(targetPower.actorId, targetPower);
//
//			POWER_RANK_MAP.put(power.rank, power.actorId);
//			POWER_RANK_MAP.put(targetPower.rank, targetPower.actorId);
			
			//是否要替换top rank map
//			TOP_RANKS_MAP.replace(power.rank, power);
//			TOP_RANKS_MAP.replace(targetPower.rank, targetPower);
			jdbc.update(power);
			jdbc.update(targetPower);
			
			// 提交
//			dbQueue.updateQueue(power, targetPower);
//		}
		
		return true;
	}

	@Override
	public Long getActorId(int rank) {
//		Long actorId = POWER_RANK_MAP.get(rank);
		Long actorId = null;
//		if (actorId == null) {
			String sql = "SELECT actorId FROM power WHERE serverid = ? AND rank= ?";
			List<Long> list = jdbc.queryForList(sql, new Object[] { SERVER_ID, rank }, Long.class);
			if(list != null && list.size() > 0) {
				actorId = list.get(0);
			} else {
				actorId = 0L;
			}
//			POWER_RANK_MAP.put(rank, actorId);
//		}
		return actorId;
	}

	@Override
	public List<Power> getFightList(List<Integer> viewUp) {
		final List<Power> list = new ArrayList<>();
		
		List<Object> param = new ArrayList<>();
		param.add(Game.getServerId());
		param.addAll(viewUp);
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM `power` WHERE serverId = ? and rank IN (");
		for (int i = 0; i < viewUp.size(); i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		jdbc.query(sb.toString(), param.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Power power = new Power();
				power.setPkId(arg0.getLong("pk"));
				power.serverId = arg0.getInt("serverId");
				power.rank = arg0.getInt("rank");
				power.actorId = arg0.getLong("actorId");
				list.add(power);
			}
		});
		return list;
	}

	@Override
	public List<Power> getUpList(int actorRank, int upRank) {
		StringBuffer sb = new StringBuffer();
		final List<Power> list = new ArrayList<>();
		List<Object> param = new ArrayList<>();
		int topRank = Math.max(1,actorRank - upRank);
		param.add(topRank);
		param.add(actorRank);
		param.add(Game.getServerId());
		sb.append("SELECT * FROM `power` WHERE rank BETWEEN ? and ? and serverId = ?");
		jdbc.query(sb.toString(), param.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Power power = new Power();
				power.setPkId(arg0.getLong("pk"));
				power.serverId = arg0.getInt("serverId");
				power.rank = arg0.getInt("rank");
				power.actorId = arg0.getLong("actorId");
				list.add(power);
			}
		});
		return list;
	}

	@Override
	public List<Power> getDownList(int actorRank, int downRank) {
		StringBuffer sb = new StringBuffer();
		final List<Power> list = new ArrayList<>();
		List<Object> param = new ArrayList<>();
		param.add(actorRank);
		param.add(actorRank + downRank);
		param.add(Game.getServerId());
		sb.append("SELECT * FROM `power` WHERE rank BETWEEN ? and ? and serverId = ?");
		jdbc.query(sb.toString(), param.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Power power = new Power();
				power.setPkId(arg0.getLong("pk"));
				power.serverId = arg0.getInt("serverId");
				power.rank = arg0.getInt("rank");
				power.actorId = arg0.getLong("actorId");
				list.add(power);
			}
		});
		return list;
	}
	
	@Override
	public Collection<Power> getPowerList(int mustRank,int actorRank, int upRank,
			int downRank, List<Integer> viewUp) {
		StringBuffer sb = new StringBuffer();
		final List<Power> list = new ArrayList<>();
		sb.append("SELECT * FROM `power` WHERE rank <= ? and serverId = ?");
		List<Object> param = new ArrayList<>();
		param.add(mustRank);
		param.add(Game.getServerId());
		if(mustRank < actorRank){
			int topRank = Math.max(1,actorRank - upRank);
			param.add(topRank);
			param.add(actorRank + downRank);
			param.add(Game.getServerId());
			param.addAll(viewUp);
			param.add(Game.getServerId());
			sb.append(" UNION ");
			sb.append("SELECT * FROM `power` WHERE rank BETWEEN ? and ? and serverId = ?");
			sb.append(" UNION ");
			sb.append("SELECT * FROM `power` WHERE rank IN (");
			for (int i = 0; i < viewUp.size(); i++) {
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") and serverId= ?");
		}
		jdbc.query(sb.toString(), param.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Power power = new Power();
				power.setPkId(arg0.getLong("pk"));
				power.serverId = arg0.getInt("serverId");
				power.rank = arg0.getInt("rank");
				power.actorId = arg0.getLong("actorId");
				list.add(power);
			}
		});
		return list;
	}
}
