package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_GET;
import static com.jiatang.common.GameStatusCodeConstant.APP_GET_FAIL;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO3;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * 
 * @author jianglf
 * 
 */
@Component
public class AppParser3 extends AppParser{

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO3 recordInfoVO3 = record.getRecordInfoVO();
		if (recordInfoVO3.isGet()) {// 已经领取过了
			return ListResult.statusCode(APP_GET);
		}
		int level = ActorHelper.getActorLevel(actorId);
		List<RewardObject> rewardList = appRuleConfig.getRewardGoodsList(level,true);
		boolean isSuccess = sendReward(actorId, rewardList);
		if (!isSuccess) {// 领取失败
			return ListResult.statusCode(APP_GET_FAIL);
		}
		recordInfoVO3.isGet = 1;
		appRecordDao.update(record);

		return ListResult.list(rewardList);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO3 recordInfoVO3 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO3 == null) {
			recordInfoVO3 = new RecordInfoVO3();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO3.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_3;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {

	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		resetValue(actorId,appId);
	}

	@Override
	public void onApplicationEvent() {
		schedule.addFixedTime(new Runnable() {
			@Override
			public void run() {
				for (Long actorId : playerSession.onlineActorList()) {
					for(long appId : AppRuleService.getAppId(getEffect().getId())){
						resetValue(actorId,appId);
					}
				}
			}
		}, 0);
	}
	
	private void resetValue(long actorId,long appId) {
		if (appEnable(actorId,appId).isFail()) {
			return;
		}

		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO3 recordInfoVO3 = record.getRecordInfoVO();
		if (!DateUtils.isToday(record.operationTime)) {
			recordInfoVO3.isGet = 0;
			appRecordDao.update(record);
			AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO3.getRecordInfoMaps());
			AppPushHelper.pushAppRecord(actorId, appRecordVO);
		}
	}
	
	

}
