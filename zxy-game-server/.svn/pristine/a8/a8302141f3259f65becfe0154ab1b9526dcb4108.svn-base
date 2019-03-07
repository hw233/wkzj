package com.jtang.gameserver.module.adventures.bable.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.db.ErrorEntityBackup;
import com.jtang.gameserver.dataconfig.service.BableService;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.BableRecord;
import com.jtang.gameserver.module.adventures.bable.constant.BableRule;
import com.jtang.gameserver.module.adventures.bable.dao.BableRecordDao;
@Component
public class BableRecordDaoImpl implements BableRecordDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(BableRecordDaoImpl.class);
	@Autowired
	private IdTableJdbc jdbcTemplate;
	
	@Autowired
	private ErrorEntityBackup entityBackup;
	
	/**
	 * key:bableId
	 */
	private Map<Integer, List<BableRecord>> yesterdayRanks = new HashMap<Integer, List<BableRecord>>();
	
	@Override
	public BableRecord get(long actorId, int bableId) {
		int statDate = getTodayYYYYMMMDD();
		LinkedHashMap<String, Object> condition = new LinkedHashMap<>();
		condition.put("actorId", actorId);
		condition.put("bableId", bableId);
		condition.put("statDate", statDate);
		
		BableRecord bableRecord = jdbcTemplate.getFirst(BableRecord.class, condition );
		if (bableRecord == null) {
			bableRecord  = BableRecord.valueOf(actorId, bableId, statDate);
		}
		return bableRecord;
	}

	@Override
	public List<BableRecord> getRank(int bableId) {
		if (yesterdayRanks.containsKey(bableId)) {
			return yesterdayRanks.get(bableId);
		}
		return new ArrayList<>();
	}
	
	private List<BableRecord> getRankFromDB(int bableId) {
		int statDate = getYesterdayYYYYMMDD();
		Object[] condition = new Object[]{ bableId, statDate, BableRule.BABLE_RANK_NUM};
		String sql = "SELECT * FROM bableRecord where bableId = ? and statDate = ? ORDER BY maxFloor DESC,maxStar DESC, useTime ASC LIMIT ?";
		List<BableRecord> bableRecords = jdbcTemplate.getList(sql, condition , BableRecord.class);
		return bableRecords;
	}

	@Override
	public int update(BableRecord bableRecord) {
		try {
			return jdbcTemplate.update(bableRecord);
		} catch (Exception e) {
			String tableName = "bableRecord";
			LOGGER.error(String.format("save db error. actorId:%s,bableId:[%s] tableName:[%s], entity drop.", bableRecord.actorId, bableRecord.bableId, tableName), e);
			entityBackup.write(bableRecord, tableName);
			return 0;
		}
	}

	@Override
	public void createRank() {
		Set<Integer> ids = BableService.getBABLE_ID_LIST();
		for (Integer id : ids) {
			List<BableRecord> records = getRankFromDB(id);
			yesterdayRanks.put(id, records);
		}
	}

	@Override
	public Map<Integer, List<BableRecord>> allRankRecords() {
		return this.yesterdayRanks;
	}
	
	/**
	 * 转换日期格式为yyyymm
	 * @param date
	 * @return
	 */
	private int getYYYYMMDD(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(date);
		return Integer.valueOf(str);
		
	}
	
	/**
	 * 获取今天的统计时间（20140108）
	 * @return
	 */
	private int getTodayYYYYMMMDD() {
		return getYYYYMMDD(new Date());
	}
	/**
	 * 获取昨天的统计时间（20140107）
	 * @return
	 */
	private int getYesterdayYYYYMMDD(){
		Calendar zeroTime = Calendar.getInstance();
		zeroTime.add(Calendar.DAY_OF_MONTH, -1);
		zeroTime.set(Calendar.MINUTE, 0);
		zeroTime.set(Calendar.SECOND, 0);
		zeroTime.set(Calendar.MILLISECOND, 0);
		return getYYYYMMDD(zeroTime.getTime());
	}

}
