package com.jtang.gameserver.module.story.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.story.handler.request.ClearStoryAwardRequest;
import com.jtang.gameserver.module.story.handler.request.StartBattleRequest;
import com.jtang.gameserver.module.story.handler.request.StoryFightRequest;
import com.jtang.gameserver.module.story.handler.response.StoryFightListResponse;
import com.jtang.gameserver.module.story.handler.response.StoryInfoResponse;
import com.jtang.gameserver.module.story.handler.response.StoryResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 故事模块handler
 * @author vinceruan
 *
 */
@Component
public class StoryHandler extends GatewayRouterHandlerImpl {
	@Autowired
	StoryFacade storyFacade;
	
	@Autowired
	BattleFacade battleFacade;

	@Override
	public byte getModule() {
		return ModuleName.STORY;
	}
	
	@Cmd(Id=StoryCmd.LOAD_STORY)
	public void loadStory(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Stories story = storyFacade.get(actorId);
		StoryResponse res = new StoryResponse(story);
		sessionWrite(session, response, res);
	}
	
	@Cmd(Id=StoryCmd.START_BATTLE)
	public void startBattle(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		StartBattleRequest request = new StartBattleRequest(bytes);
		Result res = storyFacade.startBattle(actorId, request.battleId, request.allyActorId);
		if (res.isFail()) {
			response.setStatusCode(res.statusCode);
			sessionWrite(session, response);
		}
	}
	
	@Cmd(Id=StoryCmd.CLEAR_STORY_AWARD)
	public void clearStoryAward(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ClearStoryAwardRequest request = new ClearStoryAwardRequest(bytes);
		Result res = storyFacade.clearStoryAward(actorId, request.storyId, request.rewardType);
		response.setStatusCode(res.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id=StoryCmd.STORY_FIGHT)
	public void storyFight(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		StoryFightRequest request = new StoryFightRequest(bytes);
		TResult<StoryFightListResponse> result = storyFacade.storyFight(actorId,request.allyId,request.fightNum,request.battleId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = StoryCmd.BUY_FIGHT_GOODS)
	public void buyFightGoods(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = storyFacade.buyFightGoods(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = StoryCmd.GET_STORY_FIGHT)
	public void getFightInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<StoryInfoResponse> result = storyFacade.getFightInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
}
