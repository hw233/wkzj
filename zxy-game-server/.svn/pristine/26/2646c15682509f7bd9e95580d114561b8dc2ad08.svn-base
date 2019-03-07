package com.jtang.gameserver.module.story.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class StoryInfoResponse extends IoBufferSerializer {
	
	/**
	 * 消耗的点券
	 */
	public int ticket;
	
	/**
	 * 扫荡符数量
	 */
	public int goodsNum;
	
	public StoryInfoResponse(int ticket,int goodsNum){
		this.ticket = ticket;
		this.goodsNum = goodsNum;
	}
	
	@Override
	public void write() {
		writeInt(ticket);
		writeInt(goodsNum);
	}
}
