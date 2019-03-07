package com.jtang.gameserver.module.user.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_NAME_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.CREATE_ACTOR_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.DENY_CREATE_ACTOR;
import static com.jiatang.common.GameStatusCodeConstant.ENERGY_FULL_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.SELECT_HERO_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.VIT_FULL_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.ActorBuyGoldEvent;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.ContinueLoginEvent;
import com.jtang.gameserver.component.event.EnergyLimitEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.MoraleIncreaseEvent;
import com.jtang.gameserver.component.event.VipLevelChangeEvent;
import com.jtang.gameserver.component.event.VitLimitEvent;
import com.jtang.gameserver.component.helper.EmojiFilter;
import com.jtang.gameserver.component.helper.NameHelper;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.listener.ActorLogoutListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.ActorUpgradeConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.GmService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Icon;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.dbproxy.entity.Online;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.dao.HeroDao;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.icon.dao.IconDao;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.lineup.dao.LineupDao;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.user.constant.ActorRule;
import com.jtang.gameserver.module.user.dao.ActorDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.OnlineFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.handler.response.ActorBuyResponse;
import com.jtang.gameserver.module.user.helper.ActorPushHelper;
import com.jtang.gameserver.module.user.model.ActorInfo;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;
import com.jtang.gameserver.module.user.type.ActorBuyType;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.EnergyDecreaseType;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.module.user.type.VITAddType;
import com.jtang.gameserver.module.user.type.VITDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 
 * @author 0x737263
 * 
 */
@Component
public class ActorFacadeImpl implements ActorFacade, ActorLoginListener, ActorLogoutListener, ApplicationListener<ContextRefreshedEvent>,ZeroListener,Receiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActorFacadeImpl.class);
	@Autowired
	ActorDao actorDao;
	@Autowired
	GoodsFacade goodsFacade;
	@Autowired
	EquipFacade equipFacade;
	@Autowired
	EventBus eventBus;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	HeroSoulFacade heroSoulFacade;
	@Autowired
	OnlineFacade onlineFacade;
	@Autowired
	private RecruitFacade recruitFacade;
	@Autowired
	private HeroDao heroDao;
	@Autowired
	private LineupDao lineupDao;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private IconDao iconDao;

	/**
	 * LRU缓存角色名
	 */
	private static ConcurrentLinkedHashMap<String, Byte> ACTOR_NAME_CACHE = new ConcurrentLinkedHashMap.Builder<String, Byte>()
			.maximumWeightedCapacity(Short.MAX_VALUE).build();
	
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.VIP_LEVEL_CHANGE, this);
	}
	
	@Override
	public void onLogin(long actorId) {
		Actor entity = getActor(actorId);
		
		//计算连续登陆天数
		if (entity.calculateContinueDays()) {
			// 连续登陆天数有变化，则抛事件
			eventBus.post(new ContinueLoginEvent(actorId, entity.continueDays));
		}
		
		if(entity.loginTimeOSS == 0){
			entity.loginTimeOSS = TimeUtils.getNow();
		}
		
//		fixedTimeEnergy(actorId);
		fixedTimeVIT(actorId);
		
		//重置精力、活力补满次数
		reset(actorId);
		
		actorDao.updateActor(entity);
	}
	
	@Override
	public void onLogout(long actorId) {
		Actor entity = getActor(actorId);
		// 设置退出时间
		entity.setLogoutTime();
		// 计算上下线累计时间
		entity.sumUpgradeTime();
		actorDao.updateActor(entity);

		// 移除在线
		Online online = onlineFacade.remove(actorId);
		GameOssLogger.actorLogout(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, entity.loginTimeOSS, entity.logoutTime,
				online.sim, online.mac, online.imei, online.ip);
		
		entity.loginTimeOSS = 0;
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		//每秒定时补活力调度
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				Set<Long> actors = playerSession.onlineActorList();
				for (Long actorId : actors) {
					fixedTimeVIT(actorId);
				}
			}
		}, 1);
		
		//每秒定时补精力调度
