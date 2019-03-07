package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 云游仙商购买物品事件
 * @author 0x737263
 *
 */
public class ShopBuyEvent extends GameEvent {
	
	/**
	 * 商店物品配置id
	 */
	public int shopId;
	
	/**
	 * 本项物品今天购买次数
	 */
	public int todayBuyCount;

	public ShopBuyEvent(long actorId, int shopId, int todayBuyCount) {
		super(EventKey.SHOP_BUY, actorId);
		this.shopId = shopId;
		this.todayBuyCount = todayBuyCount;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(shopId);
		list.add(todayBuyCount);
		return list;
	}

}
