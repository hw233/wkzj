package com.jtang.gameserver.module.msg.facade;

import java.util.List;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Message;

public interface MsgFacade {

	/**
	 * 获取角色消息列表
	 * @param actorId
	 * @return
	 */
	List<Message> get(long actorId);
	
	/**
	 * 发送消息
	 * @param fromActorId
	 * @param toActorId
	 * @param content
	 * @return
	 */
	TResult<Message> sendMsg(long fromActorId, long toActorId, String content);
	TResult<Message> sendMsg2one(long fromActorId, long toActorId, String content);
	
	/**
	 * 删除
	 * @param actorId
	 * @param mIdList
	 * @return
	 */
	Result removeMsg(long actorId, List<Long> mIdList);
	
	/**
	 * 设为已读
	 * @param actorId
	 * @return
	 */
	Result setReaded(long actorId);
}
