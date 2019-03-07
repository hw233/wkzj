package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_EXSIT;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_REWARD;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO20;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO20;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * <pre>
 * 活动:20
 * 描述:《新号首日冲级奖励》
 * 介绍：新建帐号当日24时之前掌教等级达到20级送普贤菩萨仙人魂魄30个
 * </pre>
 * @author hezh
 *
 */
@Component
public class AppParser20 extends AppParser{
	
	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_20;
	}
	
	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> ext,long appId) {
		Actor actor = actorFacade.getActor(actorId);
		if(actor == null){
			return ListResult.statusCode(GameStatusCodeConstant.ACTOR_ID_ERROR);
		}
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO20 recordInfoVO20 = appRecord.getRecordInfoVO();
		if(!recordInfoVO20.isCanGet()){
			return ListResult.statusCode(APP_NOT_REWARD);
		}
		if(recordInfoVO20.isGet()){
			return ListResult.statusCode(GameStatusCodeConstant.APP_GET);
		}
		List<RewardObject> rewardObject = appRuleConfig.getRewardGoodsList(level,true);
		boolean sendResult = sendReward(actorId, rewardObject);
		if (!sendResult) {
			return ListResult.statusCode(APP_NOT_REWARD);
		}
		recordInfoVO20.setIsFinish(2);
		appRecordDao.update(appRecord);
		return ListResult.statusCode(StatusCode.SUCCESS);
	}
	
	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO20 recordInfoVO20 = record.getRecordInfoVO();
		return new AppRecordVO(appId, recordInfoVO20.getRecordInfoMaps());
	}
	
	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		if(paramEvent instanceof ActorLevelUpEvent){
			ActorLevelUpEvent event = paramEvent.convert();
			long actorId = event.actorId;
			int level = ActorHelper.getActorLevel(actorId);
			Actor actor = actorFacade.getActor(actorId);
			AppRecord appRecord = appRecordDao.get(actorId, appId);
			RecordInfoVO20 recordInfoVO20 = appRecord.getRecordInfoVO();
			AppRuleConfig appRuleConfig = AppRuleService.get(appId);
			RuleConfigVO20 ruleConfigVO20 = appRuleConfig.getConfigRuleVO();
			if(!recordInfoVO20.isGet() && !recordInfoVO20.isCanGet() && DateUtils.isToday(actor.createTime) && level >= ruleConfigVO20.getNeedLevel()){
				recordInfoVO20.setIsFinish(1);
				appRecordDao.update(appRecord);
				AppPushHelper.pushAppRecord(actorId, new AppRecordVO(appId, recordInfoVO20.getRecordInfoMaps()));
			}
		}
	}

	@Override
	public void onActorLogin(long actorId, long appId) {
		
	}

	@Override
	public void onApplicationEvent() {
		
	}
	
	@Override
	public Result appEnable(long actorId,long appId){
		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO20 recordInfoVO20 = appRecord.getRecordInfoVO();
		Actor actor = actorFacade.getActor(actorId);
		//奖励已领取或创建新号时间不是当天活动都移除
		if(recordInfoVO20.isGet() || !DateUtils.isToday(actor.createTime)){
			return Result.valueOf(APP_NOT_EXSIT);
		}
		return super.appEnable(actorId, appId);
	}
}
