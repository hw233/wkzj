package com.jtang.gameserver.module.extapp.beast.helper;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.extapp.beast.handler.BeastCmd;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastAttackResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastInfoResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class BeastPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<BeastPushHelper> ref = new ObjectReference<BeastPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static BeastPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushBeastInfo(long actorId, BeastInfoResponse response){
		Response rsp = Response.valueOf(ModuleName.BEAST, BeastCmd.INFO, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushBeastAttacker(long actorId, byte status, byte percent)  {
		BeastStatusResponse response = new BeastStatusResponse(status, percent);
		Response rsp = Response.valueOf(ModuleName.BEAST, BeastCmd.STATUS, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushAllBeastAttacker(Collection<Long> beastActorList, byte status, byte percent)  {
		BeastStatusResponse response = new BeastStatusResponse(status, percent);
		Response rsp = Response.valueOf(ModuleName.BEAST, BeastCmd.STATUS, response.getBytes());
		getInstance().playerSession.push(beastActorList, rsp);
	}
	
	public static void pushBeastBattleResponse(long actorId, BeastAttackResponse response) {
		Response rsp = Response.valueOf(ModuleName.BEAST, BeastCmd.ACTTACK, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送攻击boss失败
	 * @param actorId
	 * @param result
	 */
	public static void attackBossFail(long actorId, Result result) {
		Response response = Response.valueOf(ModuleName.BEAST, BeastCmd.ACTTACK);
		response.setStatusCode(result.statusCode);
		ref.get().playerSession.push(actorId, response);
	}
	
}
