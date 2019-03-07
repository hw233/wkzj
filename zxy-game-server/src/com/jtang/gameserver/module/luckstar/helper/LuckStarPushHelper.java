package com.jtang.gameserver.module.luckstar.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.luckstar.handler.LuckStarCmd;
import com.jtang.gameserver.module.luckstar.handler.response.LuckStarResponse;
import com.jtang.gameserver.module.luckstar.module.LuckStarVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LuckStarPushHelper {

	@Autowired
	PlayerSession playerSession;

	@Autowired
	Broadcast broadcast;

	private static ObjectReference<LuckStarPushHelper> ref = new ObjectReference<LuckStarPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static LuckStarPushHelper getInstance() {
		return ref.get();
	}

	public static void push(long actorId, LuckStarVO luckStarVO) {
		LuckStarResponse response = new LuckStarResponse(luckStarVO);
		Response rsp = Response.valueOf(ModuleName.LUCKSTAR, LuckStarCmd.PUSH_LUCK_STAR, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
