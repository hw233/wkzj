package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.component.event.OpenBoxEvent;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig.LeastGoods;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dataconfig.service.UseGoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.GoodsType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.goods.type.UseGoodsResultType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;

/**
 * 碎片使用
 * effectValue:type(类型)_rate(概率)_expr(支持表达式)|...
 * @author ludd
 *
 */
@Component
public class UseParser12 extends UseParser {

	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private EventBus eventBus;
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE12;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId,int useNum, int useFlag) {
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig.depends != null && goodsConfig.depends != ""){
			String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
			int dependsId = Integer.valueOf(depends[0]);
			int dependsNum = Integer.valueOf(depends[1]);
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.TREASURE_BOX, dependsId, dependsNum);
		}
		
		List<UseGoodsResult> resultList = new ArrayList<>();
		
		UseGoodsResult result = giveLeastGoods(actorId, goodsId);
		if (result != null) { //保底
			resultList.add(result);
		    return TResult.sucess(resultList);
		
		}
		
		
		List<String[]> list = StringUtils.delimiterString2Array(goodsConfig.effectValue);
		List<Box> boxList = new ArrayList<>();
		Map<Integer, Integer> rateMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			String[] item = list.get(i);
			Box box = new Box(item);
			boxList.add(box);
			rateMap.put(i, box.rate);
		}
		
		Integer index = RandomUtils.randomHit(1000, rateMap);
		
		if (index == null ){
			index = new Integer(0);
		}
		Box box = boxList.get(index);
		Actor actor = actorFacade.getActor(actorId);
		switch (box.type) {
		case 1:{//金币
			int num = FormulaHelper.executeInt(box.expression, actor.level);
			actorFacade.addGold(actorId, GoldAddType.USEGOODS, num);
			UseGoodsResult useGoodsResult = new UseGoodsResult(GoodsRule.GOODS_ID_GOLD, UseGoodsResultType.GOODS, num);
			resultList.add(useGoodsResult);
			break;
		}
		case 2:{//点券
			int num = Integer.valueOf(box.expression);
			vipFacade.addTicket(actorId, TicketAddType.USEGOODS, num);
			UseGoodsResult useGoodsResult = new UseGoodsResult(GoodsRule.GOODS_ID_TICKET, UseGoodsResultType.GOODS, num);
			resultList.add(useGoodsResult);
			break;
		}
		case 3:{//物品
			String[] goods = box.expression.split(Splitable.BETWEEN_ITEMS);
			int gId = Integer.valueOf(goods[0]);
			int num = FormulaHelper.executeInt(goods[1], actor.level);
			goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, gId, num);
			UseGoodsResult useGoodsResult = new UseGoodsResult(gId, UseGoodsResultType.GOODS, num);
			resultList.add(useGoodsResult);
			break;
		}
		case 4:{//碎片
			String[] goods = box.expression.split(Splitable.BETWEEN_ITEMS);
			int subType = Integer.valueOf(goods[0]);
			int star = Integer.valueOf(goods[1]);
			int hasId = goodsFacade.hasGoodsByTypeStar(actorId, GoodsType.PIECE, subType, star);
			if (hasId != 0){//玩家有此类型碎片
				goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, hasId, 1);
			} else {
				List<GoodsConfig> cfgList = GoodsService.getTypeList(GoodsType.PIECE);
				int indexCfg = RandomUtils.nextIntIndex(cfgList.size());
				hasId = cfgList.get(indexCfg).getGoodsId();
				goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, hasId, 1);
			}
			UseGoodsResult useGoodsResult = new UseGoodsResult(hasId, UseGoodsResultType.GOODS, 1);
			resultList.add(useGoodsResult);
			break;
		}
		case 5:{//装备
			String[] equip = box.expression.split(Splitable.BETWEEN_ITEMS);
			int equipId =  Integer.valueOf(equip[0]);
			equipFacade.addEquip(actorId, EquipAddType.EQUIP_FRAGMENT, equipId);
			UseGoodsResult useGoodsResult = new UseGoodsResult(equipId, UseGoodsResultType.EQUIP, Integer.valueOf(equip[1]));
			resultList.add(useGoodsResult);
			break;
		}
		case 6:{//仙人魂魄
			String[] hSoul = box.expression.split(Splitable.BETWEEN_ITEMS);
			int heroId = Integer.valueOf(hSoul[0]);
			int num = Integer.valueOf(hSoul[1]);
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.USEGOODS, heroId, num);
			UseGoodsResult useGoodsResult = new UseGoodsResult(heroId, UseGoodsResultType.HERO_SOUL, num);
			resultList.add(useGoodsResult);
			break;
		}
			

		default:
			break;
		}
//		eventBus.post(new OpenBoxEvent(actorId, goodsId, resultList));
		return TResult.sucess(resultList);
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
				equipFacade.addEquip(actorId, EquipAddType.OPEN_TREASURE_BOX, leastGoods.id);
				useGoodsResult = new UseGoodsResult(leastGoods.id, UseGoodsResultType.EQUIP, leastGoods.num);
				break;
			}
			case GOLD: {
				actorFacade.addGold(actorId, GoldAddType.USEGOODS, leastGoods.num);
				useGoodsResult = new UseGoodsResult(GoodsRule.GOODS_ID_GOLD, UseGoodsResultType.GOODS, leastGoods.num);
				break;
			}
			case GOODS: {
				goodsFacade.addGoodsVO(actorId, GoodsAddType.OPEN_TREASURE_BOX, leastGoods.id, leastGoods.num);
				useGoodsResult = new UseGoodsResult(leastGoods.id, UseGoodsResultType.GOODS, leastGoods.num);
				break;
			}
			case HEROSOUL: {
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.USEGOODS, leastGoods.id, leastGoods.num);
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
	
	private class Box{
		public int type;
		public int rate;
		public String expression;
		private Box(String[] str) {
			super();
			this.type = Integer.valueOf(str[0]);
			this.rate = Integer.valueOf(str[1]);
			this.expression = str[2];
		}
		
	}

}
