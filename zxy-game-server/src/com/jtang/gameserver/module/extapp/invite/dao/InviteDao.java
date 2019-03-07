package com.jtang.gameserver.module.extapp.invite.dao;

import com.jtang.gameserver.dbproxy.entity.Invite;

public interface InviteDao {

	public Invite get(long actorId);

	long getInviteByCode(String inviteCode);
	
	boolean update(Invite invite);
}
