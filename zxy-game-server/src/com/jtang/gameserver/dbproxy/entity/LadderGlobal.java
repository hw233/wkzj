package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 天梯赛季表
 * @author jianglf
 *
 */
@TableName(name = "ladderGlobal", type = DBQueueType.DEFAULT)
public class LadderGlobal extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2901704333269459532L;
	
	@Column(pk = true)
	private long id;
	
	/**
	 * 赛季开始时间
	 */
	@Column
	public int startTime;

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
		LadderGlobal ladderGlobal = new LadderGlobal();
		ladderGlobal.id = rs.getLong("id");
		ladderGlobal.startTime = rs.getInt("startTime");
		return ladderGlobal;
	}

	@Override
	protected void hasReadEvent() {
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.id);
		}
		values.add(this.startTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	@Override
	protected void disposeBlob() {
		
	}

	public static LadderGlobal valueOf(int now) {
		LadderGlobal ladderGlobal = new LadderGlobal();
		ladderGlobal.startTime = now;
		return ladderGlobal;
	}

}
