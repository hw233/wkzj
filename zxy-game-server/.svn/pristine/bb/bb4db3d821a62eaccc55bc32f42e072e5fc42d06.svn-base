package com.jtang.gameserver.module.user.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.RechargeLog;
import com.jtang.gameserver.module.user.dao.RechargeLogDao;

@Component
public class RechargeLogDaoImpl implements RechargeLogDao {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private IdTableJdbc xJdbcTemplate;
	
	private ConcurrentLinkedHashMap<String, RechargeLog> LOG_MAPS = new ConcurrentLinkedHashMap.Builder<String, RechargeLog>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	private ConcurrentLinkedHashMap<String, List<RechargeLog>> ACTOR_LOG_MAPS = new ConcurrentLinkedHashMap.Builder<String, List<RechargeLog>>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	@Override
	public int save(RechargeLog log) {
		if (LOG_MAPS.containsKey(log.orderSnid)) {
			return 1;
		} else {
			LOG_MAPS.put(log.orderSnid, log);
			String key = getKey(log.platformId, log.serverId, log.uid);
			List<RechargeLog> list = null;
			if (ACTOR_LOG_MAPS.containsKey(key)) {
				list = ACTOR_LOG_MAPS.get(key);
			} else {
				list = new ArrayList<>();
			}
			list.add(log);
		}
		try {
			xJdbcTemplate.save(log);
			return 0;
		} catch (DuplicateKeyException e) {
			LOGGER.error("数据库错误：", e);
			return 1;
		} catch (Exception e) {
			LOGGER.error("数据库错误：", e);
			return 2;
		}
	}

	@Override
	public RechargeLog getByOrderSid(String orderSid) {
		if (LOG_MAPS.containsKey(orderSid)){
			return LOG_MAPS.get(orderSid);
		}
		LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
		condition.put("orderSnid", orderSid);
		List<RechargeLog> orders = xJdbcTemplate.getList(RechargeLog.class, condition);
		if (orders.size() > 0){
			RechargeLog rechargeLog = orders.get(0);
			LOG_MAPS.put(rechargeLog.orderSnid, rechargeLog);
			return rechargeLog;
		}
		return null;
	}

	
	/**
	 * platformId-serverId-uid
	 * @return
	 */
	public String getKey(int platformId, int serverId, String uid) {
		StringBuffer sb = new StringBuffer();
		sb.append(platformId);
		sb.append(serverId);
		sb.append(uid);
		return sb.toString();
	}

	@Override
	public int getPaymoneyTotal(int platformId, int serverId, String uid, int start, int end) {
		String sql = "select sum(payMoney) from rechargeLog where platformId = ? and serverId = ? and uid = ? and rechargeTime between ? and ?";
		return xJdbcTemplate.queryForInt(sql, platformId, serverId, uid, start, end);
	}
	
	
}
