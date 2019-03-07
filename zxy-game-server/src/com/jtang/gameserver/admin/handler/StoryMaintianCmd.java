package com.jtang.gameserver.admin.handler;


public interface StoryMaintianCmd {

	/**
	 * 增加战场
	 * {@code AddStoryRequest}
	 * {@code AddStoryResponse}
	 */
	byte ADD_STORY = 1;
	
	/**
	 * 删除战场
	 * {@code Request}
	 * {@code DeleteStoryResponse}
	 */
	byte DEL_STORY = 2;
	
}
