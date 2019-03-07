package com.jtang.gameserver.module.extapp.invite.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jtang.core.cache.CacheListener;
import com.jtang.core.cache.CacheRule;
import com.jtang.core.db.DBQueue;
import com.jtang.gameserver.dbproxy.IdTableJdbc;
import com.jtang.gameserver.dbproxy.entity.Invite;
import com.jtang.gameserver.module.extapp.invite.dao.InviteDao;

@Component
public class InviteDaoImpl implements InviteDao,CacheListener {

	@Autowired
	IdTableJdbc jdbc;
	@Autowired
	DBQueue dbQueue;
	
	private static ConcurrentLinkedHashMap<Long, Invite> INVITE_MAP = new ConcurrentLinkedHashMap.Builder<Long, Invite>()
			.maximumWeightedCapacity(CacheRule.LRU_CACHE_SIZE).build();
	

	@Override
	public Invite get(long actorId) {
		if(INVITE_MAP.containsKey(actorId)){
			return INVITE_MAP.get(actorId);
		}
		Invite invite = jdbc.get(Invite.class, actorId);
		if(invite == null){
			invite = Invite.valueOf(actorId);
			update(invite);
		}
		INVITE_MAP.put(actorId, invite);
		return invite;
	}


	@Override
	public long getInviteByCode(String inviteCode) {
		try {
			String sql = "SELECT actorId FROM invite WHERE inviteCode = ? ";
			return jdbc.queryForLong(sql, new Object[] { inviteCode });
		} catch (Exception ex) {
		}
		return 0;
	}

	@Override
	public boolean update(Invite invite) {
		dbQueue.updateQueue(invite);
		return true;
	}

	@Override
	public int cleanCache(long actorId) {
		INVITE_MAP.remove(actorId);
		return INVITE_MAP.size();
	}
}
