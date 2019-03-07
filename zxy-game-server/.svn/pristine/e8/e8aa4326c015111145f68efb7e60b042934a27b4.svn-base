package com.jtang.gameserver.module.story.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送故事的星数(注意:只有故事的星数比原来增加了才会推送)
 * @author vinceruan
 *
 */
public class UpdateStoryStarResponse extends IoBufferSerializer {
	/**
	 * 故事ID
	 */
	int storyId;
	
	/**
	 * 星数
	 */
	byte star;

	@Override
	public void write() {
		writeInt(storyId);
		writeByte(star);
	}
	
	public UpdateStoryStarResponse(int storyId, byte star) {
		this.storyId = storyId;
		this.star = star;
	}
}
