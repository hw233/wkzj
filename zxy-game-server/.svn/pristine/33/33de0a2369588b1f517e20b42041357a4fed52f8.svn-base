package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_FINISH;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_REWARD;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO1;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO1;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * <pre>
 * 活动:1
 * 描述:《限量仙人大放送》
 * 介绍：充值累计，每达200元则可获送5星仙人魂包。（如充值200获得1个魂包，充值达200x2的值则可获得2个魂包），外加大量潜修石、精炼石哟~机会难得，重在把握~！
 * </pre>
 * @author 0x737263
 *
 */
@Component
public class AppParser1 extends AppParser {
	
	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_1;
	}
	
	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> ext,long appId) {
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO1 recordInfoVO1 = record.getRecordInfoVO();
		int level = ActorHelper.getActorLevel(actorId);
		List<RewardObject> rewardObject = appRuleConfig.getRewardGoodsList(level,true);
		ChainLock lock = LockUtils.getLock(recordInfoVO1);
		try {
			lock.lock();
			if (recordInfoVO1.isReward() == false) {
				return ListResult.statusCode(APP_NOT_FINISH);
			}
			boolean sendResult = sendReward(actorId, rewardObject);
			if (sendResult == false) {
				return ListResult.statusCode(APP_NOT_REWARD);
			}

			recordInfoVO1.hadRewardNum += 1;

		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		appRecordDao.update(record);

		AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO1.getRecordInfoMaps());
		AppPushHelper.pushAppRecord(actorId, appRecordVO);
		return ListResult.list(rewardObject);
	}
	
	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO1 recordInfoVO1 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO1 == null) {
			recordInfoVO1 = new RecordInfoVO1();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO1.getRecordInfoMaps());
		return appRecordVO;
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
		if (paramEvent instanceof RechargeTicketsEvent) {
			RechargeTicketsEvent event = paramEvent.convert();
			AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
			RuleConfigVO1 ruleConfigVO1 = appRuleConfig.getConfigRuleVO();
			int needMoney = ruleConfigVO1.needMoney;
			
			AppRecord record = appRecordDao.get(event.actorId, appId);
			RecordInfoVO1 recordInfoVO1 = record.getRecordInfoVO();
			ChainLock lock = LockUtils.getLock(record, recordInfoVO1);
			try {
				lock.lock();
				recordInfoVO1.rechargeMoney += event.money;
				if (recordInfoVO1.rechargeMoney >= needMoney) {
					recordInfoVO1.rewardNum = recordInfoVO1.rechargeMoney / needMoney;
				}
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
			appRecordDao.update(record);
			AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO1.getRecordInfoMaps());
			AppPushHelper.pushAppRecord(event.actorId, appRecordVO);

		}
	}

	@Override
	public void onActorLogin(long actorId, long appId) {
		
	}

	@Override
	public void onApplicationEvent() {
		
	}
	
}
