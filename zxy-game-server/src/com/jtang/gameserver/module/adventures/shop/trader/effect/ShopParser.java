package com.jtang.gameserver.module.adventures.shop.trader.effect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.core.event.GameEvent;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

@Component
public abstract class ShopParser {

	private static Map<ShopType, ShopParser> facades = new HashMap<ShopType, ShopParser>();
	
	@PostConstruct
	private void init() {
		facades.put(getEffect(), this);
	}

	public abstract ShopType getEffect();
	
	/**
	 * 获取商店业务处理类
	 * @param effectId
	 * @return
	 */
	public static ShopParser getShopParser(ShopType shopType) {
		return facades.get(shopType);
	}
	
	/**
	 * 时间调度
	 * @param actorId
	 * @param appId
	 */
	public abstract void onApplicationEvent();
	
	/**
	 * 游戏事件处理
	 * @param paramEvent
	 * @param appId
	 */
	public abstract void onGameEvent(GameEvent paramEvent);
	
	/**
	 * 角色登陆
	 * @param actorId
	 * @param appId
	 */
	public abstract void onActorLogin(long actorId);

	/**
	 * 获取商店信息
	 * @return
	 */
	public abstract TResult<ShopInfoResponse> getInfo(long actorId);

	/**
	 * 购买商品
	 * @param shopId
	 * @param num
	 * @return
	 */
	public abstract Result shopBuy(long actorId,int shopId, int num);

	/**
	 * 刷新商品列表
	 * @return
	 */
	public abstract TResult<ShopInfoResponse> shopFlush(long actorId);

	/**
	 * 购买永久
	 * @return
	 */
	public abstract TResult<ShopInfoResponse> buyPermanent(long actorId);

	public abstract void onZero();
	
	
	
}
