package com.jtang.gameserver.module.vampiir.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.VAMPIIR_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.VAMPIIR_FAIL_GOLD_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.VAMPIIR_FAIL_GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.VAMPIIR_HERO_LV_LIMIT;
import static com.jiatang.common.GameStatusCodeConstant.VAMPIIR_SELECTED_HEROS_LIMIT;
import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.core.protocol.StatusCode.SUCCESS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroUseGoodsConfig;
import com.jtang.gameserver.dataconfig.model.VampiirExchangeConfig;
import com.jtang.gameserver.dataconfig.service.ActorService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.HeroUpgradeService;
import com.jtang.gameserver.dataconfig.service.HeroUseGoodsService;
import com.jtang.gameserver.dataconfig.service.VampiirService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldDecreaseType;
import com.jtang.gameserver.module.vampiir.constant.VampiirRule;
import com.jtang.gameserver.module.vampiir.facade.VampiirFacade;
import com.jtang.gameserver.module.vampiir.model.ExchangeVO;

@Component
public class VampiirFacadeImpl implements VampiirFacade {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VampiirFacadeImpl.class);
	
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private LineupFacade lineupFacade;
	
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private GoodsFacade goodsFacade;
	
	@Override
	public ListResult<ExchangeVO> exchange(long actorId,Map<Integer, Integer> heroSouls) {
		Actor actor = actorFacade.getActor(actorId);
		if(actor == null){
			return ListResult.statusCode(ACTOR_ID_ERROR);
		}
		if(heroSouls.isEmpty()){
			return ListResult.statusCode(DATA_VALUE_ERROR);
		}
		Map<Integer,Integer> goodsMap = new HashMap<>();
		for(Entry<Integer,Integer> heroSoul:heroSouls.entrySet()){
			HeroConfig heroConfig = HeroService.get(heroSoul.getKey());
			if(heroConfig == null || heroSoul.getValue() == 0){
				return ListResult.statusCode(VAMPIIR_FAIL);
			}
			VampiirExchangeConfig config = VampiirService.getExchangeConfig(heroConfig.getStar());
			int soulNum = heroSoulFacade.getSoulNum(actorId, heroSoul.getKey());
			if(soulNum < heroSoul.getValue()){
				return ListResult.statusCode(VAMPIIR_FAIL);
			}else{
				heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.VAMPIIR, heroSoul.getKey(), heroSoul.getValue());
				int goodsId = config.goodsId;//桃子id
				int num = config.num * heroSoul.getValue();//桃子数量
				if(goodsMap.containsKey(goodsId)){
					goodsMap.put(goodsId,goodsMap.get(goodsId) + num);
				}else{
					goodsMap.put(goodsId, num);
				}
			}
		}
		goodsFacade.addGoodsVO(actorId, GoodsAddType.VAMPIIR_EXCHANGE, goodsMap);
		List<ExchangeVO> list = new ArrayList<>();
		for(Entry<Integer,Integer> entry:goodsMap.entrySet()){
			ExchangeVO exchangeVO = new ExchangeVO(entry.getKey(),entry.getValue());
			list.add(exchangeVO);
		}
		return ListResult.list(list);
	}
