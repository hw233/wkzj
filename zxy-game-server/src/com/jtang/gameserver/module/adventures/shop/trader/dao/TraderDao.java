package com.jtang.gameserver.module.adventures.shop.trader.dao;

import com.jtang.gameserver.dbproxy.entity.Trader;

public interface TraderDao {

	public Trader get(long actorId);

	public void update(Trader trader);

}
