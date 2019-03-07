package com.jtang.gameserver.module.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.module.app.dao.AppGlobalDao;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;

@Component
public class AppGlobalDaoImpl implements AppGlobalDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppGlobalDaoImpl.class);
	@Autowired
	private IdTableJdbc jdbcTemplate;

	@Autowired
	private DBQueue dbQueue;

	private static ConcurrentLinkedHashMap<Long, AppGlobal> APP_GLOBAL_MAPS = new ConcurrentLinkedHashMap.Builder<Long, AppGlobal>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();

	/**
	 * 清理过期活动记录
	 */
	@PostConstruct
	private void init() {
		List<Long> appIds = new ArrayList<>();
		for (Long appId : AppRuleService.getAllApp()) {
			AppRuleConfig appRuleConfig = AppRuleService.get(appId);
			if (appRuleConfig != null && (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false)) {
				appIds.add(appId);
			}
		}
		if (appIds.size() > 0) {
//			deleteTimeOutApp(appIds);
			LOGGER.info(String.format("timeout appGlobal clear complete, id:[%s]",
					StringUtils.collection2SplitString(appIds, Splitable.BETWEEN_ITEMS)));
		}
	}

	@Override
	public AppGlobal get(long appId) {
		if (APP_GLOBAL_MAPS.containsKey(appId)) {
			return APP_GLOBAL_MAPS.get(appId);
		}
		AppGlobal appConfig = jdbcTemplate.get(AppGlobal.class, appId);
		if (appConfig == null) {
			appConfig = AppGlobal.valueOf(appId);
			jdbcTemplate.save(appConfig);
		}
		APP_GLOBAL_MAPS.put(appId, appConfig);
		return appConfig;
	}

	@Override
	public void update(AppGlobal appGlobal) {
		appGlobal.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(appGlobal);
	}

	@Override
	public long getMaxLevelOfAcotr() {
		String sql = "SELECT actorId FROM `actor` WHERE serverId = ? ORDER BY level DESC,reputation DESC LIMIT ?";
		Object[] params = new Object[] { Game.getServerId(), 1 };
		try {
			return jdbcTemplate.queryForLong(sql, params);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	@Override
	public long getMaxScoreOfActor() {
		String sql = "SELECT actorId FROM `snatch` ORDER BY score DESC LIMIT 1";
		return jdbcTemplate.queryForLong(sql);
	}

	@Override
	public long getMaxPowerOfActor() {
		String sql = "SELECT actorId FROM `power`  WHERE serverId = ?  ORDER BY rank ASC LIMIT ?";
		Object[] params = new Object[] { Game.getServerId(), 1 };
		
		try {
			return jdbcTemplate.queryForLong(sql, params);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	@Override
	public List<Long> getMaxPayMoney(int startTime, int endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT actorId ,SUM(payMoney) AS 'totalMoney'");
		sql.append(" ");
		sql.append("FROM rechargeLog a JOIN actor b ON a.serverid=b.serverid AND a.platformId=b.platformType AND a.uid=b.uid");
		sql.append(" ");
		sql.append("WHERE rechargeTime>=? AND rechargeTime<? GROUP BY actorid");
		sql.append(" ");
		sql.append("ORDER BY SUM(payMoney)DESC LIMIT 3");
		final List<Long> list = new ArrayList<>();
		jdbcTemplate.query(sql.toString(), new Object[] { startTime, endTime }, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet arg0) throws SQLException {
				long actorId = arg0.getLong("actorId");
				list.add(actorId);
			}
		});
		return list;
	}

	@SuppressWarnings("unused")
	private void deleteTimeOutApp(List<Long> ids) {
		String sql = "delete from appGlobal where appId = ?";
		List<Object[]> values = new ArrayList<>();
		for (long id : ids) {
			values.add(new Object[] { id });
		}
		jdbcTemplate.batchUpdate(sql, values);
	}

	@Override
	public <T extends BaseGlobalInfoVO> T getGloabalInfoVO(long appId) {
		AppGlobal appGloabal = get(appId);
		return appGloabal.getGlobalInfoVO();
	}

}
