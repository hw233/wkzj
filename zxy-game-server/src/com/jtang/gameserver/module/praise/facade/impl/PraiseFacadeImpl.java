package com.jtang.gameserver.module.praise.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.PRAISE_ACTIVE_REWARD_HAS_GET;
import static com.jiatang.common.GameStatusCodeConstant.PRAISE_NOT_ACTIVE;
import static com.jiatang.common.GameStatusCodeConstant.PRAISE_REWARD_HAS_GET;
import static com.jiatang.common.GameStatusCodeConstant.PRAISE_REWARD_TYPE_ERROR;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.EquipEnhancedEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.SnatchResultEvent;
import com.jtang.gameserver.dataconfig.model.PraiseConfig;
import com.jtang.gameserver.dataconfig.service.PraiseService;
import com.jtang.gameserver.dbproxy.entity.Praise;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.praise.dao.PraiseDao;
import com.jtang.gameserver.module.praise.facade.PraiseFacade;
import com.jtang.gameserver.module.praise.handler.response.PraiseDataResponse;
import com.jtang.gameserver.module.praise.helper.PraisePushHelper;
import com.jtang.gameserver.module.praise.type.PraiseRewardType;
@Component
public class PraiseFacadeImpl implements PraiseFacade, Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(PraiseFacadeImpl.class);
	@Autowired
	private PraiseDao praiseDao;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private GoodsFacade goodsFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private BattleFacade battleFacade;

	@PostConstruct
	private void init() {
		eventBus.register(EventKey.SNATCH_RESULT, this);
		eventBus.register(EventKey.EQUIP_ENHANCED, this);
	}
	
	@Override
	public TResult<PraiseDataResponse> getPraiseData(long actorId) {
		Praise praise = praiseDao.get(actorId);
		PraiseDataResponse commentDataResponse = new PraiseDataResponse(praise.isActive, praise.activeRewardGet, praise.praiseRewardGet);
		return TResult.sucess(commentDataResponse);
	}

	@Override
	public TResult<List<RewardObject>> getReward(long actorId, PraiseRewardType praiseRewardType) {
		if (praiseRewardType == null) {
			return TResult.valueOf(PRAISE_REWARD_TYPE_ERROR);
		}
		Praise praise = praiseDao.get(actorId);
		if (praise.isActive == 0) {
			return TResult.valueOf(PRAISE_NOT_ACTIVE);	
		}
		if (praiseRewardType.equals(PraiseRewardType.ACTIVE_REWARD) && praise.activeRewardGet == 1) {
			return TResult.valueOf(PRAISE_ACTIVE_REWARD_HAS_GET);
		}
		if (praiseRewardType.equals(PraiseRewardType.COMMENT_REWARD) && praise.praiseRewardGet == 1) {
			return TResult.valueOf(PRAISE_REWARD_HAS_GET);
		}
		
		
		PraiseConfig cfg = PraiseService.get();
		List<RewardObject> list = new ArrayList<>();
		if (praiseRewardType.equals(PraiseRewardType.ACTIVE_REWARD)) {
			if (cfg != null) {
				praise.activeRewardGet = 1;
				list = cfg.getActiveRewardList();
				sendReward(actorId, list);
				praiseDao.update(praise);
			}
		} else if (praiseRewardType.equals(PraiseRewardType.COMMENT_REWARD)) {
			if (cfg != null) {
				praise.praiseRewardGet = 1;
				list = cfg.getPraiseRewardList();
				sendReward(actorId, list);
				praiseDao.update(praise);
			}
		} else {
			return TResult.valueOf(PRAISE_REWARD_TYPE_ERROR);
		}
		
		
		return TResult.sucess(list);
	}

	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			switch (rewardObject.rewardType) {
			case EQUIP: {
				for (int i = 0; i < rewardObject.num; i++) {
					equipFacade.addEquip(actorId, EquipAddType.PRAISE, rewardObject.id);
				}
				break;
			}
			case HEROSOUL: {
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.PRAISE, rewardObject.id, rewardObject.num);
				break;
			}
			case GOODS: {
				goodsFacade.addGoodsVO(actorId, GoodsAddType.PRAISE, rewardObject.id, rewardObject.num);
				break;
			}

			default:
				LOGGER.error(String.format("类型错误，type:[%s]", rewardObject.rewardType.getCode()));
				break;
			}
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		
		if (paramEvent.getName() == EventKey.SNATCH_RESULT) {
			PraiseConfig cfg = PraiseService.get();
			if (cfg == null) {
				return;
			}
			SnatchResultEvent snatchResultEvent = paramEvent.convert();
			Praise praise = praiseDao.get(snatchResultEvent.actorId);
			if (praise.isActive != 0) {
				return;
			}
			int battleNum = battleFacade.getBatteTotalNum(snatchResultEvent.actorId, BattleType.SNATCH);
			if (battleNum >= cfg.getSnatchNum()) {
				praise.isActive = 1;
				praiseDao.update(praise);
				PraisePushHelper.pushActive(snatchResultEvent.actorId);
			}
			
		} else if (paramEvent.getName() == EventKey.EQUIP_ENHANCED) {
			PraiseConfig cfg = PraiseService.get();
			if (cfg == null) {
				return;
			}
			EquipEnhancedEvent enhancedEvent = paramEvent.convert();
			Praise praise = praiseDao.get(enhancedEvent.actorId);
			if (praise.isActive != 0) {
				return;
			}
			if (enhancedEvent.level >= cfg.getEquipUpgradeNum()){
				praise.isActive = 1;
				praiseDao.update(praise);
				PraisePushHelper.pushActive(enhancedEvent.actorId);
			}
		}
	}

}
