package com.jtang.gameserver.module.love.dao;

import com.jtang.gameserver.dbproxy.entity.LoveShop;

public interface LoveShopDao {

	public LoveShop get(long actorId);
	
	public void update(LoveShop loveShop);

	public void remove(LoveShop loveShop);

	
}
