package com.jtang.gameserver.module.app.effect;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.ListResult;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO10;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO10;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketAddType;

@Component
public class AppParser10 extends AppParser {

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
		if(actor == null){
			return ListResult.statusCode(GameStatusCodeConstant.ACTOR_ID_ERROR);
		}
		
		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO10 recordInfoVO10 = appRecord.getRecordInfoVO();
		if(recordInfoVO10.isGet()){
			return ListResult.statusCode(GameStatusCodeConstant.APP_GET);
		}
		
		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		RuleConfigVO10 ruleConfigVO10 = appRuleConfig.getConfigRuleVO();
		boolean isSuccess = vipFacade.addTicket(actorId, TicketAddType.APP, ruleConfigVO10.ticket);
		if(isSuccess){
			recordInfoVO10.isGet = 1;
			appRecordDao.update(appRecord);
		}
		return ListResult.statusCode(StatusCode.SUCCESS);

	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO10 recordInfoVO10 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO10 == null) {
			recordInfoVO10 = new RecordInfoVO10();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO10.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_10;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		
	}

	@Override
	public void onApplicationEvent() {
		
	}
}
