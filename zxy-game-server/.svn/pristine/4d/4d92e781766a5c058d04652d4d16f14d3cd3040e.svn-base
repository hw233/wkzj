package com.jtang.gameserver.module.gift.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACCEPT_GIFT_NUM_REACH_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.DUP_GIVE_GIFT;
import static com.jiatang.common.GameStatusCodeConstant.GIFT_CONFIG_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.GIFT_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.GIFT_NOT_REACH_MAX_COUNT;
import static com.jiatang.common.GameStatusCodeConstant.GIFT_PACKAGE_HAD_ACCEPT;
import static com.jiatang.common.GameStatusCodeConstant.GIVE_GIFT_NUM_REACH_LIMIT;

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
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.MapResult;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.component.event.AllyGiveGiftEvent;
import com.jtang.gameserver.component.event.AllyRecevieGiftEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.AwardGoodsPacketConfig;
import com.jtang.gameserver.dataconfig.model.GiftConfig;
import com.jtang.gameserver.dataconfig.service.GiftService;
import com.jtang.gameserver.dbproxy.entity.Gift;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.facade.impl.AllyFacadeImpl;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.gift.constant.GiftRule;
import com.jtang.gameserver.module.gift.dao.GiftDao;
import com.jtang.gameserver.module.gift.facade.GiftFacade;
import com.jtang.gameserver.module.gift.handler.response.PushGiftStateResponse;
import com.jtang.gameserver.module.gift.helper.PushGiftHelper;
import com.jtang.gameserver.module.gift.model.GiftStateType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 礼物的服务类
 * 
 * @author vinceruan
 * 
 */
@Component
public class GiftFacadeImpl implements GiftFacade, ActorLoginListener, ZeroListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(GiftFacadeImpl.class);

	@Autowired
	GiftDao giftDao;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	AllyFacade allyFacade;
	@Autowired
	NotifyFacade notifyFacade;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	private EventBus eventBus;

	@Override
	public Gift get(long actorId) {
		Gift gift = this.giftDao.get(actorId);
		return gift;
	}

	@Override
	public Result giveGift(long actorId, long allyActorId) {
		// 是否是盟友
		if (!allyFacade.isAlly(actorId, allyActorId)) {
			return Result.valueOf(GameStatusCodeConstant.ALLY_NOT_EXISTS);
		}

		Gift gift = this.giftDao.get(actorId);
		Gift allyGift = this.giftDao.get(allyActorId);

		// 不能重复送礼
		if (gift.getAllysReceivedMyGiftSet().contains(allyActorId)) {
			return Result.valueOf(DUP_GIVE_GIFT);
		}

		// 送礼次数到达上限
		if (gift.getAllysReceivedMyGiftSet().size() >= GiftRule.GIFT_MAX_GIVE_NUM) {
			return Result.valueOf(GIVE_GIFT_NUM_REACH_LIMIT);
		}

		// 查看礼物的配置是否存在
		int level = ActorHelper.getActorLevel(allyActorId);
		GiftConfig config = GiftService.get(level);
		if (config == null) {
			return Result.valueOf(GIFT_CONFIG_NOT_FOUND);
		}

		// 抽取礼物
		List<AwardGoodsPacketConfig> packetList = config.getGiftList();
		int random = RandomUtils.nextInt(0, 1000);
		int rate = 0;
		AwardGoodsPacketConfig packet = null;
		for (AwardGoodsPacketConfig conf : packetList) {
			rate += conf.rate;
			if (rate > random) {
				packet = conf;
				break;
			}
		}

		// 配置错误导致不能随机出礼物
		if (packet == null) {
			return Result.valueOf(GIFT_CONFIG_NOT_FOUND);
		}

		// 添加礼物
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : packet.goodsMap.entrySet()) {
			Integer goodsId = entry.getKey();
			Integer num = entry.getValue();
			map.put(goodsId, num);
		}

		if(AllyFacadeImpl.ROBOT_ID != allyActorId){
			allyGift.receiveGiftFromAlly(actorId, map);// 将我的礼物放到盟友礼物信息里面
			this.giftDao.update(allyGift);
		}
		
		gift.sendGift2Ally(allyActorId);// 记录我已送礼的盟友id
		this.giftDao.update(gift);

