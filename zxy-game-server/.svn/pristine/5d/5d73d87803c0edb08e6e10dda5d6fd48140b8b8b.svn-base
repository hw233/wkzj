package com.jtang.gameserver.module.battle.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jtang.gameserver.module.goods.model.GoodsVO;

public class BattleDropedGoods {
	/**
	 * 掉落的物品,格式是:
	 * <pre>
	 * 	ConcurrentMap<FightId, Map<GoodUuid, GoodsVO>>
	 * </pre>
	 */
	private ConcurrentMap<String, DropedGoods> dropedGoods = new ConcurrentHashMap<String, DropedGoods>();

	/**
	 * 初始化战场的物品掉落缓存
	 * @param battleId
	 * @return
	 */
	public void initDropedGoods(String fightId, DropedGoods dg) {
		if (dg == null) {
			return;
		}
		dropedGoods.put(fightId, dg);
	}
	
	public GoodsVO fetchGoods(String fightId, String goodUuid) {
		DropedGoods goods = dropedGoods.get(fightId);
		if (goods != null) {
			return goods.fetch(goodUuid);
		}
		return null;
	}
}
