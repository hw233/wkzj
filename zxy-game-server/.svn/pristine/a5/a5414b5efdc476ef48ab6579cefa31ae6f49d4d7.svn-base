package com.jtang.gameserver.module.story.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

public class StoryFightListResponse extends IoBufferSerializer {
	private List<StoryFightResponse> list;

	public StoryFightListResponse(List<StoryFightResponse> list) {
		super();
		this.list = list;
	}
	
	@Override
	public void write() {
		this.writeShort((short)list.size());
		for (StoryFightResponse item : list) {
			this.writeBytes(item.getBytes());
		}
	}
}
