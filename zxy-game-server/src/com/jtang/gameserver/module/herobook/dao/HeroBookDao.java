package com.jtang.gameserver.module.herobook.dao;

import com.jtang.gameserver.dbproxy.entity.HeroBook;

public interface HeroBookDao {
	HeroBook get(long actorId);
	boolean update(HeroBook heroBook);
}
