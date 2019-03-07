package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.model.RandomExprRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.component.event.OpenBoxEvent;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig.LeastGoods;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dataconfig.service.UseGoodsService;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.goods.type.UseGoodsResultType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.GoldAddType;

@Component
public class UseParser15 extends UseParser {

	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private HeroFacade heroFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE15;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId,
			int useNum, int useFlag) {
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		int goodsCount = goodsFacade.getCount(actorId, goodsId);
		if(goodsCount < useNum){
			if(goodsCount == 0){
				return TResult.valueOf(GOODS_NOT_ENOUGH);
			}else{
				useNum = goodsCount;
			}
		}
		Result decreaseResult = null;
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig.depends != null && goodsConfig.depends != ""){
			String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
			int dependsId = Integer.valueOf(depends[0]);
			int dependsNum = Integer.valueOf(depends[1]) * useNum;
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.TREASURE_BOX, dependsId, dependsNum);
		}
		
		decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.TREASURE_BOX, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		
		List<UseGoodsResult> resultList = new ArrayList<>();
		List<String[]> list = StringUtils.delimiterString2Array(goodsConfig.effectValue);
		List<RandomExprRewardObject> rewardList = new ArrayList<>();
		for(String[] str:list){
			RandomExprRewardObject rewardObject = RandomExprRewardObject.valueOf(str);
			rewardList.add(rewardObject);
		}
		List<RewardObject> reward = new ArrayList<>();
		for (int x = 0; x < useNum; x++) {
			UseGoodsResult result = giveLeastGoods(actorId, goodsId);
			if (result != null) { //保底
				resultList.add(result);
				continue;
			}
			Map<Integer,Integer> map = new HashMap<>();
			for(int i = 0;i<rewardList.size();i++){
				RandomExprRewardObject rewardObject = rewardList.get(i);
				map.put(i,rewardObject.rate);
			}
			Integer index = RandomUtils.randomHit(1000, map);
			if(index == null){
				return null;
			}
			RandomExprRewardObject rewardObject = rewardList.get(index).clone();
			rewardObject.calculateNum(ActorHelper.getActorLevel(actorId));
			reward.add(rewardObject);
		}
		for(RewardObject rewardObject:reward){
			switch(rewardObject.rewardType){
			case EQUIP:
				resultList.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.EQUIP, rewardObject.num));
				break;
			case GOLD:
				resultList.add(new UseGoodsResult(GoodsRule.GOODS_ID_GOLD, UseGoodsResultType.GOODS, rewardObject.num));
				break;
			case GOODS:
				resultList.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.GOODS, rewardObject.num));
				break;
			case HEROSOUL:
				resultList.add(new UseGoodsResult(rewardObject.id, UseGoodsResultType.HERO_SOUL, rewardObject.num));
				break;
			default:
				break;
			}
		}
		sendReward(actorId,resultList);
//		eventBus.post(new OpenBoxEvent(actorId, goodsId, resultList));
		return TResult.sucess(resultList);
	}
	
	private void sendReward(long actorId, List<UseGoodsResult> resultList) {
		for(UseGoodsResult result : resultList){
			switch(result.type){
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.OPEN_TREASURE_BOX,result.id);
				break;
			case HERO_SOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.OPEN_TREASURE_BOX, result.id, result.num);
				break;
			case GOODS:
				if(result.id == GoodsRule.GOODS_ID_GOLD){
					actorFacade.addGold(actorId, GoldAddType.USEGOODS, result.num);
				}else{
					goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, result.id,result.num);
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 保底物品
	 * @param actorId
	 * @param goodsId
	 * @return
	 */
	private UseGoodsResult giveLeastGoods(long actorId, int goodsId) {
		boolean flag = goodsFacade.leastGoods(actorId, goodsId);
		UseGoodsResult useGoodsResult = null;
		GoodsUseConfig useGoodsConfig = UseGoodsService.get(goodsId);
		if (flag == false || useGoodsConfig == null) {
			return null;
		} else {
			LeastGoods leastGoods = useGoodsConfig.getRandomGoods();
			switch (leastGoods.rewardType) {
			case EQUIP:{
				//equipFacade.addEquip(actorId, EquipAddType.OPEN_TREASURE_BOX, leastGoods.id);
				useGoodsResult = new UseGoodsResult(leastGoods.id, UseGoodsResultType.EQUIP, leastGoods.num);
				break;
			}
			case GOLD: {
				//actorFacade.addGold(actorId, GoldAddType.USEGOODS, leastGoods.num);
				useGoodsResult = new UseGoodsResult(GoodsRule.GOODS_ID_GOLD, UseGoodsResultType.GOODS, leastGoods.num);
				break;
			}
			case GOODS: {
				//goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, leastGoods.id, leastGoods.num);
				useGoodsResult = new UseGoodsResult(leastGoods.id, UseGoodsResultType.GOODS, leastGoods.num);
				break;
			}
			case HEROSOUL: {
				//heroSoulFacade.addSoul(actorId, HeroSoulAddType.USEGOODS, leastGoods.id, leastGoods.num);
				useGoodsResult = new UseGoodsResult(leastGoods.id, UseGoodsResultType.HERO_SOUL, leastGoods.num);
				break;
			}
				

			default:
				break;
			}
			List<UseGoodsResult> list = new ArrayList<>();
			list.add(useGoodsResult);
			eventBus.post(new OpenBoxEvent(actorId, goodsId, list));
		}
		
		return useGoodsResult;
	}

}
