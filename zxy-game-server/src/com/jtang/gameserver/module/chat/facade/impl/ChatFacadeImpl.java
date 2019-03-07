package com.jtang.gameserver.module.chat.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.CHAT_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.CHAT_INTERVAL_TIME_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.CHAT_IN_FORBIDDEN;
import static com.jiatang.common.GameStatusCodeConstant.CHAT_UN_VALID_MSG;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.component.event.AddEquipEvent;
import com.jtang.gameserver.component.event.AddHeroEvent;
import com.jtang.gameserver.component.event.ChatEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.OpenBoxEvent;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.ChatService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Chat;
import com.jtang.gameserver.module.adventures.achievement.processor.AbstractAchieve;
import com.jtang.gameserver.module.chat.constant.ChatRule;
import com.jtang.gameserver.module.chat.dao.ChatDao;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;
import com.jtang.gameserver.module.chat.helper.ChatHelper;
import com.jtang.gameserver.module.chat.helper.ChatPushHelper;
import com.jtang.gameserver.module.chat.helper.MessageHelper;
import com.jtang.gameserver.module.chat.type.SystemChatType;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;

@Component
public class ChatFacadeImpl extends AbstractAchieve implements ChatFacade,Receiver {
	
	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private ChatDao chatDao;

	private static final int CHAT_HISTORY_NUM = 40;

	private static ConcurrentLinkedQueue<ChatResponse> HISTORY_MSG = new ConcurrentLinkedQueue<ChatResponse>();

	@Override
	public void register() {
		eventBus.register(EventKey.ADD_EQUIP, this);
		eventBus.register(EventKey.ADD_HERO, this);
		eventBus.register(EventKey.OPEN_BOX, this);
	}

	/**
	 * 添加聊天内容
	 * 
	 * @param chatResponse
	 */
	@Override
	public void putChat(ChatResponse chatResponse) {
		if (HISTORY_MSG.size() > 0 && HISTORY_MSG.size() >= CHAT_HISTORY_NUM) {
			HISTORY_MSG.remove();
		}
		HISTORY_MSG.add(chatResponse);
	}

	@Override
	public List<ChatResponse> getChat() {
		List<ChatResponse> msgList = new ArrayList<ChatResponse>();
		msgList.addAll(HISTORY_MSG);
		return msgList;
	}

	@Override
	public Result chat(long actorId, String msg) {
		
		Chat chat = chatDao.get(actorId);
		if (MessageHelper.isForbidden(chat.forbiddenTime, chat.unforbiddenTime)) {
			return Result.valueOf(CHAT_IN_FORBIDDEN);
		}
		
		if (MessageHelper.checkIntervalTime(actorId) == false) {
			return Result.valueOf(CHAT_INTERVAL_TIME_LIMIT);
		}

		boolean isValid = MessageHelper.isValid(msg);
		if (!isValid) {
			return Result.valueOf(CHAT_UN_VALID_MSG);
		}

		Actor actor = actorFacade.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		int needGolds = FormulaHelper.executeInt(
				ChatRule.CHAT_CONSUME_GOLDS_EXPR, actor.level);
		if (!GmService.isGm(actorId)) {
			if (!actorFacade.decreaseGold(actorId, GoldDecreaseType.CHAT,
					needGolds)) {
				return Result.valueOf(CHAT_GOLD_NOT_ENOUGH);
			}
		}
		ChatResponse rsp = ChatHelper.getActorMsgResponse(actor.actorName,
				actor.getPkId(), actor.level, msg, vipLevel);
		putChat(rsp);

		ChatPushHelper.broabcastMsg(rsp);

		// 抛出聊天事件
		eventBus.post(new ChatEvent(actorId));
		return Result.valueOf();
	}

	@Override
	public void onEvent(Event paramEvent) {
		GameEvent gameEvent = paramEvent.convert();
		Actor actor = actorFacade.getActor(gameEvent.actorId);
		String actorName = actor.actorName;
		int vipLevel = vipFacade.getVipLevel(actor.getPkId());
		if (paramEvent.getName() == EventKey.ADD_EQUIP) {
			addEquip(paramEvent, actor, actorName, vipLevel);
		}
		if (paramEvent.getName() == EventKey.ADD_HERO) {
			addHero(paramEvent, actor, actorName, vipLevel);
		}
		if (paramEvent.getName() == EventKey.OPEN_BOX) {
			openBox(paramEvent, actor, actorName, vipLevel);
		}
	}

