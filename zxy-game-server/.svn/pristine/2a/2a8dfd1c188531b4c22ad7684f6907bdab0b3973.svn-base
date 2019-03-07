package com.jtang.gameserver.module.battle.model;

import java.util.HashMap;
import java.util.Map;

import com.jtang.gameserver.module.goods.model.GoodsVO;

public class DropedGoods {
	/**
	 * 已经掉落的物品
	 */
	Map<Long, GoodsVO> goods = new HashMap<Long, GoodsVO>();
	
	public void store(GoodsVO g) {
		goods.put(Long.valueOf(g.goodsId), g);
	}
	
	public GoodsVO fetch(String uuid) {
		return goods.remove(uuid);
	}
}
