package com.jtang.gameserver.module.extapp.ernie.facade.impl;

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
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.service.ErnieService;
import com.jtang.gameserver.dataconfig.service.InternalActorService;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.dbproxy.entity.Ernie;
import com.jtang.gameserver.module.app.dao.AppGlobalDao;
import com.jtang.gameserver.module.app.model.extension.global.GlobalInfoVO19;
import com.jtang.gameserver.module.app.model.extension.rulevo.ErnieVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.ernie.dao.ErnieDao;
import com.jtang.gameserver.module.extapp.ernie.facade.ErnieFacade;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieInfoResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieRecordResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;
import com.jtang.gameserver.module.extapp.ernie.helper.ErniePushHelper;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ErnieFacadeImpl implements ErnieFacade, ZeroListener, ActorLoginListener, ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ErnieFacadeImpl.class);
	@Autowired
	ErnieDao ernieDao;
	
	@Autowired
	AppGlobalDao appGlobalDao;
	
	@Autowired
	VipFacade vipFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	MaintainFacade maintainFacade;
	
	@Autowired
	Schedule schedule;

	/**
	 * 是否开放
	 */
	private volatile boolean isOpen = false;
	
	@Override
	public TResult<ErnieStatusResponse> getStatus() {
		Result result = baseConditionCheck();
		int status = 0;
		ErnieStatusResponse response = new ErnieStatusResponse(status);
		if (result.isOk()) {
			response.status = 1;
		}
		return TResult.sucess(response);
	}

	@Override
	public TResult<ErnieRecordResponse> getRecord(long actorId) {
		Result result = baseConditionCheck();
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		int startTime = ErnieService.getStartTime();
		int endTime = ErnieService.getEndTime();
		String desc = ErnieService.ERNIE_GLOBAL_CONFIG.desc;
		List<RewardObject> pool = ErnieService.getAllErnieGoodsList();
		ErnieRecordResponse recordResponse = new ErnieRecordResponse(startTime, endTime, desc, pool);
		return TResult.sucess(recordResponse);
	}

	@Override
	public TResult<ErnieInfoResponse> getInfo(long actorId) {
		Result result = baseConditionCheck();
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		Ernie ernie = ernieDao.get(actorId);
		if (ernie.rewardList.isEmpty()) {
			ernie.rewardList = ErnieService.ERNIE_GLOBAL_CONFIG.showGoodsList;
		}
		int useTimes = ernie.ernieCount;
		int freeTimes = ErnieService.ERNIE_GLOBAL_CONFIG.freeTimes;
		int needTicket = ErnieService.getCostByErnieCount(useTimes);
		ErnieInfoResponse infoResponse = new ErnieInfoResponse(freeTimes, useTimes, needTicket, ernie.rewardList);
		return TResult.sucess(infoResponse);
	}

	@Override
	public TResult<ErnieInfoResponse> runErnie(long actorId) {
		Result result = baseConditionCheck();
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		Ernie ernie = ernieDao.get(actorId);
		int cost = ErnieService.getCostByErnieCount(ernie.ernieCount);
		if (vipFacade.hasEnoughTicket(actorId, cost) == true) {
			if (cost > 0) {
				vipFacade.decreaseTicket(actorId, TicketDecreaseType.ERNIE, cost, 0, 0);
			}
			List<RewardObject> rewardObjects = new ArrayList<RewardObject>();
			
			rewardObjects = this.tryToGetBigReward(actorId, rewardObjects);
			int nowBillNum = getNowBillNum(GoodsRule.GOODS_ID_BILL);
			ernie.setLeastNum(ernie.getLeastNum() + 1);
			List<RewardObject> ernieRewardObjects = ErnieService.getLeastRewardObjectByErnieCount(rewardObjects, ernie, nowBillNum);
			this.saveAppInfoIfNeeded(actorId, ernieRewardObjects);
			this.sendRewardAndNotice(actorId, ernieRewardObjects);
			int useTimes = 0;
			int freeTimes = ErnieService.ERNIE_GLOBAL_CONFIG.freeTimes;
			ChainLock lock = LockUtils.getLock(ernie);
			try{
				lock.lock();
				ernie.ernieCount++;
				ernie.rewardList = ernieRewardObjects;
				useTimes = ernie.ernieCount;
				ernieDao.update(ernie);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
			cost = ErnieService.getCostByErnieCount(useTimes);
			ErnieInfoResponse response = new ErnieInfoResponse(freeTimes, useTimes, cost, ernieRewardObjects);
			int serverId = Game.getServerId();
			for (RewardObject rewardObject : ernieRewardObjects) {
				GameOssLogger.runErnie(serverId, EffectId.EFFECT_ID_19.getId(), actorId, useTimes, rewardObject.rewardType.getCode(), rewardObject.id, rewardObject.num);
			}
			return TResult.sucess(response);
		} else {
			return TResult.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
		}
	}

	private int getNowBillNum(int goodsId) {
		AppGlobal appInfo = appGlobalDao.get(EffectId.EFFECT_ID_19.getId());
		ChainLock lock = LockUtils.getLock(appInfo);
		GlobalInfoVO19 info = null;
		try {
			lock.lock();
			info = appInfo.getGlobalInfoVO();
			if (info != null) {
				List<ErnieVO> billRewardList = info.ernieVOMap.get(goodsId);
				if (billRewardList != null) {
					return billRewardList.size();
				}
			}
		} catch (Exception e) {
			LOGGER.error("{}",e);
		} finally {
			lock.unlock();
		}
		return ErnieService.ERNIE_GLOBAL_CONFIG.maxBillNum;
	}
	
	private List<RewardObject> tryToGetBigReward(long actorId, List<RewardObject> rewardObjects) {
		boolean isInternalActor = InternalActorService.isInternalActor(actorId);
		if (isInternalActor == true) {
			AppGlobal appInfo = appGlobalDao.get(EffectId.EFFECT_ID_19.getId());
			ChainLock lock = LockUtils.getLock(appInfo);
			try {
				lock.lock();
				GlobalInfoVO19 info = appInfo.getGlobalInfoVO();
				if (info != null) {
					List<ErnieVO> bigRewardList = info.ernieVOMap.get(GoodsRule.GOODS_ID_IPHONE6);
					if (bigRewardList == null || bigRewardList.isEmpty()) {
						boolean isYou = InternalActorService.tryGet(actorId);
						if (isYou == true) {
							bigRewardList = new ArrayList<ErnieVO>();
							ErnieVO whoVo = new ErnieVO();
							whoVo.actorId = actorId;
							whoVo.goodsId = GoodsRule.GOODS_ID_IPHONE6;
							bigRewardList.add(whoVo);
							info.ernieVOMap.put(whoVo.goodsId, bigRewardList);
							appGlobalDao.update(appInfo);
							
							RewardObject bigReward = new RewardObject(RewardType.GOODS, whoVo.goodsId, 1);
							rewardObjects.add(bigReward);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("{}",e);
			} finally {
				lock.unlock();
			}
		}
		return rewardObjects;
	}
	
	/**
	 * 检查是否有话费,有需要保存数据
	 * @param actorId
	 * @param ernieRewardObjects
	 */
	private void saveAppInfoIfNeeded(long actorId, List<RewardObject> ernieRewardObjects) {
		boolean needSave = false;
		for (RewardObject rewardObject : ernieRewardObjects) {
			if (rewardObject.id == GoodsRule.GOODS_ID_BILL) {
				needSave = true;
				break;
			}
		}
		if (needSave == true) {
			AppGlobal appInfo = appGlobalDao.get(EffectId.EFFECT_ID_19.getId());
			ChainLock lock = LockUtils.getLock(appInfo);
			try {
				lock.lock();
				GlobalInfoVO19 info = appInfo.getGlobalInfoVO();
				if (info != null) {
					List<ErnieVO> billRewardList = info.ernieVOMap.get(GoodsRule.GOODS_ID_BILL);
					if (billRewardList != null) {
						ErnieVO whoVo = new ErnieVO();
						whoVo.actorId = actorId;
						whoVo.goodsId = GoodsRule.GOODS_ID_BILL;
						billRewardList.add(whoVo);
						info.ernieVOMap.put(whoVo.goodsId, billRewardList);
						appGlobalDao.update(appInfo);
					}
				}
			} catch (Exception e) {
				LOGGER.error("{}",e);
			} finally {
				lock.unlock();
			}
		}
	}
	
	private Result baseConditionCheck() {
		if (ErnieService.isOpen() == false) {	
			return Result.valueOf(GameStatusCodeConstant.ERNIE_NOT_OPEN);
		}
		return Result.valueOf();
	}
	
	/**
	 * 检查是否有话费或者iphone ,有,需要发送通告. 并且发奖
	 * @param actorId
	 * @param ernieRewardObjects
	 */
	private void sendRewardAndNotice(long actorId, List<RewardObject> ernieRewardObjects) {
		for (RewardObject rewardObject : ernieRewardObjects) {
			if(rewardObject.id == GoodsRule.GOODS_ID_BILL) {
				String msg = String.format(ErnieService.ERNIE_GLOBAL_CONFIG.billMsg, ActorHelper.getActorName(actorId));
				maintainFacade.sendNotice(msg, 1, 0, new ArrayList<Integer>());
			}
			if(rewardObject.id == GoodsRule.GOODS_ID_IPHONE6) {
				String msg = String.format(ErnieService.ERNIE_GLOBAL_CONFIG.bigRewardMsg, ActorHelper.getActorName(actorId));
				maintainFacade.sendNotice(msg, 1, 0, new ArrayList<Integer>());
			}
		}
		sendReward(actorId, ernieRewardObjects);
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
				equipFacade.addEquip(actorId, EquipAddType.ERNIE, id);
			}
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.ERNIE, id, num);
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.ERNIE, id, num);
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
	public void onZero() {
		//倒计时
		Set<Long> actorIds = playerSession.onlineActorList();
		for(long actorId : actorIds){
			Ernie ernie = ernieDao.get(actorId);
			ChainLock lock = LockUtils.getLock(ernie);
			try{
				lock.lock();
				ernie.ernieCount = 0;
				ernie.lastErnieTime = TimeUtils.getNow();
				if (ernie.rewardList != null) {
					ernie.rewardList.clear();
				}
				ernieDao.update(ernie);
				int useTimes = ernie.ernieCount;
				int freeTimes = ErnieService.ERNIE_GLOBAL_CONFIG.freeTimes;
				int needTicket = ErnieService.getCostByErnieCount(useTimes);
				ErnieInfoResponse response = new ErnieInfoResponse(freeTimes, ernie.ernieCount, needTicket, ernie.rewardList);
				ErniePushHelper.pushErnieInfo(actorId, response);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onLogin(long actorId) {
		boolean isInTime = ErnieService.isExchangeTime();
		if (isInTime == false) {
			int iphoneNum = goodsFacade.getCount(actorId, GoodsRule.GOODS_ID_IPHONE6);
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.ERNIE, GoodsRule.GOODS_ID_IPHONE6, iphoneNum);
			int billNum = goodsFacade.getCount(actorId, GoodsRule.GOODS_ID_BILL);
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.ERNIE, GoodsRule.GOODS_ID_BILL, billNum);
		}
		Ernie ernie = ernieDao.get(actorId);
		if (DateUtils.isToday(ernie.lastErnieTime) == false) {
			ChainLock lock = LockUtils.getLock(ernie);
			try{
				lock.lock();
				ernie.ernieCount = 0;
				ernie.lastErnieTime = TimeUtils.getNow();
				if (ernie.rewardList != null) {
					ernie.rewardList.clear();
				}
				ernieDao.update(ernie);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEveryMinute(new Runnable() {
			@Override
			public void run() {
				Result result = baseConditionCheck();
				if (isOpen == false) { // 未开放时才可开放
					if (result.isOk()) {
						ErnieStatusResponse response = new ErnieStatusResponse(1);
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							ErniePushHelper.pushErnieStatus(actorId, response);
						}
						isOpen = true;
					}
				}
				
				if (isOpen == true) {// 开放时才可关闭
					if (result.isFail()) {
						ErnieStatusResponse response = new ErnieStatusResponse(2);
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							ErniePushHelper.pushErnieStatus(actorId, response);
						}
						isOpen = false;
					}
				}
			}
		}, 1);
	}
}
