package com.jtang.gameserver.module.adventures.shop.trader.helper;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.TraderDiscountConfig;
import com.jtang.gameserver.dataconfig.model.TraderGlobalConfig;
import com.jtang.gameserver.dataconfig.model.TraderRewardPoolConfig;
import com.jtang.gameserver.dataconfig.service.TraderService;
import com.jtang.gameserver.dbproxy.entity.Trader;
import com.jtang.gameserver.module.adventures.shop.trader.effect.ShopParser;
import com.jtang.gameserver.module.adventures.shop.trader.effect.TraderShopParser;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class TraderShopHelp {
	

	/**
	 * 第一次触发初始化
	 * @param trader
	 */
	public static void initFirst(Trader trader){
		TraderShopParser traderShopParser = (TraderShopParser)ShopParser.getShopParser(ShopType.TYPE1);
		TraderGlobalConfig globalConfig = TraderService.getGlobalConfig();
		Map<Integer,Integer> firstList = globalConfig.firstList;
		int level = ActorHelper.getActorLevel(trader.actorId);
		for(Entry<Integer,Integer> entry : firstList.entrySet()){
			TraderRewardPoolConfig rewardConfig = TraderService.getPoolRewardConfig(entry.getKey());
			TraderDiscountConfig discountConfig = TraderService.getDiscountConfig(entry.getValue());
			traderShopParser.parserItemVo(trader, rewardConfig, discountConfig, level);
		}
	}
}