	private void addEquip(Event paramEvent, Actor actor, String actorName,
			int vipLevel) {
		AddEquipEvent addEquipEvent = paramEvent.convert();
		EquipConfig equipConfig = addEquipEvent.equipConfig;
		EquipAddType equipAddType = addEquipEvent.equipAddType;
		boolean isAddType = ChatService.isAddEquipType(equipAddType.getId());
		boolean isEquip = ChatService.isSendEquip(equipConfig);
		if (isEquip && isAddType) {
			ChatResponse rsp = ChatHelper.getEquipHeroResponse(actorName,
					actor.getPkId(), actor.level, vipLevel,
					SystemChatType.EQUIP.getCode(), equipConfig.getEquipId(),
					equipAddType.getId());
			ChatPushHelper.broabcastMsg(rsp);
			putChat(rsp);
		}
	}

	private void addHero(Event paramEvent, Actor actor, String actorName,
			int vipLevel) {
		AddHeroEvent addHeroEvent = paramEvent.convert();
		HeroConfig heroConfig = addHeroEvent.config;
		HeroAddType heroAddType = addHeroEvent.addType;
		boolean isAddType = ChatService.isAddHeroType(heroAddType.getId());
		if (heroConfig.getStar() >= ChatService.getHeroStar() && isAddType) {
			ChatResponse rsp = ChatHelper.getEquipHeroResponse(actorName,
					actor.getPkId(), actor.level, vipLevel,
					SystemChatType.HERO.getCode(), heroConfig.getHeroId(),
					heroAddType.getId());
			ChatPushHelper.broabcastMsg(rsp);
			putChat(rsp);
		}
	}

	private void openBox(Event paramEvent, Actor actor, String actorName,
			int vipLevel) {
		OpenBoxEvent openBoxEvent = paramEvent.convert();
		int boxId = openBoxEvent.boxId;
		List<UseGoodsResult> results = new ArrayList<UseGoodsResult>(openBoxEvent.results);
		List<UseGoodsResult> sendList = new ArrayList<>();
		for (UseGoodsResult goodsResult : results) {
			boolean isOk = false;
			switch (goodsResult.type) {
			case EQUIP:
				isOk = ChatService.isSendEquip(EquipService.get(goodsResult.id));
				break;
			case HERO:
				HeroConfig heroConfig = HeroService.get(goodsResult.id);
				isOk = heroConfig.getStar() >= ChatService.getHeroStar();
				break;
			case HERO_SOUL:
				HeroConfig heroSoulConfig = HeroService.get(goodsResult.id);
				isOk = heroSoulConfig.getStar() >= ChatService.getHeroStar();
				break;
			case GOODS:
				break;
			default:
				break;
			}
			if (isOk) {
				sendList.add(goodsResult);
			}
		}
		if (sendList.isEmpty() == false) {
			ChatResponse rsp = ChatHelper.getOpenBoxResponse(actorName,
					actor.getPkId(), actor.level, vipLevel, boxId, sendList);
			ChatPushHelper.broabcastMsg(rsp);
			putChat(rsp);
		}
	}

	@Override
	public Result sendDemonBossChat(String actorName, long actorId, int level,
			int vipLevel, String boosName, List<RewardObject> reward) {
		ChatResponse chat = ChatHelper.getDemonChatResponse(actorName, actorId,
				level, vipLevel, boosName, reward);
		ChatPushHelper.broabcastMsg(chat);
		putChat(chat);
		return Result.valueOf();
	}

	@Override
	public Result sendPowerChat(String actorName, long actorId, int level,
			int vipLevel, int isWin, int targetLevel, int targetVipLevel,
			String targetName, int isFirst) {
		ChatResponse chat = ChatHelper
				.getPowerResponse(actorName, actorId, level, vipLevel, isWin,
						targetLevel, targetVipLevel, targetName, isFirst);
		ChatPushHelper.broabcastMsg(chat);
		putChat(chat);
		return Result.valueOf();
	}

