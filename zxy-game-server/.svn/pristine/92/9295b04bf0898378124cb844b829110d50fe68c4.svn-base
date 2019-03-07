package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.app.AppInit;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
/**
 * 活动记录
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为自增id,一个角色多条活动记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="appRecord", type=DBQueueType.DEFAULT)
public class AppRecord extends Entity<Long> {
	private static final long serialVersionUID = 6540124264466677893L;

	/**
	 * 自增id
	 */
	@Column(pk = true)
	private long id;
	
	/**
	 * 角色id
	 */
	@Column
	public long actorId;
	
	/**
	 * 活动id
	 */
	@Column
	public long appId;
	
	/**
	 * 扩展记录
	 */
	@Column
	private String recordInfo = "";
	
	/**
	 * 操作时间
	 */
	@Column
	public int operationTime;
	
	/**
	 * recordInfo序列化后的对象
	 */
	private BaseRecordInfoVO recordInfoVO;
	
	@Override
	public Long getPkId() {
		return this.id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		AppRecord appRecord = new AppRecord();
		appRecord.id = resultset.getLong("id");
		appRecord.actorId = resultset.getLong("actorId");
		appRecord.appId = resultset.getLong("appId");
		appRecord.recordInfo = resultset.getString("recordInfo");
		appRecord.operationTime = resultset.getInt("operationTime");
		return appRecord;
	}

	@Override
	protected void hasReadEvent() {
		recordInfoVO = AppInit.getRecordInfoVO(appId, this.recordInfo);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.id);
			value.add(this.actorId);
			value.add(this.appId);
			value.add(this.recordInfo);
			value.add(this.operationTime);
		} else {
			value.add(this.actorId);
			value.add(this.appId);
			value.add(this.recordInfo);
			value.add(this.operationTime);
		}
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		if (recordInfoVO != null) {
			this.recordInfo = recordInfoVO.parse2String();
		} else {
			this.recordInfo = "";
		}
	}

	public static AppRecord valueOf(long actorId, long appId) {
		AppRecord appRecord = new AppRecord();
		appRecord.actorId = actorId;
		appRecord.appId = appId;
		BaseRecordInfoVO vo = AppInit.getRecordInfoVO(appId, appRecord.recordInfo);
		if (vo != null) {
			appRecord.recordInfoVO = vo.valueOf(appRecord.recordInfo);
		}
		appRecord.operationTime = TimeUtils.getNow();
		return appRecord;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRecordInfoVO() {
		if (recordInfoVO == null) {
			recordInfoVO = AppInit.getRecordInfoVO(appId, this.recordInfo);
		}
		return (T) recordInfoVO;
	}

	public void reset() {
		this.recordInfo = "";
		this.operationTime = 0;
		this.recordInfoVO = AppInit.getRecordInfoVO(appId, this.recordInfo);
	}
	
	@Override
	protected void disposeBlob() {
		this.recordInfo = EMPTY_STRING;
	}
}
