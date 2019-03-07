package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.gameserver.module.user.type.DisableType;

/**
 * 帐号禁用表
 * @author 0x737263
 *
 */
@TableName(name="UserDisabled", type = DBQueueType.NONE)
public class UserDisabled extends Entity<Long> {
	private static final long serialVersionUID = 7414440466174101579L;

	@Column(pk=true)
	public long id;

	/**
	 * 1.actorId,2.sim,3.mac,4.imei,5.ip
	 */
	@Column
	public int disabledType;

	/**
	 * 禁用值
	 */
	@Column
	public String value;

	/**
	 * 开始时间(utc)秒
	 */
	@Column
	public Date beginTime;

	/**
	 * 结束时间(utc)秒
	 */
	@Column
	public Date endTime;

	@Override
	public Long getPkId() {
		return id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		UserDisabled userDisabled = new UserDisabled();
		userDisabled.id = rs.getLong("id");
		userDisabled.disabledType = rs.getInt("disabledType");
		userDisabled.value = rs.getString("value");
		userDisabled.beginTime = rs.getDate("beginTime");
		userDisabled.endTime = rs.getDate("endTime");
		return userDisabled;
	}

	@Override
	protected void hasReadEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<Object>();
		if(containsPK){
			values.add(id);
		}
		values.add(disabledType);
		values.add(value);
		values.add(beginTime);
		values.add(endTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		// TODO Auto-generated method stub
		
	}

	public static UserDisabled valueOf(long actorId,Date endTime) {
		UserDisabled userDisabled = new UserDisabled();
		userDisabled.disabledType = DisableType.ACTOR_ID.getCode();
		userDisabled.value = String.valueOf(actorId);
		userDisabled.beginTime = new Date();
		userDisabled.endTime = endTime;
		return userDisabled;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
