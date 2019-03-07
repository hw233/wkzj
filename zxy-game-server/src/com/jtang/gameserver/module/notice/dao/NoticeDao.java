package com.jtang.gameserver.module.notice.dao;


public interface NoticeDao {
	
	/**
	 * 能否使用广播
	 * @param actorId
	 * @return
	 */
	boolean ableBroadcast(long actorId);
	
	/**
	 * 广播成功，修改已广播次数
	 * @param actorId
	 * @return
	 */
	void broadcastSuccess(long actorId);
}
