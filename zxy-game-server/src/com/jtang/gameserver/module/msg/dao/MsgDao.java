package com.jtang.gameserver.module.msg.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Message;

public interface MsgDao {

	/**
	 * 创建消息，需保存到数据库
	 * @param fromActorId
	 * @param toActorId
	 * @param content
	 * @param sendTime
	 */
	public Message createMsg(long fromActorId, long toActorId, String content, int sendTime);
	/**
	 * 获取角色消息列表
	 * @param toActorId
	 * @return
	 */
	public List<Message> get(long toActorId);
	/**
	 * 删除超过条数限制的信息
	 * @param toActorId
	 * @param mId
	 * @return
	 */
	public List<Long> removeOldMessage(long toActorId, long mId);
	/**
	 * 设为已读
	 * @param toActorId
	 */
	public void setReaded(long toActorId);
	/**
	 * 删除
	 * @param actorId
	 * @param mIdList
	 */
	public void remove(long actorId, List<Long> mIdList);
	
	/**
	 * 获取我发出的消息
	 */
	public List<Message> getSendMsg(long fromActorId);
}
