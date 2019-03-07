package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_ARGS_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_FINISH;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
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
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO8;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO8;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeLoginVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * <pre>
 * 《七天大礼包》
 * 介绍：活动期间，根据上线的天数送大礼，七天里天天上线的玩家会额外获得奖励哟~~
 * </pre>
 */
@Component
public class AppParser8 extends AppParser{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	
	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		int rewardDay = Integer.valueOf(paramsMaps.get(AppKey.LOGIN_DAY.getKey()));
		RuleConfigVO8 ruleConfigVO8 = getAppRuleConfig(appId).getConfigRuleVO();
		if(ruleConfigVO8.reward.keySet().contains(rewardDay) == false){
			return ListResult.statusCode(APP_ARGS_ERROR);
		}

		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO8 recordInfoVO8 = record.getRecordInfoVO();
		if(recordInfoVO8.reward.get(rewardDay) != 1){
			return ListResult.statusCode(APP_NOT_FINISH);
		}
		
		ExchangeLoginVO exchangeLoginVO = ruleConfigVO8.reward.get(rewardDay);

		if (exchangeLoginVO == null) {// 没有满足领取条件
			return ListResult.statusCode(APP_NOT_FINISH);
		}
		RewardObject rewardGoods = new RewardObject(RewardType.GOODS, exchangeLoginVO.goodsId, exchangeLoginVO.num);
		sendReward(actorId, rewardGoods);

		recordInfoVO8.reward.put(rewardDay, 2);
		appRecordDao.update(record);
		
		AppRecordVO appRecordVO = getAppRecord(actorId,appId);
		AppPushHelper.pushAppRecord(actorId, appRecordVO);

		return ListResult.list(rewardGoods);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO8 recordInfoVO8 =  appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO8 == null) {
			recordInfoVO8 = new RecordInfoVO8();
		}
		Map<AppKey, Object> map = recordInfoVO8.getRecordInfoMaps();
		AppRecordVO appRecordVO = new AppRecordVO(appId, map);
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_8;
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
		RecordInfoVO8 recordInfoVO8 = appRecord.getRecordInfoVO();
		ChainLock lock = LockUtils.getLock(recordInfoVO8);
		try {
			lock.lock();
			if(TimeUtils.inYesterday(recordInfoVO8.loginTime)){//如果是昨天
				recordInfoVO8.loginDay+=1;
			}else{
				if(DateUtils.isToday(recordInfoVO8.loginTime) == false){//如果不是今天
					recordInfoVO8.loginDay = 1;
				}
			}
			recordInfoVO8.loginTime = TimeUtils.getNow();
			
			
			RuleConfigVO8 ruleConfigVO8 = getAppRuleConfig(appId).getConfigRuleVO();
			if(!ruleConfigVO8.reward.isEmpty()){
				for (Integer key : ruleConfigVO8.reward.keySet()) {
					if(recordInfoVO8.reward.get(key) == 2){//已经领取过了
						continue;
					}
					if (recordInfoVO8.loginDay >= key) {
						recordInfoVO8.reward.put(key, 1);//设置为可领取状态
					}
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
