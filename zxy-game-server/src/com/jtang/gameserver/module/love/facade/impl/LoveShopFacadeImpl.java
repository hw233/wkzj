package com.jtang.gameserver.module.love.facade.impl;

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
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.LoveShopGlobalConfig;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.dbproxy.entity.LoveShop;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.love.dao.LoveDao;
import com.jtang.gameserver.module.love.dao.LoveShopDao;
import com.jtang.gameserver.module.love.facade.LoveShopFacade;
import com.jtang.gameserver.module.love.handler.response.LoveShopInfoResponse;
import com.jtang.gameserver.module.love.helper.LovePushHelper;
import com.jtang.gameserver.module.love.model.LoveShopVO;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LoveShopFacadeImpl implements LoveShopFacade,ActorLoginListener,ZeroListener,ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoveShopFacadeImpl.class);
	
	@Autowired
	private LoveShopDao loveShopDao;
	@Autowired
	private LoveDao loveDao;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private EquipFacade equipFacade;

	@Override
	public TResult<LoveShopInfoResponse> getInfo(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_SHOP);
		}
		LoveShop loveShop = loveShopDao.get(actorId);
		LoveShopGlobalConfig globalConfig = LoveService.getShopGlobalConfig();
		int goodsNum = goodsFacade.getCount(actorId, globalConfig.costGoods);
		LoveShopInfoResponse response = new LoveShopInfoResponse(goodsNum, loveShop.rewardMap, loveShop.flushNum);
		return TResult.sucess(response);
	}

	@Override
	public Result shopBuy(long actorId, int shopId,int num) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_SHOP);
		}
		LoveShop loveShop = loveShopDao.get(actorId);
		LoveShopVO loveShopVO = loveShop.rewardMap.get(shopId);
		if(loveShopVO == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if(loveShopVO.num == 0){
			return Result.valueOf(GameStatusCodeConstant.LOVE_SHOP_NOT_ENOUGH);
		}
		LoveShopGlobalConfig globalConfig = LoveService.getShopGlobalConfig();
		
		if(loveShopVO.costTicket > 0){//用点券买
			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE_SHOP, loveShopVO.costTicket, 0, 0);
			if(isOk == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
		}else{//扣除物品购买
			int goodsNum = goodsFacade.getCount(actorId, globalConfig.costGoods);
			if(goodsNum < loveShopVO.costGoods){
				return Result.valueOf(GameStatusCodeConstant.LOVE_SHOP_GOODS_NOT_ENOUGH);
			}
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.LOVE_SHOP, globalConfig.costGoods, loveShopVO.costGoods);
		}
		RewardObject reward = new RewardObject();
		reward.rewardType = RewardType.getType(loveShopVO.type);
		reward.id = loveShopVO.goodsId;
		reward.num = loveShopVO.num;
		sendReward(actorId, reward);
		loveShopVO.num = 0;
		loveShopDao.update(loveShop);
		return Result.valueOf();
	}

	@Override
	public TResult<LoveShopInfoResponse> shopFlush(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return TResult.valueOf(GameStatusCodeConstant.NOT_SHOP);
		}
		LoveShop loveShop = loveShopDao.get(actorId);
		LoveShopGlobalConfig globalConfig = LoveService.getShopGlobalConfig();
		if(globalConfig.flushMax <= loveShop.flushNum){
			return TResult.valueOf(GameStatusCodeConstant.FLUSH_SHOP_MAX);
		}
		int flushNum = loveShop.flushNum + 1;
		int costTicket = LoveService.getFlushCostTicket(flushNum);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE_SHOP_FLUSH, costTicket, 0, 0);
		if(isOk == false){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		loveShop.rewardMap = LoveService.initShop();
		loveShop.flushNum = flushNum;
		loveShop.ticketFlushTime = TimeUtils.getNow();
		loveShopDao.update(loveShop);
		return getInfo(actorId);
	}

	@Override
	public TResult<LoveShopInfoResponse> buyPermanent(long actorId) {
		return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
	}

	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for(Long actorId:actors){
			Love love = loveDao.get(actorId);
			if(love.married() == false){
				continue;
			}
			LoveShop loveShop = loveShopDao.get(actorId);
			loveShop.flushNum = 0;
			loveShop.ticketFlushTime = TimeUtils.getNow();
			loveShopDao.update(loveShop);
			LovePushHelper.pushShopInfo(actorId, getInfo(actorId).item);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable(){
			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				int now = TimeUtils.getNow();
				for(Long actorId : actors){
					Love love = loveDao.get(actorId);
					if(love.married() == false){
						continue;
					}
					LoveShop loveShop = loveShopDao.get(actorId);
					if(LoveService.isFlush(loveShop.flushTime, now)){
						loveShop.rewardMap = LoveService.initShop();
						loveShop.flushTime = now;
						loveShopDao.update(loveShop);
						LovePushHelper.pushShopInfo(actorId, getInfo(actorId).item);
					}
				}
			}
		},1);
	}

	@Override
	public void onLogin(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return;
		}
		LoveShop loveShop = loveShopDao.get(actorId);
		int now = TimeUtils.getNow();
		if(LoveService.isFlush(loveShop.flushTime, now)){//长期不在线,登陆判断是否需要刷新列表
			loveShop.flushTime = now;
			loveShop.rewardMap = LoveService.initShop();
			loveShopDao.update(loveShop);
		}
		if(DateUtils.isToday(loveShop.ticketFlushTime) == false){
			loveShop.ticketFlushTime = 0;
			loveShop.flushNum = 0;
			loveShopDao.update(loveShop);
		}
	}
	
	private void sendReward(long actorId, RewardObject rewardObject) {
		switch (rewardObject.rewardType) {
		case EQUIP: {
			equipFacade.addEquip(actorId, EquipAddType.LOVE_SHOP, rewardObject.id);
			break;
		}
		case GOLD: {
			actorFacade.addGold(actorId, GoldAddType.LOVE_SHOP, rewardObject.num);
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.LOVE_SHOP, rewardObject.id, rewardObject.num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.LOVE_SHOP, rewardObject.id, rewardObject.num);
			break;
		}
		case TICKET:{
			vipFacade.addTicket(actorId, TicketAddType.LOVE_SHOP, rewardObject.num);
		}
		default:
		}
	}

	@Override
	public Result unMarry(long actorId) {
		Love love = loveDao.get(actorId);
		if(love.married() == false){
			return Result.valueOf(GameStatusCodeConstant.NOT_MARRY);
		}
		LoveShop loveShop = loveShopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(loveShop);
		try{
			lock.lock();
			loveShopDao.remove(loveShop);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

}
