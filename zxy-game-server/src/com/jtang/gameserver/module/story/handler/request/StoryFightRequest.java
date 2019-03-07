package com.jtang.gameserver.module.story.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class StoryFightRequest extends IoBufferSerializer {
	
	/**
	 * 盟友id
	 */
	public long allyId;

	/**
	 * 扫荡次数
	 */
	public int fightNum;
	
	/**
	 * 扫荡关卡id
	 */
	public int battleId;
	
	public StoryFightRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		allyId = readLong();
		fightNum = readInt();
		battleId = readInt();
	}
}
