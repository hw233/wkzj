package com.jtang.gameserver.module.crossbattle.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.CrossBattleActorRewardFlag;
import com.jtang.gameserver.dbproxy.entity.CrossBattleReward;
import com.jtang.gameserver.module.crossbattle.constant.CrossBattleRule;
import com.jtang.gameserver.module.crossbattle.dao.CrossBattleDao;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleRewardFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class CrossBattleRewardFacadeImpl implements CrossBattleRewardFacade {

	@Autowired
	CrossBattleDao crossBattleDao;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	
	@Override
	public TResult<List<RewardObject>> getReward(long actorId) {
		CrossBattleReward reward = crossBattleDao.getReward();
		if(reward == null){
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_SERVER_NOT_ENOUGH);
		}
		CrossBattleActorRewardFlag actorReward = crossBattleDao.get(actorId);
		long getTime = actorReward.getTime;
		if(getTime > reward.rewardTime){
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_REWARD_GET);
		}
		int level = ActorHelper.getActorLevel(actorId);
		if(level < CrossBattleRule.CROSS_BATTLE_REWARD_LEVEL){
			return TResult.valueOf(GameStatusCodeConstant.CROSS_BATTLE_SERVER_NOT_ENOUGH);
		}
		actorReward.getTime = System.currentTimeMillis();
		crossBattleDao.update(actorReward);
		List<RewardObject> rewardList = reward.getRewardList(level,reward.reward);
		sendReward(actorId,rewardList);
		return TResult.sucess(rewardList);
	}

	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for(RewardObject reward:rewardList){
			switch(reward.rewardType){
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.CROSS_BATTLE, reward.id);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.CROSS_BATTLE, reward.id,reward.num);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.CROSS_BATTLE, reward.id, reward.num);
				break;
			case NONE:
				break;
			default:
				break;
			}
		}
	}

	@Override
	public TResult<Integer> isGet(long actorId) {
		CrossBattleReward reward = crossBattleDao.getReward();
		if(reward == null){
			return TResult.sucess(0);
		}
		int level = ActorHelper.getActorLevel(actorId);
		if(level < CrossBattleRule.CROSS_BATTLE_REWARD_LEVEL){
			return TResult.sucess(0);
		}
		CrossBattleActorRewardFlag actorReward = crossBattleDao.get(actorId);
		if(actorReward.getTime > reward.rewardTime){
			return TResult.sucess(0);
		}
		return TResult.sucess(1);
	}

}
