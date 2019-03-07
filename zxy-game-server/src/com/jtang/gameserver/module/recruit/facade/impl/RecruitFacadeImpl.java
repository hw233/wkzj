package com.jtang.gameserver.module.recruit.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.EventBus;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.RecruitEvent;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.RecruitConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.RecruitService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Recruit;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.recruit.dao.RecruitDao;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.recruit.handler.response.GetInfoResponse;
import com.jtang.gameserver.module.recruit.type.RecruitType;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 聚仙阵实现接口
 * @author 0x737263
 *
 */
@Component
public class RecruitFacadeImpl implements RecruitFacade {
//	private static final Log LOGGER = LogFactory.getLog(RecruitFacadeImpl.class);
	
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private RecruitDao recruitDao;
	@Autowired
	private StoryFacade storyFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private Schedule schedule;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private EventBus eventBus;
	
	
	public void init(long actorId) {
		Recruit recruit = recruitDao.get(actorId);
		int now = TimeUtils.getNow();
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(RecruitType.BIG.getCode());
		boolean flag = false;
		if (recruitConfig.getInitCoolTime() > 0) {
			int time = now - (recruitConfig.getTotalInterval(0) - recruitConfig.getInitCoolTime());
			recruit.setBigUseTime(time);
			flag = true;
		}
		recruitConfig = RecruitService.getRecruitConfig(RecruitType.SMALL.getCode());
		if (recruitConfig.getInitCoolTime() > 0) {
			int time = now - (recruitConfig.getTotalInterval(0) - recruitConfig.getInitCoolTime());
			recruit.setSmallUseTime(time);
			flag = true;
		}
		if (flag) {
			recruitDao.update(recruit);
		}
	}
	
	
	@Override
	public GetInfoResponse getInfo(long actorId) {
		resetUseNum(actorId, RecruitType.SMALL.getCode());
		resetUseNum(actorId, RecruitType.BIG.getCode());
		Recruit recruit = recruitDao.get(actorId);
		int smalltotalTime = 0;
		int bigtotalTime = 0;
		int now = TimeUtils.getNow();
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(RecruitType.SMALL.getCode());
		smalltotalTime =  recruitConfig.getTotalInterval(0) - (now - recruit.getUseTime(RecruitType.SMALL.getCode()));
		
		recruitConfig = RecruitService.getRecruitConfig(RecruitType.BIG.getCode());
		int modifPercent = 0;
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege != null) {
			modifPercent = vipPrivilege.recruitTime;
		}
		bigtotalTime =  recruitConfig.getTotalInterval(modifPercent) - (now - recruit.getUseTime(RecruitType.BIG.getCode()));
		int smallLeastNum = RecruitService.getLeastNum(RecruitType.SMALL.getCode(), recruit.smallTotalUseNum);
		int bigLeastNum = RecruitService.getLeastNum(RecruitType.BIG.getCode(), recruit.bigTotalUseNum);
		GetInfoResponse getInfoResponse = new  GetInfoResponse(smalltotalTime, bigtotalTime, smallLeastNum, bigLeastNum);
		return getInfoResponse;
	}
	
	/**
	 * 单抽
	 * @param actorId
	 * @param type 聚仙类型
	 * @param star 星级限制
	 * @return
	 */
	private TResult<List<RewardObject>> single(long actorId, byte type, int star) {
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
		Recruit recruit = recruitDao.get(actorId);
		int now = TimeUtils.getNow();
		int useTime = recruit.getUseTime(type);
		int useNum = recruit.getUseNum(type) ;
		boolean freeTime = false;
		int modifPercent = 0;
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege != null) {
			modifPercent = vipPrivilege.recruitTime;
		}
		if (now - useTime >= recruitConfig.getTotalInterval(modifPercent)) {
			freeTime = true;
		}
		if (useNum >= recruitConfig.getFreeNum() || freeTime == false){ //免费次数用完
			if (type == RecruitType.SMALL.getCode()) {
				
				int useGoodsId = recruitConfig.getUseGoodsId();
				int useGoodsNum = recruitConfig.getEachUseGoodsNum();
				int hasGoosNum = goodsFacade.getCount(actorId, useGoodsId);
				if (hasGoosNum < useGoodsNum) {
					return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOODS_NOT_ENOUGH);
				}
				goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, useGoodsId, useGoodsNum);
				
			} else {
				int useTicket = recruitConfig.getEachUseTicket();
				
				int hasTicket = vipFacade.getTicket(actorId);
				if (hasTicket < useTicket) {
					return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TICKET_NOT_ENOUGH);
				}
				vipFacade.decreaseTicket(actorId, TicketDecreaseType.RECRUIT_RAND, useTicket, 0, 0);
				
			}
			
		} else {
			useNum += 1;
			recruit.setUseTime(type);
			recruit.setUseNum(type, (byte) (useNum));
		}
		recruit.setTotleUseNum(type);
		recruitDao.update(recruit);
		List<RewardObject> result = new ArrayList<RewardObject>();
		if (recruit.getTotleUseNum(type) == 1) {

			List<RewardObject> first = RecruitService.getFirstReward(type);
			if (first != null && first.isEmpty() == false) {
				result.addAll(first);
			}
		} else {

			List<RewardObject> list = RecruitService.getLeastReward(type, recruit.getTotleUseNum(type));
			if (list == null) {
				RewardObject rewardObject = RecruitService.getReward(type);
				if ( recruit.historyNum(type) >= 1) { // 10次内不出现2个star以上的
					rewardObject = randomNotStarHero(type, star);
				} else {
					rewardObject = RecruitService.getReward(type);
				}
				if (rewardObject.rewardType.equals(RewardType.HERO)) {
					HeroConfig cfg = HeroService.get(rewardObject.id);
					if (cfg.getStar() >= star ) {
						recruit.putHistory(true, type);
					} else {
						recruit.putHistory( false, type);
					}
				} else {
					recruit.putHistory( false, type);
				}
				result.add(rewardObject);
			} else {
				result.addAll(list);
			}
		}
		return TResult.sucess(result);
	}
	
	/**
	 * 小聚仙10连抽
	 * @param actorId
	 * @return
	 */
	private TResult<List<RewardObject>> mutiSmall(long actorId) {
		
		byte type = RecruitType.SMALL.getCode();
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
		int useGoodsId = recruitConfig.getUseGoodsId();
		int useGoodsNum = recruitConfig.getSeriesUseGoodsNum();
		int hasGoosNum = goodsFacade.getCount(actorId, useGoodsId);
		if (hasGoosNum < useGoodsNum) {
			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOODS_NOT_ENOUGH);
		}
		goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, useGoodsId, useGoodsNum);
		List<RewardObject> list = new ArrayList<RewardObject>();
		int star = 3; //不超过星数 
		boolean flag = false;
		for (int i = 0; i < 9; i++) {
			RewardObject rewardObject = null;
			if (flag) {
				rewardObject = randomNotStarHero(type, star);
			} else {
				rewardObject = RecruitService.getReward(type);
			}
			if (rewardObject.rewardType.equals(RewardType.HERO)) {
				HeroConfig cfg = HeroService.get(rewardObject.id);
				if (cfg.getStar() >= star ) {
					flag = true;
				}
			}
			list.add(rewardObject);
		}
		
		List<RewardObject> mutiLeast = RecruitService.getMutiLeastReward(type);
		list.addAll(mutiLeast);
		
		return TResult.sucess(list);
	}
	/**
	 * 大聚仙10连抽
	 * @param actorId
	 * @return
	 */
	private TResult<List<RewardObject>> mutiBig(long actorId) {
		
		byte type = RecruitType.BIG.getCode();
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
		if (recruitConfig == null) {
			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TYPE_ERROR);
		}
		int useGold = recruitConfig.getSeriesUseGold();
		int useTicket = recruitConfig.getSeriesUseTicket();
		Actor actor = actorFacade.getActor(actorId);
		
		long hasGold = actor.gold;
		if (hasGold < useGold){
			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOLD_NOT_ENOUGH);
		}
		int hasTicket = vipFacade.getTicket(actorId);
		if (hasTicket < useTicket) {
			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TICKET_NOT_ENOUGH);
		}
		actorFacade.decreaseGold(actorId, GoldDecreaseType.RECRUIT, useGold);
		vipFacade.decreaseTicket(actorId, TicketDecreaseType.RECRUIT_RAND, useTicket, 0, 0);
		List<RewardObject> list = new ArrayList<RewardObject>();
		boolean flag = false;
		int star = 5; // 不超过的星数
		for (int i = 0; i < 9; i++) {
			RewardObject rewardObject = null;
			if (flag) {
				rewardObject = randomNotStarHero(type, star);
			} else {
				rewardObject = RecruitService.getReward(type);
			}
			if (rewardObject.rewardType.equals(RewardType.HERO)) {
				HeroConfig cfg = HeroService.get(rewardObject.id);
				if (cfg.getStar() >= star ) {
					flag = true;
				}
			}
			list.add(rewardObject);
		}
		List<RewardObject> mutiLeast = RecruitService.getMutiLeastReward(type);
		list.addAll(mutiLeast);
		
		return TResult.sucess(list);
	}
	
	@Override
	public TResult<List<RewardObject>> randRecruit(long actorId, byte recruitType, byte single) {
		if (recruitType == 1 && single == 1 ) {//小聚仙单抽
			byte type = RecruitType.SMALL.getCode();
			resetUseNum(actorId, type );
			TResult<List<RewardObject>> result = single(actorId, type, 3);
			if (result.isOk()) {
				sendReward(actorId, result.item);
				eventBus.post(new RecruitEvent(actorId,1));
			}
			return result;
		}
		
		if (recruitType == 1 && single == 2) {//小聚仙10连抽
			byte type = RecruitType.SMALL.getCode();
			resetUseNum(actorId, type );
			TResult<List<RewardObject>> result = mutiSmall(actorId);
			if (result.isOk()) {
				sendReward(actorId, result.item);
				eventBus.post(new RecruitEvent(actorId,10));
			}
			return result;
		}
		if (recruitType == 2 && single == 1) {//大聚仙单抽
			byte type = RecruitType.BIG.getCode();
			resetUseNum(actorId, type );
			TResult<List<RewardObject>> result = single(actorId, type, 5);
			if (result.isOk()) {
				sendReward(actorId, result.item);
				eventBus.post(new RecruitEvent(actorId,1));
			}
			return result;
		}
		if (recruitType == 2 && single == 2) {//大聚仙10连抽
			byte type = RecruitType.BIG.getCode();
			resetUseNum(actorId, type );
			TResult<List<RewardObject>> result = mutiBig(actorId);
			if (result.isOk()) {
				sendReward(actorId, result.item);
				eventBus.post(new RecruitEvent(actorId,10));
			}
			return result;
		}
		return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TYPE_ERROR);
	}
	
	private void resetUseNum(long actorId, byte type) {
		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
		Recruit recruit = recruitDao.get(actorId);
		int useTime = recruit.getUseTime(type);
		int useNum = recruit.getUseNum(type) ;
		int now = TimeUtils.getNow();
		int modifPercent = 0;
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege != null) {
			modifPercent = vipPrivilege.recruitTime;
		}
		if (useNum >= recruitConfig.getFreeNum() && (now - useTime) > recruitConfig.getTotalInterval(modifPercent)) {
			recruit.timeReset(type);
			recruit.setUseNum(type, (byte) 0);
			recruitDao.update(recruit);
		}
	}
	
