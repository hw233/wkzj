package com.jtang.gameserver.module.love.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.LoveConfig;
import com.jtang.gameserver.dataconfig.service.LoveService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.love.dao.LoveDao;
import com.jtang.gameserver.module.love.facade.LoveFacade;
import com.jtang.gameserver.module.love.facade.LoveFightFacade;
import com.jtang.gameserver.module.love.facade.LoveMonsterFacade;
import com.jtang.gameserver.module.love.facade.LoveShopFacade;
import com.jtang.gameserver.module.love.handler.response.GetMarryGiftResponse;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;
import com.jtang.gameserver.module.love.helper.LovePushHelper;
import com.jtang.gameserver.module.love.model.MarryAcceptVO;
import com.jtang.gameserver.module.msg.facade.MsgFacade;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.type.SysmailType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class LoveFacadeImpl implements LoveFacade, ActorLoginListener, ZeroListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoveFacadeImpl.class);
	@Autowired
	private LoveDao loveDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private IconFacade iconfacade;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private GoodsFacade goodsFacade;

	@Autowired
	private HeroSoulFacade heroSoulFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private SysmailFacade sysmailFacade;
	
	@Autowired
	private MaintainFacade maintainFacade;
	
	@Autowired
	private MsgFacade msgFacade;
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private LoveMonsterFacade loveMonsterFacade;
	
	@Autowired
	private LoveShopFacade loveShopFacade;
	
	@Autowired
	private LoveFightFacade loveFightFacade;
	
	@Override
	public LoveInfoResponse getLoveInfo(long actorId) {
		Love love = loveDao.get(actorId);
		long loveActorId = 0;
		String loveActorName = "";
		IconVO loveActorIcon = new IconVO(0, 0, (byte) 0);
		byte hasGift = love.getHasGift();
		byte hasGive = love.hasGive();
//		if (love.married()) {
//			hasGive = DateUtils.isToday(love.getGiveGiftTime()) ? (byte) 1 : (byte) 0;
//		}
		if (love.getLoveActorId() > 0) {
			Actor target = actorFacade.getActor(love.getLoveActorId());
			if (target != null) {
				loveActorId = target.getPkId();
				loveActorName = target.actorName;
				loveActorIcon = iconfacade.getIconVO(loveActorId);
			}
		}
		List<MarryAcceptVO> acceptList = getAcceptList(love);
		LoveInfoResponse loveInfoResponse = new LoveInfoResponse(loveActorId, loveActorName, loveActorIcon, hasGift, hasGive, acceptList);
		return loveInfoResponse;
	}

	private List<MarryAcceptVO> getAcceptList(Love love) {
		List<MarryAcceptVO> list = new ArrayList<>();
		ConcurrentHashMap<Long, Integer> map = love.getAcceptMap();
		for (Entry<Long, Integer> entry : map.entrySet()) {
			long targetActorId = entry.getKey();
			Actor actor = actorFacade.getActor(targetActorId);
			IconVO iconVO = iconfacade.getIconVO(targetActorId);
			int timeout = entry.getValue();
			MarryAcceptVO marryAcceptVO = new MarryAcceptVO(targetActorId, actor.actorName, timeout, iconVO);
			list.add(marryAcceptVO);
		}
		return list;
	}

	@Override
	public Result marry(long actorId, long targetActorId) {
		if (targetActorId == 101) {
			return Result.valueOf(GameStatusCodeConstant.ACTOR_NOT_FOUND);
		}
		Actor actor = actorFacade.getActor(actorId);
		Actor targetActor = actorFacade.getActor(targetActorId);
		if (targetActor == null) {
			return Result.valueOf(GameStatusCodeConstant.ACTOR_NOT_FOUND);
		}
		LoveConfig loveConfig = LoveService.get();
		if (loveConfig == null) {
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		if (actor.level < loveConfig.getActorLimitLevel() || targetActor.level < loveConfig.getActorLimitLevel()) {
			return Result.valueOf(GameStatusCodeConstant.MARRY_LEVEL_NOT_ENOUGH);
		}
		
		Love love = loveDao.get(actorId);
		Love targetLove = loveDao.get(targetActorId);
		ChainLock lock = LockUtils.getLock(love, targetLove);
		try {
			lock.lock();
			if (DateUtils.isToday(love.getUnloveTime()) || DateUtils.isToday(targetLove.getUnloveTime())) {
				return Result.valueOf(GameStatusCodeConstant.MARRY_COOL);
			}
			if (love.married() || targetLove.married()) {
				return Result.valueOf(GameStatusCodeConstant.HAS_MARRIED);
			}
			
			if (targetLove.getAcceptMap().size() >= loveConfig.getMarryRequestMax()) {
				return Result.valueOf(GameStatusCodeConstant.HAS_REQUEST_MARRY_MAX);
			}

			if (targetLove.getAcceptMap().containsKey(actorId)) {
				int time = TimeUtils.getNow();
				if (time <= targetLove.getAcceptMap().get(actorId)) {
					return Result.valueOf(GameStatusCodeConstant.HAS_REQUEST_MARRY);
				}
			}

			int goodsNum = goodsFacade.getCount(actorId, loveConfig.getMarryConsumeGoodsId());
			if (goodsNum < loveConfig.getMarryConsumeGoodsNum()) {
				return Result.valueOf(GameStatusCodeConstant.MARRY_USE_GOODS_NOT_ENOUGH);
			}
			int ticket = vipFacade.getTicket(actorId);
			if (ticket < loveConfig.getMarryConsumTicketNum()) {
				return Result.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
			}

			IconVO loveIcon = iconfacade.getIconVO(actorId);
			IconVO tarIconVO = iconfacade.getIconVO(targetActorId);
			if (loveIcon.sex == tarIconVO.sex) {
				return Result.valueOf(GameStatusCodeConstant.MARRY_SEX_SAME);
			}

			targetLove.getAcceptMap().put(actorId, TimeUtils.getNow() + loveConfig.getMarrayRequestTimeOut());
			loveDao.update(targetLove);
			// 推送目标消息
			LovePushHelper.pushAcceptList(targetActorId, getAcceptList(targetLove));
			msgFacade.sendMsg2one(actorId, targetActorId, String.format(loveConfig.getPrivateMsg(), actor.actorName));
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public TResult<LoveInfoResponse> acceptMarry(long actorId, byte accept, long targetActorId) {
		Love love = loveDao.get(actorId);
		Love targetLove = loveDao.get(targetActorId);
		LoveConfig cfg = LoveService.get();
		ChainLock lock = LockUtils.getLock(love, targetLove);
		try {
			lock.lock();
			if (love.married() || targetLove.married()) {
				if (targetLove.married()) {
					love.getAcceptMap().remove(targetActorId);
					loveDao.update(love);
					LovePushHelper.pushAcceptList(actorId, getAcceptList(love));
				}
				return TResult.valueOf(GameStatusCodeConstant.HAS_MARRIED);
			}
			if (DateUtils.isToday(love.getUnloveTime())) {
				return TResult.valueOf(GameStatusCodeConstant.MARRY_COOL);
			}
			if (love.getAcceptMap().size() == 0) {
				return TResult.valueOf(GameStatusCodeConstant.MARRY_ACCEPT_TIMEOUT);
			}
			if (love.getAcceptMap().containsKey(targetActorId) == false) {
				return TResult.valueOf(GameStatusCodeConstant.MARRY_ACCEPT_TIMEOUT);
			}

			IconVO loveIcon = iconfacade.getIconVO(actorId);
			IconVO tarIconVO = iconfacade.getIconVO(targetActorId);
			if (loveIcon.sex == tarIconVO.sex) {
				return TResult.valueOf(GameStatusCodeConstant.MARRY_SEX_SAME);
			}

			Actor actor = actorFacade.getActor(actorId);
			Actor targetActor = actorFacade.getActor(targetActorId);

			if (accept == 0) {
				love.getAcceptMap().remove(targetActorId);
				sysmailFacade.sendSysmail(targetActorId, SysmailType.REQUEST_MARRY, new ArrayList<RewardObject>(), actor.actorName);
			} else {
				if (TimeUtils.getNow() > love.getAcceptMap().get(targetActorId)) {
					love.getAcceptMap().remove(targetActorId);
					return TResult.valueOf(GameStatusCodeConstant.MARRY_ACCEPT_TIMEOUT);
				}

				if (cfg == null) {
					return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
				}
				Result flag = goodsFacade.decreaseGoods(targetActorId, GoodsDecreaseType.LOVE, cfg.getMarryConsumeGoodsId(), cfg.getMarryConsumeGoodsNum());
				if (flag.isFail()) {
					return TResult.valueOf(GameStatusCodeConstant.MARRY_USE_GOODS_NOT_ENOUGH);
				}
				boolean tt = vipFacade.decreaseTicket(targetActorId, TicketDecreaseType.LOVE, cfg.getMarryConsumTicketNum(), 0, 0);
				if (tt == false) {
					return TResult.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
				}

				love.setLoveActorId(targetActorId);
				targetLove.setLoveActorId(actorId);
				for (long id : love.getAcceptMap().keySet()) {
					if (id != targetActorId) {
						sysmailFacade.sendSysmail(id, SysmailType.REQUEST_MARRY, new ArrayList<RewardObject>(), actor.actorName);
					}
				}
				for (long id : targetLove.getAcceptMap().keySet()) {
					if (id != actorId) {
						sysmailFacade.sendSysmail(id, SysmailType.REQUEST_MARRY, new ArrayList<RewardObject>(), targetActor.actorName);
					}
				}
				love.getAcceptMap().clear();
				targetLove.getAcceptMap().clear();
				
				iconfacade.marryUnLock(targetActorId);
				iconfacade.marryUnLock(actorId);
				
				// TODO 推送目标信息 广播结婚消息
				String loveActorName = actorFacade.getActor(actorId).actorName;
				IconVO loveActorIcon = iconfacade.getIconVO(actorId);
				LovePushHelper.pushMarry(targetActorId, actorId, loveActorName, loveActorIcon);
				LovePushHelper.pushGift(targetActorId, targetLove.getHasGift(), targetLove.hasGive());
				LovePushHelper.pushAcceptList(targetActorId, getAcceptList(targetLove));

				String msg = String.format(cfg.getSystemMsg(), actor.actorName, targetActor.actorName);
				maintainFacade.sendNotice(msg, 1, 0, new ArrayList<Integer>());
			}

			loveDao.update(love);
			loveDao.update(targetLove);

		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}

		return TResult.sucess(getLoveInfo(actorId));
	}

	@Override
	public TResult<LoveInfoResponse> unMarry(long actorId) {
		Love love = loveDao.get(actorId);
		Love targetLove = loveDao.get(love.getLoveActorId());

		ChainLock lock = LockUtils.getLock(love, targetLove);
		try {
			lock.lock();
			if (love.married() == false) {
				return TResult.valueOf(GameStatusCodeConstant.NOT_MARRY);
			}
			LoveConfig cfg = LoveService.get();
			if (cfg == null) {
				return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			boolean tt = vipFacade.decreaseTicket(actorId, TicketDecreaseType.LOVE, cfg.getUnloveUseTicket(), 0, 0);
			if (tt == false) {
				return TResult.valueOf(GameStatusCodeConstant.TICKET_NOT_ENOUGH);
			}
			
			//离婚之前需要清空双方的仙侣排行、商店、合作数据
			loveMonsterFacade.unMarry(actorId);
			loveShopFacade.unMarry(actorId);
			loveFightFacade.unMarry(actorId);
			
			long targetActorId = love.getLoveActorId();
			int unloveTime = TimeUtils.getNow();
			love.fightStateMap.clear();
			love.setLoveActorId(0);
			love.setUnloveTime(unloveTime);
			love.removeGift();
			love.rankFightNum = 0;
			love.rankFightTime = 0;
			love.rankFlushNum = 0;
			love.rankFlushTime = 0;
			love.fightTime = 0;
			targetLove.setUnloveTime(unloveTime);
			targetLove.setLoveActorId(0);
			targetLove.removeGift();
			targetLove.fightStateMap.clear();
			

			loveDao.update(love);
			loveDao.update(targetLove);
			
			iconfacade.marryLock(actorId);
			iconfacade.marryLock(targetActorId);
			
			IconVO loveActorIcon = new IconVO(0, 0, (byte) 0);
			LovePushHelper.pushMarry(targetActorId, 0, "", loveActorIcon);
			LovePushHelper.pushGift(targetActorId, targetLove.getHasGift(), targetLove.hasGive());
			Actor actor = actorFacade.getActor(actorId);
			sysmailFacade.sendSysmail(targetActorId, SysmailType.UN_MARRY, new ArrayList<RewardObject>(), actor.actorName);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}

		// 推送目标信息
		return TResult.sucess(getLoveInfo(actorId));
	}

	@Override
	public Result giveGift(long actorId) {
		Love love = loveDao.get(actorId);
		Love target = loveDao.get(love.getLoveActorId());
		ChainLock lock = LockUtils.getLock(love, target);
		try {
			lock.lock();
			if (love.married() == false) {
				return Result.valueOf(GameStatusCodeConstant.NOT_MARRY);
			}
			if (DateUtils.isToday(love.getGiveGiftTime())) {
				return Result.valueOf(GameStatusCodeConstant.HAS_GIFT);
			}
			if (target.getHasGift() != 0) {
				return Result.valueOf(GameStatusCodeConstant.HAS_GIFT);
			}
			LoveConfig cfg = LoveService.get();
			if (cfg == null) {
				return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
			}
			Actor actor = actorFacade.getActor(actorId);
			target.addGift(cfg.getGiftList(actor.level));
			love.setGiveGiftTime(TimeUtils.getNow());
			loveDao.update(love);
			loveDao.update(target);
			LovePushHelper.pushGift(actorId, love.getHasGift(), love.hasGive());
			LovePushHelper.pushGift(target.getPkId(), target.getHasGift(), target.hasGive());
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}

		return Result.valueOf();
	}

	@Override
	public TResult<GetMarryGiftResponse> acceptGift(long actorId) {
		Love love = loveDao.get(actorId);
		if (love.getHasGift() != 1) {
			return TResult.valueOf(GameStatusCodeConstant.NOT_HAS_GIFT);
		}
		List<RewardObject> gift = love.removeGift();
		love.setReciveGiftTime(TimeUtils.getNow());
		loveDao.update(love);
		sendReward(actorId, gift);
		LovePushHelper.pushGift(actorId, love.getHasGift(), love.hasGive());
		return TResult.sucess(new GetMarryGiftResponse(gift));
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
				equipFacade.addEquip(actorId, EquipAddType.LOVE_GIFT, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.LOVE_GIFT, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.LOVE_GIFT, id, num);
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
			sendReward(actorId, rewardObject.id, rewardObject.num, rewardObject.rewardType);
		}
	}

	@Override
	public void onLogin(long actorId) {
		Love love = loveDao.get(actorId);
		ChainLock lock = LockUtils.getLock(love);
		try {
			lock.lock();
			int now = TimeUtils.getNow();
			List<Long> timeout = new ArrayList<>();
			for (Map.Entry<Long, Integer> entry : love.getAcceptMap().entrySet()) {
				if (entry.getValue() < now) {
					timeout.add(entry.getKey());
				}
			}

			for (long id : timeout) {
				Actor actor = actorFacade.getActor(id);
				love.getAcceptMap().remove(id);
				loveDao.update(love);
				sysmailFacade.sendSysmail(id, SysmailType.REQUEST_MARRY, new ArrayList<RewardObject>(), actor.actorName);
			}

		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public boolean isMarry(long actorId) {
		Love love = loveDao.get(actorId);
		return love.married();
	}
	
	
	@Override
	public void onZero() {
		Set<Long> online = playerSession.onlineActorList();
		for (long actorId : online) {
			Love love = loveDao.get(actorId);
			LovePushHelper.pushGift(actorId, love.getHasGift(), love.hasGive());
		}
	}
}
