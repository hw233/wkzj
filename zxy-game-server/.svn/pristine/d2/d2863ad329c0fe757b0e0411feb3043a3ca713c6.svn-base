package com.jtang.gameserver.module.adventures.shop.vipshop.facade.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.VipShopConfig;
import com.jtang.gameserver.dataconfig.service.VipShopService;
import com.jtang.gameserver.dbproxy.entity.VipShop;
import com.jtang.gameserver.module.adventures.shop.vipshop.dao.VipShopDao;
import com.jtang.gameserver.module.adventures.shop.vipshop.facade.VipShopFacade;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.response.VipShopResponse;
import com.jtang.gameserver.module.adventures.shop.vipshop.helper.VipShopPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class VipShopFacadeImpl implements VipShopFacade,ZeroListener,ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(VipShopFacadeImpl.class);
	
	@Autowired
	VipShopDao vipShopDao;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Override
	public TResult<VipShopResponse> getInfo(long actorId) {
		VipShop vipShop = vipShopDao.get(actorId);
		VipShopResponse response = new VipShopResponse(vipShop.dayMap,vipShop.allMap);
		return TResult.sucess(response);
	}

	@Override
	public Result buy(long actorId, int id, int num) {
		VipShopConfig config = VipShopService.getConfig(id);
		if(config == null || num <= 0){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		VipShop vipShop = vipShopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vipShop);
		try{
			lock.lock();
			int vipLevel = vipFacade.getVipLevel(actorId);
			if(vipLevel < config.vipLevel){
				return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
			}
			if(vipShop.getDayNum(id) + num > config.dayMaxNum){
				return Result.valueOf(GameStatusCodeConstant.SHOP_BUY_MAX);
			}
			if(config.maxNum != -1){//有限制购买
				if(vipShop.getAllNum(id) + num > config.maxNum){
					return Result.valueOf(GameStatusCodeConstant.SHOP_BUY_MAX);
				}
			}
			int costTicket = config.costTicket * num;
			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.VIP_SHOP, costTicket, id, num);
			if(isOk == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
			int dayNum = vipShop.getDayNum(id);
			int allNum = vipShop.getAllNum(id);
			vipShop.dayMap.put(id, dayNum + num);
			vipShop.allMap.put(id,allNum + num);
			vipShop.buyTime = TimeUtils.getNow();
			vipShopDao.update(vipShop);
			sendReward(actorId,config.rewardType,config.rewardId,config.rewardNum * num);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public void onZero() {
		for(long actorId : playerSession.onlineActorList()){
			VipShop vipShop = vipShopDao.get(actorId);
			ChainLock lock = LockUtils.getLock(vipShop);
			try{
				lock.lock();
				vipShop.dayMap.clear();
				vipShop.buyTime = 0;
				vipShopDao.update(vipShop);
				VipShopPushHelper.pushReset(actorId);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onLogin(long actorId) {
		VipShop vipShop = vipShopDao.get(actorId);
		if(DateUtils.isToday(vipShop.buyTime) == false){
			ChainLock lock = LockUtils.getLock(vipShop);
			try{
				lock.lock();
				vipShop.dayMap.clear();
				vipShop.buyTime = 0;
				vipShopDao.update(vipShop);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	private void sendReward(long actorId,int rewardType, int rewardId, int rewardNum) {
		RewardType type = RewardType.getType(rewardType);
		switch(type){
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_SHOP, rewardId,rewardNum);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.VIP_SHOP, rewardId, rewardNum);
			break;
		default:
			break;
		
		}
		
	}

}
