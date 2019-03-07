package com.jtang.gameserver.module.lineup.helper;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.lineup.handler.LineupCmd;
import com.jtang.gameserver.module.lineup.handler.response.PushLineupUnlockResponse;
import com.jtang.gameserver.module.story.helper.StoryPushHelper;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LineupPushHelper {
	protected static final Logger LOGGER = LoggerFactory.getLogger(StoryPushHelper.class);

	@Autowired
	PlayerSession playerSession;

	@Autowired
	Broadcast broadcast;

	private static ObjectReference<LineupPushHelper> ref = new ObjectReference<LineupPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static LineupPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushLineupUnlock(long actorId, int activedGridCount) {
		PushLineupUnlockResponse pr = new PushLineupUnlockResponse(activedGridCount);
		Response rsp = Response.valueOf(ModuleName.LINEUP, LineupCmd.PUSH_LINEUP_UNLOCK, pr.getBytes());
		getInstance().broadcast.push(actorId, rsp);
	}
}
