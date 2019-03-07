package com.jtang.gameserver.module.extapp.basin.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.BasinConfig;
import com.jtang.gameserver.dataconfig.model.BasinRewardConfig;
import com.jtang.gameserver.dataconfig.service.BasinService;
import com.jtang.gameserver.dbproxy.entity.Basin;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.basin.dao.BasinDao;
import com.jtang.gameserver.module.extapp.basin.facade.BasinFacade;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinResponse;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;
import com.jtang.gameserver.module.extapp.basin.helper.BasinPushHelper;
import com.jtang.gameserver.module.extapp.basin.model.BasinVO;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class BasinFacadeImpl implements BasinFacade,ApplicationListener<ApplicationEvent>,ActorLoginListener,Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasinFacadeImpl.class);
	
	@Autowired
	private BasinDao basinDao;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private EventBus eventBus;
	
	/**
	 * 活动状态
	 */
	private boolean isOpen = false;
	
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
	}
	
	@Override
	public TResult<BasinResponse> getInfo(long actorId) {
		if(isOpen == false){
			return TResult.valueOf(GameStatusCodeConstant.BASIN_NOT_OPEN);
		}
		Basin basin = basinDao.get(actorId);
		if(basin.rewardMap.isEmpty()){
			initRewardMap(basin);
		}
		BasinConfig basinConfig = BasinService.getGlobalConfig();
		List<BasinVO> reward = parserVO(basin);
		BasinResponse response = new BasinResponse(basin.recharge,basinConfig.end,reward);
		return TResult.sucess(response);
	}

	private List<BasinVO> parserVO(Basin basin) {
		List<BasinVO> reward = new ArrayList<>();
		for(Entry<Integer,Integer> entry:basin.rewardMap.entrySet()){
			BasinRewardConfig rewardConfig = BasinService.getRewardConfig(entry.getKey());
			BasinVO basinVO = new BasinVO(entry.getValue(),rewardConfig.recharge,rewardConfig.rewardList,rewardConfig.rewardIcon);
			reward.add(basinVO);
		}
		return reward;
	}

	@Override
	public Result getReward(long actorId, int recharge) {
		if(isOpen == false){
			return Result.valueOf(GameStatusCodeConstant.BASIN_NOT_OPEN);
		}
		Basin basin = basinDao.get(actorId);
		if(basin.rewardMap.isEmpty()){
			initRewardMap(basin);
		}
		if(basin.rewardMap.containsKey(recharge) == false){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(basin.rewardMap.get(recharge) != 1){
			return Result.valueOf(GameStatusCodeConstant.BASIN_NOT_GET);
		}
		ChainLock lock = LockUtils.getLock(basin);
		try{
			lock.lock();
			basin.rewardMap.put(recharge, 2);
			BasinRewardConfig rewardConfig = BasinService.getRewardConfig(recharge);
			sendReward(actorId,rewardConfig.rewardList);
			basinDao.update(basin);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}
	
	@Override
	public TResult<BasinStateResponse> getState() {
		BasinStateResponse response = new BasinStateResponse(isOpen);
		return TResult.sucess(response);
	}
	
	@Override
	public void onLogin(long actorId) {
		BasinConfig globalConfig = BasinService.getGlobalConfig();
		Basin basin = basinDao.get(actorId);
		ChainLock lock = LockUtils.getLock(basin);
		try {
			lock.lock();
			if (globalConfig.start > basin.operationTime) {
				basin.reset();
				initRewardMap(basin);
				basinDao.update(basin);
			} 
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				BasinConfig globalConfig = BasinService.getGlobalConfig();
				int now = TimeUtils.getNow();
				if(isOpen){//开启才可关闭
					if(globalConfig.start >= now || now > globalConfig.end){
						isOpen = false;
						for(Long actorId : playerSession.onlineActorList()){
							BasinPushHelper.pushBasinState(actorId, isOpen);
						}
					}
				}else{//关闭才能开启
					if(globalConfig.start <= now && now < globalConfig.end){
						isOpen = true;
						for(Long actorId : playerSession.onlineActorList()){
							BasinPushHelper.pushBasinState(actorId, isOpen);
							Basin basin = basinDao.get(actorId);
							if (globalConfig.start > basin.operationTime){
								basin.reset();
								basinDao.update(basin);
							}
						}
					}
				}
			}
		}, 1); 
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.getName() == EventKey.TICKETS_RECHARGE){
			if(isOpen == false){
				return;
			}
			RechargeTicketsEvent event = paramEvent.convert();
			BasinConfig globalConfig = BasinService.getGlobalConfig();
			Basin basin = basinDao.get(event.actorId);
			ChainLock lock = LockUtils.getLock(basin);
			try{
				lock.lock();
				if(globalConfig.start > basin.operationTime){
					basin.recharge = 0;
					initRewardMap(basin);
				}
				if(basin.rewardMap.isEmpty()){
					initRewardMap(basin);
				}
				basin.recharge += event.currentTicket;
				for(Entry<Integer,Integer> entry:basin.rewardMap.entrySet()){
					if(entry.getValue() == 0 && entry.getKey() <= basin.recharge){
						basin.rewardMap.put(entry.getKey(), 1);
					}
				}
				basinDao.update(basin);
				List<BasinVO> reward = parserVO(basin);
				BasinPushHelper.pushBasinState(event.actorId,basin.recharge,reward);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	private void initRewardMap(Basin basin) {
		for(BasinRewardConfig config:BasinService.getAllRewardConfig()){
			basin.rewardMap.put(config.recharge, 0);
		}
	}
	
	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for(RewardObject reward:rewardList){
			sendReward(actorId, reward);
		}
	}
	
	private void sendReward(long actorId,RewardObject rewardObject){
		switch(rewardObject.rewardType){
		case EQUIP: {
			equipFacade.addEquip(actorId, EquipAddType.BASIN, rewardObject.id);
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.BASIN, rewardObject.id, rewardObject.num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.BASIN, rewardObject.id, rewardObject.num);
			break;
		}
		default:
			break;
		
		}
	}

}