//	
//	@Override
//	public Vampiir get(long actorId) {
//		Vampiir vampiir = vampiirDao.get(actorId);
//		if (vampiir == null) {
//			LOGGER.error(String.format("吸灵室不存在,actorId:[%s]", actorId));
//			return null;
//		}
//		return vampiir;
//	}
//
//	@Override
//	public TResult<Integer> upgrade(long actorId) {
//		Vampiir vampiir = get(actorId);
//		if (vampiir == null) {
//			return TResult.valueOf(VAMPIIR_LEVEL_UP_FAIL);
//		}
//		
//		int oldLevel = vampiir.level;
//		int nextLevel = oldLevel + 1;
//		
//		if (nextLevel > VampiirService.getMaxVampiirLevel()) {
//			return TResult.valueOf(VAMPIIR_LEVEL_UP_IS_MAX);
//		}
//
//		VampiirConfig vampiirConfig = VampiirService.get(nextLevel);// 获取升级所需条件
//		int needTicket = vampiirConfig.getUpgradeTicket(); 		// 消耗角色电券
//		if (!vipFacade.decreaseTicket(actorId, TicketDecreaseType.VAMPIIR_UPGRADE, needTicket, oldLevel, nextLevel)) {
//			return TResult.valueOf(TICKET_NOT_ENOUGH);
//		}
//		if (!vampiirDao.update(actorId, nextLevel)) {
//			return TResult.valueOf(VAMPIIR_LEVEL_UP_FAIL);
//		}
//		
//		Actor actor = actorFacade.getActor(actorId);
//		GameOssLogger.vampiirUpgrade(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, oldLevel, nextLevel);
//		
//		return TResult.sucess(nextLevel);
//	}
//
	@Override
	public TResult<HeroVO> doVampiir(Long actorId, int heroId, List<Integer> heroIds, Map<Integer, Integer> heroSouls, Map<Integer, Integer> goods) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		if (hero == null) {
			LOGGER.error(String.format("英雄不存在,actorId:[%s], heroId:[%s]", actorId, heroId));
			return TResult.valueOf(HERO_NOT_FOUND);
		}
		
		HeroConfig heroConfig = HeroService.get(heroId);
		if (hero.getLevel() >= HeroUpgradeService.getMaxLevel(heroConfig.getStar())) {
			return TResult.valueOf(VAMPIIR_HERO_LV_LIMIT);// 超出可升级上限
		}

		if (heroIds.size() > VampiirRule.VAMPIIR_MAX_SELECTED_HEROS) {
			return TResult.valueOf(VAMPIIR_SELECTED_HEROS_LIMIT);
		}
		
		if (heroIds.size() < 1 && heroSouls.size() < 1 && goods.size() < 1) {
			return TResult.valueOf(VAMPIIR_FAIL);
		}
		if (goods.size() > VampiirRule.VAMPIIR_MAX_USE_GOODS) {
			return TResult.valueOf(VAMPIIR_FAIL);
		}
		
		//吸灵室增加的经验百分比
		float vampiirLevelRate = 0;
