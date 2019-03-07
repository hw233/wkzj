package com.jtang.gameserver.module.extapp.randomreward.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.randomreward.constant.RandomRewardRule;
import com.jtang.gameserver.module.extapp.randomreward.facade.RandomRewardFacade;
import com.jtang.gameserver.module.extapp.randomreward.handler.request.GetRewardRequest;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.GetRewardResponse;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.RandomRewardResponse;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class RandomRewardHandler extends GatewayRouterHandlerImpl {
	
	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	RandomRewardFacade randomRewardFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.RANDOM_REWARD;
	}
	
	@Cmd(Id = RandomRewardCmd.RANDOM_REWARD_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		int level = ActorHelper.getActorLevel(actorId);
		if(level < RandomRewardRule.RANDOM_REWARD_OPEN_LEVEL){
			response.setStatusCode(GameStatusCodeConstant.RANDOM_REWARD_NOT_USE);
			sessionWrite(session, response);
			return;
		}
		TResult<RandomRewardResponse> result = randomRewardFacade.getInfo(actorId);
		sessionWrite(session, response,result.item);
	}
	
	@Cmd(Id = RandomRewardCmd.GET_REWARD)
	public void getReward(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		int level = ActorHelper.getActorLevel(actorId);
		if(level < RandomRewardRule.RANDOM_REWARD_OPEN_LEVEL){
			response.setStatusCode(GameStatusCodeConstant.RANDOM_REWARD_NOT_USE);
			sessionWrite(session, response);
			return;
		}
		GetRewardRequest request = new GetRewardRequest(bytes);
		TResult<GetRewardResponse> result = randomRewardFacade.getReward(actorId,request.id);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}

}
