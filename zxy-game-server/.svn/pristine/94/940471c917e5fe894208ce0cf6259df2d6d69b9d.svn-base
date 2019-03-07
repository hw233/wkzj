package com.jtang.gameserver.module.app.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.dao.AppRecordDao;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;

@Component
public class AppRecordDaoImpl implements AppRecordDao, CacheListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppRecordDaoImpl.class);
	@Autowired
	private DBQueue dbQueue;
	
	@Autowired
	private IdTableJdbc jdbcTemplate;

	/**
	 * key 角色id
	 * value map:key 活动id
	 */
	private static ConcurrentLinkedHashMap<Long, Map<Long, AppRecord>> APP_RECORD_MAPS = new ConcurrentLinkedHashMap.Builder<Long, Map<Long, AppRecord>>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	/**
	 * 清理过期活动记录
	 */
	@PostConstruct
	private void init() {
		List<Long> appIds = new ArrayList<>();
		for(Long appId:AppRuleService.getAllApp()){
			AppRuleConfig appRuleConfig = AppRuleService.get(appId);
			if (appRuleConfig != null && (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false)) {
				appIds.add(appId);
			}
		}
		if (appIds.size() > 0) {
//			deleteTimeOutApp(appIds);
			LOGGER.info(String.format("timeout apprecord clear complete, id:[%s]", StringUtils.collection2SplitString(appIds, Splitable.BETWEEN_ITEMS)));
		}
	}
	
	@Override
	public Map<Long, AppRecord> getAll(long actorId) {
		if (APP_RECORD_MAPS.containsKey(actorId)) {
			return APP_RECORD_MAPS.get(actorId);
		}
		Map<Long, AppRecord> recordMap = new ConcurrentHashMap<Long, AppRecord>();
		LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
		condition.put("actorId", actorId);
		List<AppRecord> list = jdbcTemplate.getList(AppRecord.class, condition);
		for (AppRecord appRecord : list) {
			recordMap.put(appRecord.appId, appRecord);
			jdbcTemplate.update(appRecord);
		}
		APP_RECORD_MAPS.put(actorId, recordMap);
		return recordMap;
	}

	@Override
	public void update(AppRecord appRecord) {
		appRecord.operationTime = TimeUtils.getNow();
		dbQueue.updateQueue(appRecord);
	}

	@Override
	public AppRecord get(long actorId, long appId) {
		//key 活动id
		Map<Long, AppRecord> map = getAll(actorId);
		if (map.containsKey(appId)) {
			return map.get(appId);
		}
		
		AppRecord record = AppRecord.valueOf(actorId, appId);
		long nId = jdbcTemplate.saveAndIncreasePK(record);
		record.setPkId(nId);
		map.put(appId, record);
		return record;
	}

	@Override
	public boolean resetRecord(long actorId, long appId) {
		AppRecord appRecord = get(actorId, appId);
		appRecord.reset();
		update(appRecord);
		return true;
	}
	
	@Override
	public boolean resetRecord(long actorId) {
		Map<Long, AppRecord> all = getAll(actorId);
		for (Map.Entry<Long, AppRecord> entry : all.entrySet()) {
			AppRecord appRecord = entry.getValue();
			appRecord.reset();
			update(appRecord);
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	private void deleteTimeOutApp(List<Long> ids) {
		String sql = "delete from appRecord where appId = ?";
		List<Object[]> values = new ArrayList<>();
		for (long id : ids) {
			values.add(new Object[]{id});
		}
		jdbcTemplate.batchUpdate(sql, values);
	}
	@Override
	public <T extends BaseRecordInfoVO> T getRecordInfoVO(long actorId, long appId) {
		AppRecord appRecord = get(actorId, appId);
		return appRecord.getRecordInfoVO();
	}

	@Override
	public int cleanCache(long actorId) {
		APP_RECORD_MAPS.remove(actorId);
		return APP_RECORD_MAPS.size();
	}
	
//	public static void main(String[] args) {
//		AppRecordDaoImpl appRecordDaoImpl = (AppRecordDaoImpl) SpringContext.getBean(AppRecordDaoImpl.class);
//		AppId appId = AppId.getById(1001L);
//		appRecordDaoImpl.delete(62914567, appId);
//	}

}
