package com.jtang.gameserver.module.extapp.monthcard.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.jtang.core.model.ExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.MonthCardConfig;
import com.jtang.gameserver.dataconfig.model.RechargeRateConfig;
import com.jtang.gameserver.dataconfig.service.MonthCardService;
import com.jtang.gameserver.dataconfig.service.RechargeRateService;
import com.jtang.gameserver.dbproxy.entity.MonthCard;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.monthcard.dao.MonthCardDao;
import com.jtang.gameserver.module.extapp.monthcard.facade.MonthCardFacade;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;
import com.jtang.gameserver.module.extapp.monthcard.helper.MonthCardPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.RechargeType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class MonthCardFacadeImpl implements MonthCardFacade,ActorLoginListener,Receiver,ZeroListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonthCardFacadeImpl.class);
	
	@Autowired
	MonthCardDao monthCardDao;
	
	@Autowired
	HeroSoulFacade heroSoulFacade; 
	
	@Autowired
	HeroFacade heroFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	private EventBus eventBus;
	@Autowired
	private Schedule schedule;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private PlayerSession playerSession;
	
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
	}
	
	@Override
	public TResult<MonthCardResponse> getInfo(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		int rechargeNum = vipFacade.getRechargeNum(actorId);
		MonthCardResponse response = new MonthCardResponse(monthCard,rechargeNum);
		return TResult.sucess(response);
	}

	@Override
	public Result getReward(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		ChainLock lock = LockUtils.getLock(monthCard);
		try{
			lock.lock();
			if(monthCard.day == 0){//月卡已经过期
				return Result.valueOf(GameStatusCodeConstant.NOT_MONTH_CARD);
			}
			monthCard.nextDay();
			if(monthCard.isDayReward == 2 && DateUtils.isToday(monthCard.getTime)){//今天已经领取过了
				return Result.valueOf(GameStatusCodeConstant.MONTH_CARD_GET);
			}
			monthCard.getTime = TimeUtils.getNow();
			monthCard.isDayReward = 2;
			monthCardDao.update(monthCard);
			MonthCardConfig monthCardConfig = MonthCardService.getConfig(RechargeType.MONTH_CARD.getId());
			List<RewardObject> dayList = new ArrayList<>();
			List<ExprRewardObject> rewardList = monthCardConfig.dayList;
			int actorLevel = ActorHelper.getActorLevel(actorId);
			int vipLevel = vipFacade.getVipLevel(actorId);
			for(ExprRewardObject ext:rewardList){
				dayList.add(ext.clone(actorLevel,vipLevel));
			}
			sendReward(actorId, dayList);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public void onLogin(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		ChainLock lock = LockUtils.getLock(monthCard);
		try{
			lock.lock();
			if(monthCard.lifelongFirsetReward == 1 && DateUtils.isToday(monthCard.lifelongGetTime) == false){
				monthCard.lifelongDayReward = 1;
				monthCardDao.update(monthCard);
			}
			if(monthCard.nextDay()){
				monthCardDao.update(monthCard);
			}
			if(monthCard.yearNextDay()){
				monthCardDao.update(monthCard);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.getName() == EventKey.TICKETS_RECHARGE){
			RechargeTicketsEvent event = paramEvent.convert();
			RechargeRateConfig rechargeRateConfig = RechargeRateService.getConfig(event.rechargeId);
			if(rechargeRateConfig == null){
				LOGGER.error(String.format("未找到充值id为[%s]的档次",event.rechargeId));
				return;
			}
			MonthCard monthCard = monthCardDao.get(event.actorId);
			ChainLock lock = LockUtils.getLock(monthCard);
			try{
				lock.lock();
				int rechargeNum = vipFacade.getRechargeNum(event.actorId);
				switch(rechargeRateConfig.getRechargeType()){
				case LIFELONG_CARD:
					MonthCardConfig lifelongCardConfig = MonthCardService.getConfig(RechargeType.LIFELONG_CARD.getId());
					if(monthCard.lifelongFirsetReward == 1){//已经是终身卡
						return;
					}
					if(monthCard.lifelongFirsetReward == 0){
						sendReward(event.actorId, lifelongCardConfig.firstList);
						monthCard.lifelongFirsetReward = 1;
					}
					monthCard.lifelongDayReward = 1;
					monthCardDao.update(monthCard);
					break;
				case MONTH_CARD:
					MonthCardConfig monthCardConfig = MonthCardService.getConfig(RechargeType.MONTH_CARD.getId());
					if(monthCard.day > monthCardConfig.endDay){
						return;
					}
					if(monthCard.isFirsetReward == 0){
						sendReward(event.actorId, monthCardConfig.firstList);
						monthCard.isFirsetReward = 1;
					}
					monthCard.isDayReward = 1;
					monthCard.day += monthCardConfig.day;
					monthCard.getTime = TimeUtils.getNow();
					monthCardDao.update(monthCard);
					break;
				case YEAR_CARD:
					MonthCardConfig yearCardConfig = MonthCardService.getConfig(RechargeType.YEAR_CARD.getId());
					if(monthCard.yearCardDay > yearCardConfig.endDay){
						return;
					}
					if(monthCard.yearCardFrast == 0){
						sendReward(event.actorId, yearCardConfig.firstList);
						monthCard.yearCardFrast = 1;
					}
					monthCard.yearCardDayReward = 1;
					monthCard.yearCardDay += yearCardConfig.day;
					monthCard.yearCardTime = TimeUtils.getNow();
					monthCardDao.update(monthCard);
					break;
				default:
					break;
				}
				MonthCardPushHelper.pushMonthCard(event.actorId, monthCard,rechargeNum);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for(Long actor:actors){
			MonthCard monthCard = monthCardDao.get(actor);
			ChainLock lock = LockUtils.getLock(monthCard);
			try{
				lock.lock();
				if(monthCard.lifelongFirsetReward == 1){
					monthCard.lifelongDayReward = 1;
					monthCardDao.update(monthCard);
				}
				if(monthCard.day > 0){
					monthCard.day -= 1;
					monthCard.isDayReward = 1;
					monthCard.getTime = TimeUtils.getNow();
					monthCardDao.update(monthCard);
				}
				if(monthCard.yearCardDay > 0){
					monthCard.yearCardDay -= 1;
					monthCard.yearCardDayReward = 1;
					monthCard.yearCardTime = TimeUtils.getNow();
					monthCardDao.update(monthCard);
				}
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
			int rechargeNum = vipFacade.getRechargeNum(actor);
			MonthCardPushHelper.pushMonthCard(actor, monthCard,rechargeNum);
		}
	}
	
	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId,List<RewardObject> reward){
		for(RewardObject rewardObject:reward){
			sendReward(actorId,rewardObject);
		}
	}
	
	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId, RewardObject reward) {
		switch (reward.rewardType) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.MONTH_CARD, reward.id,reward.num);
			break;
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.MONTH_CARD, reward.id);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.MONTH_CARD, reward.id,reward.num);
			break;
		case HERO:
			if(heroFacade.isHeroExits(actorId, reward.id) == false){
				heroFacade.addHero(actorId, HeroAddType.MONTH_CARD, reward.id);
			}else{
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.MONTH_CARD, reward.id, reward.num);
			}
			break;
		case TICKET:
			vipFacade.addTicket(actorId, TicketAddType.MONTH_CARD, reward.num);
			break;
		default:
			break;
		}
	}

	@Override
	public Result getlifelongReward(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		ChainLock lock = LockUtils.getLock(monthCard);
		try{
			lock.lock();
			if(monthCard.lifelongFirsetReward == 0){//还未充值年卡
				return Result.valueOf(GameStatusCodeConstant.LIFELONG_NOT_RECHARGE);
			}
			monthCard.nextDay();
			if(monthCard.lifelongDayReward == 2 && DateUtils.isToday(monthCard.lifelongGetTime)){//今天已经领取过了
				return Result.valueOf(GameStatusCodeConstant.LIFELONG_CARD_GET);
			}
			monthCard.lifelongGetTime = TimeUtils.getNow();
			monthCard.lifelongDayReward = 2;
			monthCardDao.update(monthCard);
			MonthCardConfig monthCardConfig = MonthCardService.getConfig(RechargeType.LIFELONG_CARD.getId());
			List<RewardObject> dayList = new ArrayList<>();
			List<ExprRewardObject> rewardList = monthCardConfig.dayList;
			int actorLevel = ActorHelper.getActorLevel(actorId);
			int vipLevel = vipFacade.getVipLevel(actorId);
			for(ExprRewardObject ext:rewardList){
				dayList.add(ext.clone(actorLevel,vipLevel));
			}
			sendReward(actorId, dayList);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public int getDay(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		return monthCard.day;
	}

	@Override
	public Result getYearReward(long actorId) {
		MonthCard monthCard = monthCardDao.get(actorId);
		ChainLock lock = LockUtils.getLock(monthCard);
		try{
			lock.lock();
			if(monthCard.yearCardDay == 0){//月卡已经过期
				return Result.valueOf(GameStatusCodeConstant.NOT_YEAR_CARD);
			}
			monthCard.nextDay();
			if(monthCard.yearCardDayReward == 2 && DateUtils.isToday(monthCard.yearCardTime)){//今天已经领取过了
				return Result.valueOf(GameStatusCodeConstant.YEAR_CARD_GET);
			}
			monthCard.yearCardTime = TimeUtils.getNow();
			monthCard.yearCardDayReward = 2;
			monthCardDao.update(monthCard);
			MonthCardConfig monthCardConfig = MonthCardService.getConfig(RechargeType.YEAR_CARD.getId());
			List<RewardObject> dayList = new ArrayList<>();
			List<ExprRewardObject> rewardList = monthCardConfig.dayList;
			int actorLevel = ActorHelper.getActorLevel(actorId);
			int vipLevel = vipFacade.getVipLevel(actorId);
			for(ExprRewardObject ext:rewardList){
				dayList.add(ext.clone(actorLevel,vipLevel));
			}
			sendReward(actorId, dayList);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

}