//		// 通知盟友
//		notifyFacade.createGiveGift(actorId, allyActorId);

		PushGiftStateResponse giveResponse = new PushGiftStateResponse(GiftStateType.GIVEN, allyActorId);
		PushGiftHelper.pushGiftState(actorId, giveResponse);
		PushGiftStateResponse acceptResponse = new PushGiftStateResponse(GiftStateType.RECEIVED, actorId);
		PushGiftHelper.pushGiftState(allyActorId, acceptResponse);
		eventBus.post(new AllyGiveGiftEvent(actorId, allyActorId));

		return Result.valueOf();
	}

	@Override
	public MapResult<Long, Integer> acceptGift(long actorId, long allyActorId) {
		Gift gift = this.giftDao.get(actorId);

		// 礼物不存在
		Map<Integer, Integer> gifts = gift.getReceivedGiftsMap().get(allyActorId);
		if (gifts == null) {
			return MapResult.StatusCode(GIFT_NOT_EXISTS);
		}

		//收礼已达上限
		if (gift.getAcceptedAllysSet().size() >= GiftRule.ACCEPT_GIFT_CAP) {
			return MapResult.StatusCode(ACCEPT_GIFT_NUM_REACH_LIMIT);
		}

		// 收礼
		MapResult<Long, Integer> result = new MapResult<>();
		for (Entry<Integer, Integer> entry : gifts.entrySet()) {
			Integer num = entry.getValue();
			Integer goodsId = entry.getKey();
			TResult<Long> res = goodsFacade.addGoodsVO(actorId, GoodsAddType.ACCEPT_GIFT, goodsId, num);
			if (res.isOk()) {
				result.put(res.item, num);
			}
		}

		// 更新DB
		gift.acceptAllyGift(allyActorId);
		this.giftDao.update(gift);

//		// 感谢对方
//		notifyFacade.createThankAlly(actorId, allyActorId, ThankAllyType.ACCEPT_GIFT);

		PushGiftStateResponse response = new PushGiftStateResponse(GiftStateType.ACCEPTED, allyActorId);
		PushGiftHelper.pushGiftState(actorId, response);
		eventBus.post(new AllyRecevieGiftEvent(actorId, allyActorId));

		return result;
	}

	@Override
	public MapResult<Long, Integer> openGiftPackage(long actorId) {
		Gift gift = this.giftDao.get(actorId);

		// 礼物领取数未到达要求时不能拿大礼包
		int acceptGiftCount = gift.getAcceptedAllysSet().size();
		if (acceptGiftCount < GiftRule.GIFT_ACCEPT_NUM_4_PACKAGE) {
			return MapResult.StatusCode(GIFT_NOT_REACH_MAX_COUNT);
		}

		// 是否重新领取
		if (gift.acceptGiftPackage) {
			return MapResult.StatusCode(GIFT_PACKAGE_HAD_ACCEPT);
		}

		// 查看礼包的配置是否存在
		int level = actorFacade.getActor(actorId).level;
		GiftConfig config = GiftService.get(level);
		if (config == null) {
			return MapResult.StatusCode(GIFT_CONFIG_NOT_FOUND);
		}

		// 抽取大礼包
		List<AwardGoodsPacketConfig> packetList = config.getGiftPackageList();
		int random = RandomUtils.nextInt(0, 1000);
		int rate = 0;
		AwardGoodsPacketConfig packet = null;
		for (AwardGoodsPacketConfig conf : packetList) {
			rate += conf.rate;
			if (rate > random) {
				packet = conf;
				break;
			}
		}

		// 配置错误导致不能随机出礼包
		if (packet == null) {
			return MapResult.StatusCode(GIFT_CONFIG_NOT_FOUND);
		}

		// 添加礼物
		MapResult<Long, Integer> result = new MapResult<>();
		for (Entry<Integer, Integer> entry : packet.goodsMap.entrySet()) {
			Integer num = entry.getValue();
			Integer goodsId = entry.getKey();
			TResult<Long> res = goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_GIFT, goodsId, num);
			if (res.isOk()) {
				result.put(res.item, num);
			}
		}

		// 更新DB
		gift.acceptGiftPackage = true;
		this.giftDao.update(gift);
		PushGiftStateResponse response = new PushGiftStateResponse(GiftStateType.GIFT_PACKAGE_STATE, 1);
		PushGiftHelper.pushGiftState(actorId, response);

		return result;
	}

	@Override
	public void onLogin(long actorId) {
		Gift gift = this.giftDao.get(actorId);
		ChainLock lock = LockUtils.getLock(gift);
		try {
			lock.lock();
			if (!DateUtils.isToday(gift.resetTime)) {
				gift.cleanGiftInfo();
				giftDao.update(gift);
				PushGiftHelper.pushGiftInfo(actorId, gift);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for (Long actorId : actorIds) {// 清除在线玩家购买信息
			Gift gift = giftDao.get(actorId);
			ChainLock lock = LockUtils.getLock(gift);
			try {
				lock.lock();
				gift.cleanGiftInfo();
				giftDao.update(gift);
				PushGiftHelper.pushGiftInfo(actorId, gift);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("清理送礼进度条时在线id:" + actorIds);
		}
	}

	@Override
	public Result oneKeyGiveGift(long actorId) {
		Gift gift = this.giftDao.get(actorId);
		PushGiftStateResponse giveResponse = new PushGiftStateResponse();
		PushGiftStateResponse acceptResponse = new PushGiftStateResponse(GiftStateType.RECEIVED, actorId);
		for(AllyVO allyVO:allyFacade.getAlly(actorId)){
			// 是否是盟友
			if (!allyFacade.isAlly(actorId, allyVO.actorId)) {
				return Result.valueOf(GameStatusCodeConstant.ALLY_NOT_EXISTS);
			}

			Gift allyGift = this.giftDao.get(allyVO.actorId);

			// 送礼次数到达上限
			if (gift.getAllysReceivedMyGiftSet().size() >= GiftRule.GIFT_MAX_GIVE_NUM) {
				return Result.valueOf(GIVE_GIFT_NUM_REACH_LIMIT);
			}

			// 查看礼物的配置是否存在
			int level = ActorHelper.getActorLevel(allyVO.actorId);
			GiftConfig config = GiftService.get(level);
			if (config == null) {
				return Result.valueOf(GIFT_CONFIG_NOT_FOUND);
			}

			// 抽取礼物
			List<AwardGoodsPacketConfig> packetList = config.getGiftList();
			int random = RandomUtils.nextInt(0, 1000);
			int rate = 0;
			AwardGoodsPacketConfig packet = null;
			for (AwardGoodsPacketConfig conf : packetList) {
				rate += conf.rate;
				if (rate > random) {
					packet = conf;
					break;
				}
			}

			// 配置错误导致不能随机出礼物
			if (packet == null) {
				return Result.valueOf(GIFT_CONFIG_NOT_FOUND);
			}

			// 添加礼物
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			for (Entry<Integer, Integer> entry : packet.goodsMap.entrySet()) {
				Integer goodsId = entry.getKey();
				Integer num = entry.getValue();
				map.put(goodsId, num);
			}

			if(AllyFacadeImpl.ROBOT_ID != allyVO.actorId){
				allyGift.receiveGiftFromAlly(actorId, map);// 将我的礼物放到盟友礼物信息里面
				this.giftDao.update(allyGift);
			}
			
			gift.sendGift2Ally(allyVO.actorId);// 记录我已送礼的盟友id
			this.giftDao.update(gift);

			giveResponse.map.put(allyVO.actorId,GiftStateType.GIVEN);
			PushGiftHelper.pushGiftState(allyVO.actorId, acceptResponse);
			eventBus.post(new AllyGiveGiftEvent(actorId, allyVO.actorId));
		}
		PushGiftHelper.pushGiftState(actorId, giveResponse);
		return Result.valueOf();
	}

	@Override
	public MapResult<Long, Integer> oneKeyAcceptGift(long actorId) {
		Gift gift = this.giftDao.get(actorId);
		MapResult<Long, Integer> result = new MapResult<>();
		PushGiftStateResponse response = new PushGiftStateResponse();
		for(AllyVO allyVO:allyFacade.getAlly(actorId)){
			// 礼物不存在
			Map<Integer, Integer> gifts = gift.getReceivedGiftsMap().get(allyVO.actorId);
			if (gifts == null) {
				continue;
			}

			//收礼已达上限
			if (gift.getAcceptedAllysSet().size() >= GiftRule.ACCEPT_GIFT_CAP) {
				continue;
			}

			// 收礼
			for (Entry<Integer, Integer> entry : gifts.entrySet()) {
				Integer num = entry.getValue();
				Integer goodsId = entry.getKey();
				TResult<Long> res = goodsFacade.addGoodsVO(actorId, GoodsAddType.ACCEPT_GIFT, goodsId, num);
				if (res.isOk()) {
					if(result.item.containsKey(res.item)){
						result.put(res.item, result.item.get(res.item) + num);
					}else{
						result.put(res.item, num);
					}
				}
			}
			
			// 更新DB
			gift.acceptAllyGift(allyVO.actorId);
			this.giftDao.update(gift);
			response.map.put(allyVO.actorId,GiftStateType.ACCEPTED);
			eventBus.post(new AllyRecevieGiftEvent(actorId, allyVO.actorId));
		}
		PushGiftHelper.pushGiftState(actorId, response);
		return result;
	}
}