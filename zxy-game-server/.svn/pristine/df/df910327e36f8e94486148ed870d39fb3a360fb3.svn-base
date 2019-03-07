package com.jtang.gameserver.module.story.helper;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.story.handler.StoryCmd;
import com.jtang.gameserver.module.story.handler.response.BattleDataResponse;
import com.jtang.gameserver.module.story.handler.response.UpdateBattleStarResponse;
import com.jtang.gameserver.module.story.handler.response.UpdateStoryStarResponse;
import com.jtang.gameserver.module.story.model.StoryBattleResult;
//import com.jtang.sm2.module.story.handler.response.OccupiedBattleResponse;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 故事模块推送类
 * 
 * @author vinceruan
 * 
 */
@Component
public class StoryPushHelper {
	protected static final Logger LOGGER = LoggerFactory.getLogger(StoryPushHelper.class);
	
	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<StoryPushHelper> ref = new ObjectReference<StoryPushHelper>();
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static StoryPushHelper getInstance() {
		return ref.get();
	}
	
	/**
	 * 推送战斗结果
	 * @param battleResult
	 */
	public static void pushBattleResult(long actorId, StoryBattleResult battleResult) {		
		BattleDataResponse res = new BattleDataResponse(battleResult);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("\r\n" + res.format("") + "\r\n\r\n");
		}
		Response rsp = Response.valueOf(ModuleName.STORY, StoryCmd.START_BATTLE, res.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 战斗处理失败也需要下发
	 * @param actorId
	 * @param statusCode
	 */
	public static void pushBattleFailResult(long actorId, short statusCode) {
		Response rsp = Response.valueOf(ModuleName.STORY, StoryCmd.START_BATTLE);
		rsp.setStatusCode(statusCode);
		getInstance().playerSession.push(actorId, rsp);
	}
	
	
	/**
	 * 推送故事的星数
	 * @param storyId
	 * @param star
	 */
	public static void pushStoryStar(long actorId, int storyId, byte star) {
		UpdateStoryStarResponse res = new UpdateStoryStarResponse(storyId, star);
		Response rsp = Response.valueOf(ModuleName.STORY, StoryCmd.PUSH_STORY_STAR, res.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送战场的星数
	 * @param actorId
	 * @param storyId
	 * @param star
	 */
	public static void pushBattleStar(long actorId, int battleId, byte star) {
		UpdateBattleStarResponse res = new UpdateBattleStarResponse(battleId, star);
		Response rsp = Response.valueOf(ModuleName.STORY, StoryCmd.PUSH_BATTLE_STAR, res.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
//	/**
//	 * 推送已占领的新战场
//	 * @param actorId
//	 * @param battleId
//	 */
//	public static void pushOccupiedBattle(long actorId, int battleId) {
//		OccupiedBattleResponse res = new OccupiedBattleResponse(battleId);
//		Response rsp = Response.valueOf(ModuleName.STORY, StoryCmd.PUSH_OCCUPIED_BATTLE, res.getBytes());
//		getInstance().broadcast.push(actorId, rsp);
//	}
}
