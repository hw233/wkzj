package com.jtang.gameserver.module.user.facade;

import com.jtang.gameserver.dbproxy.entity.Online;

/**
 * 角色在线接口
 * @author 0x737263
 *
 */
public interface OnlineFacade {

	/**
	 * 添加在线
	 * @param actorId
	 * @param sim
	 * @param mac
	 * @param imei
	 * @param ip
	 */
	void add(long actorId, String sim, String mac, String imei, String ip);
	
	/**
	 * 删除在线数据
	 * @param actorId
	 * @return
	 */
	Online remove(long actorId);
	
}
