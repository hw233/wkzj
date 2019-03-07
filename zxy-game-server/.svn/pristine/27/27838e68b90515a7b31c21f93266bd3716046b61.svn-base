package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 好评实体
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="praise", type = DBQueueType.IMPORTANT)
public class Praise extends Entity<Long> {
	private static final long serialVersionUID = -4210509031595658803L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 是否激活（0未激活，1激活）
	 */
	@Column
	public byte isActive;
	
	/**
	 * 是否已领取激活奖励（0未领取，1领取）
	 */
	@Column
	public byte activeRewardGet;
	
	/**
	 * 是否已领取好评奖励（0未领取，1领取）
	 */
	@Column
	public byte praiseRewardGet;
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;

	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		Praise praise = new Praise();
		praise.actorId = resultset.getLong("actorId");
		praise.isActive = resultset.getByte("isActive");
		praise.activeRewardGet = resultset.getByte("activeRewardGet");
		praise.praiseRewardGet = resultset.getByte("praiseRewardGet");
		return praise;
	}

	@Override
	protected void hasReadEvent() {

	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.isActive);
		value.add(this.activeRewardGet);
		value.add(this.praiseRewardGet);

		return value;
	}

	@Override
	protected void beforeWritingEvent() {

	}

	public static Praise valueOf(long actorId) {
		Praise comment = new Praise();
		comment.actorId = actorId;
		return comment;
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}
