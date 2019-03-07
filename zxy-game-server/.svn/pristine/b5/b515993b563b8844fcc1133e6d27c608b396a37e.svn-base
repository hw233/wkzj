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
import com.jtang.gameserver.module.app.model.extension.global.GlobalInfoVO6;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO6;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO6;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class AppParser6 extends AppParser{
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId); 
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		GlobalInfoVO6 globalInfoVO6 = appGlobalDao.getGloabalInfoVO(appId);
		Map<AppKey,Object> map = new HashMap<>();
		Actor actor = actorFacade.getActor(globalInfoVO6.maxLevelActorId);
		if(actor != null){
			map.put(AppKey.MAX_ACTOR_LEVEL, actor.actorName);
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
		RecordInfoVO6 recordInfoVO6 = appRecordDao.getRecordInfoVO(actorId, appId);
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO6.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_6;
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
					RuleConfigVO6 ruleConfig = appRuleConfig.getConfigRuleVO();
	
					if (ruleConfig == null) {
						return;
					}
	
					if (ruleConfig.startFlushTime > TimeUtils.getNow()) {
						return;
					}
	
					AppGlobal global = appGlobalDao.get(appId);
					GlobalInfoVO6 globalInfoVO6 = global.getGlobalInfoVO();
					if (global.operationTime != 0) {
						if ((global.operationTime + ruleConfig.fixTime) > TimeUtils.getNow()) {
							return;
						}
					}
					
					Map<AppKey,Object> map = new HashMap<>();
					long maxLevelActorId = appGlobalDao.getMaxLevelOfAcotr();
					if(maxLevelActorId == globalInfoVO6.maxLevelActorId){
						return;
					}
					globalInfoVO6.maxLevelActorId = maxLevelActorId;
					Actor actor = actorFacade.getActor(maxLevelActorId);
					if(actor != null){
						map.put(AppKey.MAX_ACTOR_LEVEL, actor.actorName);
					}else{
						map.put(AppKey.MAX_ACTOR_LEVEL, "");
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
