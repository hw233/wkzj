package com.jtang.gameserver.module.adventures.favor.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.favor.handler.FavorCmd;
import com.jtang.gameserver.module.adventures.favor.handler.response.FavorResponse;
import com.jtang.gameserver.module.adventures.favor.model.FavorVO;
import com.jtang.gameserver.server.broadcast.Broadcast;
/**
 * 
 * @author pengzy
 *
 */
@Component
public class FavorPushHelper {

	@Autowired
	Broadcast broadcast;

	private static ObjectReference<FavorPushHelper> ref = new ObjectReference<FavorPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static FavorPushHelper getInstance() {
		return ref.get();
	}


	public static void pushActiveFavor(long actorId, FavorVO favorVO) {
		FavorResponse packet = new FavorResponse(favorVO);
		Response response = Response.valueOf(ModuleName.FAVOR, FavorCmd.PUSH_FAVOR_ACTIVE, packet.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

	public static void pushDisapperFavor(long actorId) {
		Response response = Response.valueOf(ModuleName.FAVOR, FavorCmd.CHANGE_FAVOR_DISAPPER);
		getInstance().broadcast.push(actorId, response);
	}

	public static void pushReset(long actorId, FavorVO favorVO) {
		FavorResponse packet = new FavorResponse(favorVO);
		Response response = Response.valueOf(ModuleName.FAVOR, FavorCmd.PUSH_RESET, packet.getBytes());
		getInstance().broadcast.push(actorId, response);
	}

}
