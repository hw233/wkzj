package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.StringUtils;

/**
 * 问答活动表
 * @author jianglf
 *
 */
@TableName(name="questions", type = DBQueueType.IMPORTANT)
public class Questions extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 125620272286559242L;

	/**
	 * 玩家id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * 活动记录
	 * 问题id_结果|...
	 */
	@Column
	public String record;
	
	/**
	 * 已使用答题次数
	 */
	@Column
	public int useNum;
	
	/**
	 * 操作时间
	 */
	@Column
	public int operationTime;
	
	public Map<Integer,Integer> recordMap = new ConcurrentHashMap<>();
	
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Questions questions = new Questions();
		questions.actorId = rs.getLong("actorId");
		questions.record = rs.getString("record");
		questions.useNum = rs.getInt("useNum");
		questions.operationTime = rs.getInt("operationTime");
		return questions;
	}

	@Override
	protected void hasReadEvent() {
		recordMap = StringUtils.delimiterString2IntMap(record);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.actorId);
		}
		values.add(this.record);
		values.add(this.useNum);
		values.add(this.operationTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		record = StringUtils.numberMap2String(recordMap);
	}

	@Override
	protected void disposeBlob() {
		this.record = EMPTY_STRING;
	}

	public static Questions valueOf(long actorId) {
		Questions questions = new Questions();
		questions.actorId = actorId;
		return questions;
	}

}
