package com.jtang.gameserver.module.msg.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 通知客户端删除Message
 * @author pengzy
 *
 */
public class MsgRemoveResponse  extends IoBufferSerializer{

	/**
	 * 被删除的Message的Id列表
	 */
	private List<Long> deletedMsgList;
	
	public MsgRemoveResponse(List<Long> deletedMsgList){
		this.deletedMsgList = deletedMsgList;
	}
	@Override
	public void write() {
		writeLongList(deletedMsgList);
	}

}