//	@Override
//	public TResult<List<RewardObject>> bigRecruit(long actorId, byte type) {
//		if (type == 0) {
//			return singleRecruit(actorId, RecruitType.BIG.getCode());
//		}
//		return mutiRecruit(actorId, RecruitType.BIG.getCode());
//	}
//	
//	@Override
//	public TResult<List<RewardObject>> smallRecruit(long actorId, boolean useGoods, int num) {
//		if (num <= 0) {
//			return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
//		}
//		resetUseNum(actorId, RecruitType.SMALL.getCode());
//		byte type = RecruitType.SMALL.getCode();
//		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
//		
//		resetUseNum(actorId, type );
//		Recruit recruit = recruitDao.get(actorId);
//		int now = TimeUtils.getNow();
//		int useTime = recruit.getUseTime(type);
//		int useNum = recruit.getUseNum(type) ;
//		boolean freeTime = false;
//		if (now - useTime >= recruitConfig.getFreeInterval()) {
//			freeTime = true;
//		}
//		if (useNum >= recruitConfig.getFreeNum() || freeTime == false){ //免费次数用完
//			if (useGoods == false) {
//				return TResult.valueOf(GameStatusCodeConstant.RAND_FAILED_CHANGE_NOT_ENOUGH);
//			}
//			int useGoodsId = recruitConfig.getUseGoodsId();
//			int useGoodsNum = recruitConfig.getUseGoodsNum() * num;
//			int hasGoosNum = goodsFacade.getCount(actorId, useGoodsId);
//			if (hasGoosNum < useGoodsNum) {
//				return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOODS_NOT_ENOUGH);
//			}
//			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, useGoodsId, useGoodsNum);
//			
//		} else {
//			if (useGoods || num > 1) {
//				return TResult.valueOf(StatusCode.DATA_VALUE_ERROR);
//			}
//			useNum += num;
//			recruit.setUseTime(type);
//			recruit.setUseNum(type, (byte) (useNum));
//		}
//		recruit.setTotleUseNum(type);
//		recruitDao.update(recruit);
//		List<RewardObject> result = new ArrayList<RewardObject>();
//		for (int i = 0; i < num; i++) {
//			
//			if (recruit.getSmallTotalUseNum() == 1){
//				
//				List<RewardObject> first = RecruitService.getFirstReward(type);
//				if (first != null && first.isEmpty() == false){
////					sendReward(actorId, first);
////					return TResult.sucess(first);
//					result.addAll(first);
//				}
//			} else {
//				
//				List<RewardObject> list = RecruitService.getLeastReward(type, recruit.getSmallTotalUseNum());
//				if (list == null ){
////					list = new ArrayList<>();
//					RewardObject  rewardObject = RecruitService.getReward(type, RecruitRandomType.SINGLE.getCode());	
////					list.add(rewardObject);
//					result.add(rewardObject);
//				}  else {
//					result.addAll(list);
//				}
//			}
//			
//		}
//		
//		sendReward(actorId, result);
//		eventBus.post(new RecruitEvent(actorId,num));
//		return TResult.sucess(result);
//		
//	}
//	
//	private TResult<List<RewardObject>> singleRecruit(long actorId, byte type) {
//		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
//		if (recruitConfig == null) {
//			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TYPE_ERROR);
//		}
//		resetUseNum(actorId, type);
//		Recruit recruit = recruitDao.get(actorId);
//		int now = TimeUtils.getNow();
//		int useTime = recruit.getUseTime(type);
//		int useNum = recruit.getUseNum(type) ;
//		boolean freeTime = false;
//		if (now - useTime >= recruitConfig.getFreeInterval()) {
//			freeTime = true;
//		}
//		if (useNum >= recruitConfig.getFreeNum() || freeTime == false){ //免费次数用完
//			int useGold = recruitConfig.getEachUseGold();
//			int useTicket = recruitConfig.getEachUseTicket();
//			Actor actor = actorFacade.getActor(actorId);
//			
//			long hasGold = actor.gold;
//			if (hasGold < useGold){
//				return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOLD_NOT_ENOUGH);
//			}
//			int hasTicket = vipFacade.getTicket(actorId);
//			if (hasTicket < useTicket) {
//				return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TICKET_NOT_ENOUGH);
//			}
//			actorFacade.decreaseGold(actorId, GoldDecreaseType.RECRUIT, useGold);
//			vipFacade.decreaseTicket(actorId, TicketDecreaseType.RECRUIT_RAND, useTicket, 0, 0);
//			
//		} else {
//			useNum += 1;
//			recruit.setUseTime(type);
//			recruit.setUseNum(type, (byte) (useNum));
//		}
//		recruit.setTotleUseNum(type);
//		recruitDao.update(recruit);
//		if (recruit.getBigTotalUseNum() == 1){
//			
//			List<RewardObject> first = RecruitService.getFirstReward(type);
//			if (first != null && first.isEmpty() == false){
//				sendReward(actorId, first);
//				return TResult.sucess(first);
//			}
//		}
//		
//		List<RewardObject> list = RecruitService.getLeastReward(type, recruit.getBigTotalUseNum());
//		if (list == null ){
//			list = new ArrayList<>();
//			RewardObject  rewardObject = RecruitService.getReward(type, RecruitRandomType.SINGLE.getCode());	
//			
//			if (type == RecruitType.BIG.getCode() && recruit.fiveStarNum() >= 1) { // 大聚仙10次内不出现2个五星以上的
//				rewardObject = randomNotFiveStarHero(type, RecruitRandomType.SINGLE);
//			} else {
//				rewardObject = RecruitService.getReward(type, RecruitRandomType.SINGLE.getCode());
//			}
//			if (type == RecruitType.BIG.getCode()){
//				if (rewardObject.rewardType.equals(RewardType.HERO)) {
//					HeroConfig cfg = HeroService.get(rewardObject.id);
//					if (cfg.getStar() >=5 ) {
//						recruit.putHistory(recruit.bigTotalUseNum, true);
//					} else {
//						recruit.putHistory(recruit.bigTotalUseNum, false);
//					}
//				} else {
//					recruit.putHistory(recruit.bigTotalUseNum, false);
//				}
//			} else {
//				recruit.putHistory(recruit.bigTotalUseNum, false);
//			}
//			list.add(rewardObject);
//		} else {
//			recruit.putHistory(recruit.bigTotalUseNum, false);
//		}
//		sendReward(actorId, list);
//		eventBus.post(new RecruitEvent(actorId,1));
//		return TResult.sucess(list);
//	}
//	
//	private TResult<List<RewardObject>> mutiRecruit(long actorId, byte type) {
//		RecruitConfig recruitConfig = RecruitService.getRecruitConfig(type);
//		if (recruitConfig == null) {
//			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TYPE_ERROR);
//		}
//		int useGold = recruitConfig.getSeriesUseGold();
//		int useTicket = recruitConfig.getSeriesUseTicket();
//		Actor actor = actorFacade.getActor(actorId);
//		
//		long hasGold = actor.gold;
//		if (hasGold < useGold){
//			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_GOLD_NOT_ENOUGH);
//		}
//		int hasTicket = vipFacade.getTicket(actorId);
//		if (hasTicket < useTicket) {
//			return TResult.valueOf(GameStatusCodeConstant.RECRUIT_TICKET_NOT_ENOUGH);
//		}
//		actorFacade.decreaseGold(actorId, GoldDecreaseType.RECRUIT, useGold);
//		vipFacade.decreaseTicket(actorId, TicketDecreaseType.RECRUIT_RAND, useTicket, 0, 0);
//		
//		List<RewardObject> list = new ArrayList<>();
//		if (type == RecruitType.BIG.getCode()) {// 大聚仙一次10连抽不出现2个以上的5星仙人
//			boolean flag = false;
//			for (int i = 0; i < 9; i++) {
//				RewardObject rewardObject = null;
//				if (flag) {
//					rewardObject = randomNotFiveStarHero(type, RecruitRandomType.SINGLE);
//				} else {
//					rewardObject = RecruitService.getReward(type, RecruitRandomType.SINGLE.getCode());
//				}
//				if (rewardObject.rewardType.equals(RewardType.HERO)) {
//					HeroConfig cfg = HeroService.get(rewardObject.id);
//					if (cfg.getStar() >=5 ) {
//						flag = true;
//					}
//				}
//				list.add(rewardObject);
//			}
//			
//		} else {
//			for (int i = 0; i < 9; i++) {
//				RewardObject rewardObject = RecruitService.getReward(type, RecruitRandomType.SINGLE.getCode());
//				list.add(rewardObject);
//			}
//		}
//		Recruit recruit = recruitDao.get(actorId);
//		recruit.tenRecruitNum += 1;
//		recruitDao.update(recruit);
//		List<RewardObject> seriesGoodsList = null;
//		if (recruit.tenRecruitNum == 1) {
//			seriesGoodsList = RecruitService.getSeriesFirstReward(type);
//			list.addAll(seriesGoodsList);
//		} else {
//			RewardObject seriesGoods = RecruitService.getReward(type, RecruitRandomType.MUTI.getCode());
//			list.add(seriesGoods);
//		}
//			
//		sendReward(actorId, list);
//		eventBus.post(new RecruitEvent(actorId,10));
//		return TResult.sucess(list);
//	}

	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject);
		}
	}

	private void sendReward(long actorId, RewardObject rewardObject) {
		switch (rewardObject.rewardType) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.RECRUIT, rewardObject.id, rewardObject.num);
			break;
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.RECRUIT, rewardObject.id);
			break;
		case HEROSOUL:
