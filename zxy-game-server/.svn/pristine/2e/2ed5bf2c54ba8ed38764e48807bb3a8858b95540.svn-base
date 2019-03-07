package com.jtang.gameserver.module.notify.dao;

import com.jtang.gameserver.dbproxy.entity.FightVideo;
import com.jtang.gameserver.module.notify.type.FightVideoRemoveType;

/**
 * 
 * @author 0x737263
 *
 */
public interface FightVideoDao {

	/**
	 * 获取录像
	 * @param actorId
	 * @param notifyId
	 * @return
	 */
	FightVideo get(long actorId, long notifyId);
	
	/**
	 * 添加录像
	 * @param actorId
	 * @param notifyId
	 * @param videoData
	 */
	long create(long actorId, long notifyId, byte[] videoData);
	
	/**
	 * 移除录像
	 * @param actorId
	 * @param notifyId
	 * @param type
	 */
	void remove(long actorId, long notifyId,FightVideoRemoveType type);

	/**
	 *  获取录像
	 * @param fightVideoId
	 */
	byte[] get(long fightVideoId);
}
