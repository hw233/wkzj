package com.jtang.gameserver.module.extapp.onlinegifts.dao;

import com.jtang.gameserver.dbproxy.entity.OnlineGifts;

public interface OnlineGiftsDao {

	/**
	 * 获取
	 * @param actorId
	 * @return
	 */
	OnlineGifts get(long actorId);

	/**
	 * 更新
	 * @param gifts
	 * @return
	 */
	boolean update(OnlineGifts gifts);
}
