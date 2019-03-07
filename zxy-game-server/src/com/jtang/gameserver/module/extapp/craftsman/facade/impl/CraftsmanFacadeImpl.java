package com.jtang.gameserver.module.extapp.craftsman.facade.impl;

import static com.jtang.core.protocol.StatusCode.SUCCESS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.service.CraftsmanService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dbproxy.entity.Craftsman;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.extapp.craftsman.dao.CraftsmanDao;
import com.jtang.gameserver.module.extapp.craftsman.facade.CraftsmanFacade;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.BuildEquipResponse;
import com.jtang.gameserver.module.extapp.craftsman.helper.CraftsmanPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class CraftsmanFacadeImpl implements CraftsmanFacade,  ActorLoginListener, ZeroListener, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CraftsmanFacadeImpl.class);
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	CraftsmanDao craftsmanDao;
	
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
	
	@Autowired
	Schedule schedule;
	
	/**
	 * 是否开放
	 */
	private boolean isOpen = false;
	
	@Override
	public TResult<BuildEquipResponse> build(long actorId, long uuid, byte buildId, boolean useTicket) {
		if (uuid <= 0) {
			return TResult.valueOf(GameStatusCodeConstant.ERROR_EQUIP_UUID);
		}
//		if (buildId >= 2) {
//			return TResult.valueOf(GameStatusCodeConstant.ERROR_BUILD_EQUIP);
//		}
		EquipVO vo = equipFacade.get(actorId, uuid);
		if (vo == null) {
			return TResult.valueOf(GameStatusCodeConstant.EQUIP_NOT_EXIST);
		}
		EquipConfig config = EquipService.get(vo.equipId);
		if (config == null) {
			return TResult.valueOf(GameStatusCodeConstant.EQUIP_NOT_EXIST);
		}
		int buildEquipId = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.buildEquipsMap.get(buildId + 1);
		EquipConfig buildConfig = EquipService.get(buildEquipId);
		if (config.getStar() > buildConfig.getStar()) {
			return TResult.valueOf(GameStatusCodeConstant.ERROR_BUILD_EQUIP);
		}
		
		Craftsman craftsman = craftsmanDao.get(actorId);
		if (craftsman.buildNum >= CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.buildCount) {
			return TResult.valueOf(GameStatusCodeConstant.BUILD_COUNT_HAD_USED);
		}
		
		int equipStar = config.getStar();
		int probility = CraftsmanService.getProbilityByStar(equipStar, buildId);
		if (useTicket) {
			int ticket = CraftsmanService.getCostByStar(equipStar, buildId);
			if (vipFacade.hasEnoughTicket(actorId, ticket) == false) {
				return TResult.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
			}
			vipFacade.decreaseTicket(actorId, TicketDecreaseType.CRAFTSMAN_BUILD, ticket, 0, 0);
			probility = 1000;
		}
		
		List<Long> uuidList = new ArrayList<Long>();
		uuidList.add(uuid);
		short result = equipFacade.delEquip(actorId, EquipDecreaseType.CRAFTSMAN_BUILD, uuidList);
		if (result == SUCCESS) {
			ChainLock lock = LockUtils.getLock(craftsman);
			try{
				lock.lock();
				craftsman.buildNum += 1;
				craftsman.operationTime = TimeUtils.getNow();
				craftsmanDao.update(craftsman);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
			boolean isSuccess = RandomUtils.is1000Hit(probility);
			List<RewardObject> rewardObjects = null;
			int actorLevel = ActorHelper.getActorLevel(actorId);
			rewardObjects = CraftsmanService.getRewardList(isSuccess, buildId + 1, vo.equipId, equipStar, actorLevel);
			sendReward(actorId, rewardObjects);
			int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount - craftsman.buildNum;
			BuildEquipResponse response = new BuildEquipResponse(isSuccess, buildNum, rewardObjects);
			return TResult.sucess(response);
		} else {
			return TResult.valueOf(GameStatusCodeConstant.EQUIP_NOT_EXIST);
		}
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
				equipFacade.addEquip(actorId, EquipAddType.CRAFTSMAN_BUILD, id);
			}
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.CRAFTSMAN_BUILD, id, num);
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

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEveryMinute(new Runnable() {
			@Override
			public void run() {
				Long openTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.openDateTime.getTime() / 1000;
				Long endTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.closeDateTime.getTime() / 1000;
				boolean inTime = DateUtils.isActiveTime(openTime.intValue(), endTime.intValue());
				int maybe = 0;
				int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount;
				if (isOpen == false) { // 未开放时才可开放
					if (inTime == true) {
						int now = DateUtils.getNowInSecondes();
						maybe = endTime.intValue() - now;
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							Craftsman craftsman = craftsmanDao.get(actorId);
							craftsman.reset();
							craftsmanDao.update(craftsman);
							CraftsmanPushHelper.pushCraftsmanStatus(actorId, maybe, buildNum - craftsman.buildNum);
						}
						isOpen = true;
					}
				}

				if (isOpen == true) {// 开放时才可关闭
					if (inTime == false) {
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							Craftsman craftsman = craftsmanDao.get(actorId);
							CraftsmanPushHelper.pushCraftsmanStatus(actorId, maybe, buildNum - craftsman.buildNum);
						}
						isOpen = false;
					}
				}
			}
		}, 1);
	}

	@Override
	public void onZero() {
		Long openTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.openDateTime.getTime() / 1000;
		Long endTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.closeDateTime.getTime() / 1000;
		//倒计时
		int maybe = 0;
		if (DateUtils.isActiveTime(openTime.intValue(), endTime.intValue())) {
			int now = DateUtils.getNowInSecondes();
			maybe = endTime.intValue() - now;
		}
		Set<Long> actorIds = playerSession.onlineActorList();
		for(long actorId : actorIds){
			Craftsman craftsman = craftsmanDao.get(actorId);
			int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount;
			ChainLock lock = LockUtils.getLock(craftsman);
			try{
				lock.lock();
				craftsman.buildNum = 0;
				craftsmanDao.update(craftsman);
				if (maybe != 0) {
					CraftsmanPushHelper.pushCraftsmanStatus(actorId, maybe, buildNum - craftsman.buildNum);
				}
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}		
	}

	@Override
	public void onLogin(long actorId) {
		this.checkCraftsmanOpen(actorId);
	}
	
	private void checkCraftsmanOpen(long actorId) {
		Long openTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.openDateTime.getTime() / 1000;
		Long endTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.closeDateTime.getTime() / 1000;
		int maybe = 0;
		int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount;
		Craftsman craftsman = craftsmanDao.get(actorId);
		ChainLock lock = LockUtils.getLock(craftsman);
		try {
			lock.lock();
			if (DateUtils.isActiveTime(openTime.intValue(), endTime.intValue()) && DateUtils.isToday(craftsman.operationTime)) {
				int now = DateUtils.getNowInSecondes();
				maybe = endTime.intValue() - now;
				buildNum = buildNum - craftsman.buildNum;
			} else {
				craftsman.reset();
				buildNum = buildNum - craftsman.buildNum;
				craftsmanDao.update(craftsman);
			}
			CraftsmanPushHelper.pushCraftsmanStatus(actorId, maybe, buildNum);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}


	@Override
	public int getBuildNum(long actorId) {
		Craftsman craftsman = craftsmanDao.get(actorId);
		return craftsman.buildNum;
	}
	
}