package com.jtang.gameserver.module.extapp.deitydesc.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.DEITY_DESCEND_HAD_RECEIVED;
import static com.jiatang.common.GameStatusCodeConstant.DEITY_DESCEND_NOT_OPEN;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.DeityDescendHeroConfig;
import com.jtang.gameserver.dataconfig.service.DeityDescendService;
import com.jtang.gameserver.dbproxy.entity.DeityDescend;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.deitydesc.dao.DeityDescendDao;
import com.jtang.gameserver.module.extapp.deitydesc.facade.DeityDescendFacade;
import com.jtang.gameserver.module.extapp.deitydesc.helper.DeityDescendPushHelper;
import com.jtang.gameserver.module.extapp.deitydesc.model.DeityDescendVO;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class DeityDescendFacadeImpl implements DeityDescendFacade, ActorLoginListener, ApplicationListener<ContextRefreshedEvent>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeityDescendFacadeImpl.class);
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	DeityDescendDao deityDescendDao;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	HeroFacade heroFacade;

	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	PlayerSession playerSession;
	/**
	 * 是否开启
	 */
	private boolean isOpen = false;
	
	@Override
	public TResult<DeityDescendVO> getDeityDescendInfo(long actorId) {
		Result result = this.baseConditionCheck();
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		DeityDescend deityDescend = deityDescendDao.get(actorId);
		int currentHeroId = DeityDescendService.getCurrentHeroId();
		DeityDescendVO vo = deityDescend.getDeityDescendVO(currentHeroId);
		return TResult.sucess(vo);
	}

	@Override
	public TResult<List<RewardObject>> hitDeityDescend(long actorId, byte hitCount) {
		Result result = this.baseConditionCheck();
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		int currentHeroId = DeityDescendService.getCurrentHeroId();
		int cost = DeityDescendService.getCostByHitCount(currentHeroId, hitCount);
		if (cost == 0) {
			return TResult.valueOf(TICKET_NOT_ENOUGH);
		}
		if (vipFacade.hasEnoughTicket(actorId, cost)) {
			vipFacade.decreaseTicket(actorId, TicketDecreaseType.DEITY_DESCEND, cost, 0, 0);
		} else {
			return TResult.valueOf(TICKET_NOT_ENOUGH);
		}
		//发奖励
		DeityDescendHeroConfig heroConfig = DeityDescendService.getDescendHeroConfigByHeroId(currentHeroId);
		List<RewardObject> rewardObjects = new ArrayList<RewardObject>();

		DeityDescend deityDescend = deityDescendDao.get(actorId);
		ChainLock lock = LockUtils.getLock(deityDescend);
		try {
			lock.lock();
			DeityDescendVO vo = deityDescend.getDeityDescendVO(currentHeroId);
			int actorLevel = ActorHelper.getActorLevel(actorId);
			int probility = DeityDescendService.getHitPorbByHeroIdAndIndex(currentHeroId, vo.curIndex);
			int useNum = DeityDescendService.getUseNumByHeroIdAndIndex(currentHeroId, vo.curIndex);
			for (int i = 0; i < hitCount; i++) {
				vo.totalHit++;
				deityDescend.totalHit++;
				rewardObjects.addAll(heroConfig.getHitRewardList(actorLevel));
				if (vo.isAllLighted()) {
					continue;
				}
				//计算点亮
				boolean probLight = RandomUtils.is1000Hit(probility);
				boolean leastLight = vo.totalHit >= useNum;
				if (probLight | leastLight) {
					vo.curIndex++;
					deityDescend.currentCharIndex = vo.curIndex;
					probility = DeityDescendService.getHitPorbByHeroIdAndIndex(currentHeroId, vo.curIndex);
					useNum = DeityDescendService.getUseNumByHeroIdAndIndex(currentHeroId, vo.curIndex);
				}
			}
			deityDescendDao.update(deityDescend);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		sendReward(actorId, rewardObjects);
		return TResult.sucess(rewardObjects);
	}
	

	private Result baseConditionCheck() {
		int startTime = DeityDescendService.getStartDate();
		int endTime = DeityDescendService.getEndDate();
		if (DateUtils.isActiveTime(startTime, endTime) == false) {
			return Result.valueOf(DEITY_DESCEND_NOT_OPEN);
		}
		return Result.valueOf();
	}
	
	@Override
	public Result receiveHero(long actorId) {
		Result result = this.baseConditionCheck();
		if (result.isFail()) {
			return result;
		}
		int currentHeroId = DeityDescendService.getCurrentHeroId();
		DeityDescend deityDescend = deityDescendDao.get(actorId);
		DeityDescendVO vo = deityDescend.getDeityDescendVO(currentHeroId);
		if (vo.curIndex == 99) {
			return Result.valueOf(DEITY_DESCEND_HAD_RECEIVED);
		}
		if (heroFacade.isHeroExits(actorId, currentHeroId) == false) {
			heroFacade.addHero(actorId, HeroAddType.DEITY_DESCEND, currentHeroId);
		} else {
			int convertNum = DeityDescendService.getConvertNumByHero(currentHeroId);
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.DEITY_DESCEND, currentHeroId, convertNum);
		}
		ChainLock lock = LockUtils.getLock(deityDescend);
		try {
			lock.lock();
			vo.curIndex = 99;
			deityDescendDao.update(deityDescend);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		return Result.valueOf();
	}
	

	@Override
	public Result getStatus(long actorId) {
		Result result = this.baseConditionCheck();
		if (result.isFail()) {
			return result;
		}
		return Result.valueOf();
	}

	@Override
	public DeityDescendVO getCurDeityDescendVO(long actorId) {
		int currentHeroId = DeityDescendService.getCurrentHeroId();
		DeityDescend deityDescend = deityDescendDao.get(actorId);
		DeityDescendVO vo = deityDescend.getDeityDescendVO(currentHeroId);
		return vo;
	}
	

	@Override
	public void onLogin(long actorId) {
		this.checkDeityDescendOpen(actorId);
	}
	
	private void checkDeityDescendOpen(long actorId) {
		int openTime = DeityDescendService.getStartDate();
		int endTime = DeityDescendService.getEndDate();
		byte status = 0;
		if (DateUtils.isActiveTime(openTime, endTime) == true) {
			status = 1;
		} else {
			DeityDescend deityDescend = deityDescendDao.get(actorId);
			ChainLock lock = LockUtils.getLock(deityDescend);
			try {
				lock.lock();
				deityDescend.totalHit = 0;
				deityDescend.hitHistory = "";
				deityDescendDao.update(deityDescend);
			} catch (Exception e) {
				LOGGER.error("{}", e);
			} finally {
				lock.unlock();
			}
		}
		DeityDescendPushHelper.pushDeityDescendStatus(actorId, status);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				byte status = 0;
				int openTime = DeityDescendService.getStartDate();
				int endTime = DeityDescendService.getEndDate();
				if (isOpen == false) { // 未开放时才可开放
					if (DateUtils.isActiveTime(openTime, endTime) == true) {
						isOpen = true;
						status = 1;
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							DeityDescendPushHelper.pushDeityDescendStatus(actorId, status);
						}
					}
				}

				if (isOpen == true) {// 开放时才可关闭
					if (DateUtils.isActiveTime(openTime, endTime) == false) {
						isOpen = false;
						status = 0;
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							DeityDescendPushHelper.pushDeityDescendStatus(actorId, status);
						}
					}
				}
			}
		}, 2);
	}
	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId,List<RewardObject> reward){
		for(RewardObject rewardObject:reward){
			sendReward(actorId,rewardObject);
		}
	}
	
	/**
	 * 发放奖励
	 */
	private void sendReward(long actorId, RewardObject reward) {
		switch (reward.rewardType) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.DEITY_DESCEND, reward.id,reward.num);
			break;
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.DEITY_DESCEND, reward.id);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.DEITY_DESCEND, reward.id,reward.num);
			break;
		default:
			break;
		}
	}
}