//		schedule.addEverySecond(new Runnable() {
//			@Override
//			public void run() {
//				Set<Long> actors = playerSession.onlineActorList();
//				for (Long actorId : actors) {
//					fixedTimeEnergy(actorId);
//				}
//			}
//		}, 1);
	}
	
	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for(long actorId:actorIds){
			reset(actorId);
			fullEnergy(actorId, EnergyAddType.FIXTIME);
		}
	}

	@Override
	public Actor getActorId(int platformId, String uid, int serverId,long actorId) {
		return actorDao.getActorId(platformId, uid, serverId, actorId);
	}
	
	@Override
	public List<ActorInfo> getActorId(int platformId, String uid, int serverId) {
		List<Actor> actors =  actorDao.getActorId(platformId, uid, serverId);
		List<ActorInfo> list = new ArrayList<>();
		for (Actor actor : actors) {
			Icon icon = iconDao.get(actor.getPkId());
			IconVO iconVO = new IconVO(icon.icon,icon.fram,icon.sex);
			int vipLevel = vipFacade.getVipLevel(actor.getPkId());
			ActorInfo actorInfo = new ActorInfo(actor.getPkId(), actor.actorName, actor.logoutTime,actor.level,vipLevel,iconVO);
			list.add(actorInfo);
		}
		return list;
	}

	@Override
	public Actor getActor(long actorId) {
		return actorDao.getActor(actorId);
	}

	@Override
	public boolean isExists(long actorId) {
		return getActor(actorId) == null ? false : true;
	}
	
	@Override
	public TResult<Long> createActor(int platformType, String uid, int channelId, int serverId, int heroId, String actorName, String ip, String sim,
			String mac, String imei) {
		actorName = org.springframework.util.StringUtils.trimAllWhitespace(actorName);
		TResult<Long> result = new TResult<Long>();
		List<Actor> list = actorDao.getActorId(platformType, uid, serverId);
		if (list.size() > 0) {
			result.statusCode = DENY_CREATE_ACTOR;
			return result;
		}

		HeroConfig heroConfig = HeroService.get(heroId);
		if (heroConfig == null) {
			result.statusCode = SELECT_HERO_ID_ERROR;
			return result;
		}
		if (NameHelper.validateActorName(actorName) == false || EmojiFilter.containsEmoji(actorName) || ACTOR_NAME_CACHE.containsKey(actorName)) {
			result.statusCode = ACTOR_NAME_EXISTS;
			return result;
		}

		long actorId = actorDao.getActorId(actorName);
		if (actorId > 0) {
			result.statusCode = ACTOR_NAME_EXISTS;
			return result;
		}

		result.item = actorDao.createActor(platformType, uid, channelId, serverId, heroId, actorName, ip, sim, mac, imei);
		if(result.isOk() && result.item > 1) {
//			Heros heros = heroDao.get(result.item);
//			HeroVO herovo = Heros.createHeroVO( HeroService.get(heroId));
//			heros.addHeroVO(herovo);
//			heroDao.update(heros);
			heroFacade.addHero(result.item, HeroAddType.CREATE_ACTOR, heroId);
			Lineup lineup = lineupDao.getLineup(result.item);
			lineup.assignHero(heroId, 1, 2);
			lineupDao.updateLineup(lineup);
			
			
			ACTOR_NAME_CACHE.putIfAbsent(actorName, (byte) 0);
			
			//看是否配置了礼包物品id，并添加进仓库
			int goodsId = ActorRule.ACTOR_CREATE_GIVE_GOODS_ID;
			if (goodsId > 0) {
				goodsFacade.addGoodsVO(result.item, GoodsAddType.LOGIN_GIVE, goodsId, 1);
			}
			recruitFacade.init(result.item);
			
		} else {
			result.statusCode = CREATE_ACTOR_FAIL;
		}

		return result;
	}

	@Override
	public boolean addReputation(long actorId, ReputationAddType type, long reputationNum) {
		if (reputationNum < 1) {
			return false;
		}

		Map<ActorAttributeKey, Object> pushAttributes = new HashMap<>();
		Actor entity = getActor(actorId);
		int oldLevel = entity.level; // 当前的旧等级
		
		//当角色等级达到最大配置可升等级,不增加任何经验.
		if(oldLevel >= ActorService.maxLevel()){
			return false;
		}
		
		long oldReputation = entity.reputation;	//当前的声望值
		entity.reputation += reputationNum;  //总计声望
		
		// 判断可升几级
		int ableUpgradeNum = ActorService.getAbleUpgrades(oldLevel, entity.reputation);
		if (ableUpgradeNum > 0) {
			int totalLevel = oldLevel + ableUpgradeNum;
			for (int i = oldLevel; i < totalLevel; i++) {
				ActorUpgradeConfig config = ActorService.getUpgradeConfig(i);
				entity.level += 1;
				entity.reputation -= config.getNeedReputation();
				config = ActorService.getUpgradeConfig(entity.level);
				addVITLimit(actorId, config.getAddVitMax());

				// 推送升级后添加的物品
				List<RewardObject> reward = ActorService.getRewardList(entity.level);
				sendReward(actorId,reward);
//				for (Map.Entry<Integer, Integer> entry : goods.entrySet()) {
//					goodsFacade.addGoodsVO(actorId, GoodsAddType.ACTOR_UPGRADE, entry.getKey(), entry.getValue());
//				}
			}
			
			fullVIT(actorId, VITAddType.ACTOR_UPGRADE);
			//fullEnergy(actorId, EnergyAddType.ACTOR_UPGRADE);
			
			pushAttributes.put(ActorAttributeKey.LEVEL, entity.level);
			pushAttributes.put(ActorAttributeKey.REPUTATION, entity.reputation);
			pushAttributes.put(ActorAttributeKey.VIT, entity.vit);
			pushAttributes.put(ActorAttributeKey.ENERGY, entity.energy);
			pushAttributes.put(ActorAttributeKey.MAXVIT, entity.maxVit);

			eventBus.post(new ActorLevelUpEvent(entity, oldLevel));

			GameOssLogger.actorUpgrade(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, oldLevel, entity.level,
					entity.upgradeTime);
			entity.cleanUpgradeTime();
			
		} else {
			pushAttributes.put(ActorAttributeKey.REPUTATION, entity.reputation);	
		}
		
		GameOssLogger.reputationAdd(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, oldReputation, reputationNum);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttributeList(actorId, pushAttributes);
		
		return true;
	}
	
	@Override
	public boolean hasGold(long actorId, int goldNum) {
		if (goldNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);
		if(entity == null) {
			return false;
		}
		
		if(entity.gold < goldNum) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean addGold(long actorId, GoldAddType type, long goldNum) {
		if (goldNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			entity.gold += goldNum;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.GOLD, entity.gold);
		GameOssLogger.goldAdd(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, goldNum, entity.gold);
		
		return true;
	}

	@Override
	public boolean decreaseGold(long actorId,GoldDecreaseType type, int goldNum) {
		if(goldNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if (entity.gold < goldNum) {
				return false;
			}
			entity.gold -= goldNum;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.GOLD, entity.gold);
		GameOssLogger.goldDecrease(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, goldNum, entity.gold);
		return true;
	}

	@Override
	public boolean addEnergy(long actorId, EnergyAddType energyAddType, int energyNum) {
		if (energyNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);	
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if(ActorService.isEnergy(energyAddType)){
				entity.energy += energyNum;
				entity.energy = Math.min(entity.energy, ActorRule.ACTOR_ENERGY_SUPER_MAX);
			}else{
				if(entity.energy >= entity.maxEnergy){
					return false;
				}
				entity.energy = Math.min(entity.energy + energyNum, entity.maxEnergy);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		GameOssLogger.energyAdd(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, energyAddType, energyNum);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.ENERGY, entity.energy);
		return true;
	}

	@Override
	public boolean fullEnergy(long actorId, EnergyAddType energyAddType) {
		Actor entity = getActor(actorId);
		return addEnergy(actorId, energyAddType, entity.maxEnergy);
	}
	
	@Override
	public boolean addEnergyLimit(long actorId, int energyMaxNum) {
		if (energyMaxNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);		
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			entity.maxEnergy += energyMaxNum;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		entity.maxEnergy = Math.min(ActorRule.ACTOR_ENERGY_SUPER_MAX, entity.maxEnergy);

		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.MAXENERGY, entity.maxEnergy);
		
		eventBus.post(new EnergyLimitEvent(actorId, entity.maxEnergy));
		
		return true;
	}

	@Override
	public boolean decreaseEnergy(long actorId, EnergyDecreaseType type, int energyNum) {
		if(energyNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);	
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if (entity.energy < energyNum) {
				return false;
			}
			
			if (entity.energy == entity.maxEnergy || entity.energy - energyNum < entity.maxEnergy){
				entity.lastAddEnergyTime = TimeUtils.getNow();
			}
			entity.energy -= energyNum;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		
		GameOssLogger.energyDecrease(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, energyNum);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.ENERGY, entity.energy);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.ENERGY_COUNT_DOWN, entity.getEnergyCountdown());
		return true;
	}

	@Override
	public boolean addVIT(long actorId,VITAddType type, int vitNums) {
		if (vitNums < 1) {
			return false;
		}
		Actor entity = getActor(actorId);
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if(ActorService.isVit(type)){
				entity.vit += vitNums;
				entity.vit = Math.min(entity.vit, ActorRule.ACTOR_VIT_SUPER_MAX);
			}else{
				if(entity.vit >= entity.maxVit){
					return false;
				}
				entity.vit = Math.min(entity.vit + vitNums, entity.maxVit);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		GameOssLogger.vitAdd(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, vitNums);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIT, entity.vit);
		return true;
	}

	@Override
	public boolean fullVIT(long actorId, VITAddType type) {
		Actor entity = getActor(actorId);
		return addVIT(actorId, type, entity.maxVit);
	}
	
	@Override
	public boolean addVITLimit(long actorId, int vitLimitNum) {
		if (vitLimitNum < 1) {
			return false;
		}
		
		Actor entity = getActor(actorId);		
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			entity.maxVit += vitLimitNum;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		entity.maxVit = Math.min(entity.maxVit, ActorRule.ACTOR_VIT_SUPER_MAX);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.MAXVIT, entity.maxVit);
		
		eventBus.post(new VitLimitEvent(actorId, entity.maxVit));
		
		return true;
	}

	@Override
	public boolean decreaseVIT(long actorId, VITDecreaseType type, int vitNums) {
		if(vitNums < 1) {
			return false;
		}
		Actor entity = getActor(actorId);		
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if (entity.vit < vitNums) {
				return false;
			}
			if (entity.vit == entity.maxVit || entity.vit - vitNums < entity.maxVit){
				entity.lastAddVitTime = TimeUtils.getNow();
			}
			entity.vit -= vitNums;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		GameOssLogger.vitDecrease(entity.uid, entity.platformType, entity.channelId, entity.serverId, actorId, type, vitNums);
		actorDao.updateActor(entity);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIT, entity.vit);
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIT_COUNT_DOWN, entity.getVitCountdown());
		return true;
	}
	
	@Override
	public void addMorale(long actorId, int morale) {
		Actor entity = getActor(actorId);

		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			entity.morale += morale;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		actorDao.updateActor(entity);
		eventBus.post(new MoraleIncreaseEvent(actorId, entity.morale));
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.MORALE, entity.morale);
	}
	
	@Override
	public int costMorale(long actorId, int morale) {
		Actor entity = getActor(actorId);

		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			if(entity.morale - morale < 0){
				return 0;
			}else{
				entity.morale -= morale;
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		actorDao.updateActor(entity);
		eventBus.post(new MoraleIncreaseEvent(actorId, entity.morale));
		ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.MORALE, entity.morale);
		return morale;
	}

	@Override
	public void saveGuideStep(long actorId, int key, int value) {
		if (key > ActorRule.ACTOR_GUIDE_MAX_KEY || value > ActorRule.ACTOR_GUIDE_MAX_VALUE) {
			return;
		}
		Actor entity = getActor(actorId);
		entity.addGuide(key, value);
		actorDao.updateActor(entity);
	}
		
	/**
	 * 定时补满活力
	 * @param actorId
	 */
	private void fixedTimeVIT(long actorId) {	
		Actor actor = getActor(actorId);
		ChainLock lock = LockUtils.getLock(actor);
		try {
			lock.lock();
		
			if (actor.vit >= actor.maxVit) {
				return;
			}
		
			if (actor.getVitCountdown() == 0) {
	//			int vipLevel = vipFacade.getVipLevel(actorId);
				int addValue = ActorRule.ACTOR_X_MIN_ADD_VIT;
	//			if (vipLevel >= Vip1Privilege.vipLevel){
	//				Vip1Privilege vip1Privilege = (Vip1Privilege) vipFacade.getVipPrivilege(Vip1Privilege.vipLevel);
	//				addValue = ActorRule.ACTOR_X_MIN_ADD_VIT * vip1Privilege.recoveryNum;
	//			}
				
				int vitNum = ((TimeUtils.getNow() - actor.lastAddVitTime) / ActorRule.ACTOR_VIT_FIXED_TIME) * addValue;
				if(vitNum > 0) {
					actor.lastAddVitTime = TimeUtils.getNow();
					addVIT(actorId, VITAddType.FIXTIME, vitNum);
					actorDao.updateActor(actor);
					ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.VIT_COUNT_DOWN, actor.getVitCountdown());			
				}
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 定时补满精力
	 * @param actorId
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void fixedTimeEnergy(long actorId) {
		Actor actor = getActor(actorId);
		if (actor.energy >= actor.maxEnergy) {
			return;
		}

		if (actor.getEnergyCountdown() == 0) {
//			int vipLevel = vipFacade.getVipLevel(actorId);
			int addValue = ActorRule.ACTOR_X_MIN_ADD_ENERGY;
//			if (vipLevel >= Vip1Privilege.vipLevel) {
//				Vip1Privilege vip1Privilege = (Vip1Privilege) vipFacade.getVipPrivilege(Vip1Privilege.vipLevel);
//				addValue = ActorRule.ACTOR_X_MIN_ADD_ENERGY * vip1Privilege.recoveryNum;
//			}

			int energyNum = ((TimeUtils.getNow() - actor.lastAddEnergyTime) / ActorRule.ACTOR_ENERGY_FIXED_TIME) * addValue;
			if(energyNum > 0) {
				actor.lastAddEnergyTime = TimeUtils.getNow();
				addEnergy(actorId, EnergyAddType.FIXTIME, energyNum);
				actorDao.updateActor(actor);
				ActorPushHelper.pushAttribute(actorId, ActorAttributeKey.ENERGY_COUNT_DOWN, actor.getEnergyCountdown());				
			}
		}
	}
	
	@Override
	public void savePushKey(long actorId, int type, String pushKey) {
		if(type < 1 || type > 2) {
			return;
		}
		
		if (StringUtils.isBlank(pushKey) || pushKey.length() > ActorRule.ACTOR_PUSH_KEY_LENGTH) {
			return;
		}

		Actor entity = getActor(actorId);
		if (type == 1) {
			entity.androidPushkey = pushKey;
		} else if (type == 2) {
			entity.iosPushKey = pushKey;
		}
		actorDao.updateActor(entity);
	}

	@Override
	public Result rename(long actorId, String actorName) {
		if(GmService.isGm(actorId)){
			return Result.valueOf(ACTOR_NAME_EXISTS);
		}
		actorName = org.springframework.util.StringUtils.trimAllWhitespace(actorName);
		if (NameHelper.validateActorName(actorName) == false || EmojiFilter.containsEmoji(actorName) || ACTOR_NAME_CACHE.containsKey(actorName)) {
			return Result.valueOf(ACTOR_NAME_EXISTS);
		}
		long targetActorId = actorDao.getActorId(actorName);
		if (targetActorId > 0) {
			return Result.valueOf(ACTOR_NAME_EXISTS);
		}
		Actor actor = actorDao.getActor(actorId);
		if(actor.renameNum > 0){//首次修改名称免费
			int costTicket = FormulaHelper.executeCeilInt(ActorRule.RENAME_COST_TICKET, actor.renameNum);
			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTOR_RENAME, costTicket, 0, 0);
			if(isOk == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
//			return Result.valueOf();
		}
		ACTOR_NAME_CACHE.putIfAbsent(actorName, (byte) 0);
		actor.actorName = actorName;
		actor.renameNum += 1;
		actorDao.updateActor(actor);
		ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
		return Result.valueOf();
	}

	@Override
	public Result costTicketFullEnergy(long actorId) {
		Actor actor = actorDao.getActor(actorId);
		if(isAddEnergy(actorId, EnergyAddType.FULL_ENERGY) == false){
			return Result.valueOf(ENERGY_FULL_ERROR);
		}
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vip = vipFacade.getVipPrivilege(vipLevel);
		if(vip == null){
			return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		if(actor.energyNum >= vip.energyNum && vip.energyNum != -1){
			return Result.valueOf(GameStatusCodeConstant.ENERGY_BUY_MAX);
		}
		int costTicket = ActorService.getTicket(ActorBuyType.ENERGY.getCode(), actor.energyNum,vip.energyNum);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.FULL_ENERGY, costTicket, 0, 0);
		if(isOk == false){
			return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		actor.energyNum += 1;
		actor.operationTime = TimeUtils.getNow();
		actorDao.updateActor(actor);
		fullEnergy(actorId, EnergyAddType.FULL_ENERGY);
		ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
		return Result.valueOf();
	}

	@Override
	public Result costTicketFullVit(long actorId) {
		Actor actor = actorDao.getActor(actorId);
		if(isAddVit(actorId, VITAddType.FULL_VIT) == false){
			return Result.valueOf(VIT_FULL_ERROR);
		}
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vip = vipFacade.getVipPrivilege(vipLevel);
		if(vip == null){
			return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		if(actor.vitNum >= vip.vitNum && vip.vitNum != -1){
			return Result.valueOf(GameStatusCodeConstant.VIT_BUY_MAX);
		}
		int costTicket = ActorService.getTicket(ActorBuyType.VIT.getCode(), actor.vitNum,vip.vitNum);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.FULL_VIT, costTicket, 0, 0);
		if(isOk == false){
			return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		actor.vitNum += 1;
		actor.operationTime = TimeUtils.getNow();
		actorDao.updateActor(actor);
		fullVIT(actorId, VITAddType.FULL_VIT);
		ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
		return Result.valueOf();
	}
	
	@Override
	public Result costTicketBuyGold(long actorId) {
		Actor actor = actorDao.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vip = vipFacade.getVipPrivilege(vipLevel);
		if(vip == null){
			return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		if(actor.goldNum >= vip.goldNum && vip.goldNum != -1){
			return Result.valueOf(GameStatusCodeConstant.GOLD_BUY_MAX);
		}
		int costTicket = ActorService.getTicket(ActorBuyType.GOLD.getCode(), actor.goldNum,vip.goldNum);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.BUY_GOLD, costTicket, 0, 0);
		if(isOk == false){
			return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		actor.goldNum += 1;
		actor.operationTime = TimeUtils.getNow();
		addGold(actorId, GoldAddType.GOLD_BUY, ActorRule.BUY_GOLD_NUM);
		//goodsFacade.addGoodsVO(actorId, GoodsAddType.GOLD_BUY,ActorRule.BUY_GOLD_ID,1);
		actorDao.updateActor(actor);
		//抛出事件
		eventBus.post(new ActorBuyGoldEvent(actorId, actor.goldNum));
		ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
		return Result.valueOf();
	}
	
	@Override
	public boolean isAddEnergy(long actorId,EnergyAddType type) {
		Actor actor = actorDao.getActor(actorId);
		if(actor.energy >= ActorRule.ACTOR_ENERGY_SUPER_MAX){//精力值超过最大上限的时候
			return false;
		}else if(actor.energy >= actor.maxEnergy){//精力值超过上限的时候
			return ActorService.isEnergy(type);
		}else{
			return actor.energy < ActorRule.ACTOR_ENERGY_SUPER_MAX;
		}
	}

	@Override
	public boolean isAddVit(long actorId,VITAddType type) {
		Actor actor = actorDao.getActor(actorId);
		if(actor.vit >= ActorRule.ACTOR_VIT_SUPER_MAX){//活力值超过最大上限的时候
			return false;
		}else if(actor.vit >= actor.maxVit){//活力值超过上限的时候
			return ActorService.isVit(type);
		}else{
			return actor.vit < ActorRule.ACTOR_VIT_SUPER_MAX;
		}
	}

	@Override
	public TResult<ActorBuyResponse> getActorBuy(long actorId) {
		Actor actor = actorDao.getActor(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		int vitMaxNum = 0;
		int energyMaxNum = 0;
		int goldMaxNum = 0;
		if(vipPrivilege == null){
			vitMaxNum = 0;
			energyMaxNum = 0;
			goldMaxNum = 0;
		}else{
			vitMaxNum = vipPrivilege.vitNum;
			energyMaxNum = vipPrivilege.energyNum;
			goldMaxNum = vipPrivilege.goldNum;
		}
		ActorBuyResponse response = new ActorBuyResponse();
		response.vitNum = actor.vitNum;
		if(actor.vitNum == vitMaxNum){
			response.vitCostTicket = 0;
		}else{
			response.vitCostTicket = ActorService.getTicket(ActorBuyType.VIT.getCode(), actor.vitNum,vitMaxNum);
		}
		response.vitMaxNum = vitMaxNum;
		response.energyNum = actor.energyNum;
		if(actor.energyNum == energyMaxNum){
			response.energyCostTicket = 0;
		}else{
			response.energyCostTicket = ActorService.getTicket(ActorBuyType.ENERGY.getCode(), actor.energyNum,energyMaxNum);
		}
		response.energyMaxNum = energyMaxNum;
		response.goldNum = actor.goldNum;
		if(actor.goldNum == goldMaxNum){
			response.goldCostTicket = 0;
		}else{
			response.goldCostTicket = ActorService.getTicket(ActorBuyType.GOLD.getCode(), actor.goldNum,goldMaxNum);
		}
		if(actor.renameNum > 0){//首次修改名称免费
			int costTicket = FormulaHelper.executeCeilInt(ActorRule.RENAME_COST_TICKET, actor.renameNum);
			response.renameTicket = costTicket;
		}else{
			response.renameTicket = 0;
		}
		Icon icon = iconDao.get(actorId);
		if(icon.modifySexNum > 0){//首次修改名称免费
			int costTicket = FormulaHelper.executeCeilInt(ActorRule.RESEX_COST_TICKET, icon.modifySexNum);
			response.resexTicket = costTicket;
		}else{
			response.resexTicket = 0;
		}
		response.goldMaxNum = goldMaxNum;
		return TResult.sucess(response);
	}
	
	private void reset(long actorId) {
		Actor actor = actorDao.getActor(actorId);
		ChainLock lock = LockUtils.getLock(actor);
		try{
			lock.lock();
			if(DateUtils.isToday(actor.operationTime) == false){
				actor.energyNum = 0;
				actor.vitNum = 0;
				actor.goldNum = 0;
				actor.operationTime = TimeUtils.getNow();
				int value = actor.maxEnergy - actor.energy;
				if (value > 0) {
					addEnergy(actorId, EnergyAddType.FIXTIME, value);
				}
				actorDao.updateActor(actor);
				ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		VipLevelChangeEvent vipLevelEvent = paramEvent.convert();
		long actorId = vipLevelEvent.actorId;
		ActorPushHelper.pushActorBuyInfo(actorId,getActorBuy(actorId).item);
	}
	
	private void sendReward(long actorId, List<RewardObject> reward) {
		for(RewardObject rewardObject : reward){
			switch(rewardObject.rewardType){
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.ACTOR_UP_LEVEL, rewardObject.id);
				break;
			case GOLD:
				addGold(actorId, GoldAddType.ACTOR_UP_LEVEL, rewardObject.num);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.ACTOR_UPGRADE, rewardObject.id,rewardObject.num);
				break;
			case HERO:
				heroFacade.addHero(actorId, HeroAddType.ACTOR_UP_LEVEL, rewardObject.id);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.ACTOR_UP_LEVEL, rewardObject.id, rewardObject.num);
				break;
			case TICKET:
				vipFacade.addTicket(actorId, TicketAddType.ACTOR_UP_LEVEL, rewardObject.num);
				break;
			default:
				break;
			}
		}
	}

}
