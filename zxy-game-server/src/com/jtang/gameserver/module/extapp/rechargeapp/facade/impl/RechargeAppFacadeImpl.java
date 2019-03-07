package com.jtang.gameserver.module.extapp.rechargeapp.facade.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.dataconfig.model.RechargeAppConfig;
import com.jtang.gameserver.dataconfig.service.RechargeAppService;
import com.jtang.gameserver.dbproxy.entity.RechargeApp;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.rechargeapp.dao.RechargeAppDao;
import com.jtang.gameserver.module.extapp.rechargeapp.facade.RechargeAppFacade;
import com.jtang.gameserver.module.extapp.rechargeapp.handler.response.RecharegeAppResponse;
import com.jtang.gameserver.module.extapp.rechargeapp.helper.RechargeAppPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketAddType;

@Component
public class RechargeAppFacadeImpl implements RechargeAppFacade,Receiver{
	
	@Autowired
	VipFacade vipFacade;
	@Autowired
	EventBus eventBus;
	@Autowired
	RechargeAppDao rechargeAppDao;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	HeroFacade heroFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	GoodsFacade goodsFacade;
	
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
	}

	@Override
	public TResult<RecharegeAppResponse> getRecharege(long actorId) {
		RechargeApp rechargeApp = rechargeAppDao.get(actorId);
		RecharegeAppResponse response = new RecharegeAppResponse(rechargeApp.rechargeMap);
		return TResult.sucess(response);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.name == EventKey.TICKETS_RECHARGE){
			RechargeTicketsEvent event = paramEvent.convert();
			RechargeAppConfig config = RechargeAppService.get(event.rechargeId);
			RechargeApp rechargeApp = rechargeAppDao.get(event.actorId);
			if(config != null){
				if(rechargeApp.isRecharge(event.money) == false){//没有进行过此类充值
					if(config.rechargeCount == 1){//首次充值即赠送
						vipFacade.addTicket(event.actorId, TicketAddType.APP, config.giveTicket);
						sendReward(event.actorId, config.rewardList);
					}
					rechargeApp.rechargeMap.put(event.money, 1);
					RechargeAppPushHelper.pushPlantOpen(event.actorId, getRecharege(event.actorId).item);
				}else{//有进行过此类充值
					int rechargeCount = rechargeApp.rechargeMap.get(event.money);
					rechargeCount += 1;
					if(rechargeCount == config.rechargeCount){//多次充值后赠送
						vipFacade.addTicket(event.actorId, TicketAddType.APP, config.giveTicket);
						sendReward(event.actorId, config.rewardList);
					}
					rechargeApp.rechargeMap.put(event.money, rechargeCount);
					RechargeAppPushHelper.pushPlantOpen(event.actorId, getRecharege(event.actorId).item);
				}
				rechargeAppDao.update(rechargeApp);
			}
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
		default:
			break;
		}
	}

}
