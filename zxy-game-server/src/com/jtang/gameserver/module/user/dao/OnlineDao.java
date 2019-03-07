package com.jtang.gameserver.module.user.dao;

import com.jtang.gameserver.dbproxy.entity.Online;

/**
 * 在线数据访问接口
 * @author 0x737263
 *
 */
public interface OnlineDao {

	/**
	 * 获取在线信息
	 * @param actorId
	 * @return
	 */
	Online get(long actorId);
	
	/**
	 * 服务器启动时清除online表
	 * @param serverId	游戏服id
	 * @return	返回清除记录数
	 */
	int cleanOnline(int serverId);
	
	/**
	 * 添加一条记录
	 * @param online
	 */
	void add(Online online);
	
	/**
	 * 删除一条记录
	 * @param actorId
	 */
	void remove(long actorId);
	
}
