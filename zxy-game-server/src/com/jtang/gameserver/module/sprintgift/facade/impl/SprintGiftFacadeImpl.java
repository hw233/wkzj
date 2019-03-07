package com.jtang.gameserver.module.sprintgift.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.GIFT_HAD_RECEIVED;
import static com.jiatang.common.GameStatusCodeConstant.GIFT_LEVEL_NOT_REACH;
import static com.jiatang.common.GameStatusCodeConstant.PERVIOUS_GIFT_NOT_RECEIVE;
import static com.jtang.core.protocol.StatusCode.SUCCESS;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.gameserver.dataconfig.service.SprintGiftService;
import com.jtang.gameserver.dataconfig.service.VipService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.SprintGift;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.sprintgift.dao.SprintGiftDao;
import com.jtang.gameserver.module.sprintgift.facade.SprintGiftFacade;
import com.jtang.gameserver.module.sprintgift.type.SprintGiftStatusType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorPushHelper;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;

/**
 * @author ligang
 */
@Component
public class SprintGiftFacadeImpl implements SprintGiftFacade {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SprintGiftFacadeImpl.class);
	
	@Autowired
	SprintGiftDao sprintGiftDao;

	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	HeroFacade heroFacade;

	@Autowired
	EquipFacade equipFacade;

	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	EventBus eventBus;
	
	@Override
	public Map<Integer, Integer> getSprintGiftStatusList(long actorId) {
		SprintGift gifts = sprintGiftDao.get(actorId);
		Actor actor = actorFacade.getActor(actorId);
		Map<Integer, Integer> unreceiveMap = gifts.getUnreceivedMap();
		for (Map.Entry<Integer, Integer> entry : unreceiveMap.entrySet()) {
			if (entry.getKey() <= actor.level) {
				unreceiveMap.put(entry.getKey(), SprintGiftStatusType.CAN_RECEIVE.getType());
			}
		}
		return unreceiveMap;
	}

	@Override
	public SprintGift get(long actorId) {
		SprintGift gifts = sprintGiftDao.get(actorId);
		return gifts;
	}

	
	@Override
	public Result receiveGift(long actorId, int receiveLevel) {
		SprintGift gifts = sprintGiftDao.get(actorId);
		if (gifts.acceptAllGift) {
			return Result.valueOf(GIFT_HAD_RECEIVED);
		}
		Actor actor = actorFacade.getActor(actorId);
		if (actor.level < receiveLevel) {
			return Result.valueOf(GIFT_LEVEL_NOT_REACH);
		}
		if (this.getSprintGiftStatusList(actorId).containsKey(receiveLevel) == false) {
			return Result.valueOf(GIFT_HAD_RECEIVED);
		}

		int status = this.getSprintGiftStatusList(actorId).get(receiveLevel).intValue();
		SprintGiftStatusType type = SprintGiftStatusType.getByType(status);
		
		if (type == SprintGiftStatusType.DO_NOT_RECEIVE) {
			return Result.valueOf(GIFT_LEVEL_NOT_REACH);
		}
		if (type == SprintGiftStatusType.HAD_RECEIVED) {
			return Result.valueOf(GIFT_HAD_RECEIVED);
		}
		//检查前一级奖励是否已经领取
		if (this.getSprintGiftStatusList(actorId).containsKey(receiveLevel - 5)) {
			return Result.valueOf(PERVIOUS_GIFT_NOT_RECEIVE);
		}
		int selfLevel = vipFacade.getVipLevel(actorId);
		//读取配置中赠送的VIP等级
		int vipGiftLevel = SprintGiftService.getSprintGiftVIPLevelByLevel(receiveLevel);
		
		if (vipGiftLevel > 0) {
			if (selfLevel < vipGiftLevel && vipGiftLevel < VipService.maxLevel()) {
				List<RewardObject> rewardObjects =  SprintGiftService.getReward(receiveLevel, selfLevel, vipGiftLevel);
				sendReward(actorId, rewardObjects);

				vipFacade.updateVipLevel(actorId, vipGiftLevel);
				vipFacade.sendRewardByVipLevel(actorId, selfLevel, vipGiftLevel); // 送vip仅送vip等级
				ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIP_LEVEL, vipGiftLevel);
			} else {
				List<RewardObject> rewardObjects =  SprintGiftService.getReward(receiveLevel, selfLevel, vipGiftLevel);
				sendReward(actorId, rewardObjects);
			}
		} else {
			List<RewardObject> rewardObjects =  SprintGiftService.getReward(receiveLevel, selfLevel, 0);
			sendReward(actorId, rewardObjects);
		}
		
		//修改 对应等级礼包的领取状态
		gifts.sprintGiftsMap.put(receiveLevel, SprintGiftStatusType.HAD_RECEIVED.getType());
		gifts.judgeHadAcceptAllGift();
		sprintGiftDao.update(gifts);
		return Result.valueOf(SUCCESS);
	}
	
	@Override
	public boolean isAllGiftReceived(long actorId) {
		SprintGift gifts = this.get(actorId);
		return gifts.acceptAllGift;
	}
	
	
	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.SPRINT_GIFT, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.SPRINT_GIFT, id,
					num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SPRINT_GIFT, id,
					num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}

	/**
	 * 发放奖励
	 * 
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num,rewardObject.rewardType);
		}
	}


}
