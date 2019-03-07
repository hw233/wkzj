package com.jtang.gameserver.admin.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Stories;

public interface StoryMaintianFacade {
	
	/**
	 * 添加战场
	 * @param actorId
	 * @return
	 */
	public TResult<Stories> addNextStory(long actorId,int star);
	
	/**
	 * 删除战场
	 * @param actorId
	 * @return
	 */
	public TResult<Stories> deleteLastStory(long actorId);
}
