package com.jtang.gameserver.module.app.effect;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.GameEvent;
import com.jtang.core.lop.LopClient;
import com.jtang.core.lop.result.LopResult;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.lop.request.GivePhoneChargeRequest;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO9;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO9;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class AppParser9 extends AppParser {

	@Autowired
	LopClient lopClient;

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		Actor actor = actorFacade.getActor(actorId);

		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		RuleConfigVO9 ruleConfigVO9 = appRuleConfig.getConfigRuleVO();
		if(ruleConfigVO9.type == 1){//话费
			if (actor.level < ruleConfigVO9.ext) {
				return ListResult.statusCode(GameStatusCodeConstant.APP_NOT_FINISH);
			}
		}else{
			int rechargeNum = vipFacade.getRechargeNum(actorId);
			if(rechargeNum <= 0){
				return ListResult.statusCode(GameStatusCodeConstant.APP_NOT_FINISH);
			}
		}

		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO9 recordInfoVO9 = appRecord.getRecordInfoVO();
		if (recordInfoVO9.isFinish == 0) {
			return ListResult.statusCode(GameStatusCodeConstant.APP_NOT_FINISH);
		}

		if (recordInfoVO9.isFinish == 2) {
			return ListResult.statusCode(GameStatusCodeConstant.APP_GET);
		}

		String phoneNumber = paramsMaps.get(AppKey.PHONE_NUMBER.getKey()).trim();

		GivePhoneChargeRequest request = new GivePhoneChargeRequest();
		request.channelId = actor.channelId;
		request.platformId = actor.platformType;
		request.uid = actor.uid;
		request.serverId = actor.serverId;
		request.actorId = actorId;
		request.actorName = actor.actorName;
		request.mobilenum = phoneNumber;
		request.actId = appId;
		request.type = ruleConfigVO9.type;
		LopResult result = lopClient.executeResult(request);
		if (result.successed) {
			recordInfoVO9.isFinish = 2;
			appRecordDao.update(appRecord);
		}
		Map<AppKey, Object> map = new HashMap<>();
		map.putAll(recordInfoVO9.getRecordInfoMaps());
		
		String msg = result.message;
		msg = msg.replaceAll(":", "");
		map.put(AppKey.MSG, msg);
		
		AppRecordVO appRecordVO = new AppRecordVO(appId, map);
		AppPushHelper.pushAppRecord(actorId, appRecordVO);
		return ListResult.statusCode(StatusCode.SUCCESS);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO9 recordInfoVO9 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO9 == null) {
			recordInfoVO9 = new RecordInfoVO9();
		}

		Map<AppKey, Object> map = new HashMap<>();
		map.put(AppKey.MSG, "");
		map.putAll(recordInfoVO9.getRecordInfoMaps());
		AppRecordVO appRecordVO = new AppRecordVO(appId, map);
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_9;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		if (paramEvent instanceof ActorLevelUpEvent) {
			ActorLevelUpEvent event = paramEvent.convert();
			AppRuleConfig appRuleConfig = AppRuleService.get(appId);
			checkFinish(event.actorId, appRuleConfig,appId);
		}
		if (paramEvent instanceof RechargeTicketsEvent){
			RechargeTicketsEvent event = paramEvent.convert();
			AppRuleConfig appRuleConfig = AppRuleService.get(appId);
			checkFinish(event.actorId, appRuleConfig, appId);
		}
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		checkFinish(actorId, appRuleConfig,appId);
	}

	private void checkFinish(long actorId, AppRuleConfig appRuleConfig,long appId) {
		RuleConfigVO9 ruleConfigVO9 = appRuleConfig.getConfigRuleVO();
		Actor actor = actorFacade.getActor(actorId);
		boolean isOk = false;
		if(ruleConfigVO9.type == 1){//话费
			if (ruleConfigVO9.ext <= actor.level) {// 等级符合
				isOk = true;
			}
		}else{
			int rechargeNum = vipFacade.getRechargeNum(actorId);
			if(rechargeNum > 0){
				isOk = true;
			}
		}
		if(isOk){
			AppRecord record = appRecordDao.get(actorId, appId);
			RecordInfoVO9 recordInfoVO9 = record.getRecordInfoVO();
			if (recordInfoVO9.isFinish != 2) {// 状态不是已领取
				recordInfoVO9.isFinish = 1;// 设置状态为可领取
				appRecordDao.update(record);
				AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO9.getRecordInfoMaps());
				AppPushHelper.pushAppRecord(actorId, appRecordVO);
			}
		}
		
	}

	@Override
	public void onApplicationEvent() {
		
	}

}
