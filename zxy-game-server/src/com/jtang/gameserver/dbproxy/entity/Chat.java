package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

/**
 * 禁言列表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author lig
 * 
 */
@TableName(name="chat", type = DBQueueType.IMPORTANT)
public class Chat extends Entity<Long>{
	private static final long serialVersionUID = 2934083195275960745L;

	/**
	 * 角色id  主键
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 禁言开始时间int字段
	 */
	@Column
	public int forbiddenTime;
	
	/**
	 * 禁言结束时间int字段
	 */
	@Column
	public int unforbiddenTime;
	
	
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
		Chat chat = new Chat();
		chat.actorId = rs.getLong("actorId");
		chat.forbiddenTime = rs.getInt("forbiddenTime");
		chat.unforbiddenTime = rs.getInt("unforbiddenTime");
		return chat;
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
		value.add(this.forbiddenTime);
		value.add(this.unforbiddenTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
	}

	public static Chat valueOf(long actorId2) {
		Chat chat = new Chat();
		chat.actorId = actorId2;
		chat.forbiddenTime = 0;
		chat.unforbiddenTime = 0;
		return chat;
	}

	@Override
	protected void disposeBlob() {
		
	}

}
