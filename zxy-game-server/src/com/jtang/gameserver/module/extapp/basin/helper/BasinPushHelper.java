package com.jtang.gameserver.module.extapp.basin.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dataconfig.model.BasinConfig;
import com.jtang.gameserver.dataconfig.service.BasinService;
import com.jtang.gameserver.module.extapp.basin.handler.BasinCmd;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinResponse;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;
import com.jtang.gameserver.module.extapp.basin.model.BasinVO;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class BasinPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<BasinPushHelper> ref = new ObjectReference<BasinPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static BasinPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushBasinState(long actorId,int recharege,List<BasinVO> reward){
		BasinConfig basinConfig = BasinService.getGlobalConfig();
		BasinResponse response = new BasinResponse(recharege,basinConfig.end,reward);
		Response rsp = Response.valueOf(ModuleName.BASIN, BasinCmd.GET_BASIN,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushBasinState(long actorId,boolean state){
		BasinStateResponse response = new BasinStateResponse(state);
		Response rsp = Response.valueOf(ModuleName.BASIN, BasinCmd.IS_OPEN,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}
