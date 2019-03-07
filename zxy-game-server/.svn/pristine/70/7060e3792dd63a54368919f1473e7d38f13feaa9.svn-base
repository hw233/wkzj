package com.jtang.gameserver.module.ladder.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Ladder;
import com.jtang.gameserver.dbproxy.entity.LadderGlobal;
import com.jtang.gameserver.module.ladder.dao.LadderDao;

@Component
public class LadderDaoImpl implements LadderDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Ladder> LADDER_MAP = new ConcurrentLinkedHashMap.Builder<Long, Ladder>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Override
	public Ladder get(long actorId) {
		if(LADDER_MAP.containsKey(actorId)){
			return LADDER_MAP.get(actorId);
		}
		Ladder ladder = jdbc.get(Ladder.class, actorId);
		if(ladder == null){
			ladder = Ladder.valueOf(actorId);
			dbQueue.updateQueue(ladder);
		}
		LADDER_MAP.put(actorId, ladder);
		return ladder;
	}
	
	@Override
	public void update(Ladder ladder) {
		dbQueue.updateQueue(ladder);
	}

	@Override
	public int cleanCache(long actorId) {
		LADDER_MAP.remove(actorId);
		return LADDER_MAP.size();
	}

	@Override
	public List<Long> getRank(long sportId,int rewardRank) {
		final List<Long> list = new ArrayList<>();
//		String sql = "SELECT actorId FROM ladder WHERE sportId = ? ORDER BY (win-lost) DESC limit ?";
		String sql = "SELECT actorId FROM ladder WHERE sportId = ? ORDER BY score DESC limit ?";
		jdbc.query(sql, new Object[] { sportId, rewardRank } ,new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				list.add(rs.getLong("actorId"));
			}
		});
		return list;
	}
	
	@Override
	public SortedMap<Integer, ArrayList<Long>> getAllRank(long sportId) {
		final SortedMap<Integer, ArrayList<Long>> sortedMap = new TreeMap<Integer, ArrayList<Long>>();
		String sql = "SELECT actorId,score FROM ladder WHERE sportId = ? ORDER BY score DESC,lastFightTime ASC";
		jdbc.query(sql, new Object[] { sportId } ,new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Integer key = rs.getInt("score");
				Long perValue = rs.getLong("actorId");
				if (sortedMap.containsKey(key) == false) {
					sortedMap.put(key, new ArrayList<Long>());
				}
				sortedMap.get(key).add(perValue);
			}
		});
		return sortedMap;
	}

	@Override
	public LadderGlobal getLadderGlobal() {
		LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
		LadderGlobal ladderGlobal = jdbc.getFirst(LadderGlobal.class, condition);
		return ladderGlobal;
	}

	@Override
	public long save(int now) {
		LadderGlobal ladderGlobal = getLadderGlobal();
		if(ladderGlobal != null){
			jdbc.delete(ladderGlobal);
		}
		LadderGlobal global = LadderGlobal.valueOf(now);
		return jdbc.saveAndIncreasePK(global);
	}


}
