package com.jtang.gameserver.module.icon.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.icon.hander.IconCmd;
import com.jtang.gameserver.module.icon.hander.response.FramUnLockResponse;
import com.jtang.gameserver.module.icon.hander.response.IconUnLockRespones;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class IconPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<IconPushHelper> ref = new ObjectReference<IconPushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static IconPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushUnLockFram(long actorId, List<Integer> list) {
		FramUnLockResponse response = new FramUnLockResponse(list);
		Response rsp = Response.valueOf(ModuleName.ICON, IconCmd.PUSH_FRAM_UNLOCK,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushUnLockIcon(long actorId, int heroId) {
		IconUnLockRespones response = new IconUnLockRespones(heroId);
		Response rsp = Response.valueOf(ModuleName.ICON, IconCmd.PUSH_ICON_UNLOCK,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushLockIcon(long actorId,int heroId){
		IconUnLockRespones response = new IconUnLockRespones(heroId);
		Response rsp = Response.valueOf(ModuleName.ICON, IconCmd.PUSH_ICON_LOCK,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
