package com.jtang.gameserver.module.msg.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Message;
/**
 * 消息推送
 * @author pengzy
 *
 */
public class MsgResponse extends IoBufferSerializer{

	private Message msg;
	public MsgResponse(){}
	public MsgResponse(Message msg){
		this.msg = msg;
	}
	@Override
	public void write() {
		writeMsg(msg);
	}
	protected void writeMsg(Message msg) {
		writeLong(msg.getPkId());
		writeLong(msg.fromActorId);
		writeLong(msg.toActorId);
		writeString(msg.content);
		writeInt(msg.sendTime);
		writeByte((byte)msg.isReaded);
		writeString(msg.fromActorName);
		writeInt(msg.fromActorLevel);
		writeBytes(msg.iconVO.getBytes());
		writeInt(msg.vipLevel);
	}
}
