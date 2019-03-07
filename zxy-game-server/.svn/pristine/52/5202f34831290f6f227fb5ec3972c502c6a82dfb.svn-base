package com.jtang.gameserver.module.love.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.love.handler.LoveCmd;
import com.jtang.gameserver.module.love.handler.response.AcceptListResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveFightResponse;
import com.jtang.gameserver.module.love.handler.response.LoveGiftResponse;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterBossResponse;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterFightResponse;
import com.jtang.gameserver.module.love.handler.response.LoveMonsterInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveShopInfoResponse;
import com.jtang.gameserver.module.love.handler.response.MarriedResponse;
import com.jtang.gameserver.module.love.model.MarryAcceptVO;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LovePushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<LovePushHelper> ref = new ObjectReference<LovePushHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static LovePushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushMarry(long actorId, long loveActorId, String loveActorName,IconVO loveActorIcon) {
		MarriedResponse response = new MarriedResponse(loveActorId, loveActorName, loveActorIcon);
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_MARRY_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushAcceptList(long actorId, List<MarryAcceptVO> acceptList) {
		AcceptListResponse response = new AcceptListResponse(acceptList);
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_ACCEPT_LIST,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	public static void pushGift(long actorId, byte hasGift, byte hasGive) {
		LoveGiftResponse loveGiftResponse = new LoveGiftResponse(hasGift, hasGive);
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_GIFT_STATE,loveGiftResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushFightData(long actorId,LoveFightResponse loveFightResponse){
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.LOVE_FIGHT,loveFightResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushLoveRankReset(Long actorId) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_LOVE_FIGHT_RESET);
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushLoveFightInfo(long acotrId,LoveFightInfoResponse response) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_FIGHT_INFO,response.getBytes());
		getInstance().playerSession.push(acotrId, rsp);
	}

	public static void pushBossHp(long actorId,int id,int hp) {
		LoveMonsterBossResponse response = new LoveMonsterBossResponse(id,hp);
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.PUSH_BOSS_HP,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushBossBattle(long actorId,LoveMonsterFightResponse response) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.LOVE_MONSTER_FIGHT,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushLoveMonsterInfo(long actorId, LoveMonsterInfoResponse response) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.GET_LOVE_MONSTER_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushShopInfo(Long actorId, LoveShopInfoResponse response) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.LOVE_SHOP_INFO,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	public static void pushBossFightError(Long actorId, short status) {
		Response rsp = Response.valueOf(ModuleName.LOVE, LoveCmd.LOVE_MONSTER_FIGHT, status);
		getInstance().playerSession.push(actorId, rsp);
	}

}
