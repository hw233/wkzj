package com.jtang.gameserver.module.extapp.onlinegifts.facade.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.listener.ActorLogoutListener;
import com.jtang.gameserver.dataconfig.service.OnlineGiftsService;
import com.jtang.gameserver.dbproxy.entity.OnlineGifts;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.onlinegifts.dao.OnlineGiftsDao;
import com.jtang.gameserver.module.extapp.onlinegifts.facade.OnlineGiftsFacade;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;
import com.jtang.gameserver.module.extapp.onlinegifts.helper.OnlineGiftsPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.constant.ActorRule;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketAddType;

@Component
public class OnlineGiftsFacadeImpl implements OnlineGiftsFacade, Receiver, ActorLoginListener, ActorLogoutListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineGiftsFacadeImpl.class);
	
	@Autowired
	OnlineGiftsDao onlineGiftsDao;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	HeroFacade heroFacade;

	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	EventBus eventBus;
	
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}
	
	@Override
	public TResult<OnlineGiftsInfoResponse> getOnlineInfo(long actorId) {
		Result result = baseConditionCheck(actorId);
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		int leftTime = getLeftTime(actorId);
		OnlineGifts gifts = onlineGiftsDao.get(actorId);
		int curIndex = gifts.lastRecIndex + 1;
		OnlineGiftsInfoResponse response = new OnlineGiftsInfoResponse(curIndex, leftTime);
		return TResult.sucess(response);
	}

	@Override
	public Result receiveGift(long actorId) {
		Result result = baseConditionCheck(actorId);
		if (result.isFail()) {
			return Result.valueOf(result.statusCode);
		}
		OnlineGifts gifts = onlineGiftsDao.get(actorId);
		int leftTime = getLeftTime(actorId);
		if (leftTime > 0) {
			return Result.valueOf(GameStatusCodeConstant.ONLINE_GIFT_CAN_NOT_RECEIVE);
		}
		
		int curIndex = gifts.lastRecIndex + 1;
		List<RewardObject> rewardObjects = OnlineGiftsService.getRewards(curIndex);
		if (rewardObjects == null) {
			return Result.valueOf(GameStatusCodeConstant.ONLINE_GIFT_ALL_RECEIVED);
		}
		sendReward(actorId, rewardObjects);
		ChainLock lock = LockUtils.getLock(gifts);
		try {
			lock.lock();
			gifts.receiveAddition();
			onlineGiftsDao.update(gifts);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		return Result.valueOf();
	}


	private int getLeftTime(long actorId) {
		OnlineGifts gifts = onlineGiftsDao.get(actorId);
		int curIndex = gifts.lastRecIndex + 1;
		int CDTime = OnlineGiftsService.getCoolDownTime(curIndex);
		int loginTime = gifts.lastRecTime;
		int loginDuration = TimeUtils.getNow() - loginTime;
		int onlineTime = gifts.elapseTime + loginDuration;
		int leftTime = Math.max(0, CDTime - onlineTime);
		return leftTime;
	}
	
	private Result baseConditionCheck(long actorId) {
		int actorLevel = ActorHelper.getActorLevel(actorId);
		if (actorLevel < ActorRule.ONLINE_GIFTS_MIN_LEVEL) {
			return Result.valueOf(GameStatusCodeConstant.ONLINE_GIFT_LEVEL_NOT_ENOUGH);
		}
		
		OnlineGifts gifts = onlineGiftsDao.get(actorId);
		int curIndex = gifts.lastRecIndex;
		if (curIndex >= OnlineGiftsService.getMaxIndex()) {
			return Result.valueOf(GameStatusCodeConstant.ONLINE_GIFT_ALL_RECEIVED);
		}
		
		return Result.valueOf();
	}
	
	@Override
	public void onLogin(long actorId) {
		OnlineGifts onlineGifts = onlineGiftsDao.get(actorId);
		int total = OnlineGiftsService.getMaxIndex();
		if (onlineGifts.isAllReceived(total) == false) {
			onlineGifts.lastRecTime = TimeUtils.getNow();
			onlineGiftsDao.update(onlineGifts);
		}
	}
	
	@Override
	public void onLogout(long actorId) {
		OnlineGifts onlineGifts = onlineGiftsDao.get(actorId);
		int total = OnlineGiftsService.getMaxIndex();
		if (onlineGifts.isAllReceived(total) == false) {
			onlineGifts.elapseTime += TimeUtils.getNow() - onlineGifts.lastRecTime;
			onlineGiftsDao.update(onlineGifts);
		}
	}
	
	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num,rewardObject.rewardType);
		}
	}
	
	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.ONLINE_GIFTS, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.ONLINE_GIFTS, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.ONLINE_GIFTS, id, num);
			break;
		}
		case TICKET: {
			vipFacade.addTicket(actorId, TicketAddType.ONLINE_GIFTS, num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}
	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.name == EventKey.ACTOR_LEVEL_UP){
			ActorLevelUpEvent event = paramEvent.convert();
			long actorId = event.actorId;
			Result result = baseConditionCheck(actorId);
			if (result.isOk()) {
				OnlineGifts onlineGifts = onlineGiftsDao.get(actorId);
				if (onlineGifts.lastRecIndex == 0) {
					ChainLock lock = LockUtils.getLock(onlineGifts);
					try {
						lock.lock();
						onlineGifts.lastRecTime = TimeUtils.getNow();
						onlineGiftsDao.update(onlineGifts);
					} catch (Exception e) {
						LOGGER.error("{}", e);
					} finally {
						lock.unlock();
					}
				}
				TResult<OnlineGiftsInfoResponse> result2 = this.getOnlineInfo(actorId);
				if (result2.isOk()) {
					OnlineGiftsPushHelper.pushOnlineGiftsInfo(actorId, result2.item);
				}
			}
			
		}
	}

}

