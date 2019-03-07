package com.jtang.gameserver.module.delve.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.DELVE_DO_LAST;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_FAIL_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_FAIL_STONE_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_FAIL_UP_PROP_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_HERO_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.DELVE_HERO_MAX_TIMES;
import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_EXITS;
import static com.jiatang.common.GameStatusCodeConstant.REPEAT_DELVE_FAIL;
import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.gameserver.module.delve.constant.DelveRule.DELVE_USE_GOODS_MIN_LEVEL;
import static com.jtang.gameserver.module.delve.constant.DelveRule.DELVE_USE_GOODS_MIN_STAR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.event.EventBus;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.component.event.HeroAttributeChangeEvent;
import com.jtang.gameserver.component.event.HeroDelveEvent;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.DelveConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroDelveConfig;
import com.jtang.gameserver.dataconfig.service.DelveService;
import com.jtang.gameserver.dataconfig.service.HeroDelveSerive;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.delve.dao.DelveDao;
import com.jtang.gameserver.module.delve.facade.DelveFacade;
import com.jtang.gameserver.module.delve.handler.response.LastDelveResponse;
import com.jtang.gameserver.module.delve.model.DelveResult;
import com.jtang.gameserver.module.delve.model.DoDelveResult;
import com.jtang.gameserver.module.delve.type.DelveType;
import com.jtang.gameserver.module.delve.type.OneKeyDelveType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;

