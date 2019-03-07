package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_REWARD;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.constant.AppRule;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO11;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO11;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketAddType;

@Component
public class AppParser11 extends AppParser {

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		return ListResult.statusCode(APP_NOT_REWARD);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO11 recordInfoVO11 = record.getRecordInfoVO();
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		RuleConfigVO11 ruleConfigVO11 = appRuleConfig.getConfigRuleVO();
		int firstTime = vipFacade.getFirstRechargeTime(actorId);
		int now = TimeUtils.getNow();
		int endTime = firstTime + ruleConfigVO11.getTime() - now;
		endTime = endTime > 0 ? endTime : 0;
		Map<AppKey, Object> map = new HashMap<>();
		map.put(AppKey.REMAIN_TIME, endTime);
		map.put(AppKey.RECHARGE_MONEY, recordInfoVO11.rechargeMoney);
		return new AppRecordVO(appId, map);
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_11;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		if (paramEvent instanceof RechargeTicketsEvent) {
			RechargeTicketsEvent event = paramEvent.convert();
			int rechargeNum = vipFacade.getRechargeNum(event.actorId);
			if (rechargeNum > 1) { // 首次充值不处理
				AppRecord record = appRecordDao.get(event.actorId, appId);
				RecordInfoVO11 recordInfoVO11 = record.getRecordInfoVO();
				if (recordInfoVO11.rechargeMoney < AppRule.APP_RECARGE_MONEY_LIMIT) {// 充值金额未大于限制金额
					AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
					RuleConfigVO11 ruleConfigVO11 = appRuleConfig.getConfigRuleVO();
					int now = TimeUtils.getNow();
					int firstTime = vipFacade.getFirstRechargeTime(event.actorId);
					if (now - firstTime <= ruleConfigVO11.getTime()) {
						int giveNum = event.currentTicket * ruleConfigVO11.getNum();
						vipFacade.addTicket(event.actorId, TicketAddType.APP, giveNum);
						recordInfoVO11.rechargeMoney += event.money;
						appRecordDao.update(record);
					}
				}
			}
			AppRecordVO appRecordVO = getAppRecord(event.actorId,appId);
			AppPushHelper.pushAppRecord(event.actorId, appRecordVO);
		}
	}

	@Override
	public void onActorLogin(long actorId,long appId) {

	}

	@Override
	public void onApplicationEvent() {
		
	}

}
