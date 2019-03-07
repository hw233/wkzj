package com.jtang.gameserver.module.msg.handler.response;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Message;
/**
 * 获取我的消息列表
 * @author pengzy
 *
 */
public class MsgListResponse extends MsgResponse{

	private List<Message> msgList;
	
	public MsgListResponse(List<Message> msgList){
		this.msgList = msgList;
	}
	
	@Override
	public void write() {
		writeShort((short)msgList.size());
		for(Message msg : msgList){
			writeMsg(msg);
		}
	}
}
