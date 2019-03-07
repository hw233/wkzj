package com.jtang.gameserver.module.adventures.shop.shop.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EXCHANGE_FAIL_COND_NOT_MEET;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_BUY_MAX;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.SHOP_NOT_EXIST;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.BlackShopConfig;
import com.jtang.gameserver.dataconfig.model.BlackShopRewardConfig;
import com.jtang.gameserver.dataconfig.service.BlackShopService;
import com.jtang.gameserver.dbproxy.entity.Shop;
import com.jtang.gameserver.module.adventures.shop.shop.dao.ShopDao;
import com.jtang.gameserver.module.adventures.shop.shop.facade.BlackShopFacade;
import com.jtang.gameserver.module.adventures.shop.shop.handler.response.ExchangeListResponse;
import com.jtang.gameserver.module.adventures.shop.shop.helper.ShopPushHelp;
import com.jtang.gameserver.module.adventures.shop.shop.model.ExchangeVO;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.snatch.type.SnatchExchangeType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class BlackShopFacadeImpl implements BlackShopFacade,ActorLoginListener,ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlackShopFacadeImpl.class);
	
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private Schedule schedule;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private ActorFacade actorFacade;
	
	
	@Override
	public Result exchange(long actorId, int cfgId) {
		if(isOpen(actorId) == false){
			return Result.valueOf(SHOP_LEVEL_NOT_ENOUGH);
		}
		Shop shop = shopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(shop);
		try{
			lock.lock();
			ExchangeVO exchangeVO = shop.rewardMap.get(cfgId);
			if(exchangeVO == null){
				return Result.valueOf(SHOP_NOT_EXIST);
			}
			if(exchangeVO.isExchange()){
				return Result.valueOf(SHOP_BUY_MAX);
			}
			boolean result = vipFacade.decreaseTicket(actorId, TicketDecreaseType.SHOP_BUY, exchangeVO.costTicket, 0, 0);
			if(result == false){
				return Result.valueOf(TICKET_NOT_ENOUGH);
			}
			result = actorFacade.decreaseGold(actorId, GoldDecreaseType.SHOP_BUY, exchangeVO.costGold);
			if(result == false && exchangeVO.costGold > 0){
				return Result.valueOf(SHOP_GOLD_NOT_ENOUGH);
			}
			exchangeVO.setExchange();
			shopDao.updateShop(shop);
			sendReward(actorId, exchangeVO.id, exchangeVO.num, exchangeVO.type);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public TResult<ExchangeListResponse> getExchangeList(long actorId) {
		Shop shop = shopDao.get(actorId);
		BlackShopConfig globalConfig = BlackShopService.getGlobalConfig();
		int now = TimeUtils.getNow();
		boolean isFlush = BlackShopService.isFlush(shop.flushTime, now);
		if(shop.rewardMap == null || shop.rewardMap.isEmpty() || isFlush){//没有兑换列表,初始化一个兑换列表
			ChainLock lock = LockUtils.getLock(shop);
			try{
				lock.lock();
				shop.rewardMap = new HashMap<>();
				Map<Integer,BlackShopRewardConfig> map = BlackShopService.getReward();
				for(BlackShopRewardConfig config:map.values()){
					ExchangeVO exchangeVO = ExchangeVO.valueOf(config);
					shop.rewardMap.put(exchangeVO.exchangeId, exchangeVO);
				}
				shop.flushTime = now;
				shopDao.updateShop(shop);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
		int needTicket = globalConfig.getNeedTicket(shop.ticketFlush);
		int goodsCount = goodsFacade.getCount(actorId, globalConfig.goodsId);
		ExchangeListResponse response  = new ExchangeListResponse(shop.rewardMap.values(),goodsCount,needTicket);
		return TResult.sucess(response);
	}

	@Override
	public TResult<ExchangeListResponse> flushExchange(long actorId) {
		if(isOpen(actorId) == false){
			return TResult.valueOf(SHOP_LEVEL_NOT_ENOUGH);
		}
		BlackShopConfig globalConfig = BlackShopService.getGlobalConfig();
		int goodsCount = goodsFacade.getCount(actorId, globalConfig.goodsId);
		int ticket = vipFacade.getTicket(actorId);
		Shop shop = shopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(shop);
		try{
			lock.lock();
			if(goodsCount >= globalConfig.goodsNum && globalConfig.goodsNum > 0){//物品数量足够
				goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.BLACK_SHOP_FLUSH, globalConfig.goodsId, globalConfig.goodsNum);
			}else if(ticket >= globalConfig.getNeedTicket(shop.ticketFlush)){//点券数量足够
				vipFacade.decreaseTicket(actorId, TicketDecreaseType.BLACK_SHOP_FLUSH, globalConfig.getNeedTicket(shop.ticketFlush), 0, 0);
				shop.ticketFlush ++;
				shop.resetTime = TimeUtils.getNow();
			}else{//两个都不够
				return TResult.valueOf(EXCHANGE_FAIL_COND_NOT_MEET);
			}
			shop.rewardMap = new HashMap<>();
			Map<Integer,BlackShopRewardConfig> map = BlackShopService.getReward();
			for(BlackShopRewardConfig config:map.values()){
				ExchangeVO exchangeVO = ExchangeVO.valueOf(config);
				shop.rewardMap.put(exchangeVO.exchangeId, exchangeVO);
			}
			shopDao.updateShop(shop);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		goodsCount = goodsFacade.getCount(actorId, globalConfig.goodsId);
		int needTicket = globalConfig.getNeedTicket(shop.ticketFlush);
		ExchangeListResponse response = new ExchangeListResponse(shop.rewardMap.values(),goodsCount,needTicket);
		return TResult.sucess(response);
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable(){

			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				int now = TimeUtils.getNow();
				for(Long actorId:actors){
					if(isOpen(actorId) == false){
						continue;
					}
					BlackShopConfig globalConfig = BlackShopService.getGlobalConfig();
					Shop shop = shopDao.get(actorId);
					if(BlackShopService.isFlush(shop.flushTime, now)){
						ChainLock lock = LockUtils.getLock(shop);
						try{
							lock.lock();
							shop.rewardMap = new HashMap<>();
							Map<Integer,BlackShopRewardConfig> map = BlackShopService.getReward();
							for(BlackShopRewardConfig config:map.values()){
								ExchangeVO exchangeVO = ExchangeVO.valueOf(config);
								shop.rewardMap.put(exchangeVO.exchangeId, exchangeVO);
							}
							shop.flushTime = now;
							shop.ticketFlush = 0;
							shopDao.updateShop(shop);
						}catch(Exception e){
							LOGGER.error("{}",e);
						}finally{
							lock.unlock();
						}
						int needTicket = globalConfig.getNeedTicket(shop.ticketFlush);
						int goodsCount = goodsFacade.getCount(actorId, globalConfig.goodsId);
						ExchangeListResponse response  = new ExchangeListResponse(shop.rewardMap.values(),goodsCount,needTicket);
						ShopPushHelp.pushExchangeResponse(actorId,response);
					}
				}
			}
			
		}, 1);
	}

	@Override
	public void onLogin(long actorId) {
		if(isOpen(actorId) == false){
			return;
		}
		Shop shop = shopDao.get(actorId);
		ChainLock lock = LockUtils.getLock(shop);
		try{
			lock.lock();
			int now = TimeUtils.getNow();
			if(BlackShopService.isFlush(shop.flushTime, now)){
				shop.flushTime = now;
				shop.rewardMap = new HashMap<>();
				Map<Integer,BlackShopRewardConfig> map = BlackShopService.getReward();
				for(BlackShopRewardConfig config:map.values()){
					ExchangeVO exchangeVO = ExchangeVO.valueOf(config);
					shop.rewardMap.put(exchangeVO.exchangeId, exchangeVO);
				}
				shopDao.updateShop(shop);
			}
			if(DateUtils.isToday(shop.resetTime) == false){
				shop.ticketFlush = 0;
				shop.resetTime = now;
				shopDao.updateShop(shop);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}
	
	
	/**
	 * 发放奖励
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, int type) {
		SnatchExchangeType snatchExchange = SnatchExchangeType.getType(type);
		switch (snatchExchange) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.SHOP_BUY, id);
			}
			break;
		}
		case HERO_SOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.SHOP_BUY, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SHOP_BUY, id, num);
			break;
		}
		case HERO:{
			if(heroFacade.isHeroExits(actorId, id)){
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.SHOP_BUY, id, num);
			}else{
				heroFacade.addHero(actorId, HeroAddType.SHOP_BUY, id);
			}
		}
		case PIECE:{
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SHOP_BUY, id,num);
		}
		default:
			LOGGER.error(String.format("类型错误，type:[%s]", snatchExchange.getType()));
			break;
		}
	}
	
	private boolean isOpen(long actorId){
		BlackShopConfig globalConfig = BlackShopService.getGlobalConfig();
		int actorLevel = ActorHelper.getActorLevel(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		return globalConfig.level <= actorLevel && globalConfig.vipLevel <= vipLevel;
	}

}
