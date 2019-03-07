package com.jtang.gameserver.module.luckstar.facade.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.LuckStarConfig;
import com.jtang.gameserver.dataconfig.model.LuckStarRewardConfig;
import com.jtang.gameserver.dataconfig.service.LuckStarService;
import com.jtang.gameserver.dbproxy.entity.LuckStar;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.luckstar.dao.LuckStarDao;
import com.jtang.gameserver.module.luckstar.facade.LuckStarFacade;
import com.jtang.gameserver.module.luckstar.handler.response.LuckStarRewardResponse;
import com.jtang.gameserver.module.luckstar.helper.LuckStarPushHelper;
import com.jtang.gameserver.module.luckstar.module.LuckStarVO;
import com.jtang.gameserver.module.luckstar.type.LuckStarRewardType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LuckStarFacadeImpl implements LuckStarFacade, ApplicationListener<ApplicationEvent>, ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuckStarFacadeImpl.class);

	@Autowired
	EquipFacade equipFacade;

	@Autowired
	HeroSoulFacade heroSoulFacade;

	@Autowired
	GoodsFacade goodsFacade;

	@Autowired
	ActorFacade actorFacade;

	@Autowired
	VipFacade vipFacade;

	@Autowired
	LuckStarDao luckStarDao;

	@Autowired
	private Schedule schedule;

	@Autowired
	private PlayerSession playerSession;

	@Override
	public TResult<LuckStarVO> getLuckStar(long actorId) {
		LuckStar luckStar = luckStarDao.get(actorId);
		LuckStarVO vo = parseToVO(luckStar);
		return TResult.sucess(vo);
	}

	@Override
	public TResult<LuckStarRewardResponse> getReward(long actorId) {
		LuckStar luckStar = luckStarDao.get(actorId);
		LuckStarConfig luckStarConfig = LuckStarService.getLuckStarConfig();
		
		int actorLevel = ActorHelper.getActorLevel(actorId);
		if (luckStarConfig.openLevel > actorLevel) {
			return TResult.valueOf(GameStatusCodeConstant.LUCK_STAR_NOT_OPEN_LEVEL);
		}
		
		if (luckStar.useNum < 1) {
			return TResult.valueOf(GameStatusCodeConstant.LUCK_STAR_NUM_REACH_LIMIT);
		}
		LuckStarRewardConfig rewardConfig = LuckStarService.random();
		ChainLock lock = LockUtils.getLock(luckStar);
		int flushTime = luckStarConfig.flushTime;
		try {
			lock.lock();
			luckStar.costUseNum();
			if (luckStar.isGet()) {
				rewardConfig = LuckStarService.getMastReward();
				luckStar.isGet = 1;
			}
			luckStarDao.update(luckStar);
			sendReward(actorId, rewardConfig, LuckStarService.getLuckStarConfig(), luckStar);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		LuckStarRewardResponse result = new LuckStarRewardResponse(rewardConfig.id, luckStar.useNum, flushTime);
		return TResult.sucess(result);
	}

	private void sendReward(long actorId, LuckStarRewardConfig config, LuckStarConfig luckStarConfig, LuckStar luckStar) {
		int actorLevel = ActorHelper.getActorLevel(actorId);
		LuckStarRewardType type = LuckStarRewardType.getType(config.rewardType);
		int num = 0;
		switch (type) {
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.LUCK_STAR, config.rewardId);
			break;
		case GOLD:
			num = FormulaHelper.executeCeilInt(config.num, actorLevel);
			actorFacade.addGold(actorId, GoldAddType.LUCK_STAR, num);
			break;
		case GOODS:
			num = FormulaHelper.executeCeilInt(config.num, actorLevel);
			goodsFacade.addGoodsVO(actorId, GoodsAddType.LUCK_STAR, config.rewardId, num);
			break;
		case HEROSOUL:
			num = Integer.valueOf(config.num);
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.LUCK_STAR, config.rewardId, num);
			break;
		case LUCKSTAR:
			luckStar.useNum = luckStarConfig.maxStar;
			luckStarDao.update(luckStar);
			break;
		case TICKET:
			num = FormulaHelper.executeCeilInt(config.num, actorLevel);
			vipFacade.addTicket(actorId, TicketAddType.LUCK_STAR, num);
			break;
		default:
			break;
		}
	}

	private LuckStarVO parseToVO(LuckStar luckStar) {
		int nextFlushTime = luckStar.flushTime + LuckStarService.getLuckStarConfig().flushTime - TimeUtils.getNow();
		LuckStarVO vo = new LuckStarVO();
		vo.useNum = luckStar.useNum;
		vo.time = nextFlushTime;
		return vo;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				for (Long actorId : actors) {
					flushStar(actorId);
				}
			}
		}, 1);
	}

	@Override
	public void onLogin(long actorId) {
		flushStar(actorId);
	}

	private void flushStar(Long actorId) {
		LuckStar luckStar = luckStarDao.get(actorId);
		LuckStarConfig config = LuckStarService.getLuckStarConfig();
		ChainLock lock = LockUtils.getLock(luckStar);
		try {
			lock.lock();
			if(luckStar.isAddUseNum(config.flushTime, config.maxStar)){
				luckStarDao.update(luckStar);
				LuckStarPushHelper.push(actorId, parseToVO(luckStar));
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}

}
