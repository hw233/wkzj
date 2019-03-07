package com.jtang.gameserver.module.notify.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.notify.handler.NotifyCmd;
import com.jtang.gameserver.module.notify.handler.response.RemoveNotifyResponse;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class NotifyPushHelper {
	@Autowired
	Broadcast broadcast;
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<NotifyPushHelper> REF = new ObjectReference<NotifyPushHelper>();

	@PostConstruct
	private void init() {
		REF.set(this);
	}

	private static NotifyPushHelper getInstance() {
		return REF.get();
	}

	/**
	 * 
	 * @param actorId
	 * @param notifyIds
	 */
	public static void pushRemoveNotify(long actorId, List<Long> notifyIds) {
		if(notifyIds.size() <  1) {
			return;
		}
		
		RemoveNotifyResponse packet = new RemoveNotifyResponse(notifyIds);
		Response response = Response.valueOf(ModuleName.NOTIFY, NotifyCmd.PUSH_REMOVE_NOTIFY, packet.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

	/**
	 * 
	 * @param actorId
	 * @param Notify
	 */
	public static void pushNotify(long actorId, AbstractNotifyVO notifyVO) {
		if (getInstance().playerSession.isOnline(actorId)) {
			Response response = Response.valueOf(ModuleName.NOTIFY, NotifyCmd.RECEIVE_NOFITY, notifyVO.getBytes());
			getInstance().broadcast.push(actorId, response);
		}
	}
}
