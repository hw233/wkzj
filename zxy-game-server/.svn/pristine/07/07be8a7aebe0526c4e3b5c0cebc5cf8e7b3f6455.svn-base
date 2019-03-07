package com.jtang.gameserver.module.story.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 故事通关奖励
 * @author vinceruan
 *
 */
public class ClearStoryAwardRequest extends IoBufferSerializer {
	
	/**
	 * 故事id
	 */
	public int storyId;
	
	/**
	 * 奖励类型:1:通关奖励，2:2星奖励，3:3星奖励
	 * 
	 */
	public byte rewardType;
	
	public ClearStoryAwardRequest(byte bytes[]) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.storyId = this.readInt();
		this.rewardType = this.readByte();		
	}
	
}