//			int heroSoulId = RecruitService.getHeroSoul(ActorHelper.getActorLevel(actorId),rewardObject.id);
//			if(heroSoulId > 0){
//				rewardObject.id = heroSoulId;
//			}
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.RECRUIT_RAND, rewardObject.id, rewardObject.num);
			break;
		case HERO:
//			int heroId = RecruitService.getHero(ActorHelper.getActorLevel(actorId),rewardObject.id);
//			if(heroId > 0){
//				rewardObject.id = heroId;
//			}
			boolean flag = heroFacade.isHeroExits(actorId, rewardObject.id);
			if (flag) {
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.RECRUIT_RAND, rewardObject.id, rewardObject.num);// num有问题
			} else {
				heroFacade.addHero(actorId, HeroAddType.RECRUIT_RAND, rewardObject.id);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取一个非指定星级以上仙人的奖励
	 * @param type
	 * @param randomType
	 * @return
	 */
	private RewardObject randomNotStarHero(byte type, int star) {
		RewardObject rewardObject = RecruitService.getReward(type);
		if (rewardObject.rewardType.equals(RewardType.HERO)) {
			HeroConfig cfg = HeroService.get(rewardObject.id);
			if (cfg.getStar() >= star ) {
				return randomNotStarHero(type, star);
			}
		}
		return rewardObject;
	}

}