//		Vampiir vampiir = get(actorId);
//		if (vampiir != null) {
//			vampiirLevelRate = VampiirService.getIncreaseExp(vampiir.level);
//		}
		
		//判断这些仙人是否都能吸
		short canConsume = heroFacade.canDelete(actorId, heroIds);
		if (canConsume != SUCCESS) {
			return TResult.valueOf(canConsume);
		}
		
		//被吸的总经验
		int absorbedExp = 0;
		HeroVO absorbedHero;
		HeroConfig vampiirHeroConfig;
		//循环处理多个仙人经验
		for (Integer absorbedHeroId : heroIds) {
			if (absorbedHeroId == heroId) {
				return TResult.valueOf(VAMPIIR_FAIL);
			}

			absorbedHero = heroFacade.getHero(actorId, absorbedHeroId);			
			vampiirHeroConfig = HeroService.get(absorbedHeroId);

			//当前仙人的总经验
			int heroTotalExp = absorbedHero.exp + HeroUpgradeService.getExp(absorbedHero.level, vampiirHeroConfig.getStar());
			
			//累计每个仙人吸灵后的经验
			absorbedExp += VampiirService.vampiirHeroExp(vampiirHeroConfig.getStar(), heroTotalExp, vampiirLevelRate);
		}
	
		
		// 魂魄经验计算 = 不同品质仙人基础经验*（1+吸灵室经验加成百分比）
		for (Entry<Integer, Integer> entry : heroSouls.entrySet()) {
			vampiirHeroConfig = HeroService.get(entry.getKey());
			absorbedExp += VampiirService.vampiirSoulExp(vampiirHeroConfig.getStar(), entry.getValue(), vampiirLevelRate);
		}
		
		//计算使用物品产生的经验
		Map<Integer, Integer> canUseMap = canUseMap(actorId, goods);
		if (canUseMap.size() > 0) {
			absorbedExp += useGoodsAddExp(actorId, goods);
		}else if(canUseMap.size() == 0 && goods.size() > 0){
			return TResult.valueOf(VAMPIIR_FAIL_GOODS_NOT_ENOUGH);
		}
	
		// 仙人可连升的级数
		int ableUpgrades = HeroUpgradeService.getAbleUpgrades(hero.getLevel(), heroConfig.getStar(), absorbedExp + hero.exp);
		int targetLevel = ableUpgrades + hero.getLevel();
		int actorLevel = actorFacade.getActor(actorId).level;
		int heroLimit = ActorService.getHeroLevelLimit(actorLevel);

		// 判断仙人目标等级是否超出可升级上限
		if (targetLevel > heroLimit) {
			return TResult.valueOf(VAMPIIR_HERO_LV_LIMIT);
		}
		
		
		// 扣除一定的金币  x1英雄等级-x2吸收的经验值-x3英雄星阶
		int needGolds = VampiirRule.getGolds(hero.getLevel(), absorbedExp, heroConfig.getStar());
		if (actorFacade.hasGold(actorId, needGolds) == false) {
			return TResult.valueOf(VAMPIIR_FAIL_GOLD_NOT_ENOUGH);
		}
		
		// 扣除物品
		for (Map.Entry<Integer, Integer> entry : canUseMap.entrySet()) {
			int goodsId = entry.getKey();
			int goodsNum = entry.getValue();
			Result useGoodsResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.VAMPIIR_USE, goodsId, goodsNum);
			if (useGoodsResult.isFail()) {
				return TResult.valueOf(useGoodsResult.statusCode);
			}
		}
		actorFacade.decreaseGold(actorId, GoldDecreaseType.VAMPIIR, needGolds);
		
		// 删除被吸收的仙人
		heroFacade.removeHero(actorId, HeroDecreaseType.VAMPIIR, heroIds);

		for (Entry<Integer, Integer> entry : heroSouls.entrySet()) {
			Result result = heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.VAMPIIR, entry.getKey(), entry.getValue());
			if (result.isFail()) {
				return TResult.valueOf(result.statusCode);
			}
		}
		
		Result result = heroFacade.addHeroExp(actorId, heroId, absorbedExp);
		if (result.isFail()) {
			return TResult.valueOf(result.statusCode);
		}

		//oss
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.heroVampiir(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, heroId, heroIds, heroSouls, absorbedExp);
		
		return TResult.sucess(hero);
	}
	
	/**
	 * 获得可使用的物品
	 * @param actorId
	 * @param goods
	 * @return
	 */
	private Map<Integer, Integer> canUseMap(long actorId, Map<Integer, Integer> goods) {
		Map<Integer, Integer> canUseMap = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : goods.entrySet()) {
			int goodsId = entry.getKey();
			int goodsNum = entry.getValue();
			if(HeroUseGoodsService.contains(goodsId) == false) { //不存在使用这种物品加经验
				continue;
			}
			GoodsVO goodsVO = goodsFacade.getGoodsVO(actorId, goodsId);
			if (goodsVO == null || goodsVO.num < goodsNum) { //如果仓库不存在这种物品或者数量不足
				continue;
			}
			canUseMap.put(goodsId, goodsNum);
		}
		
		return canUseMap;
	}
	
	/**
	 * 计算使用物品产生的经验
	 * @param actorId
	 * @param goods
	 * @return
	 */
	private int useGoodsAddExp(long actorId, Map<Integer, Integer> goods) {
		int addExp = 0;
		for (Map.Entry<Integer, Integer> entry : goods.entrySet()) {
			int goodsId = entry.getKey();
			int goodsNum = entry.getValue();
			HeroUseGoodsConfig cfg = HeroUseGoodsService.get(goodsId);
			addExp += cfg.getAddExp() * goodsNum;
		}
		return addExp;
	}
	

}