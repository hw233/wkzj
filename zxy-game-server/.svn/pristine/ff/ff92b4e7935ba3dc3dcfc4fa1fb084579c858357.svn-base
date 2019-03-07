package com.jtang.gameserver.module.smelt.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.SmeltConfig;
import com.jtang.gameserver.dataconfig.model.SmeltExchangeConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.SmeltService;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulDecreaseType;
import com.jtang.gameserver.module.smelt.constant.SmeltRule;
import com.jtang.gameserver.module.smelt.facade.SmeltFacade;
import com.jtang.gameserver.module.smelt.handler.response.SmeltInfoResponse;
import com.jtang.gameserver.module.smelt.handler.response.SmeltResponse;

@Component
public class SmeltFacadeImpl implements SmeltFacade {

	@Autowired
	HeroFacade heroFacade;
	
	@Autowired
	HeroSoulFacade heroSoulFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Override
	public TResult<SmeltResponse> convert(long actorId, int heroId, int num) {
		int soulNum = 0;//魂的数量
		int dustNum = 0;//尘的数量
		if(num == 0){//仙人转换
			boolean isExits = heroFacade.isHeroExits(actorId, heroId);
			if(isExits == false){
				return TResult.valueOf(GameStatusCodeConstant.HERO_NOT_EXITS);
			}
			List<Integer> list = new ArrayList<>();
			list.add(heroId);
			short result = heroFacade.canDelete(actorId, list);
			if(result != 0){
				return TResult.valueOf(result);
			}
			heroFacade.removeHero(actorId, HeroDecreaseType.SMELT, list);
			num = HeroService.get(heroId).getRecruitSoulCount();
		}else{
			int heroSoulNum = heroSoulFacade.getSoulNum(actorId, heroId);
			if(heroSoulNum < num){
				return TResult.valueOf(GameStatusCodeConstant.HERO_SOUL_NOT_ENOUGH);
			}
			heroSoulFacade.reductSoul(actorId, HeroSoulDecreaseType.SMELT, heroId, num);
		}
		int star = HeroService.get(heroId).getStar();
		SmeltConfig config = SmeltService.getSmeltConfig(star, false);
		for (int i = 0; i < num; i++) {
			soulNum += config.soul;
			dustNum += config.dust;
		}
		if(soulNum > 0){
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SMELT, SmeltRule.SOUL_GOODS_ID,soulNum);
		}
		if(dustNum > 0){
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SMELT, SmeltRule.DUST_GOODS_ID, dustNum);
		}
		SmeltResponse response = new SmeltResponse(soulNum, dustNum);
		return TResult.sucess(response);
	}

	@Override
	public TResult<SmeltInfoResponse> getExchangeInfo(long actorId) {
		List<Integer> heroIds = heroFacade.getHeroIds(actorId);
		SmeltInfoResponse response = new SmeltInfoResponse(heroIds);
		return TResult.sucess(response);
	}

	@Override
	public Result exchange(long actorId, int heroId, int num) {
		if(num == 0 || heroId == 0){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		SmeltExchangeConfig config = SmeltService.getExchangeConfig(heroId);
		if(config == null){
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		List<Integer> heroIds = heroFacade.getHeroIds(actorId);
		if(heroIds.contains(heroId) == false){
			return Result.valueOf(GameStatusCodeConstant.HERO_NOT_GET);
		}
		int soulNum = goodsFacade.getCount(actorId, SmeltRule.SOUL_GOODS_ID);
		int needSoulNum = config.needSoul * num;
		if(soulNum < needSoulNum){
			return Result.valueOf(GameStatusCodeConstant.SOUL_NOT_ENOUTH);
		}
		goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.SMELT, SmeltRule.SOUL_GOODS_ID, needSoulNum);
		heroSoulFacade.addSoul(actorId, HeroSoulAddType.SMELT, heroId, num);
		return Result.valueOf();
	}

}
