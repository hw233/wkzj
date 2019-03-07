package com.jtang.gameserver.module.hole.dao;

import com.jtang.gameserver.dbproxy.entity.HoleNotify;

public interface HoleNotifyDao {
	HoleNotify get(long actorId);
	void update(HoleNotify holeNotify);
}
