package com.jtang.gameserver.module.adventures.shop.vipshop.dao;

import com.jtang.gameserver.dbproxy.entity.VipShop;

public interface VipShopDao {

	VipShop get(long actorId);

	void update(VipShop vipShop);

}
