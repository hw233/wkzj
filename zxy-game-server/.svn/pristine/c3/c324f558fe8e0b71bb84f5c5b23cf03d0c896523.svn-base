package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_CLOSED;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO18;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO18;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * <pre>
 * 《七天大礼包》
 * 介绍：活动期间，根据上线的天数送大礼，七天里天天上线的玩家会额外获得奖励哟~~
 * </pre>
 */
@Component
public class AppParser18 extends AppParser{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private SysmailFacade sysmailFacade;
	
	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		return ListResult.statusCode(APP_CLOSED);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO18 recordInfoVO18 =  appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO18 == null) {
			recordInfoVO18 = new RecordInfoVO18();
		}
		Map<AppKey, Object> map = recordInfoVO18.getRecordInfoMaps();
		AppRecordVO appRecordVO = new AppRecordVO(appId, map);
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_18;
	}


	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		loginRecord(actorId,appId);
	}

	@Override
	public void onApplicationEvent() {
		schedule.addFixedTime(new Runnable() {
			@Override
			public void run() {
				for(long appId : AppRuleService.getAppId(getEffect().getId())){
					if(appEnable(appId) == false) {
						return;
					}
					for(long actorId : playerSession.onlineActorList()){
						loginRecord(actorId,appId);
					}
				}
			}
		},0);
	}
	
	private void loginRecord(long actorId,long appId) {
		AppRecord appRecord = appRecordDao.get(actorId, appId);
		RecordInfoVO18 recordInfoVO18 = appRecord.getRecordInfoVO();
		ChainLock lock = LockUtils.getLock(recordInfoVO18);
		try {
			lock.lock();
			if(DateUtils.isToday(recordInfoVO18.loginTime) == false){//不是今天
				recordInfoVO18.loginDay += 1;
			}
			recordInfoVO18.loginTime = TimeUtils.getNow();
			
			RuleConfigVO18 ruleConfigVO18 = getAppRuleConfig(appId).getConfigRuleVO();
			for(Entry<Integer,List<RewardObject>> entry:ruleConfigVO18.reward.entrySet()){
				if(recordInfoVO18.rewardType.containsKey(entry.getKey())){
					recordInfoVO18.rewardType.put(entry.getKey(),recordInfoVO18.rewardType.get(entry.getKey()));
				}else{
					recordInfoVO18.rewardType.put(entry.getKey(), 0);
				}
				if(recordInfoVO18.loginDay >= entry.getKey() && recordInfoVO18.isGet(entry.getKey()) == false){
					sysmailFacade.sendSysmail(actorId, SysmailType.APP_LOGIN_REWARD, entry.getValue(), String.valueOf(entry.getKey()));
					recordInfoVO18.rewardType.put(entry.getKey(), 1);
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		appRecordDao.update(appRecord);
		AppRecordVO appRecordVO = getAppRecord(actorId,appId);
		AppPushHelper.pushAppRecord(actorId, appRecordVO);
	}

}
