package com.jtang.gameserver.module.app.effect;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO12;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO12;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class AppParser12 extends AppParser {

	@Autowired
	SysmailFacade sysmailFacade;

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO12 recordInfoVO12 = record.getRecordInfoVO();
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		RuleConfigVO12 ruleConfigVO12 = appRuleConfig.getConfigRuleVO();
		int level = ActorHelper.getActorLevel(actorId);
		List<RewardObject> list = appRuleConfig.getRewardGoodsList(level,true);
		if(recordInfoVO12.rechargeMoney >= ruleConfigVO12.getNum() && recordInfoVO12.isGet()){
			sendReward(actorId, list);
			recordInfoVO12.isSend = 1;
			appRecordDao.update(record);
			AppPushHelper.pushAppRecord(actorId, new AppRecordVO(appId, recordInfoVO12.getRecordInfoMaps()));
		}
		return ListResult.list(list);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO12 recordInfoVO12 = record.getRecordInfoVO();
		return new AppRecordVO(appId, recordInfoVO12.getRecordInfoMaps());
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_12;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		if (paramEvent instanceof RechargeTicketsEvent) {
			RechargeTicketsEvent event = paramEvent.convert();
			AppRecord record = appRecordDao.get(event.actorId, appId);
			RecordInfoVO12 recordInfoVO12 = record.getRecordInfoVO();
			recordInfoVO12.rechargeMoney += event.money;
			AppRecordVO appRecordVO = getAppRecord(event.actorId,appId);
			AppPushHelper.pushAppRecord(event.actorId, appRecordVO);
			appRecordDao.update(record);
		}
	}

	@Override
	public void onActorLogin(long actorId,long appId) {

	}

	@Override
	public void onApplicationEvent() {
		
	}

}
