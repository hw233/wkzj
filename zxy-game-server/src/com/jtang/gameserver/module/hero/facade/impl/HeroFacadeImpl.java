package com.jtang.gameserver.module.hero.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.BREAK_THROUGH_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.DELETE_HERO_IN_LINEUP;
import static com.jiatang.common.GameStatusCodeConstant.DELETE_HERO_IS_MAIN;
import static com.jiatang.common.GameStatusCodeConstant.HERO_CONFIG_NOT_EXITST;
import static com.jiatang.common.GameStatusCodeConstant.HERO_DEL_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.HERO_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.HERO_UPGRADE_EXP_VALUE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.TOO_MUCH_HEROS;
import static com.jiatang.common.GameStatusCodeConstant.BREAK_THROUGH_LIMIT;
import static com.jtang.core.protocol.StatusCode.OPERATION_ERROR;
import static com.jtang.core.protocol.StatusCode.SUCCESS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.AddHeroEvent;
import com.jtang.gameserver.component.event.HeroAttributeChangeEvent;
import com.jtang.gameserver.component.event.HeroLevelUpEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.BreakThroughConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroUpgradeConfig;
import com.jtang.gameserver.dataconfig.service.BreakThroughService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.HeroUpgradeService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Heros;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.type.BufferSourceType;
import com.jtang.gameserver.module.hero.constant.HeroRule;
import com.jtang.gameserver.module.hero.dao.HeroDao;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.hero.model.HeroUnit;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.skill.helper.HeroActivedSkillProcessor;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class HeroFacadeImpl implements HeroFacade {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroFacadeImpl.class);
	
	@Autowired
	HeroDao heroDao;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	private BufferFacade bufferFacade;
	
	@Autowired
	EventBus eventBus;
	
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private MainHeroFacade mainHeroFacade;
	
	@Autowired
	private LineupFacade lineupFacade;
	
	@Autowired
	private HeroActivedSkillProcessor activedSkillProcessor;
	
	@Override
	public TResult<HeroVO> addHero(long actorId,HeroAddType addType, int heroId) {
		if (heroId <= 0){
			return TResult.valueOf(HERO_EXISTS);
		}
		Heros heros = heroDao.get(actorId);
		HeroVO vo = heros.getHeroVO(heroId);
		if (vo != null) {
			return TResult.valueOf(HERO_EXISTS);
		}

		int count = heros.getHeroVOMap().size();
		if (count >= HeroRule.HERO_MAX_COUNT) {
			return TResult.valueOf(TOO_MUCH_HEROS);
		}

		HeroConfig conf = HeroService.get(heroId);
		if (conf == null) {
			return TResult.valueOf(HERO_CONFIG_NOT_EXITST);
		}

		// 插入
		HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, conf.getStar());
		int availableDelveCount = upgradeConfig.getUpgradeDelve();
