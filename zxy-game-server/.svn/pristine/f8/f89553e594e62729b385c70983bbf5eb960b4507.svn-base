package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_FINISH;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.global.GlobalInfoVO17;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO17;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO17;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class AppParser17 extends AppParser{
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId); 
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		GlobalInfoVO17 globalInfoVO17 = appGlobalDao.getGloabalInfoVO(appId);
		Map<AppKey,Object> map = new HashMap<>();
		Actor actor = actorFacade.getActor(globalInfoVO17.maxPowerActorId);
		if(actor != null){
			map.put(AppKey.MAX_ACTOR_POWER, actor.actorName);
		}else{
			map.put(AppKey.MAX_ACTOR_LEVEL, "");
		}
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig, map,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		return ListResult.statusCode(APP_NOT_FINISH);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO17 recordInfoVO17 = appRecordDao.getRecordInfoVO(actorId, appId);
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO17.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_17;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {

	}

	@Override
	public void onActorLogin(long actorId,long appId) {

	}

	@Override
	public void onApplicationEvent() {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				for(long appId : AppRuleService.getAppId(getEffect().getId())){
					if (appEnable(appId) == false) {
						return;
					}
	
					AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
					RuleConfigVO17 ruleConfig = appRuleConfig.getConfigRuleVO();
	
					if (ruleConfig == null) {
						return;
					}
	
					if (ruleConfig.startFlushTime > TimeUtils.getNow()) {
						return;
					}
	
					AppGlobal global = appGlobalDao.get(appId);
					GlobalInfoVO17 globalInfoVO17 = global.getGlobalInfoVO();
					if (global.operationTime != 0) {
						if ((global.operationTime + ruleConfig.fixTime) > TimeUtils.getNow()) {
							return;
						}
					}
					
					Map<AppKey,Object> map = new HashMap<>();
					long maxPowerActorId = appGlobalDao.getMaxPowerOfActor();
					if(maxPowerActorId == globalInfoVO17.maxPowerActorId){
						return;
					}
					globalInfoVO17.maxPowerActorId = maxPowerActorId;
					Actor actor = actorFacade.getActor(maxPowerActorId);
					if(actor != null){
						map.put(AppKey.MAX_ACTOR_POWER, actor.actorName);
					}else{
						map.put(AppKey.MAX_ACTOR_POWER, "");
					}
					appGlobalDao.update(global);
	
					for (Long actorId : playerSession.onlineActorList()) {
						int level = ActorHelper.getActorLevel(actorId);
						AppGlobalVO appGlobalVO = new AppGlobalVO(appRuleConfig, map,level);
						AppPushHelper.pushAppGlobal(actorId, appGlobalVO);
					}
				}
			}
		}, 1);
	}
}
