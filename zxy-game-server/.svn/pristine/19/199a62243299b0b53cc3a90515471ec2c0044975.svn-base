package com.jtang.gameserver.module.goods.effect;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.USE_GOODS_LEVEL_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.goods.type.UseGoodsResultType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
/**
 * v礼包使用
 * effectValue:1_1_1|2_1_2 类型(1：金币，2：点券，3：英雄，4:物品,5:装备,6:英雄魂魄)_ID_数量(支持表达式)
 * @author ludd
 *
 */
@Component
public class UseParser7 extends UseParser {

	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Override
	protected UseGoodsParserType getParserType() {
		return UseGoodsParserType.TYPE7;
	}

	@Override
	public TResult<List<UseGoodsResult>> handler(long actorId, int goodsId, int useNum, int useFlag) {
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		boolean levelEnough = checkLevel(actorId, goodsConfig.useLevel);
		if (!levelEnough) {
			return TResult.valueOf(USE_GOODS_LEVEL_NOT_ENOUGH);
		}
		boolean enough = checkGoodsEnough(actorId, goodsId, useNum);
		if (!enough){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		
		Result decreaseResult = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.GIFT_BAG, goodsId, useNum);
		if (decreaseResult.isFail()){
			return TResult.valueOf(decreaseResult.statusCode);
		}
		
		List<String[]> list = StringUtils.delimiterString2Array(goodsConfig.effectValue);
		List<UseGoodsResult> result = new ArrayList<>();
		for (String[] strings : list) {
			VipReward vipReward = new VipReward(strings);
			if (vipReward.type == 1){//金币
				actorFacade.addGold(actorId, GoldAddType.USEGOODS, FormulaHelper.executeInt(vipReward.num) * useNum);
			} else if (vipReward.type == 2){//点券
				vipFacade.addTicket(actorId, TicketAddType.USEGOODS, Integer.valueOf(vipReward.num) * useNum);
			} else if (vipReward.type == 3){//英雄
				heroFacade.addHero(actorId, HeroAddType.USEGOODS, vipReward.id);
				UseGoodsResult useGoodsResult = new UseGoodsResult(vipReward.id, UseGoodsResultType.HERO, 1);
				result.add(useGoodsResult);
			} else if (vipReward.type == 4){//物品
				int num = FormulaHelper.executeInt(vipReward.num) * useNum;
				goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_GIFT_BAG, vipReward.id, num);
				UseGoodsResult useGoodsResult = new UseGoodsResult(vipReward.id, UseGoodsResultType.GOODS, num);
				result.add(useGoodsResult);
			} else if (vipReward.type == 5){//装备
//				int num = FormulaHelper.executeInt(vipReward.num);
				equipFacade.addEquip(actorId, EquipAddType.VIP_GIFT_BAG, vipReward.id);
				UseGoodsResult useGoodsResult = new UseGoodsResult(vipReward.id, UseGoodsResultType.EQUIP, 1);
				result.add(useGoodsResult);
			} else if (vipReward.type == 6){//英雄魂魄
				int num = FormulaHelper.executeInt(vipReward.num) * useNum;
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.USEGOODS, vipReward.id, num);
				UseGoodsResult useGoodsResult = new UseGoodsResult(vipReward.id, UseGoodsResultType.HERO_SOUL, num);
				result.add(useGoodsResult);
			}
		}
		return TResult.sucess(result );
	}
	
	private class VipReward{
		public int type;
		public int id;
		public String num;
		
		public VipReward(String[] str) {
			this.type = Integer.valueOf(str[0]);
			this.id = Integer.valueOf(str[1]);
			this.num = str[2];
		}
	}

}