//		int vipLevel = vipFacade.getVipLevel(actorId);
//		if (vipLevel >= Vip8Privilege.vipLevel) {
//			Vip8Privilege vip8Privilege = (Vip8Privilege) vipFacade.getVipPrivilege(Vip8Privilege.vipLevel);
//			availableDelveCount += vip8Privilege.addDelveNum;
//		}
		
		int atk = conf.getAttack();
		byte atkScope = (byte) conf.getAttackScope();
		int defense = conf.getDefense();
		int hp = conf.getHp();
		int skillId = conf.getAttackSkillId();
		vo = HeroVO.valueOf(atk, atkScope, defense, heroId, hp, skillId, availableDelveCount);
		heros.addHeroVO(vo);
		if(heros.heroIdList.contains(heroId) == false){
			heros.heroIdList.add(heroId);
		}
		heroDao.update(heros);

		// 推送
		int composeNum = getComposeNum(actorId);
		HeroPushHelper.pushAddHero(actorId, vo, composeNum);
		eventBus.post(new AddHeroEvent(actorId, heroId, addType, conf));
		activedSkillProcessor.recomputeHeroSkillAndBuffer(actorId, heroId);
		
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.heroAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, addType, heroId);
		return TResult.sucess(vo);
	}
	
	@Override
	public Result addHero(long actorId, HeroAddType addType, Set<Integer> heroIds) {
		Heros heros = heroDao.get(actorId);

		List<HeroVO> pushList = new ArrayList<>();
		List<AddHeroEvent> events = new ArrayList<AddHeroEvent>();
		
		for (int heroId : heroIds) {
			if (heroId < 1) {
				continue;
			}
			
			HeroConfig conf = HeroService.get(heroId);
			if (conf == null) {
				LOGGER.warn(String.format("hero id not in config actorid:[%s] addType:[%s] heroid:[%s]", actorId, addType.getId(), heroId));
				continue;
			}
			
			HeroVO vo = heros.getHeroVO(heroId);
			if (vo != null) {
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.ADMIN, heroId, conf.getRecruitSoulCount());
				LOGGER.warn(String.format("hero is exists. actorid:[%s] addType:[%s]  heroid:[%s]", actorId, addType.getId(), heroId));
				continue;
			}

			int count = heros.getHeroVOMap().size();
			if (count >= HeroRule.HERO_MAX_COUNT) {
				LOGGER.warn(String.format("add hero to max count.acrorid:[%s] heroid:[%s] ", actorId, heroId));
				continue;
			}
			
			HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, conf.getStar());
			int availableDelveCount = upgradeConfig.getUpgradeDelve();
			
			//vip atrribute add
