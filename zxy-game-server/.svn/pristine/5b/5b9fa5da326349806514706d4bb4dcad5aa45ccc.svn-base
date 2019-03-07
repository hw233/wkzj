package com.jtang.gameserver.module.extapp.randomreward.facade.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.RandomRewardPoolConfig;
import com.jtang.gameserver.dataconfig.service.RandomRewardService;
import com.jtang.gameserver.dbproxy.entity.RandomReward;
import com.jtang.gameserver.module.extapp.randomreward.dao.RandomRewardDao;
import com.jtang.gameserver.module.extapp.randomreward.facade.RandomRewardFacade;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.GetRewardResponse;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.RandomRewardResponse;
import com.jtang.gameserver.module.extapp.randomreward.model.RewardVO;
import com.jtang.gameserver.module.extapp.randomreward.type.RandomRewardPoolType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;

@Component
public class RandomRewardFacadeImpl implements RandomRewardFacade {

	@Autowired
	RandomRewardDao randomRewardDao;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	VipFacade vipFacade;
	
	@Override
	public TResult<RandomRewardResponse> getInfo(long actorId) {
		RandomReward randomReward = randomRewardDao.get(actorId);
		RandomRewardResponse resposne = new RandomRewardResponse(randomReward.rewardMap.values());
		return TResult.sucess(resposne);
	}

	@Override
	public TResult<GetRewardResponse> getReward(long actorId, int id) {
		RandomReward randomReward = randomRewardDao.get(actorId);
		RewardVO rewardVO = randomReward.rewardMap.get(id);
		if(rewardVO == null){
			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(rewardVO.flushTime - TimeUtils.getNow() > 0){
			return TResult.valueOf(GameStatusCodeConstant.RANDOM_REWARD_COOLING);
		}
		int level = ActorHelper.getActorLevel(actorId);
		RandomRewardPoolConfig rewardConfig = RandomRewardService.getReward(level,id);
		Map<Integer,Integer> rewardMap = new HashMap<>();
		sendReward(actorId,rewardConfig,level,rewardMap);
		rewardVO.flushTime = TimeUtils.getNow() + RandomRewardService.getRewardConfig(id).getFlushTime();
		randomRewardDao.update(randomReward);
		GetRewardResponse response = new GetRewardResponse(randomReward.rewardMap.get(id),rewardMap);
		return TResult.sucess(response);
	}

	private void sendReward(long actorId,RandomRewardPoolConfig rewardConfig,int level, Map<Integer, Integer> rewardMap) {
		RandomRewardPoolType poolType = RandomRewardPoolType.getType(rewardConfig.rewardType);
		int num = FormulaHelper.executeCeilInt(rewardConfig.num, level);
		rewardMap.put(rewardConfig.rewardType, num);
		switch(poolType){
		case TYPE1:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.RANDOM_REWARD, rewardConfig.rewardId,num);
			break;
		case TYPE2:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.RANDOM_REWARD, rewardConfig.rewardId,num);
			break;
		case TYPE3:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.RANDOM_REWARD, rewardConfig.rewardId,num);
			break;
		case TYPE4:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.RANDOM_REWARD, rewardConfig.rewardId,num);
			break;
		case TYPE5:
			actorFacade.addGold(actorId, GoldAddType.RANDOM_REWARD, num);
			break;
		case TYPE6:
			vipFacade.addTicket(actorId, TicketAddType.RANDOM_REWARD, num);
			break;
		default:
			break;
		}
	}

}
