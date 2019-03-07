package com.jtang.gameserver.module.user.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.UserDisabled;

public interface UserDisabledDao {

	/**
	 * 封号
	 * 
	 * @param actorId
	 * @param userDisabled
	 */
	void disableUser(long actorId, UserDisabled userDisabled);

	/**
	 * 解封
	 * 
	 * @param actorId
	 */
	void enableUser(UserDisabled userDisable);

	/**
	 * 通过类型获得封号列表
	 */
	List<UserDisabled> getDisableList(long actorId, String sim, String mac, String imei, String remoteIp);

}
