package com.jtang.gameserver.module.adventures.shop.trader.effect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.BableSuccessEvent;
import com.jtang.gameserver.component.event.EquipEnhancedEvent;
import com.jtang.gameserver.component.event.EquipRefinedEvent;
import com.jtang.gameserver.component.event.HeroDelveEvent;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.event.StoryPassedEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.model.TraderConditionConfig;
import com.jtang.gameserver.dataconfig.model.TraderDiscountConfig;
import com.jtang.gameserver.dataconfig.model.TraderGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TraderRewardPoolConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.dataconfig.service.TraderService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Trader;
import com.jtang.gameserver.module.adventures.shop.trader.dao.TraderDao;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.TraderShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.helper.TraderPushHelper;
import com.jtang.gameserver.module.adventures.shop.trader.model.ItemVO;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;
import com.jtang.gameserver.module.adventures.shop.trader.type.TraderShopType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.model.LineupHero;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class TraderShopParser extends ShopParser {
	
	public static final int TRADER_MAX_FLUSH = 999;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TraderShopParser.class);
	
	@Autowired
	TraderDao traderDao;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private LineupFacade linequpFacade;
	
	@Override
	public ShopType getEffect() {
		return ShopType.TYPE1;
	}

	@Override
	public void onApplicationEvent() {
		schedule.addEverySecond(new Runnable(){
			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				int now = TimeUtils.getNow();
				for(Long actorId:actors){
					TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
					int level = ActorHelper.getActorLevel(actorId);
					int vipLevel = vipFacade.getVipLevel(actorId);
					if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
						continue;
					}
					Trader trader = traderDao.get(actorId);
					if(TraderService.isFlush(trader.flushTime, now)){//定点刷新列表
						ChainLock lock = LockUtils.getLock(trader);
						try{
							lock.lock();
							trader.itemMap.clear();
							resetItemMap(level, vipLevel, trader,false);
							trader.flushTime = TimeUtils.getNow();
							traderDao.update(trader);
						}catch(Exception e){
							LOGGER.error("{}",e);
						}finally{
							lock.unlock();
						}
						int costTicket = TraderService.getFlushCostTicket(trader.flushNum + 1);
						int goodsNum = goodsFacade.getCount(actorId, globalConfig.flushGoods);
						ShopInfoResponse response = new TraderShopInfoResponse(getEffect(),trader,costTicket,goodsNum);
						TraderPushHelper.pushTraderInfo(actorId, response);
					}
				}
			}
		}, 1);
	}
	
	@Override
	public void onZero() {
		Set<Long> actors = playerSession.onlineActorList();
		for(Long actorId:actors){
			TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
			int level = ActorHelper.getActorLevel(actorId);
			int vipLevel = vipFacade.getVipLevel(actorId);
			if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
				continue;
			}
			Trader trader = traderDao.get(actorId);
			ChainLock lock = LockUtils.getLock(trader);
			try{
				lock.lock();
				if(trader.conditionMap.isEmpty()){//已经永久的
					trader.ticketFlushTime = 0;
					trader.flushNum = 0;
					trader.freeNum = globalConfig.flushNum;
					trader.goodsViewMap.clear();
					trader.goodsNum = 0;
					trader.goodsTime = TimeUtils.getNow();
					trader.equipNum = 0;
					trader.equipTime = TimeUtils.getNow();
					traderDao.update(trader);
					TraderPushHelper.pushTraderInfo(actorId, getInfo(actorId).item);
				}
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onGameEvent(GameEvent paramEvent) {
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(paramEvent.actorId);
		int vipLevel = vipFacade.getVipLevel(paramEvent.actorId);
		if(level >= globalConfig.level && vipLevel >= globalConfig.vipLevel){
			Trader trader = traderDao.get(paramEvent.actorId);
			if(trader.conditionMap.isEmpty()){//已经是永久使用了
				return;
			}
			ChainLock lock = LockUtils.getLock(trader);
			try{
				lock.lock();
				for(Entry<Integer,Integer> entry:trader.conditionMap.entrySet()){//根据事件类型计算完成条件
					if(paramEvent instanceof StoryPassedEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							StoryPassedEvent event = paramEvent.convert();
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + event.times);
						}
					}else if(paramEvent instanceof EquipRefinedEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							EquipRefinedEvent event = paramEvent.convert();
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + event.refineNum);
						}
					}else if(paramEvent instanceof EquipEnhancedEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							EquipEnhancedEvent event = paramEvent.convert();
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + event.upgradeNum);
						}
					}else if(paramEvent instanceof HeroDelveEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + 1);
						}
					}else if(paramEvent instanceof ActorLevelUpEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + 1);
						}
					}else if(paramEvent instanceof BableSuccessEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							BableSuccessEvent event = paramEvent.convert();
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + event.currentFloorNum);
						}
					}else if(paramEvent instanceof RechargeTicketsEvent){
						if(trader.conditionMap.containsKey(entry.getKey())){
							RechargeTicketsEvent event = paramEvent.convert();
							int num = trader.conditionMap.get(entry.getKey());
							trader.conditionMap.put(entry.getKey(), num + event.money);
						}
					}
				}
				for(Entry<Integer,Integer> entry:trader.conditionMap.entrySet()){
					TraderConditionConfig config = TraderService.getConditionConfig(entry.getKey());
					if(entry.getValue() >= config.context){//推送给客户端,条件达成可触发云游商人
						if(RandomUtils.is1000Hit(config.rate) == false){//没有命中
							return;
						}
						trader.freeNum = globalConfig.flushNum;
						trader.freeTime = TimeUtils.getNow();
						trader.flushTime = TimeUtils.getNow();
						trader.conditionMap.clear();
						int goodsNum = goodsFacade.getCount(paramEvent.actorId, globalConfig.flushGoods);
						ShopInfoResponse response = new TraderShopInfoResponse(getEffect(),trader,0,goodsNum);
						TraderPushHelper.pushTraderOpen(paramEvent.actorId,response);
						traderDao.update(trader);
						return;
					}
				}
				traderDao.update(trader);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

	@Override
	public void onActorLogin(long actorId) {
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
			return;
		}
		Trader trader = traderDao.get(actorId);
		ChainLock lock = LockUtils.getLock(trader);
		try{
			lock.lock();
			int now = TimeUtils.getNow();
			if(DateUtils.isToday(trader.ticketFlushTime) == false){
				trader.ticketFlushTime = now;
				trader.flushNum = 0;
			}
			if(DateUtils.isToday(trader.freeTime) == false){
				trader.freeNum = globalConfig.flushNum;
			}
			if(trader.conditionMap.isEmpty() == false && TraderService.isFlush(trader.flushTime, now)){//长期不在线,登陆判断是否需要刷新列表
				trader.flushTime = now;
				trader.itemMap.clear();
				resetItemMap(level, vipLevel, trader,false);
			}
			if(DateUtils.isToday(trader.goodsTime) == false){
				trader.goodsNum = 0;
				trader.goodsTime = TimeUtils.getNow();
			}
			if(DateUtils.isToday(trader.equipTime) == false){
				trader.equipNum = 0;
				trader.equipTime = TimeUtils.getNow();
			}
			traderDao.update(trader);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public TResult<ShopInfoResponse> getInfo(long actorId) {
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
			return TResult.valueOf(GameStatusCodeConstant.TRADER_NOT_OPEN);
		}
		Trader trader = traderDao.get(actorId);
		if(trader.conditionMap.isEmpty() == false){
			return TResult.valueOf(GameStatusCodeConstant.TRADER_CLOSE);
		}else{//已经激活的
			int goodsNum = goodsFacade.getCount(actorId, globalConfig.flushGoods);
			int costTicket = TraderService.getFlushCostTicket(trader.flushNum + 1);
			ShopInfoResponse response = new TraderShopInfoResponse(getEffect(),trader,costTicket,goodsNum);
			return TResult.sucess(response);
		}
	}

	@Override
	public Result shopBuy(long actorId,int shopId, int num) {
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
			return Result.valueOf(GameStatusCodeConstant.TRADER_NOT_OPEN);
		}
		Trader trader = traderDao.get(actorId);
		if(trader.conditionMap.isEmpty() == false){
			return Result.valueOf(GameStatusCodeConstant.TRADER_CLOSE);
		}
		ChainLock lock = LockUtils.getLock(trader);
		try{
			lock.lock();
			ItemVO itemVO = trader.itemMap.get(shopId);
			if(itemVO == null){
				return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			if(itemVO.itemNum < num){
				return Result.valueOf(GameStatusCodeConstant.TRADER_ITEM_NOT_ENOUGH);
			}
			if(itemVO.costGold > 0){//扣金币
				boolean isOk = actorFacade.decreaseGold(actorId, GoldDecreaseType.TRADER, itemVO.costGold);
				if(isOk == false){
					return Result.valueOf(GameStatusCodeConstant.TRADER_GOLD_NOT_ENOUGH);
				}
			}else{//扣点券
				boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.TRADER, itemVO.costTicket, 0, 0);
				if(isOk == false){
					return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
				}
			}
			sendReward(actorId,itemVO.itemType,itemVO.itemId,itemVO.itemNum);
			
			//记录oss
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.traderShopBuy(actor.uid, actor.platformType, actor.channelId,Game.getServerId(), actorId, itemVO.id,itemVO.itemId,itemVO.itemNum, itemVO.discount, itemVO.costTicket, itemVO.costGold);
			
			itemVO.itemNum = 0;
			traderDao.update(trader);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public TResult<ShopInfoResponse> shopFlush(long actorId) {
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		int level = ActorHelper.getActorLevel(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
			return TResult.valueOf(GameStatusCodeConstant.TRADER_NOT_OPEN);
		}
		Trader trader = traderDao.get(actorId);
		if(trader.conditionMap.isEmpty() == false){
			return TResult.valueOf(GameStatusCodeConstant.TRADER_FLUSH_NOT_USE);
		}
		if(trader.flushNum >= TRADER_MAX_FLUSH){
			return TResult.valueOf(GameStatusCodeConstant.TRADER_NOT_FLUSH);
		}
		ChainLock lock = LockUtils.getLock(trader);
		try{
			lock.lock();
			if(trader.freeNum <= 0){
				if(goodsFacade.getCount(actorId, globalConfig.flushGoods) >= 1){
					goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.TRADER_FLUSH, globalConfig.flushGoods, 1);
					trader.itemMap.clear();
					resetItemMap(level, vipLevel, trader,false);
				}else{
					int ticket = vipFacade.getTicket(actorId);
					int costTicket = TraderService.getFlushCostTicket(trader.flushNum + 1);
					if(ticket < costTicket && trader.freeNum <= 0){
						return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
					}
					trader.flushNum ++;
					trader.ticketFlushTime = TimeUtils.getNow();
					vipFacade.decreaseTicket(actorId, TicketDecreaseType.TRADER_FLUSH, costTicket, 0, 0);
					
					trader.itemMap.clear();
					resetItemMap(level, vipLevel, trader,true);
					
					List<Integer> leastList = TraderService.getLeast(trader.leastMap);//这次出现保底了,将出现保底的id次数置为0
					if(leastList.isEmpty() == false){//初始化保底
						for(Integer id : leastList){
							trader.leastMap.put(id, 0);
						}
					}
					
					Map<Integer,Integer> map = new HashMap<>(trader.leastMap);//将非保底的次数加1
					for(Entry<Integer,Integer> entry:trader.leastMap.entrySet()){
						if(leastList.contains(entry.getKey()) == false){
							map.put(entry.getKey(),entry.getValue() + 1);
						}
					}
					trader.leastMap = map;
					Actor actor = actorFacade.getActor(actorId);
					GameOssLogger.traderShopFlush(actor.uid, actor.platformType, actor.channelId,Game.getServerId(), actorId, costTicket, trader.flushNum);
				}
			}else{
				trader.freeNum --;
				trader.freeTime = TimeUtils.getNow();
				trader.itemMap.clear();
				resetItemMap(level, vipLevel, trader,false);
			}
			traderDao.update(trader);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return getInfo(actorId);
	}

	@Override
	public TResult<ShopInfoResponse> buyPermanent(long actorId) {
//		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
//		int level = ActorHelper.getActorLevel(actorId);
//		int vipLevel = vipFacade.getVipLevel(actorId);
//		if(level < globalConfig.level || vipLevel < globalConfig.vipLevel){
//			return TResult.valueOf(GameStatusCodeConstant.TRADER_NOT_OPEN);
//		}
//		Trader trader = traderDao.get(actorId);
//		if(trader.viewTime < TimeUtils.getNow() && trader.permanent == 0){
//			return TResult.valueOf(GameStatusCodeConstant.TRADER_CLOSE);
//		}
//		if(trader.permanent == 1){
//			return TResult.valueOf(GameStatusCodeConstant.TRADER_NOT_REPEAT);
//		}
//		ChainLock lock = LockUtils.getLock(trader);
//		try{
//			lock.lock();
//			int costTicket = globalConfig.buyTicket;
//			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.TRADER_EVER, costTicket, 0, 0);
//			if(isOk == false){
//				return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
//			}
//			trader.conditionMap.clear();
//			trader.viewTime = 0;
//			trader.permanent = 1;
//			trader.leastMap = TraderService.initLeast();
//			traderDao.update(trader);
//		}catch(Exception e){
//			LOGGER.error("{}",e);
//		}finally{
//			lock.unlock();
//		}
		return getInfo(actorId);
	}
	
	private void resetItemMap(int level, int vipLevel, Trader trader, boolean isLeast) {
		Map<Integer,Integer> leastMap = null;
		if(isLeast == false){
			leastMap = new HashMap<>();
		}else{
			leastMap = trader.leastMap;
		}
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		Map<Integer,Integer> rewardConfig = TraderService.randomReward(level, vipLevel, trader.goodsViewMap,leastMap);//获取本次的奖励列表
		Map<Integer,Integer> rewardMap = new HashMap<>(rewardConfig);
		for (Entry<Integer,Integer> entry : rewardConfig.entrySet()) {
			TraderRewardPoolConfig rewardPoolConfig = TraderService.getPoolRewardConfig(entry.getKey());
			if(trader.goodsNum < globalConfig.goodsViewNum){//指向性碎片
				if(rewardPoolConfig.itemType == 1){
					List<Integer> fragmentList = getAllFragment(trader.actorId);
					if(fragmentList.contains(rewardPoolConfig.itemId) == false){//出的碎片不是符合条件的,替换成已拥有的符合条件的碎片
						for(;;){
							if(fragmentList.isEmpty()){
								break;
							}
							int index = RandomUtils.nextIntIndex(fragmentList.size());//随机从符合条件的列表中取一个
							List<TraderRewardPoolConfig> rewardList = TraderService.getRewardConfigByItemId(fragmentList.get(index),level,vipLevel);
							if(rewardList.isEmpty() == false){
								rewardMap.remove(entry.getKey());
								rewardMap.put(rewardList.get(0).id, entry.getValue());
								trader.goodsNum += 1;
								trader.goodsTime = TimeUtils.getNow();
								break;
							}else{
								fragmentList.remove(index);
							}
						}
					}
				}
				if(rewardPoolConfig.itemType == 3){
					List<Integer> heroSoulList = getAllHeroSoul(trader.actorId);
					if(heroSoulList.contains(rewardPoolConfig.itemId) == false){//出的魂魄不是符合条件的,替换成已拥有的符合条件的魂魄
						for(;;){
							if(heroSoulList.isEmpty()){
								break;
							}
							int index = RandomUtils.nextIntIndex(heroSoulList.size());
							List<TraderRewardPoolConfig> rewardList = TraderService.getRewardConfigByItemId(heroSoulList.get(index),level,vipLevel);
							if(rewardList.isEmpty() == false){
								rewardMap.remove(entry.getKey());
								rewardMap.put(rewardList.get(0).id, entry.getValue());
								trader.goodsNum += 1;
								trader.goodsTime = TimeUtils.getNow();
								break;
							}else{
								heroSoulList.remove(index);
							}
						}
					}
				}
			}
			if(trader.equipNum < globalConfig.equipNum){//指向性装备
				if(rewardPoolConfig.itemType == 4){
					List<Integer> equipList = getAllEquip(trader.actorId);
					if(equipList.contains(rewardPoolConfig.itemId) == false){
						for(;;){
							if(equipList.isEmpty()){
								break;
							}
							int index = RandomUtils.nextIntIndex(equipList.size());
							List<TraderRewardPoolConfig> rewardList = TraderService.getRewardConfigByItemId(equipList.get(index),level,vipLevel);
							if(rewardList.isEmpty() == false){
								rewardMap.remove(entry.getKey());
								rewardMap.put(rewardList.get(0).id, entry.getValue());
								trader.equipNum += 1;
								trader.equipTime = TimeUtils.getNow();
								break;
							}else{
								equipList.remove(index);
							}
						}
							
					}
				}
			}
		}
		
		List<TraderDiscountConfig> discountList = new ArrayList<>(TraderService.randomDiscount());//随机获取折扣列表
		int[] array = RandomUtils.uniqueRandom(discountList.size(), 0, rewardConfig.size() - 1);//随机获取奖励列表哪几个下标会有折扣
		Integer[] integer = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			integer[i] = array[i];
		}
		List<Integer> list = Arrays.asList(integer);
		TraderDiscountConfig discountConfig = null;
		int i = 0;
		for(Entry<Integer,Integer> entry : rewardMap.entrySet()){
			TraderRewardPoolConfig rewardPoolConfig = TraderService.getPoolRewardConfig(entry.getKey());
			if(rewardPoolConfig.costGolds > 0){//该物品用金币购买,不进行打折
				discountConfig = null;
			}else if(globalConfig.mastDiscountList.contains(rewardPoolConfig.id)){//必须打折商品不计入普通打折
				discountConfig = TraderService.getDiscount();
			}else{
				if(TraderService.getDiscountConfig(entry.getValue()) != null){
					discountConfig = TraderService.getDiscountConfig(entry.getValue());
				}else{
					if(list.contains(i) && globalConfig.discountTypeList.contains(rewardPoolConfig.itemType)){//符合配置条件将折扣配置取出
						discountConfig = discountList.get(0);
						discountList.remove(0);
					}else{
						discountConfig = null;
					}
				}
			}
			ItemVO itemVO = parserItemVo(trader, rewardPoolConfig,discountConfig,level);//生成商品,并进行打折
			if(trader.goodsViewMap.containsKey(itemVO.id)){//商品存库
				trader.goodsViewMap.put(itemVO.id,trader.goodsViewMap.get(itemVO.id) + 1);
			}else{
				trader.goodsViewMap.put(itemVO.id, 1);
			}
			i++;
		}
	}
	
	public ItemVO parserItemVo(Trader trader,TraderRewardPoolConfig rewardConfig,TraderDiscountConfig discountConfig,int level) {
		ItemVO itemVO = new ItemVO();
		itemVO.id = rewardConfig.id;
		itemVO.itemId = rewardConfig.itemId;
		itemVO.itemType = rewardConfig.itemType;
		itemVO.itemNum = rewardConfig.getItemNum(level);
		if(discountConfig != null){//有折扣进行打折
			itemVO.discount = discountConfig.discount;
			BigDecimal discount = new BigDecimal(itemVO.discount/1000.0f).setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal costGolds = new BigDecimal(rewardConfig.costGolds).setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal costTicket = new BigDecimal(rewardConfig.costTicket);
			float goldNum = discount.multiply(costGolds).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
			float ticketNum = discount.multiply(costTicket).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
			itemVO.costGold = Double.valueOf(goldNum *  itemVO.itemNum).intValue();
			itemVO.costTicket = Double.valueOf(ticketNum * itemVO.itemNum).intValue();
		}else{
			itemVO.costGold = (int) Math.ceil(rewardConfig.costGolds * itemVO.itemNum);
			itemVO.costTicket = (int) Math.ceil(rewardConfig.costTicket * itemVO.itemNum);
		}
		trader.itemMap.put(itemVO.id, itemVO);
		return itemVO;
	}
	
	private void sendReward(long actorId,int itemType, int itemId, int num) {
		TraderShopType type = TraderShopType.getType(itemType);
		switch (type) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.TRADER, itemId, num);
			break;
		case FRAGMENT:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.TRADER, itemId, num);
			break;
		case TARGET_GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.TRADER, itemId, num);
			break;
		case SOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.TRADER, itemId, num);
			break;
		case EQUIPS:
			equipFacade.addEquip(actorId, EquipAddType.TRADER, itemId);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 获取所有符合条件的装备碎片
	 * @param actorId
	 * @return
	 */
	private List<Integer> getAllFragment(long actorId){
		TraderGlobalConfig config = TraderService.getGlobalConfig();
		List<Integer> fragmentList = EquipService.getAllFragmentByStar(config.goodsStar);
		List<Integer> list = new ArrayList<>();
		for(Integer goodsId : fragmentList){
			GoodsVO goodsVO = goodsFacade.getGoodsVO(actorId, goodsId);
			GoodsConfig goodsConfig = GoodsService.get(goodsId);
			String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
			int dependsNum = Integer.valueOf(depends[1]);
			if(goodsVO != null && dependsNum - goodsVO.num <= config.goodsNum){
				list.add(goodsVO.goodsId);
			}
		}
		return list;
	}
	
	/**
	 * 获取所有符合条件的魂魄
	 */
	private List<Integer> getAllHeroSoul(long actorId){
		TraderGlobalConfig config = TraderService.getGlobalConfig();
		List<Integer> heroSoul = HeroService.getAllHeroByStar(config.goodsStar);
		List<Integer> list = new ArrayList<>();
		for(Integer heroId : heroSoul){
			int heroSoulNum = heroSoulFacade.getSoulNum(actorId, heroId);
			HeroConfig heroConfig = HeroService.get(heroId);
			if(heroSoulNum == 0 && heroConfig.getRecruitSoulCount() - heroSoulNum <= config.goodsNum){
				list.add(heroId);
			}
		}
		return list;
	}
	
	/**
	 * 获取已经上阵的仙人需要激活的特性的装备id
	 */
	private List<Integer> getAllEquip(long actorId){
		List<Integer> list = new ArrayList<>();
		List<LineupHero> lineupHeros = linequpFacade.getLineupHeros(actorId);
		for(LineupHero lineupHero : lineupHeros){
			HeroVO heroVO = heroFacade.getHero(actorId, lineupHero.heroId);
			if(heroVO == null){
				continue;
			}
			HeroConfig heroConfig = HeroService.get(heroVO.heroId);
			List<Integer> skillList = new ArrayList<>(heroConfig.getPassiveSkills());
			for(Integer skillId : heroConfig.getPassiveSkills()){
				for(Integer skill : heroVO.passiveSkillList){
					if(skillId == skill){
						skillList.remove(skillId);
					}
				}
			}
			for(Integer skillId : skillList){
				PassiveSkillConfig config = SkillService.getPassiveSkill(skillId);
				if(config.getTriggerItem() == 1){
					List<Integer> equipIds = StringUtils.delimiterString2IntList(config.getTriggerValue(), Splitable.ELEMENT_SPLIT);
					list.addAll(equipIds);
				}
			}
		}
		return list;
	}

}