	@Override
	public Result sendNoticeChat(String actorName, long actorId, int level,
			int vipLevel,String otherActorName,
			int otherLevel, int otherVipLevel, int num, int isWin) {
		ChatResponse rsp = ChatHelper.getSnatchResponse(actorName, actorId,
				level, vipLevel,otherActorName, otherLevel,
				otherVipLevel, Math.abs(num), isWin);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
		return Result.valueOf();
	}

	@Override
	public Result sendDemonWinChat(long actorId,
			List<RewardObject> firstDemonReward,
			List<RewardObject> winCampReward) {
		Actor actor = actorFacade.getActor(actorId);
		String actorName = actor.actorName;
		int level = actor.level;
		int vipLevel = vipFacade.getVipLevel(actorId);
		ChatResponse rsp = ChatHelper.getDemonWinResponse(actorName, actorId,
				level, vipLevel, firstDemonReward, winCampReward);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
		return Result.valueOf();
	}

	@Override
	public Result sendMazeTreasureChat(long actorId, RewardObject rewardObject) {
		Actor actor = actorFacade.getActor(actorId);
		String actorName = actor.actorName;
		int level = actor.level;
		int vipLevel = vipFacade.getVipLevel(actorId);
		ChatResponse rsp = ChatHelper.getTreasureResponse(actorName, actorId,
				level, vipLevel, rewardObject);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
		return Result.valueOf();
	}

	@Override
	public Result sendHeroBookChat(long actorId, int num, int heroStar,
			List<RewardObject> rewardObject) {
		Actor actor = actorFacade.getActor(actorId);
		String actorName = actor.actorName;
		int level = actor.level;
		int vipLevel = vipFacade.getVipLevel(actorId);
		ChatResponse rsp = ChatHelper.getHeroBookResponse(actorName, actorId,
				level, vipLevel, num, heroStar, rewardObject);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
		return Result.valueOf();
	}

	@Override
	public Result sendTreasureChat(long actorId, long targetId, int equipType,
			int equipId) {
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		int targetVipLevel = vipFacade.getVipLevel(targetId);
		EquipConfig equipConfig = EquipService.get(equipId);
		int limitLevel = ChatRule.TREASURE_LIMIT_LEVEL;
		ChatResponse rsp = ChatHelper.getTreasureChatResponse(actor.actorName,
				actor.getPkId(), actor.level, vipLevel, targetId,
				targetVipLevel, targetActor.actorName, targetActor.level,
				equipConfig.getEquipType().getId(), equipId,limitLevel);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
		return Result.valueOf();
	}

	
	@Override
	public Result sendPlantChat(long actorId,int plantId, RewardObject rewardObject) {
		Actor actor = actorFacade.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		boolean isSend = false;
		switch (rewardObject.rewardType) {
		case EQUIP:
			EquipConfig equipConfig = EquipService.get(rewardObject.id);
			isSend = ChatService.isSendEquip(equipConfig);
			break;
		case HEROSOUL:
			HeroConfig heroConfig = HeroService.get(rewardObject.id);
			isSend = ChatService.getHeroStar() <= heroConfig.getStar();
			break;
		default:
			break;
		}
		if(isSend){
			ChatResponse rsp = ChatHelper.getPlantChatResponse(actor.actorName, actorId, actor.level, vipLevel, plantId, rewardObject);
			putChat(rsp);
			ChatPushHelper.broabcastMsg(rsp);
		}
		return Result.valueOf();
	}

	@Override
	public void sendWelkinChat(long actorId, List<RewardObject> reward, int type) {
		Actor actor = actorFacade.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		ChatResponse rsp = ChatHelper.getWelkinChatResponse(actor.actorName, actorId, actor.level, vipLevel, type, reward);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
	}

	@Override
	public void sendLadderChat(long actorId, Integer type,int winNum) {
		Actor actor = actorFacade.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		ChatResponse rsp = ChatHelper.getLadderChatResponse(actor.actorName,actorId,actor.level,vipLevel,type,winNum);
		putChat(rsp);
		ChatPushHelper.broabcastMsg(rsp);
	}

	@Override
	public void sendLoveRankChat(long actorId, long targetId) {
		
	}
}
