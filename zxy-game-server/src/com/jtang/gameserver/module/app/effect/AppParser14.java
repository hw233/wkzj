package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_ARGS_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_LEVEL_NOT_REACH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_BUY_MAX;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.constant.AppRule;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO14;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO14;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.EnergyDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

/**
 * <pre>
 * xx点券购买xx物品
 * </pre>
 */
@Component
public class AppParser14 extends AppParser{

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> paramsMaps,long appId) {
		String strId = paramsMaps.get(AppKey.GOODS_ID.getKey());
		if (StringUtils.isBlank(strId)) {
			return ListResult.statusCode(APP_ARGS_ERROR);
		}
		
		Actor actor = actorFacade.getActor(actorId);
		if(actor.level < AppRule.APP_LOW_VIP_LEVEL_LIMIT){
			return ListResult.statusCode(APP_LEVEL_NOT_REACH);
		}
		
		AppRuleConfig config = getAppRuleConfig(appId);
		RuleConfigVO14 ruleConfigVO14 = config.getConfigRuleVO();//配置
		
		
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO14 appRecordExtVO14 = record.getRecordInfoVO();//存储数据
		
		int buyNum = appRecordExtVO14.costEnergyNum + appRecordExtVO14.costTicketNum;
		if(buyNum >= ruleConfigVO14.buyNum){
			return ListResult.statusCode(SHOP_BUY_MAX);
		}
		
		boolean isSuccess = false;
		if(appRecordExtVO14.costEnergyNum < ruleConfigVO14.costEnergyNum){//使用精力购买
			isSuccess = actorFacade.decreaseEnergy(actorId, EnergyDecreaseType.APP, ruleConfigVO14.costEnergy);
			if(isSuccess == false){
				return ListResult.statusCode(GameStatusCodeConstant.APP_ENERGY_NOT_ENOUGH);
			}
			appRecordExtVO14.costEnergyNum += 1;
		}else{//使用点券购买
			isSuccess = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTIVE_BUY, ruleConfigVO14.costTicket, 0, 0);
			if(isSuccess == false){
				return ListResult.statusCode(StatusCode.TICKET_NOT_ENOUGH);
			}
			appRecordExtVO14.costTicketNum += 1;
		}
		RewardObject rewardObject = new RewardObject(RewardType.getType(ruleConfigVO14.type),ruleConfigVO14.goodsId,ruleConfigVO14.num);
		sendReward(actorId, rewardObject);
		appRecordDao.update(record);
		AppRecordVO appRecordVO = new AppRecordVO(record.appId, appRecordExtVO14.getRecordInfoMaps());
		AppPushHelper.pushAppRecord(actorId, appRecordVO);
		return ListResult.list(rewardObject);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO14 recordInfoVO14 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO14 == null) {
			recordInfoVO14 = new RecordInfoVO14();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO14.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_14;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {

	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		resetValue(actorId,appId);
	}

	private void resetValue(long actorId,long appId) {
		if(appEnable(actorId,appId).isFail()) {
			return;
		}
		AppRecord record = appRecordDao.get(actorId,appId);
		RecordInfoVO14 recordInfoVO14 = record.getRecordInfoVO();
		if (!DateUtils.isToday(record.operationTime)) {
			recordInfoVO14.costEnergyNum = 0;
			recordInfoVO14.costTicketNum = 0;
			appRecordDao.update(record);
			AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO14.getRecordInfoMaps());
			AppPushHelper.pushAppRecord(actorId, appRecordVO);
		}
	}

	@Override
	public void onApplicationEvent() {
		schedule.addFixedTime(new Runnable(){
			@Override
			public void run() {
				for (Long actorId : playerSession.onlineActorList()) {
					for(long appId : AppRuleService.getAppId(getEffect().getId())){
						resetValue(actorId,appId);
					}
				}
			}
		}, 0,0,1);
	}

}
