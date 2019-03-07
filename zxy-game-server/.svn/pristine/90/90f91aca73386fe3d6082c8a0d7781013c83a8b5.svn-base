package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_ARGS_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_GET;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_FINISH;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.global.GlobalInfoVO7;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO7;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO7;
import com.jtang.gameserver.module.app.model.extension.rulevo.RankGoodsVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * <pre>
 * 《老君降临》
 * 活动期间充值前三名,活动结束后发放奖励
 * </pre>
 */
@Component
public class AppParser7 extends AppParser{

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		GlobalInfoVO7 globalInfoVO7 = appGlobalDao.getGloabalInfoVO(appId);
		Map<AppKey, Object> map = putName(globalInfoVO7);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig, map, level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		RuleConfigVO7 ruleConfigVO7 = appRuleConfig.getConfigRuleVO();
		if (ruleConfigVO7 == null) {
			return ListResult.statusCode(APP_ARGS_ERROR);
		}
		
		AppGlobal global = appGlobalDao.get(appId);
		GlobalInfoVO7 globalInfoVO7 = global.getGlobalInfoVO();
		if(!globalInfoVO7.actors.contains(actorId)){
			return ListResult.statusCode(APP_NOT_FINISH);
		}

		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO7 recordInfoVO7 = record.getRecordInfoVO();
		if (recordInfoVO7.isGet()) {
			return ListResult.statusCode(APP_GET);
		}
		
		int rank = globalInfoVO7.actors.indexOf(actorId);
		RankGoodsVO rankGoodsVO = ruleConfigVO7.rewardMap.get(rank);
		RewardObject rewardObject = new RewardObject(RewardType.GOODS,rankGoodsVO.goodsId , rankGoodsVO.num);
		boolean flag = sendReward(actorId, rewardObject);
		if (flag == true) {
			recordInfoVO7.isGet = 1;
			recordInfoVO7.rewardTime = TimeUtils.getNow();
			appRecordDao.update(record);
		}
		return ListResult.list(rewardObject);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO7 recordInfoVO7 = appRecordDao.getRecordInfoVO(actorId,appId);
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO7.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_7;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long actorId) {

	}

	@Override
	public void onActorLogin(long actorId,long appId) {

	}


	@Override
	public void onApplicationEvent() {
		schedule.addEveryHour(new Runnable() {
			@Override
			public void run() {
				for(long appId : AppRuleService.getAppId(getEffect().getId())){
					if (appEnable(appId) == false) {
						return;
					}
	
					AppRuleConfig ruleConfig = AppRuleService.get(appId);
					List<Long> actors = appGlobalDao.getMaxPayMoney(ruleConfig.getStartTime(), TimeUtils.getNow());
					AppGlobal global = appGlobalDao.get(appId);
					GlobalInfoVO7 globalInfoVO7 = global.getGlobalInfoVO();
					globalInfoVO7.actors = actors;
					appGlobalDao.update(global);
					for (Long actorId : playerSession.onlineActorList()) {
						int level = ActorHelper.getActorLevel(actorId);
						AppGlobalVO appGlobalVO = new AppGlobalVO(ruleConfig, putName(globalInfoVO7),level);
						AppPushHelper.pushAppGlobal(actorId, appGlobalVO);
					}
				}
			}
		});
	}
	
	private Map<AppKey, Object> putName(GlobalInfoVO7 globalInfoVO7) {
		Map<AppKey, Object> map = new HashMap<>();
		map.putAll(globalInfoVO7.getGlobalInfoMaps());
		for (Entry<AppKey, Object> entity : map.entrySet()) {
			long actorId = (long) entity.getValue();
			switch (entity.getKey()) {
			case ONE_PAY_ACTOR:
				map.put(AppKey.ONE_PAY_ACTOR, actorId + Splitable.ATTRIBUTE_SPLIT + actorFacade.getActor(actorId).actorName);
				break;
			case TWO_PAY_ACTOR:
				map.put(AppKey.TWO_PAY_ACTOR, actorId + Splitable.ATTRIBUTE_SPLIT + actorFacade.getActor(actorId).actorName);
				break;
			case THREE_PAY_ACTOR:
				map.put(AppKey.THREE_PAY_ACTOR, actorId + Splitable.ATTRIBUTE_SPLIT + actorFacade.getActor(actorId).actorName);
				break;
			default:
				break;
			}
		}
		return map;
	}

}
