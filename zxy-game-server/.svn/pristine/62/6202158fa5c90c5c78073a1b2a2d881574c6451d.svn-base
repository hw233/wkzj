package com.jtang.gameserver.module.user.facade.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.event.UseTicketsEvent;
import com.jtang.gameserver.component.event.VipLevelChangeEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.RechargeConfig;
import com.jtang.gameserver.dataconfig.model.VipConfig;
import com.jtang.gameserver.dataconfig.service.RechargeAppService;
import com.jtang.gameserver.dataconfig.service.RechargeRateService;
import com.jtang.gameserver.dataconfig.service.VipService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Vip;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.user.dao.VipDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorPushHelper;
import com.jtang.gameserver.module.user.model.Vip3Privilege;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

@Component
public class VipFacadeImpl implements VipFacade , Receiver{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private VipDao vipDao;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private HeroFacade heroFacade;
	
	@Autowired
	private GoodsFacade goodsFacade;
	
	@Autowired
	private ActorFacade actorFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private StoryFacade storyFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	
	private Map<Integer, VipPrivilege> vipPrivileges = new HashMap<Integer, VipPrivilege>();
	
	@PostConstruct
	private void init(){
		for (int i = 1; i <= VipService.maxLevel(); i++) {
			VipPrivilege vipPrivilege = VipPrivilege.createInstance(i);
			VipConfig cfg = VipService.getByLevel(i);
			if (cfg != null){
				vipPrivilege.init(cfg.extArray);
			}
			vipPrivileges.put(vipPrivilege.getVipLevel(), vipPrivilege);
		}
		eventBus.register(EventKey.VIP_LEVEL_CHANGE, this);
	}
	
