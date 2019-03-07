package com.jtang.gameserver.module.extapp.welkin.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Welkin;
import com.jtang.gameserver.module.extapp.welkin.dao.WelkinDao;

@Component
public class WelkinDaoImpl implements WelkinDao,CacheListener {

	private static ConcurrentLinkedHashMap<Long, Welkin> WELKIN_MAP = new ConcurrentLinkedHashMap.Builder<Long, Welkin>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	
	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	@Override
	public Welkin getWelkin(long actorId) {
		if(WELKIN_MAP.containsKey(actorId)){
			return WELKIN_MAP.get(actorId);
		}
		Welkin welkin = jdbc.get(Welkin.class, actorId);
		if(welkin == null){
			welkin = Welkin.valueOf(actorId);
		}
		WELKIN_MAP.put(actorId, welkin);
		return welkin; 
	}

	@Override
	public int cleanCache(long actorId) {
		WELKIN_MAP.remove(actorId);
		return WELKIN_MAP.size();
	}

	@Override
	public void update(Welkin welkin) {
		welkin.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(welkin);
	}

	@Override
	public List<Welkin> getRank(int count,int rank) {
		//TODO 不能时时查库，写法没有按照之前的规范
		String sql = "SELECT actorId,useCount,ticketUseCount,operationTime FROM welkin WHERE useCount + ticketUseCount > ? ORDER BY useCount + ticketUseCount DESC,operationTime limit ?";
		final List<Welkin> list = new ArrayList<>();
		jdbc.query(sql.toString(), new Object[] { count - 1 , rank }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Welkin welkin = new Welkin();
				welkin.actorId = arg0.getLong("actorId");
				welkin.useCount = arg0.getInt("useCount");
				welkin.ticketUseCount = arg0.getInt("ticketUseCount");
				welkin.operationTime = arg0.getInt("operationTime");
				list.add(welkin);
			}
		});
		return list;
	}

	@Override
	public List<Welkin> getRuank(int count) {
		//TODO 不能时时查库，写法没有按照之前的规范
		String sql = "SELECT actorId,useCount,ticketUseCount,operationTime FROM welkin WHERE useCount + ticketUseCount > ? ORDER BY useCount + ticketUseCount DESC,operationTime";
		final List<Welkin> list = new ArrayList<>();
		jdbc.query(sql.toString(), new Object[] { count - 1 }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				Welkin welkin = new Welkin();
				welkin.actorId = arg0.getLong("actorId");
				welkin.useCount = arg0.getInt("useCount");
				welkin.ticketUseCount = arg0.getInt("ticketUseCount");
				welkin.operationTime = arg0.getInt("operationTime");
				list.add(welkin);
			}
		});
		return list;
	}

}