@Component
public class DelveFacadeImpl implements DelveFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(DelveFacadeImpl.class);
	@Autowired
	private DelveDao delveDao;

	@Autowired
	private HeroFacade heroFacade;

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private GoodsFacade goodsFacade;
	
	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private EventBus eventBus;
	/**
	 * 提升属性列表
	 */
	private List<HeroVOAttributeKey> props = new ArrayList<>();

	@PostConstruct
	private void init() {
		props.add(HeroVOAttributeKey.HP);
		props.add(HeroVOAttributeKey.ATK);
		props.add(HeroVOAttributeKey.DEFENSE);
	}

	@Override
	public TResult<Map<HeroVOAttributeKey, DoDelveResult>> doDelve(long actorId, int heroId, int delveType) {
		DelveType type = DelveType.getType(delveType);
		if (heroId <= 0){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		TResult<Map<HeroVOAttributeKey, DoDelveResult>> tResult = TResult.valueOf(DELVE_FAIL);
		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		
		if (heroVO == null){
			LOGGER.error(String.format("英雄不存在,actorId:[%s], heroId:[%s]", actorId, heroId));
			tResult = TResult.valueOf(DELVE_FAIL);
			return tResult;
		}

		DelveConfig delveConfig = DelveService.get(delveType);
		HeroDelveConfig heroDelveConfig = HeroDelveSerive.get(heroVO.heroId);

		HeroConfig heroConfig = HeroService.get(heroVO.heroId);
		if (heroVO.usedDelveCount >= heroVO.availableDelveCount) {
			tResult = TResult.valueOf(DELVE_HERO_MAX_TIMES);
			return tResult;
		}
		if(ableUseStone(heroVO) == false) {
			return TResult.valueOf(DELVE_HERO_LEVEL_NOT_ENOUGH);
		}
		int goodsNum = goodsFacade.getCount(actorId, delveConfig.goodsId);//潜修石数量
		int consumeStone = heroDelveConfig.getConsumeStone(type,heroVO.level, heroVO.usedDelveCount);//需要潜修石数量
		int consumGold = heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount);//需要金币数量
		Actor actor = actorFacade.getActor(actorId);
		if (goodsNum < consumeStone){
			return TResult.valueOf(DELVE_FAIL_STONE_NOT_ENOUGH);
		}
		if (actor.gold < consumGold){
			return TResult.valueOf(DELVE_FAIL_GOLD_NOT_ENOUGH);
		}
		switch(type){
		case TYPE_1:
			actorFacade.decreaseGold(actorId, GoldDecreaseType.DELVE, consumGold);// 扣除金币
			break;
		case TYPE_2:
			actorFacade.decreaseGold(actorId, GoldDecreaseType.DELVE, consumGold);// 扣除金币
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, delveConfig.goodsId, consumeStone);//扣除潜修石
			break;
		case TYPE_3:
			actorFacade.decreaseGold(actorId, GoldDecreaseType.DELVE, consumGold);// 扣除金币
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, delveConfig.goodsId, consumeStone);//扣除潜修石
			break;
		default:
			break;
		}

		//统计累计消耗金币和潜修石
		heroVO.delveCostGold += consumGold;
		heroVO.delveStoneNum += consumeStone;
	
		// 提升属性
		Set<HeroVOAttributeKey> prop = getProp((short) delveConfig.upPropNum); // 计算应该提升的属性
		prop.add(HeroVOAttributeKey.USED_DEVLE_COUNT);
		
		//仙人潜修次数加一		
		heroVO.usedDelveCount += 1;
		
		Map<HeroVOAttributeKey, DoDelveResult> upResult = upHeroProp(heroVO, prop, heroDelveConfig, type, delveConfig.getProportion());
		boolean result = heroFacade.updateHero(actorId, heroVO);// 更新仙人属性
		if (result == false) {
			tResult = TResult.valueOf(DELVE_FAIL_UP_PROP_ERROR);
		} else {
			tResult = TResult.sucess(upResult);
		}
		if(delveConfig.getAbleRepeatDelve()){//判断是否开启重修功能
			prop.add(HeroVOAttributeKey.ALLOW_REDELVE);
		}
		prop.add(HeroVOAttributeKey.COST_GOLD);
		prop.add(HeroVOAttributeKey.COST_STONE);
		HeroPushHelper.pushUpdateHero(actorId, heroId, prop);//推送英雄更新属性到客户端
		
		eventBus.post(new HeroAttributeChangeEvent(actorId, heroVO.heroId));
		eventBus.post(new HeroDelveEvent(actorId, heroVO.heroId,1));
		
		//OSS
		GameOssLogger.heroDelve(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, heroId, upResult.values(),
				heroVO.usedDelveCount, heroVO.availableDelveCount);
		
		return tResult;
	}

	/**
	 * 提升仙人属性
	 * 
	 * @param heroVO 仙人
	 * @param prop 属性组
	 * @param heroDelveConfig  潜修室配置
	 * @param useGoods 是否使用潜修石
	 *            
	 */
	private Map<HeroVOAttributeKey, DoDelveResult> upHeroProp(HeroVO heroVO, Set<HeroVOAttributeKey> prop, HeroDelveConfig heroDelveConfig,
			DelveType delveType, float proportion) {
		
		//先把上一次的清除掉,再通过下面逻辑来增加本次要增加的属性
		heroVO.lastDelveAtk = 0;
		heroVO.lastDelveHp = 0;
		heroVO.lastDelveDefense = 0;
		
		Map<HeroVOAttributeKey, DoDelveResult> result = new HashMap<HeroVOAttributeKey, DoDelveResult>();
		for (HeroVOAttributeKey upProp : prop) {
			switch (upProp) {
			case ATK: {
				heroVO.lastDelveAtk = heroDelveConfig.getRandomAttack(delveType.getCode(), proportion);
				heroVO.atk += heroVO.lastDelveAtk;
				result.put(upProp, new DoDelveResult(upProp, heroVO.lastDelveAtk));
				break;
			}
			case HP: {
				heroVO.lastDelveHp = heroDelveConfig.getRandomHp(delveType.getCode(), proportion);
				heroVO.hp += heroVO.lastDelveHp;
				result.put(upProp, new DoDelveResult(upProp, heroVO.lastDelveHp));
				break;
			}
			case DEFENSE:
				heroVO.lastDelveDefense = heroDelveConfig.getRandomDefence(delveType.getCode(), proportion);
				heroVO.defense += heroVO.lastDelveDefense;
				result.put(upProp, new DoDelveResult(upProp, heroVO.lastDelveDefense));
				break;
			default:
				break;
			}
		}

		return result;
	}

	/**
	 * 检查是否可以使用潜修石
	 * 
	 * @param heroVO
	 * @return
	 */
	private boolean ableUseStone(HeroVO heroVO) {
		HeroConfig heroConfig = HeroService.get(heroVO.heroId);
		if (heroVO.level >= DELVE_USE_GOODS_MIN_LEVEL || heroConfig.getStar() >= DELVE_USE_GOODS_MIN_STAR) {
			return true;
		}
		return false;
	}

	/**
	 * 获取提升属性
	 * 
	 * @param propNum
	 * @return
	 */
	private Set<HeroVOAttributeKey> getProp(short propNum) {
		Set<HeroVOAttributeKey> result = new HashSet<>();
		if (propNum > props.size() || propNum == 0) {
			LOGGER.error(String.format("提升属性个数不正确, propNum:[%s]", propNum));
			return result;
		}

		int[] index = RandomUtils.uniqueRandom(propNum, 0, props.size() - 1);
		for (int i : index) {
			result.add(props.get(i));
		}
		return result;
	}

	@Override
	public Result repeatDelve(long actorId, int heroId) {
//		DelveVO delve = get(actorId);
//		DelveConfig config = DelveService.get(delve.level);
//		if(config == null){
//			return Result.valueOf(REPEAT_DELVE_LACK_CONFIG);
//		}
//		if(!config.getAbleRepeatDelve()){//判断是否开启重修功能
//			return Result.valueOf(REPEAT_DELVE_FUNCTION_UNAVAILABLE);
//		}
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		if(hero == null){
			return Result.valueOf(HERO_NOT_EXITS);
		}
		if(hero.lastDelveAtk <= 0 && hero.lastDelveDefense <= 0 && hero.lastDelveHp <= 0){
			return Result.valueOf(DELVE_DO_LAST);
		}
		Set<HeroVOAttributeKey> attrs = new HashSet<>();
		
		//恢复到潜修之前的状态(减去上一次潜修增加的值)
		if (hero.lastDelveAtk > 0 && hero.atk > hero.lastDelveAtk) {
			hero.atk -= hero.lastDelveAtk;
			attrs.add(HeroVOAttributeKey.ATK);
		}
		
		if (hero.lastDelveDefense > 0 && hero.defense > hero.lastDelveDefense) {
			hero.defense -= hero.lastDelveDefense;
			attrs.add(HeroVOAttributeKey.DEFENSE);
		}
		
		if (hero.lastDelveHp > 0 && hero.hp > hero.lastDelveHp) {
			hero.hp -= hero.lastDelveHp;
			attrs.add(HeroVOAttributeKey.HP);
		}
		
		hero.usedDelveCount = hero.usedDelveCount - 1;
		hero.lastDelveAtk = 0;
		hero.lastDelveDefense = 0;
		hero.lastDelveHp = 0;
		boolean result = heroFacade.updateHero(actorId, hero);// 更新仙人属性
		if (result == false) {
			return Result.valueOf(REPEAT_DELVE_FAIL);
		}
		
		attrs.add(HeroVOAttributeKey.USED_DEVLE_COUNT);
		attrs.add(HeroVOAttributeKey.ALLOW_REDELVE);
		HeroPushHelper.pushUpdateHero(actorId, heroId, attrs);//推送英雄更新属性到客户端
		eventBus.post(new HeroAttributeChangeEvent(actorId, hero.heroId));
		return Result.valueOf();
	}

	@Override
	public TResult<LastDelveResponse> lastDelve(long actorId, int heroId) {
		Actor actor = actorFacade.getActor(actorId);
		if(actor == null){
			return TResult.valueOf(GameStatusCodeConstant.ACTOR_ID_ERROR);
		}
		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		if(heroVO == null){
			return TResult.valueOf(GameStatusCodeConstant.HERO_NOT_EXITS);
		}
		LastDelveResponse response = new LastDelveResponse(heroVO.lastDelveAtk,heroVO.lastDelveDefense,heroVO.lastDelveHp);
		return TResult.sucess(response);
	}

	@Override
	public TResult<List<DelveResult>> oneKeyDelve(long actorId, int heroId, int type,List<Integer> attribute) {
		if (heroId <= 0){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		
		if(attribute.size() > 2){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		
//		if(attribute.size() > 1){
//			int vipLevel = vipFacade.getVipLevel(actorId);
//			if(Vip10Privilege.vipLevel > vipLevel){
//				return TResult.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
//			}
//		}
		
		OneKeyDelveType delveType = OneKeyDelveType.getType(type);
		if(delveType == OneKeyDelveType.NONE){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		
		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		if (heroVO == null){
			LOGGER.error(String.format("英雄不存在,actorId:[%s], heroId:[%s]", actorId, heroId));
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		
		for(int att : attribute){
			HeroVOAttributeKey key = HeroVOAttributeKey.getByCode((byte) att);
			if(key == HeroVOAttributeKey.NONE){
				return TResult.valueOf(DATA_VALUE_ERROR);
			}
		}
		if(delveType == OneKeyDelveType.TYPE_1){//一键潜修一次,直接调用普通潜修
			return smallDelve(actorId, attribute, heroVO);
		}else{//10次
			return bigDelve(actorId, attribute, heroVO);
		}
	}

	/**
	 * 潜修10次
	 * @param actorId
	 * @param attribute
	 * @param heroVO
	 * @return
	 */
	private TResult<List<DelveResult>> bigDelve(long actorId, List<Integer> attribute,HeroVO heroVO) {
		if (heroVO.usedDelveCount >= heroVO.availableDelveCount) {
			return TResult.valueOf(DELVE_HERO_MAX_TIMES);
		}
		
		DelveConfig delveConfig = DelveService.get(DelveType.TYPE_3.getCode());
		HeroDelveConfig heroDelveConfig = HeroDelveSerive.get(heroVO.heroId);
		Actor actor = actorFacade.getActor(actorId);
		int consumeStone = 0;
		int consumGold = 0;
		int delveNum = heroVO.availableDelveCount - heroVO.usedDelveCount > 10 ? 10 : heroVO.availableDelveCount - heroVO.usedDelveCount;
		TResult<Integer> result = costResource(actor, delveNum, heroVO);
		if(result.isFail()){
			return TResult.valueOf(result.statusCode);
		}
		delveNum = result.item;
		//统计累计消耗金币和潜修石
		heroVO.delveCostGold += consumGold;
		heroVO.delveStoneNum += consumeStone;
		List<DelveResult> delveMap = new ArrayList<>();
		Set<HeroVOAttributeKey> addProp = new HashSet<>();
		for (int i = 0; i < delveNum; i++) {
			Set<HeroVOAttributeKey> prop = getProp((short) delveConfig.upPropNum);//随机属性
			int num = 0;
			for(Integer att : attribute){
				for (HeroVOAttributeKey key : prop) {
					if(att == key.getCode()){
						num += 1;
					}
				}
			}
			if(num == attribute.size()){//成功
				Map<HeroVOAttributeKey, DoDelveResult> map = upHeroProp(heroVO, prop, heroDelveConfig, DelveType.TYPE_3, delveConfig.getProportion());
				DelveResult delveResult = new DelveResult(map);
				delveMap.add(delveResult);
				heroVO.usedDelveCount ++ ;
				addProp.addAll(prop);
			}else{//失败
				DelveResult delveResult = new DelveResult();
				delveMap.add(delveResult);
			}
		}
		addProp.add(HeroVOAttributeKey.USED_DEVLE_COUNT);
		addProp.add(HeroVOAttributeKey.COST_GOLD);
		addProp.add(HeroVOAttributeKey.COST_STONE);
		HeroPushHelper.pushUpdateHero(actorId, heroVO.heroId, addProp);
		eventBus.post(new HeroAttributeChangeEvent(actorId, heroVO.heroId));
		eventBus.post(new HeroDelveEvent(actorId, heroVO.heroId,10));
		heroFacade.updateHero(actorId, heroVO);
		return TResult.sucess(delveMap);
	}
	
	/**
	 * 潜修一次
	 * @param actorId
	 * @param attribute
	 * @param heroVO
	 * @return
	 */
	private TResult<List<DelveResult>> smallDelve(long actorId, List<Integer> attribute,HeroVO heroVO) {
		if (heroVO.usedDelveCount >= heroVO.availableDelveCount) {
			return TResult.valueOf(DELVE_HERO_MAX_TIMES);
		}
		
		HeroConfig heroConfig = HeroService.get(heroVO.heroId);
		DelveConfig delveConfig = DelveService.get(DelveType.TYPE_3.getCode());
		HeroDelveConfig heroDelveConfig = HeroDelveSerive.get(heroVO.heroId);
		int goodsNum = goodsFacade.getCount(actorId, delveConfig.goodsId);//潜修石数量
		Actor actor = actorFacade.getActor(actorId);
		long goldNum = actor.gold;
		int consumeStone = heroDelveConfig.getConsumeStone(DelveType.TYPE_3,heroVO.level, heroVO.usedDelveCount);
		int consumGold = heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount);
		if(goodsNum < consumeStone){
			return TResult.valueOf(DELVE_FAIL_STONE_NOT_ENOUGH);
		}
		if(goldNum < consumGold){
			return TResult.valueOf(DELVE_FAIL_GOLD_NOT_ENOUGH);
		}
		actorFacade.decreaseGold(actorId, GoldDecreaseType.DELVE, consumGold);// 扣除金币
		goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PROP_BASE, delveConfig.goodsId, consumeStone);//扣除潜修石
		//统计累计消耗金币和潜修石
		heroVO.delveCostGold += consumGold;
		heroVO.delveStoneNum += consumeStone;
		List<DelveResult> delveMap = new ArrayList<>();
		Set<HeroVOAttributeKey> prop = getProp((short) delveConfig.upPropNum);//随机属性
		int i = 0;
		for(Integer att : attribute){
			for(HeroVOAttributeKey key : prop){
				if(att == key.getCode()){
					i += 1;
				}
			}
		}
		if(i != attribute.size()){
			delveMap.add(0,new DelveResult());
		}else{
			Map<HeroVOAttributeKey, DoDelveResult> map = upHeroProp(heroVO, prop, heroDelveConfig, DelveType.TYPE_3, delveConfig.getProportion());
			DelveResult result = new DelveResult(map);
			delveMap.add(result);
			heroVO.usedDelveCount += 1;
			eventBus.post(new HeroAttributeChangeEvent(actorId, heroVO.heroId));
			eventBus.post(new HeroDelveEvent(actorId, heroVO.heroId,1));
		}
		prop.add(HeroVOAttributeKey.USED_DEVLE_COUNT);
		prop.add(HeroVOAttributeKey.COST_GOLD);
		prop.add(HeroVOAttributeKey.COST_STONE);
		HeroPushHelper.pushUpdateHero(actorId, heroVO.heroId, prop);//推送英雄更新属性到客户端
		heroFacade.updateHero(actorId, heroVO);
		return TResult.sucess(delveMap);
	}
	
	/**
	 * 扣除金币和精炼石
	 * @param actor
	 * @param delveNum
	 * @return
	 */
	private TResult<Integer> costResource(Actor actor,int delveNum,HeroVO heroVO) {
		HeroConfig heroConfig = HeroService.get(heroVO.heroId);
		DelveConfig delveConfig = DelveService.get(DelveType.TYPE_3.getCode());
		HeroDelveConfig heroDelveConfig = HeroDelveSerive.get(heroVO.heroId);
		int stoneNum = 0;//消耗精炼石数量
		int goldNum = 0;//消耗金币数量
		int goodsNum = goodsFacade.getCount(actor.getPkId(), delveConfig.goodsId);//拥有精炼石数量
		for (int i = 0; i < delveNum; i++) {//获取可以精炼的次数
			stoneNum += heroDelveConfig.getConsumeStone(DelveType.TYPE_3,heroVO.level, heroVO.usedDelveCount + i);//需要精炼石数量
			goldNum += heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount + i);//需要金币数量
			if (goodsNum < stoneNum || actor.gold < goldNum){//金币或者精炼石不够
				delveNum = i;
				stoneNum -= heroDelveConfig.getConsumeStone(DelveType.TYPE_3,heroVO.level, heroVO.usedDelveCount + i);;
				goldNum -= heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount + i);
				break;
			}
		}
		if(goldNum > 0 && stoneNum > 0){
			actorFacade.decreaseGold(actor.getPkId(), GoldDecreaseType.DELVE, goldNum);//扣除金币
			goodsFacade.decreaseGoods(actor.getPkId(), GoodsDecreaseType.PROP_BASE, delveConfig.goodsId, stoneNum);//扣除精炼石
		}else if(goldNum > 0 && stoneNum == 0){
			actorFacade.decreaseGold(actor.getPkId(), GoldDecreaseType.DELVE, goldNum);//扣除金币
		}else{
			if(goldNum == 0){
				if(actor.gold < heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount)){
					return TResult.valueOf(DELVE_FAIL_GOLD_NOT_ENOUGH);
				}
			}
			if(stoneNum == 0){
				if(goodsNum < heroDelveConfig.getConsumeGold(heroConfig.getStar(), heroVO.usedDelveCount)){
					return TResult.valueOf(DELVE_FAIL_STONE_NOT_ENOUGH);
				}
			}
		}
		heroVO.delveCostGold += goldNum;
		heroVO.delveStoneNum += stoneNum;
		return TResult.sucess(delveNum);
	}
	
}
