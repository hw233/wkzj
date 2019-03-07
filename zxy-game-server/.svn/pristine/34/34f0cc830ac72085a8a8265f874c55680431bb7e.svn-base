package com.jtang.gameserver.module.app.effect;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO16;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO16;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class AppParser16 extends AppParser {

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
		RecordInfoVO16 recordInfoVO16 = record.getRecordInfoVO();
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		RuleConfigVO16 ruleConfigVO16 = appRuleConfig.getConfigRuleVO();
		int level = ActorHelper.getActorLevel(actorId);
		List<RewardObject> list = appRuleConfig.getRewardGoodsList(level,true);
		if(recordInfoVO16.rechargeMoney >= ruleConfigVO16.getNum() && recordInfoVO16.isGet()){
			sendReward(actorId, list);
			recordInfoVO16.isSend = 1;
			appRecordDao.update(record);
			AppPushHelper.pushAppRecord(actorId, new AppRecordVO(appId, recordInfoVO16.getRecordInfoMaps()));
		}
		return ListResult.list(list);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO16 recordInfoVO16 = record.getRecordInfoVO();
		return new AppRecordVO(appId, recordInfoVO16.getRecordInfoMaps());
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_16;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		if (paramEvent instanceof RechargeTicketsEvent) {
			RechargeTicketsEvent event = paramEvent.convert();
			AppRecord record = appRecordDao.get(event.actorId, appId);
			ChainLock lock = LockUtils.getLock(record);
			try{
				lock.lock();
				reset(event.actorId,appId);
				RecordInfoVO16 recordInfoVO16 = record.getRecordInfoVO();
				recordInfoVO16.rechargeMoney += event.money;
				AppRecordVO appRecordVO = getAppRecord(event.actorId,appId);
				AppPushHelper.pushAppRecord(event.actorId, appRecordVO);
				appRecordDao.update(record);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		reset(actorId,appId);
	}
	
	private void reset(long actorId,long appId) {
		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO16 recordInfoVO16 = appRecord.getRecordInfoVO();
		if(DateUtils.isToday(appRecord.operationTime) == false){
			recordInfoVO16.isSend = 0;
			recordInfoVO16.rechargeMoney = 0;
			appRecordDao.update(appRecord);
			AppRecordVO appRecordVO = getAppRecord(actorId,appId);
			AppPushHelper.pushAppRecord(actorId, appRecordVO);
		}
	}

	@Override
	public void onApplicationEvent() {
		schedule.addFixedTime(new Runnable(){
			@Override
			public void run() {
				for (long actorId : playerSession.onlineActorList()) {
					for(long appId : AppRuleService.getAppId(getEffect().getId())){
						AppRecord appRecord = appRecordDao.get(actorId, appId);
						ChainLock lock = LockUtils.getLock(appRecord);
						try{
							lock.lock();
							reset(actorId, appId);
						}catch(Exception e){
							LOGGER.error("{}",e);
						}finally{
							lock.unlock();
						}
					}
				}
			}
		}, 0, 0, 1);
	}

}
