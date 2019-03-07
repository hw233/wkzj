package com.jtang.gameserver.module.notice.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.notice.handler.NoticeCmd;
import com.jtang.gameserver.module.notice.handler.response.NoticeResponse;
import com.jtang.gameserver.module.notice.model.NoticeVO;
import com.jtang.gameserver.module.user.facade.ActorFetchFacade;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 
 * @author pengzy
 * 
 */
@Component
public class NoticePushHelper {
	@Autowired
	Broadcast broadcast;

	@Autowired
	private PlayerSession playerSession;

	@Autowired
	private ActorFetchFacade actorFetchFacade;

	private static ObjectReference<NoticePushHelper> ref = new ObjectReference<NoticePushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static NoticePushHelper getInstance() {
		return ref.get();
	}

	public static void broadcastNotice(List<Integer> channelIds, NoticeVO notice) {
		NoticeResponse allyAddResponse = new NoticeResponse(notice);
		Response response = Response.valueOf(ModuleName.NOTICE, NoticeCmd.BROADCAST_NOTICE, allyAddResponse.getBytes());
		if (channelIds.isEmpty()) {
			getInstance().broadcast.push2AllOnline(response);
		} else {
			List<Long> ids = getInstance().actorFetchFacade.getOnlineActorIds(channelIds);
			for (long actorId : ids) {
				getInstance().playerSession.push(actorId, response);
			}
		}
	}

	public static void broadcastDemonNotice(long actorId, NoticeVO notice) {
		NoticeResponse noticeResponse = new NoticeResponse(notice);
		Response response = Response.valueOf(ModuleName.NOTICE, NoticeCmd.PRIVATE_NOTICE, noticeResponse.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
}