	@Override
	public boolean rechargeTicket(long actorId, int rechargeNum, int giveNum, int money,int rechargeId) {
		if (giveNum < 1){
			giveNum = 0;
		}
		int totalGiveNum = giveNum; //本次总数赠送的点券
		boolean isPost = false;
		boolean result = false;
		Vip vip = vipDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vip);
		try{
			lock.lock();
			if (vip.rechargeNum == 0){
				vip.firstRechargeTime = DateUtils.getNowInSecondes();
				RechargeConfig recharegeConfig = RechargeAppService.getRecharege(1);
				sendReward(actorId, recharegeConfig.rewardList);
			}
			vip.rechargeNum++;
			vip.ticket += rechargeNum;
			vip.totalTicket += rechargeNum;
			totalGiveNum = RechargeRateService.getGiveTicket(rechargeId);
			vip.lastRechargeTime = DateUtils.getNowInSecondes();
			vip.giveTicket += totalGiveNum;
			vip.totalGive += totalGiveNum;
			

			VipConfig vipConfig = VipService.get(vip.totalTicket);
			int currentVipLevel = vip.vipLevel;
			int targetVipLevel = 0;
			if (vipConfig != null){
				targetVipLevel = vipConfig.vipLevel;
				if (vip.vipLevel < vipConfig.vipLevel){ // 有vip变更才会更改vip等级并抛出vip等级变更事件
					vip.vipLevel = vipConfig.vipLevel;
					isPost = true;
				}
			}
			
			result = vipDao.updata(vip);
			if (result){ //充值成功处理
				if (isPost){
					eventBus.post(new VipLevelChangeEvent(actorId, vip.vipLevel));
					ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIP_LEVEL, vip.vipLevel);
				}
				if (vipConfig != null){
					sendRewardByVipLevel(actorId, currentVipLevel, targetVipLevel);
				}
				eventBus.post(new RechargeTicketsEvent(actorId, vip.totalTicket, money, rechargeNum,rechargeId));
				ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.TICKET, vip.ticket + vip.giveTicket);
				ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.TOTAL_TICKET, vip.totalTicket);
				
				LOGGER.info(String.format("充值成功, actorId:[%s], 充入[%s]点券，赠送[%s]点券, 剩余点券:[%s],rechargeId:[%s],总充入点券：[%s]", actorId, rechargeNum, totalGiveNum, 
						vip.ticket + vip.giveTicket, rechargeId, vip.totalTicket));
				
			} else { //失败处理
				LOGGER.error(String.format("充值数据库更新失败, actorId:[%s]", actorId));
			}
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		if(result) {
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.ticketAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, TicketAddType.RECHARGE, rechargeNum,
					totalGiveNum);
			
			//首次充值记录一条日志
			if(vip.rechargeNum == 1) {
				//需要记录当时的玩家数据是：掌教等级  故事当前的关卡 当前活力 最大活力  当前精力  最大精力 ,  上阵的仙人ID及仙人的等级  当时玩家在阵型中所穿戴的武器，防具，饰品及这些物品的等级
				int lastBattleId = storyFacade.get(actorId).getBattleId();
				String lineupHeroString = LineupHelper.getOssLineupHeroString(actorId);
				String lineupEquipString = LineupHelper.getOssLineupEquipString(actorId);
				GameOssLogger.firstRecharge(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, actor.level, actor.vit,
						actor.maxVit, actor.energy, actor.maxEnergy, rechargeNum, totalGiveNum, lastBattleId, lineupHeroString, lineupEquipString,rechargeId);
			}
		}
		
		return result;
	}
	
	
	@Override
	public boolean addTicket(long actorId, TicketAddType addType, int giveNum) {
		if (giveNum <= 0){
			LOGGER.error(String.format("赠送数值错误， addType:[%s] giveNum:[%s]", addType.getId(), giveNum));
			return false;
		}
		
		boolean result = false;
		Vip vip = vipDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vip);
		try{
			lock.lock();
			vip.giveTicket += giveNum;
			vip.totalGive += giveNum;
			ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.TICKET, vip.ticket + vip.giveTicket);
			result = vipDao.updata(vip);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		if(result) {
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.ticketAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, addType, 0, giveNum);
		}
		return result;
	}
	
	
	@Override
	public boolean decreaseTicket(long actorId, TicketDecreaseType decreaseType, int ticketNum, int id, int num) {
		if (ticketNum < 0){
			LOGGER.error(String.format("点券数值错误， ticketNum:[%s]", ticketNum));
			return false;
		}
		
		boolean result = false;
		//oss记录本次消耗赠送和充值的点券详情
		int ossGiveNum = 0; // 消耗赠送的数量
		int ossRechargeNum = 0; // 消耗充值的数量
		
		Vip vip = vipDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vip);
		try {
			lock.lock();
			int totalNum = vip.ticket + vip.giveTicket;
			if (totalNum < ticketNum){
				return false;
			}
						
			// TODO 是先扣除赠送的点券还是先扣除充值的点券需要确定
			if (ticketNum <= vip.giveTicket){
				vip.giveTicket -= ticketNum;
				
				ossGiveNum = ticketNum;
			} else {
				int decrease = ticketNum - vip.giveTicket;
				ossGiveNum = vip.giveTicket;
				vip.giveTicket = 0;
				vip.ticket -= decrease;
				
				ossRechargeNum = decrease;
			}
			
			//计算角色累计的消耗点券数
			int totalDecrease = (vip.totalTicket + vip.totalGive) - (vip.ticket + vip.giveTicket);
			totalDecrease = totalDecrease > 0 ? totalDecrease : 0;
			
			eventBus.post(new UseTicketsEvent(actorId, totalDecrease));// 消耗点券抛出事件
			ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.TICKET, vip.ticket + vip.giveTicket);
			result = vipDao.updata(vip);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		if (result) {
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.ticketDecrease(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, decreaseType, ossRechargeNum,
					ossGiveNum, id, num);
		}
		
		return result;
	}
	
	@Override
	public int getVipLevel(long actorId) {
		Vip vip = vipDao.get(actorId);
		if (vip != null){
			return vip.vipLevel;
		}
		return 0;
	}
	
	@Override
	public VipPrivilege getVipPrivilege(int vipLevel) {
		return vipPrivileges.get(vipLevel);
	}
	
	@Override
	public int getTicket(long actorId) {
		Vip vip = vipDao.get(actorId);
		return vip.giveTicket + vip.ticket;
	}
	
	@Override
	public int getTotalRechargeTicket(long actorId) {
		Vip vip = vipDao.get(actorId);
		return vip.totalTicket;
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		if (paramEvent instanceof VipLevelChangeEvent){
			VipLevelChangeEvent vipLevelChangeEvent = paramEvent.convert();
			if (vipLevelChangeEvent.vipLevel >= Vip3Privilege.vipLevel){
//				Vip3Privilege vip3Privilege = (Vip3Privilege) getVipPrivilege(Vip3Privilege.vipLevel);
//				actorFacade.addMorale(vipLevelChangeEvent.actorId, vip3Privilege.morale);
			}
		}
	}
	
	
	/**
	 * 发放达到VIP等级奖励
	 * @param actorId
	 * @param vipConfig
	 */
	@Override
	public void sendRewardByVipLevel(long actorId, int currentLevel, int targetLevel) {
		if (currentLevel < targetLevel){
			for (int i = currentLevel + 1; i <= targetLevel; i++) {
				VipConfig vipConfig = VipService.getByLevel(i);
				Set<Integer> equipList = vipConfig.getGiveEquipList();
				Set<Integer> heroList = vipConfig.getGiveHerosList();
				Map<Integer, Integer> goods = vipConfig.getGiveGoodsMap();
				sendReward(actorId, equipList, heroList, goods, vipConfig.giveGold);
			}
		}
	}
	
	
	@Override
	public void updateVipLevel(long actorId, int level) {
		Vip vip = vipDao.get(actorId);
		if (vip.vipLevel < level) {
			eventBus.post(new VipLevelChangeEvent(actorId, level));
		}
		vip.vipLevel = level >= 0 ? level : vip.vipLevel;
		vipDao.updata(vip);
	}
	
	@Override
	public boolean hasEnoughTicket(long actorId, int tickitNum) {
		int ticket = getTicket(actorId);
		if (ticket >= tickitNum) {
			return true;
		}
		return false;
	}
	
	
	// -----------------------------------------private-----------------------------
	/**
	 * 发放奖励
	 * @param actorId
	 * @param equipList
	 * @param heroList
	 * @param goods
	 * @param gold
	 */
	private void sendReward(long actorId, Set<Integer> equipList, Set<Integer> heroList, Map<Integer, Integer> goods, int gold) {
		equipFacade.addEquip(actorId, EquipAddType.VIP_GIVE, equipList);
		heroFacade.addHero(actorId, HeroAddType.VIP_GIVE, heroList);
		goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_GIVE, goods);

		if (gold > 0) {
			actorFacade.addGold(actorId, GoldAddType.RECHARGE, gold);
		}
	}

	@Override
	public int getRechargeNum(long actorId) {
		Vip vip = vipDao.get(actorId);
		return vip.rechargeNum;
	}
	
	@Override
	public int getFirstRechargeTime(long actorId) {
		Vip vip = vipDao.get(actorId);
		return vip.firstRechargeTime;
	}
	
	private void sendReward(long actorId,List<RewardObject> rewardList) {
		for(RewardObject reward:rewardList){
			switch (reward.rewardType) {
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.FIRST_RECHARGE, reward.id, reward.num);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.FIRST_RECHARGE, reward.id, reward.num);
				break;
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.FIRST_RECHARGE, reward.id);
				break;
			case HERO:
				heroFacade.addHero(actorId, HeroAddType.FIRST_RECHARGE, reward.id);
			default:
				break;
			}
		}
	}

}
