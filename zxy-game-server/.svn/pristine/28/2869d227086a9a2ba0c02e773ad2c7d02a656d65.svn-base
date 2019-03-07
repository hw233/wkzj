package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.gameserver.module.icon.model.IconVO;

/**
 * 私人消息
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为mId,由Id表分配自增id
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name = "message", type = DBQueueType.NONE)
public class Message extends Entity<Long> {
	private static final long serialVersionUID = -5753950483748457641L;

	/**
	 * 自增messageid
	 */
	@Column(pk = true)
	private long mId;
	
	/**
	 * 发送者角色id
	 */
	@Column
	public long fromActorId;
	
	/**
	 * 接收者角色id
	 */
	@Column
	public long toActorId;
	
	/**
	 * 消息内容(60个字)
	 */
	@Column
	public  String content;
	
	/**
	 * 发送时间
	 */
	@Column
	public int sendTime;
	
	/**
	 * 是否已读，0为未读，1为已读，发送给客户端的时候转成byte
	 */
	@Column
	public int isReaded;
	
	/**
	 * 在发送信息时临时设定
	 */
	public String fromActorName;
	
	/**
	 * 在发送信息时临时设定
	 */
	public int fromActorLevel;
	
	/**
	 * 发送者仙人id
	 */
	public IconVO iconVO;
	
	/**
	 * 对方vip等级
	 */
	public int vipLevel;

	@Override
	public Long getPkId() {
		return this.mId;
	}

	@Override
	public void setPkId(Long pk) {
		this.mId = pk;
	}

	public static Message valueOf(long fromActorId, long toActorId, String content, int sendTime) {
		Message msg = new Message();
		msg.fromActorId = fromActorId;
		msg.toActorId = toActorId;
		msg.content = content;
		msg.sendTime = sendTime;
		msg.isReaded = 0;
		return msg;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Message msg = new Message();
		msg.mId = rs.getLong("mId");
		msg.fromActorId = rs.getLong("fromActorId");
		msg.toActorId = rs.getLong("toActorId");
		msg.content = rs.getString("content");
		msg.sendTime = rs.getInt("sendTime");
		msg.isReaded = rs.getInt("isReaded");
		return msg;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(mId);
		} 
		value.add(fromActorId);
		value.add(toActorId);
		value.add(content);
		value.add(sendTime);
		value.add(isReaded);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}
}
