package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.gameserver.module.applog.type.LogType;

@TableName(name = "appLog", type = DBQueueType.DEFAULT)
public class AppLog extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3727042281240722736L;

	/**
	 * id
	 */
	@Column(pk = true)
	public long id;
	
	/**
	 * 活动id
	 */
	@Column
	public int appId;
	
	/**
	 * 活动内容
	 */
	@Column
	public String appRecord;
	
	/**
	 * 活动开始时间
	 */
	@Column
	public int startTime;
	
	/**
	 * 活动结束时间
	 */
	@Column
	public int endTime;
	
	@Override
	public Long getPkId() {
		return id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		AppLog log = new AppLog();
		log.id = rs.getLong("id");
		log.appId = rs.getInt("appId");
		log.appRecord = rs.getString("appRecord");
		log.startTime = rs.getInt("startTime");
		log.endTime = rs.getInt("endTime");
		return log;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(id);
		}
		values.add(appId);
		values.add(appRecord);
		values.add(startTime);
		values.add(endTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	public static AppLog valueOf(LogType logType, String appRecord,int startTime,int endTime) {
		AppLog log = new AppLog();
		log.appId = logType.getCode();
		log.appRecord = appRecord;
		log.startTime = startTime;
		log.endTime = endTime;
		return log;
	}
	
	@Override
	protected void disposeBlob() {
		
	}

}
