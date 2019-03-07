package com.jtang.gameserver.module.adventures.bable.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.bable.handler.BableCmd;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableBattleResultResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableStarResponse;
import com.jtang.gameserver.module.adventures.bable.model.BableBattleResult;
import com.jtang.gameserver.server.broadcast.Broadcast;

@Component
public class BablePushHelper {

	@Autowired
	Broadcast broadcast;
	
	private static ObjectReference<BablePushHelper> ref = new ObjectReference<BablePushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	public static void pushBableBattleResult(long actorId,BableBattleResult bableBattleResult) {
		BableBattleResultResponse bableBattleResultResponse = new BableBattleResultResponse(bableBattleResult);
		Response response = Response.valueOf(ModuleName.BABLE, BableCmd.START_BATTLE, bableBattleResultResponse.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
	
	public static void pushBableStar(long actorId,int star,int useStar){
		BableStarResponse bableStarResponse = new BableStarResponse(star,useStar);
		Response response = Response.valueOf(ModuleName.BABLE, BableCmd.PUSH_BABLE_STAR,bableStarResponse.getBytes());
		ref.get().broadcast.push(actorId, response);
	}
	
	public static void pushBableReset(long actorId){
		Response rsp = Response.valueOf(ModuleName.BABLE, BableCmd.PUSH_BABLE_RESET);
		ref.get().broadcast.push(actorId,rsp);
	}
	
}
