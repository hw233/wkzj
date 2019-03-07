package com.jtang.gameserver.module.extapp.plant.facade.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.PlantConfig;
import com.jtang.gameserver.dataconfig.model.PlantGlobalConfig;
import com.jtang.gameserver.dataconfig.service.PlantService;
import com.jtang.gameserver.dbproxy.entity.Plant;
import com.jtang.gameserver.module.app.constant.AppRule;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.plant.dao.PlantDao;
import com.jtang.gameserver.module.extapp.plant.facade.PlantFacade;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantHarvestResponse;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantResponse;
import com.jtang.gameserver.module.extapp.plant.helper.PlantPushHelper;
import com.jtang.gameserver.module.extapp.plant.module.PlantVO;
import com.jtang.gameserver.module.extapp.plant.type.PlantType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class PlantFacedeImpl implements PlantFacade, ActorLoginListener,ApplicationListener<ContextRefreshedEvent>, ZeroListener{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	PlantDao plantDao;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	ChatFacade chatFacade;

	/**
	 * 是否开放
	 */
	private boolean isOpen = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				Set<Long> actorIds = playerSession.onlineActorList();
				PlantGlobalConfig globalConfig = PlantService
						.getPlantGlobalConfig();
				for (Long actorId : actorIds) {
					int now = TimeUtils.getNow();
					if (isOpen) {// 开启才能关闭
						if (globalConfig.start >= now || now > globalConfig.end) {
							isOpen = false;
							PlantPushHelper.pushPlantOpen(actorId, 0);
						}
						Plant plant = plantDao.get(actorId);
						if(plant.plantState == PlantType.GROW.getType()){
							ChainLock lock = LockUtils.getLock(plant);
							try {
								lock.lock();
								PlantConfig config = PlantService.getPlantConfig(plant.plantId);
								if (plant.plantStartTime + config.growTime <= now) {
									plant.plantState = PlantType.HARVECT.getType();
									plantDao.update(plant);
									PlantVO plantVO = PlantVO.valueOf(plant, config);
									PlantPushHelper.pushPlantState(actorId, plantVO);
								}
							} catch (Exception e) {
								LOGGER.error("{}", e);
							} finally {
								lock.unlock();
							}
						}
					} else {// 关闭才能开启
						if (globalConfig.start <= now && now < globalConfig.end) {
							isOpen = true;
							PlantPushHelper.pushPlantOpen(actorId, 1);
						}
					}
				}
			}
		}, 1);
	}

	@Override
	public void onLogin(long actorId) {
		if (isOpen) {
			PlantPushHelper.pushPlantOpen(actorId, 1);
			Plant plant = plantDao.get(actorId);
			if(DateUtils.isToday(plant.plantStartTime) == false){
				ChainLock lock = LockUtils.getLock(plant);
				try {
					lock.lock();
					plant.reSet();
					plantDao.update(plant);
				} catch (Exception e) {
					LOGGER.error("{}", e);
				} finally {
					lock.unlock();
				}
			}
		}
	}

	@Override
	public TResult<PlantResponse> getPlant(long actorId) {
		Plant plant = plantDao.get(actorId);
		int level = ActorHelper.getActorLevel(actorId);
		PlantConfig plantConfig = PlantService.getPlantConfig(plant.plantId);
		PlantGlobalConfig config = PlantService.getPlantGlobalConfig();
		PlantVO plantVO = PlantVO.valueOf(plant, plantConfig);
		List<RewardObject> reward = PlantService.getAllReward(level);
		List<RewardObject> extReward = PlantService.getAllExtReward(level);
		PlantResponse response = new PlantResponse(plantVO,reward,extReward,config.costTicket);
		return TResult.sucess(response);
	}

	@Override
	public TResult<PlantResponse> plantQuicken(long actorId) {
		if(isOpen == false){
			return TResult.valueOf(GameStatusCodeConstant.APP_CLOSED);
		}
		int level = ActorHelper.getActorLevel(actorId);
		if(AppRule.APP_MIDDLE_VIP_LEVEL_LIMIT > level){
			return TResult.valueOf(GameStatusCodeConstant.APP_LEVEL_NOT_REACH);
		}
		Plant plant = plantDao.get(actorId);
		if (plant.plantState == PlantType.NOT_PLANT.getType()) {// 未种植
			return TResult.valueOf(GameStatusCodeConstant.PLANT_NOT);
		}
		if (plant.plantState == PlantType.HARVECT.getType()) {// 可收获不可加速
			return TResult.valueOf(GameStatusCodeConstant.PLANT_HARVECT);
		}
		PlantConfig plantConfig = PlantService.getPlantConfig(plant.plantId);
		Entry<Integer, Integer> entry = PlantService.getPlantQuickenCostTicket();
		int now = TimeUtils.getNow();
		Double endTime = ((plant.plantStartTime + plantConfig.growTime) - now)/60.0;
		endTime = Math.ceil(endTime);
		Double min = endTime / entry.getKey();
		min = Math.ceil(min);
		Double ticket =  min * entry.getValue();
		int costTicket = ticket.intValue() > entry.getValue() ? ticket.intValue() : entry.getValue();
		boolean isSuccess = vipFacade.decreaseTicket(actorId,
				TicketDecreaseType.PLANT, costTicket, 0, 0);
		if (isSuccess == false) {// 点券不足
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		ChainLock lock = LockUtils.getLock(plant);
		try {
			lock.lock();
			plant.quicken(plantConfig);
			plantDao.update(plant);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		PlantGlobalConfig config = PlantService.getPlantGlobalConfig();
		List<RewardObject> reward = PlantService.getAllReward(level);
		List<RewardObject> extReward = PlantService.getAllExtReward(level);
		PlantResponse response = new PlantResponse(PlantVO.valueOf(plant,
				plantConfig),reward,extReward ,config.costTicket);
		return TResult.sucess(response);
	}

	@Override
	public TResult<PlantHarvestResponse> plantHarvest(long actorId) {
		if(isOpen == false){
			return TResult.valueOf(GameStatusCodeConstant.APP_CLOSED);
		}
		int level = ActorHelper.getActorLevel(actorId);
		if(AppRule.APP_MIDDLE_VIP_LEVEL_LIMIT > level){
			return TResult.valueOf(GameStatusCodeConstant.APP_LEVEL_NOT_REACH);
		}
		Plant plant = plantDao.get(actorId);
		if (plant.plantState == PlantType.NOT_PLANT.getType()) {// 未种植
			return TResult.valueOf(GameStatusCodeConstant.PLANT_NOT);
		}
		if (plant.plantState == PlantType.GROW.getType()) {// 成长中不可收获
			return TResult.valueOf(GameStatusCodeConstant.GROW_NOT_HARVECT);
		}
		PlantConfig config = PlantService.getPlantConfig(plant.plantId);

//		if(plant.plantStartTime + config.growTime > TimeUtils.getNow()){
//			return TResult.valueOf(GameStatusCodeConstant.NOT_HARVECT);
//		}
		
		RewardObject mastReward = PlantService.getPlantMastReward(
				plant.plantId, level);// 固定奖励
		ChainLock lock = LockUtils.getLock(plant);
		try {
			lock.lock();
			plant.harvest();// 收获
			plantDao.update(plant);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		sendReward(actorId, mastReward);
		RewardObject extReward = null;
		RewardObject reward = null;
		if (plant.harvestCount == plant.getRewardCount) {
			reward = PlantService.getPlantReward(level);
			sendReward(actorId, reward);
			plant.harvestCount = 0;
			chatFacade.sendPlantChat(actorId, plant.plantId, reward);
		} else {// 不出保底才有几率出额外
			extReward = PlantService.getExtReward(level);
			if (extReward != null) {
				sendReward(actorId, extReward);
				chatFacade.sendPlantChat(actorId, plant.plantId, extReward);
			}
		}
		PlantVO plantVO = PlantVO.valueOf(plant, config);
		PlantHarvestResponse response = new PlantHarvestResponse(plantVO,
				mastReward, extReward, reward);
		return TResult.sucess(response);
	}

	@Override
	public TResult<PlantResponse> plant(long actorId, int plantId) {
		if(isOpen == false){
			return TResult.valueOf(GameStatusCodeConstant.APP_CLOSED);
		}
		int level = ActorHelper.getActorLevel(actorId);
		if(AppRule.APP_MIDDLE_VIP_LEVEL_LIMIT > level){
			return TResult.valueOf(GameStatusCodeConstant.APP_LEVEL_NOT_REACH);
		}
		Plant plant = plantDao.get(actorId);
		if (plant.plantState == PlantType.GROW.getType()) {// 未种植
			return TResult.valueOf(GameStatusCodeConstant.NOW_NOT_PLANT);
		}
		if (plant.plantState == PlantType.HARVECT.getType()) {// 成长中不可收获
			return TResult.valueOf(GameStatusCodeConstant.NOW_NOT_PLANT);
		}

		PlantGlobalConfig globalConfig = PlantService.getPlantGlobalConfig();
		if (plant.todayPlant >= globalConfig.dayPlant) {
			return TResult.valueOf(GameStatusCodeConstant.PLANT_MAX);
		}
		PlantConfig config = PlantService.getPlantConfig(plantId);
		ChainLock lock = LockUtils.getLock(plant);
		try {
			lock.lock();
			plant.plant(config);// 种植
			plantDao.update(plant);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		List<RewardObject> reward = PlantService.getAllReward(level);
		List<RewardObject> extReward = PlantService.getAllExtReward(level);
		PlantResponse response = new PlantResponse(PlantVO.valueOf(plant,
				config),reward,extReward ,globalConfig.costTicket);
		return TResult.sucess(response);
	}

	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId, RewardObject reward) {
		switch (reward.rewardType) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.PLANT, reward.id,
					reward.num);
			break;
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.PLANT, reward.id);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.PLANT, reward.id,
					reward.num);
			break;
		default:
			break;
		}
	}

	@Override
	public void onZero() {
		if(isOpen){
			Set<Long> actorIds = playerSession.onlineActorList();
			for (Long actorId : actorIds) {
				Plant plant = plantDao.get(actorId);
				ChainLock lock = LockUtils.getLock(plant);
				try {
					lock.lock();
					plant.reSet();
					plantDao.update(plant);
				} catch (Exception e) {
					LOGGER.error("{}", e);
				} finally {
					lock.unlock();
				}
				PlantConfig plantConfig = PlantService.getPlantConfig(plant.plantId);
				PlantPushHelper.pushPlantState(actorId, PlantVO.valueOf(plant, plantConfig));
			}
		}
	}
}
