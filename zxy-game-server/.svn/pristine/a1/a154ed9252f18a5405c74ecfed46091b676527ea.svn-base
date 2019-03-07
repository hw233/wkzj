package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_ARGS_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_FINISH;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_REWARD;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO2;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeGoods;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * 造福星光
 * 介绍：活动期间，玩家金币抢夺不低于自己等级的对手大胜，有几率获得，星光碎片，星光碎片可在云游仙商处兑换好东西哟~！
 * @author ludd
 *
 */
@Component
public class AppParser2 extends AppParser{
	
	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_2;
	}
	
	@Override
	public AppGlobalVO getAppGlobalVO(long actorId,long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId, Map<String, String> ext,long appId) {
		String strId = ext.get(AppKey.GOODS_ID.getKey());
		String strNum = ext.get(AppKey.GOODS_NUM.getKey());
		if (StringUtils.isBlank(strId) || StringUtils.isBlank(strNum)) {
			return ListResult.statusCode(APP_ARGS_ERROR);
		}

		int goodsId = Integer.valueOf(strId);
		int goodsNum = Integer.valueOf(strNum);
		GoodsVO goodsVO = goodsFacade.getGoodsVO(actorId, goodsId);
		if (goodsVO == null || goodsVO.num < goodsNum) {
			return ListResult.statusCode(APP_NOT_FINISH);
		}
		AppRuleConfig config = getAppRuleConfig(appId);
		RuleConfigVO2 ruleConfigVO2 = config.getConfigRuleVO();
		ExchangeGoods reward = ruleConfigVO2.get(goodsId, goodsNum);
		if (reward == null) {
			return ListResult.statusCode(APP_NOT_REWARD);
		}

		goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.APP_DECREASE, reward.goodsId, reward.goodsNum);
		RewardObject rewardGoods = new RewardObject(reward.targetRewardType, reward.targetId, reward.targetNum);
		sendReward(actorId, rewardGoods);

		return ListResult.list(rewardGoods);
	}

	@Override
	public AppRecordVO getAppRecord(long actorId,long appId) {
		AppRecordVO appRecordVO = new AppRecordVO(appId);
		return appRecordVO;
	}

	@Override
	public void onGameEvent(GameEvent paramEvent,long appId) {
		
	}

	@Override
	public void onActorLogin(long actorId,long appId) {
		resetValue(actorId,appId);
	}
	
	private void resetValue(long actorId,long appId) {
		if (appEnable(actorId,appId).isFail()) {
			return;
		}
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

}
