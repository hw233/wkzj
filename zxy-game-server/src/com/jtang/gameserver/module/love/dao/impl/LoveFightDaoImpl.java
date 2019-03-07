package com.jtang.gameserver.module.love.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.LoveRank;
import com.jtang.gameserver.module.love.dao.LoveFightDao;

@Component
public class LoveFightDaoImpl implements LoveFightDao {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	/**
	 * 服务器启动时获取最大的排名
	 */
	private AtomicInteger maxRank = new AtomicInteger();
	
	@PostConstruct
	public void init() {
		//缓存最大排名
		maxRank.set(maxRank());
	}
	
	/**
	 * 获取最大排名
	 * @return
	 */
	private int maxRank() {
		try {
			String sql = "SELECT max(rank) FROM loveRank";
			return jdbc.queryForInt(sql);
		} catch (Exception ex) {
		}
		return 0;
	}
	
	@Override
	public LoveRank get(long actorId1,long actorId2) {
		String sql = "select * from loveRank where loveId1 = ? or loveId2 = ?";
		List<LoveRank> list = jdbc.getList(sql, new Object[]{actorId1,actorId1}, LoveRank.class);
		if(list.size() == 0){
			LoveRank loveRank = LoveRank.valueOf(actorId1,actorId2,maxRank.incrementAndGet());
			jdbc.save(loveRank);
			return loveRank;
		}
		return list.get(0);
	}

	@Override
	public Collection<LoveRank> getTopRanks(int minRank, int maxRank) {
		String sql = "select * from loveRank where rank >= ? and rank <= ?";
		final List<LoveRank> list = new ArrayList<>();
		jdbc.query(sql.toString(), new Object[] { minRank,maxRank }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				LoveRank loveRank = new LoveRank();
				loveRank.loveId1 = rs.getLong("loveId1");
				loveRank.loveId2 = rs.getLong("loveId2");
				loveRank.rank = rs.getInt("rank");
				loveRank.fightInfo = rs.getString("fightInfo");
				list.add(loveRank);
			}
		});
		return list;
	}

	@Override
	public Collection<LoveRank> getRankList(int viewRank, int rank, int viewUp, int viewDown, List<Integer> upList) {
		StringBuffer sb = new StringBuffer();
		final List<LoveRank> list = new ArrayList<>();
		sb.append("SELECT * FROM `loveRank` WHERE rank <= ?");
		List<Object> param = new ArrayList<>();
		param.add(viewRank);
		if(viewRank < rank){
			int topRank = Math.max(1,rank - viewUp);
			param.add(topRank);
			param.add(rank + viewDown);
			param.addAll(upList);
//			param.add(Game.getServerId());
			sb.append(" UNION ");
			sb.append("SELECT * FROM `loveRank` WHERE rank BETWEEN ? and ?");
			sb.append(" UNION ");
			sb.append("SELECT * FROM `loveRank` WHERE rank IN (");
			for (int i = 0; i < upList.size(); i++) {
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		jdbc.query(sb.toString(), param.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				LoveRank loveRank = new LoveRank();
				loveRank.loveId1 = rs.getLong("loveId1");
				loveRank.loveId2 = rs.getLong("loveId2");
				loveRank.rank = rs.getInt("rank");
				loveRank.fightInfo = rs.getString("fightInfo");
				list.add(loveRank);
			}
		});
		return list;
	}

	@Override
	public void changeRank(LoveRank loveRank1, LoveRank loveRank2) {
		int powerRank = loveRank1.rank; 
		int targetPowerRank = loveRank2.rank;
		loveRank1.rank = targetPowerRank;
		loveRank2.rank = powerRank; 
		jdbc.update(loveRank1);
		jdbc.update(loveRank2);
	}

	@Override
	public void formatTable() {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE `loveRank` AS p,"); 
		sql.append("(SELECT a.loveId1, a.rank, @rowId:=@rowId+1 AS rowId ");
		sql.append("FROM (SELECT loveId1, rank FROM `loveRank` ORDER BY rank ASC) AS a,");
		sql.append("(SELECT @rowId:=0) AS t) AS p1 ");
		sql.append("SET p.rank = p1.rowId WHERE p.loveId1 = p1.loveId1");
		jdbc.execute(sql.toString());
	}

	@Override
	public void remove(long actorId1,long actorId2) {
		LoveRank rank = get(actorId1, actorId2);
		jdbc.delete(rank);
	}

}
