package com.jtang.gameserver.module.app.facade.impl;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.module.app.dao.AppGlobalDao;
import com.jtang.gameserver.module.app.dao.AppRecordDao;
import com.jtang.gameserver.module.app.effect.AppParser;
import com.jtang.gameserver.module.app.facade.AppFacade;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class AppFacadeImpl implements AppFacade,Receiver,ActorLoginListener, ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	AppGlobalDao appGlobalDao;
	
	@Autowired
	AppRecordDao appRecordDao;
	
	@Autowired
	private EventBus eventBus;

	@PostConstruct
	public void init() {
		eventBus.register(EventKey.SNATCH_RESULT, this);
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
	}
	
	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		AppParser appParser = AppParser.getAppParser(appId);
		return appParser.getAppGlobalVO(actorId,appId);
	}
	
	@Override
	public ListResult<RewardObject> getReward(long actorId,long appId,Map<String, String> paramsMaps) {
		AppParser appParser = AppParser.getAppParser(appId);
		return appParser.getReward(actorId,paramsMaps,appId);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppParser appParser = AppParser.getAppParser(appId);
		return appParser.getAppRecord(actorId,appId);
	}

	@Override
	public Result appEnable(long actorId,long appId) {
		AppParser appParser = AppParser.getAppParser(appId);
		return appParser.appEnable(actorId, appId);
//		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
//		if (appRuleConfig == null) {
//			return Result.valueOf(APP_NOT_EXSIT);
//		}
//
//		if (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false) {
//			return Result.valueOf(APP_CLOSED);
//		}
//		
//		//清理过期记录
//		AppRecord record = appRecordDao.get(actorId, appId);
//		if (record.operationTime != 0 && (record.operationTime < appRuleConfig.getStartTime() || record.operationTime > appRuleConfig.getEndTime())) {
//			record.reset();
//			appRecordDao.update(record);
//		}
//				
//		List<Integer> chanel = appRuleConfig.getChanelIds();
//		if (chanel.isEmpty()) {
//			return Result.valueOf();
//		}
//		
//		Actor actor = actorFacade.getActor(actorId);
//		if (actor == null) {
//			return Result.valueOf(ACTOR_ID_ERROR);
//		}
//
//		if (chanel.contains(actor.channelId) == false) {
//			return Result.valueOf(APP_NOT_EXSIT);
//		}
//		
//		return Result.valueOf();
	}

	@Override
	public void onEvent(Event paramEvent) {
		Set<Long> appIds = AppRuleService.getAllApp();
		for(long appId:appIds){
			AppParser appParser = AppParser.getAppParser(appId);
			GameEvent gameEvent = (GameEvent) paramEvent;
			Result result = appEnable(gameEvent.actorId,appId);
			if (result.isFail()) {
				continue;
			}
			appParser.onGameEvent(gameEvent,appId);
		}
	}

	@Override
	public void onLogin(long actorId) {
		Set<Long> appIds = AppRuleService.getAllApp();
		for(long appId:appIds){
			AppParser appParser = AppParser.getAppParser(appId);
			Result result = appEnable(actorId,appId);
			if (result.isFail()) {
				continue;
			}
			appParser.onActorLogin(actorId,appId);
		}
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		for(EffectId effectId:EffectId.values()){
			AppParser appParser = AppParser.getAppParser(effectId);
			appParser.onApplicationEvent();
//			boolean result = appEnable(appId);
//			if (result == false) {
//				continue;
//			}
		}
		
	}
	
//	/**
//	 * 全局判断活动是否开启
//	 * @return
//	 */
//	private boolean appEnable(long appId) {
//		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
//		if (appRuleConfig == null) {
//			return false;
//		}
//
//		if (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false) {
//			return false;
//		}
//		
//		//清理过期记录
//		AppGlobal gloabalRecord = appGlobalDao.get(appId);
//		if (gloabalRecord.operationTime != 0 && (gloabalRecord.operationTime < appRuleConfig.getStartTime() || gloabalRecord.operationTime > appRuleConfig.getEndTime())) {
//			gloabalRecord.reset();
//			appGlobalDao.update(gloabalRecord);
//		}
//
//		
//		return true;
//	}

}
