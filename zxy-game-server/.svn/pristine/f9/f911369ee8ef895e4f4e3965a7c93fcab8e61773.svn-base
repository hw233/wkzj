package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_ARGS_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_LEVEL_NOT_REACH;

import java.util.List;
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
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO5;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO5;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeBuyVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

/**
 * <pre>
 * xx点券购买xx物品
 * </pre>
 */
@Component
public class AppParser5 extends AppParser{

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
		if(actor.level < AppRule.APP_LEVEL_LIMIT){
			return ListResult.statusCode(APP_LEVEL_NOT_REACH);
		}
		
		int goodsId = Integer.valueOf(strId);
		AppRuleConfig config = getAppRuleConfig(appId);
		RuleConfigVO5 ruleConfigVO5 = config.getConfigRuleVO();
		List<ExchangeBuyVO> list = ruleConfigVO5.buyList;

		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO5 recordInfoVO5 = record.getRecordInfoVO();
		Map<Integer, Integer> recordMap = recordInfoVO5.recordVOs;
		int buyCount = 0;
		if (recordMap.containsKey(goodsId)) {
			buyCount = recordMap.get(goodsId);
		}
		RewardObject rewardGoods = null;
		for (ExchangeBuyVO buyVO : list) {
			if (buyVO.id == goodsId && buyCount < buyVO.buyNum) {
				boolean isSuccess = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTIVE_BUY, buyVO.costTicket, buyVO.id, buyVO.num);
				if (isSuccess) {
					rewardGoods = new RewardObject(RewardType.getType(buyVO.type), buyVO.id, buyVO.num);
					sendReward(actorId, rewardGoods);
					buyCount++;
					recordMap.put(buyVO.id, buyCount);
					appRecordDao.update(record);
					AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO5.getRecordInfoMaps());
					AppPushHelper.pushAppRecord(actorId, appRecordVO);
				} else {
					return ListResult.statusCode(StatusCode.TICKET_NOT_ENOUGH);
				}
			} else {
				return ListResult.statusCode(GameStatusCodeConstant.SHOP_BUY_MAX);
			}
		}
		return ListResult.list(rewardGoods);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		RecordInfoVO5 recordInfoVO5 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO5 == null) {
			recordInfoVO5 = new RecordInfoVO5();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO5.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_5;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {

	}

	@Override
	public void onActorLogin(long actorId, long appId) {
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
		RecordInfoVO5 recordInfoVO5 = record.getRecordInfoVO();
		if (!DateUtils.isToday(record.operationTime)) {
			recordInfoVO5.recordVOs.clear();
			appRecordDao.update(record);
			AppRecordVO appRecordVO = new AppRecordVO(record.appId, recordInfoVO5.getRecordInfoMaps());
			AppPushHelper.pushAppRecord(actorId, appRecordVO);
		}
	}

}