//			int vipLevel = vipFacade.getVipLevel(actorId);
//			if (vipLevel >= Vip8Privilege.vipLevel){
//				Vip8Privilege vip8Privilege = (Vip8Privilege) vipFacade.getVipPrivilege(Vip8Privilege.vipLevel);
//				availableDelveCount += vip8Privilege.addDelveNum;
//			}
			
			int atk = conf.getAttack();
			int atkScope = conf.getAttackScope();
			int defense = conf.getDefense();
			int hp = conf.getHp();
			int skillId = conf.getAttackSkillId();
			
			vo = HeroVO.valueOf(atk, atkScope, defense, heroId, hp, skillId, availableDelveCount);
			
			pushList.add(vo);
			heros.addHeroVO(vo);
			if(heros.heroIdList.contains(heroId) == false){
				heros.heroIdList.add(heroId);
			}
			heroDao.update(heros);
			events.add(new AddHeroEvent(actorId, heroId, addType, conf));
			
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.heroAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, addType, heroId);
		}

		// 推送
		HeroPushHelper.pushAddheroList(actorId, pushList, heros.composeNum);
		for (AddHeroEvent addHeroEvent : events) {
			eventBus.post(addHeroEvent);
			activedSkillProcessor.recomputeHeroSkillAndBuffer(addHeroEvent.actorId, addHeroEvent.heroId);
		}
		return Result.valueOf();
	}

	@Override
	public boolean updateHero(long actorId, HeroVO hero) {
		Heros heros = heroDao.get(actorId);
		heros.updateHeroVO(hero);
		return heroDao.update(heros);
	}

	@Override
	public short removeHero(long actorId, HeroDecreaseType decreaseType, List<Integer> heroIds) {
		short canDelete = canDelete(actorId, heroIds);
		if (canDelete != SUCCESS ){
			return canDelete;
		}
		Heros heros = heroDao.get(actorId);
		ChainLock lock = LockUtils.getLock(heros);
		try {
			lock.lock();
			boolean result = heros.removeHeroVO(heroIds);
			if (result) {
				Actor actor = actorFacade.getActor(actorId);
				GameOssLogger.heroDecrease(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, decreaseType, heroIds);
			}
			result = heroDao.update(heros);
			if (result == false) {
				return HERO_DEL_FAIL;
			}
			
			HeroPushHelper.pushHeroRemove(actorId, heroIds);
			return SUCCESS;
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return OPERATION_ERROR;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isHeroExits(long actorId, int heroId) {
		HeroVO vo = getHero(actorId, heroId);
		return vo == null ? false : true;
	}

	@Override
	public List<Integer> getStarHeroIds(long actorId, int star) {
		List<Integer> heroList = new ArrayList<Integer>();
		Heros heros = heroDao.get(actorId);

		Map<Integer, HeroVO> heroMap = heros.getHeroVOMap();
		if (CollectionUtils.isEmpty(heroMap)) {
			return heroList;
		}

		for (HeroVO hero : heroMap.values()) {
			HeroConfig conf = HeroService.get(hero.getHeroId());
			if (conf.getStar() == star) {
				heroList.add(hero.getHeroId());
			}
		}
		return heroList;
	}
	
	@Override
	public Collection<HeroVO> getList(long actorId) {
		Heros heros = heroDao.get(actorId);
		return heros.getHeroVOMap().values();
	}

	@Override
	public HeroVO getHero(long actorId, int heroId) {
		Heros heros = heroDao.get(actorId);
		return heros.getHeroVO(heroId);
	}

	@Override
	public TResult<HeroUnit> soul2Hero(long actorId, int heroId) {
		//检查仙人是否已经存在
		boolean flag = isHeroExits(actorId, heroId);
		if (flag) {
			return TResult.valueOf(HERO_EXISTS);
		}
		
		//扣除魂魄数量
		HeroConfig heroConf = HeroService.get(heroId);
		if (heroConf == null) {
			return TResult.valueOf(HERO_NOT_FOUND);
		}
		Result result = heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.SOUL2HERO, heroId, heroConf.getRecruitSoulCount());
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}
		
		//推送魂魄数量
//		Integer count = heroSoulFacade.getSoul(actorId).getSoulCount(heroId);
//		HeroPushHelper.pushHeroSoul(actorId, heroId, count == null ? 0 : count);
		
		//添加仙人
		TResult<HeroVO> addResult = addHero(actorId, HeroAddType.SOUL_CONVERT, heroId);
		
		if (addResult.isFail()) {
			return TResult.valueOf(addResult.statusCode);
		}
		
		HeroUnit unit = new HeroUnit();
		unit.type = 1;
		unit.id = heroId;
		
		return TResult.sucess(unit);
	}

	@Override
	public Result breakThrough(long actorId, int heroId) {
		HeroVO hero = getHero(actorId, heroId);
		// 仙人不存在
		if (hero == null) {
			return Result.valueOf(HERO_EXISTS);
		}

		HeroConfig heroConfig = HeroService.get(hero.getHeroId());

		BreakThroughConfig breakThrConf = BreakThroughService.get(heroConfig.getStar(), hero.breakThroughCount + 1);
		
		int addDelve= 0;
		int addExp = 0;
		int soulNum = 0;
		int addAttack = 0;
		int addDefense = 0;
		int addHp = 0;
		//如果通过突破增加潜修次数的配置存在,则增加潜修次数,否则根据公式增加仙人经验
		if (breakThrConf != null) {
			addDelve = breakThrConf.getAddDelve();
			soulNum = breakThrConf.getSoulCount();
			addAttack = breakThrConf.attack;
			addDefense = breakThrConf.defense;
			addHp = breakThrConf.hp;
		} else {
//			int star = heroConfig.getStar();
//			int level = hero.getLevel();
//			soulNum = FormulaHelper.executeInt(HeroRule.HERO_BREAKOUT_NEED_SOUL_EXPR, star);
//			addExp = FormulaHelper.executeInt(HeroRule.HERO_BREAKOUT_OUTPUT_EXP_EXPR, star, level);
			return Result.valueOf(BREAK_THROUGH_LIMIT);
		}
		
		//掌教等级限制仙人突破次数
		int actorLevel = ActorHelper.getActorLevel(actorId);
		if(breakThrConf.level > actorLevel){
			return Result.valueOf(BREAK_THROUGH_LEVEL_NOT_ENOUGH);
		}

		// 扣除魂魄数
		Result result = heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.BREAK_THROUGH, heroId, soulNum);
		if (result.isFail()) {
			return result;
		}
		
		//更新数据库
		hero.breakThroughCount += 1;
		
		Set<HeroVOAttributeKey> updateAttrSet = new HashSet<>();
		//加潜修次数
		if (addDelve > 0) {
			hero.availableDelveCount += addDelve;
			updateAttrSet.add(HeroVOAttributeKey.AVAILABLE_DEVLE_COUNT);
		}
		//加仙人经验
		if (addExp > 0) {
			this.addHeroExp(actorId, heroId, addExp);
			updateAttrSet.add(HeroVOAttributeKey.EXP);
		}
		
		//加仙人属性
		if(addAttack>0||addDefense>0||addHp>0){
			if(addAttack > 0){
				hero.atk += addAttack;
				updateAttrSet.add(HeroVOAttributeKey.ATK);
			}
			if(addDefense > 0){
				hero.defense += addDefense;
				updateAttrSet.add(HeroVOAttributeKey.DEFENSE);
			}
			if(addHp > 0){
				hero.hp += addHp;
				updateAttrSet.add(HeroVOAttributeKey.HP);
			}
		}

		updateHero(actorId, hero);

		// 推送仙人信息更新和魂魄数量更新
		updateAttrSet.add(HeroVOAttributeKey.BREAK_THROUGH_COUNT);
		HeroPushHelper.pushUpdateHero(actorId, heroId, updateAttrSet);

		eventBus.post(new HeroAttributeChangeEvent(actorId, heroId));
		return Result.valueOf();
	}

	@Override
	public Result addHeroExp(long actorId, int heroId, int exp) {
		if (exp < 1) {
			return Result.valueOf(HERO_UPGRADE_EXP_VALUE_ERROR);
		}
		
		Actor actor = actorFacade.getActor(actorId);
		Heros heros = heroDao.get(actorId);
		HeroVO hero = heros.getHeroVO(heroId);
		if (hero == null) {
			return Result.valueOf(HERO_EXISTS);
		}
		HeroConfig heroConfig = HeroService.get(heroId);
		if (heroConfig == null) {
			return Result.valueOf(HERO_CONFIG_NOT_EXITST);
		}

		Set<HeroVOAttributeKey> pushKey = new HashSet<HeroVOAttributeKey>();
		int oldLevel = hero.getLevel();
		int oldExp = hero.exp;
		//增加经验
		hero.exp += exp;
		// 可升的等级		
		int ableLevel = HeroUpgradeService.getAbleUpgrades(oldLevel, heroConfig.getStar(), hero.exp);

		// 是否超出仙人等级上限
		if (ableLevel > 0) {
			int totalLevel = oldLevel + ableLevel;
			int maxHeroLevel = FormulaHelper.executeInt(HeroRule.HERO_LEVEL_MAX_EXPR, actor.level);
			totalLevel = totalLevel > maxHeroLevel ? maxHeroLevel : totalLevel;
			for (int i = oldLevel; i < totalLevel; i++) {
				// 计算升级后的 攻、防、血、潜修次数
				HeroUpgradeConfig heroUpgradeConfig = HeroUpgradeService.get(hero.getLevel(), heroConfig.getStar());

				hero.atk += heroUpgradeConfig.getUpgradeAttack(heroConfig.getUpgradeAttack());
				hero.defense += heroUpgradeConfig.getUpgradeDefense(heroConfig.getUpgradeDefense());
				hero.hp += heroUpgradeConfig.getUpgradeHp(heroConfig.getUpgradeHp());
				hero.availableDelveCount += heroUpgradeConfig.getUpgradeDelve();
				hero.level += 1;
				hero.exp -= heroUpgradeConfig.getNeedExp();

				GameOssLogger.heroUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, heroId, i, hero.level);
			}

			pushKey.add(HeroVOAttributeKey.ATK);
			pushKey.add(HeroVOAttributeKey.DEFENSE);
			pushKey.add(HeroVOAttributeKey.HP);
			pushKey.add(HeroVOAttributeKey.AVAILABLE_DEVLE_COUNT);
			pushKey.add(HeroVOAttributeKey.LEVEL);

			// 发布仙人属性变更事件
			eventBus.post(new HeroLevelUpEvent(actorId, hero.heroId, hero.level));
			eventBus.post(new HeroAttributeChangeEvent(actorId, hero.heroId));
		}
		
		pushKey.add(HeroVOAttributeKey.EXP);
		updateHero(actorId, hero);
		
		//OSS
		GameOssLogger.heroExp(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, heroId, oldExp, exp);

		// 推送升级消息
		HeroPushHelper.pushUpdateHero(actorId, heroConfig.getHeroId(), pushKey);

		return Result.valueOf();
	}

	@Override
	public int size(long actorId) {
		return heroDao.get(actorId).getHeroVOMap().size();
	}

	@Override
	public int getHeroBaseAttribute(long actorId, int heroId, HeroVOAttributeKey key) {
		HeroVO heroVo = getHero(actorId, heroId);
		if (heroVo == null) {
			return 0;
		}
		
		//目前只有装备加成是固定值加成的.
		int addition = bufferFacade.getBuffAddValue(actorId, heroId, key, BufferSourceType.EQUIP_ATTR_BUFFER);
//		int cabalaAdd = bufferFacade.getBuffAddValue(actorId, heroId, key, BufferSourceType.CABALA_ATTR_BUFFER);
		int base = 0;
		
		switch (key) {
		case ATK:
			base = heroVo.atk;
			break;
			
		case DEFENSE:
			base = heroVo.defense;
			break;
			
		case HP:
			base = heroVo.hp;
			break;
			
		case ATTACK_SCOPE:
			base = heroVo.atkScope;
			
		default:
			break;
		}
		return base + addition;
	}


	@Override
	public Heros getHeros(long actorId) {
		return heroDao.get(actorId);
	}

	@Override
	public void recordCompose(long actorId) {
		heroDao.recordCompose(actorId);
	}

	@Override
	public boolean canCompose(long actorId, int num) {
		return heroDao.canCompose(actorId, num);
	}

	@Override
	public void checkAndReset(long actorId) {
		heroDao.checkAndReset(actorId);
	}


	@Override
	public short canDelete(long actorId, List<Integer> heroIds) {
		for (Integer heroId: heroIds) {
			HeroVO absorbedHero = getHero(actorId, heroId);
			if (absorbedHero == null) {
				return HERO_NOT_FOUND;
			}
			boolean isMainHero = mainHeroFacade.isMainHero(actorId, heroId);
			if (isMainHero) {
				return DELETE_HERO_IS_MAIN;
			}
			if (lineupFacade.isHeroInLineup(actorId, heroId)) {
				return DELETE_HERO_IN_LINEUP;
			}
		}
		return SUCCESS;
	}

	@Override
	public int getComposeNum(long actorId) {
		Heros heros = getHeros(actorId);
		return heros.composeNum;
	}

	@Override
	public int getResetNum(long actorId) {
		return getHeros(actorId).resetNum;
	}

	@Override
	public void addResetNum(long actorId) {
		Heros heros = getHeros(actorId);
		heros.resetNum += 1;
		heros.resetTime = TimeUtils.getNow();
		heroDao.update(heros);
	}

	@Override
	public void flushResetNum(Long actorId) {
		Heros heros = getHeros(actorId);
		if(DateUtils.isToday(heros.resetTime) == false){
			heros.resetNum = 0;
			heros.resetTime = 0;
			heroDao.update(heros);
		}
	}

	@Override
	public List<Integer> getHeroIds(long actorId) {
		Heros heros = getHeros(actorId);
		return heros.heroIdList;
	}
	
	
	
}
