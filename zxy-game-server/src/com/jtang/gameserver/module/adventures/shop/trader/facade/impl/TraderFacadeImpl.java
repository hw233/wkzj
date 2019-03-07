package com.jtang.gameserver.module.adventures.shop.trader.facade.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.module.adventures.shop.trader.effect.ShopParser;
import com.jtang.gameserver.module.adventures.shop.trader.facade.TraderFacade;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopInfoResponse;
import com.jtang.gameserver.module.adventures.shop.trader.type.ShopType;

@Component
public class TraderFacadeImpl implements TraderFacade,Receiver,ActorLoginListener, ApplicationListener<ContextRefreshedEvent>,ZeroListener {

	@Autowired
	private EventBus eventBus;
	
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.STORY_PASSED, this);
		eventBus.register(EventKey.EQUIP_REFINED, this);
		eventBus.register(EventKey.EQUIP_ENHANCED, this);
		eventBus.register(EventKey.HERO_DELVE, this);
		eventBus.register(EventKey.ACTOR_LEVEL_UP, this);
		eventBus.register(EventKey.BABLE_SUCESS, this);
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
		eventBus.register(EventKey.VIP_LEVEL_CHANGE, this);
	}
	
	@Override
	public TResult<ShopInfoResponse> getInfo(ShopType shopType,long actorId) {
		ShopParser parser = ShopParser.getShopParser(shopType);
		return parser.getInfo(actorId);
	}

	@Override
	public Result shopBuy(ShopType shopType, long actorId, int shopId, int num) {
		ShopParser parser = ShopParser.getShopParser(shopType);
		return parser.shopBuy(actorId,shopId,num);
	}

	@Override
	public TResult<ShopInfoResponse> shopFlush(ShopType shopType,long actorId) {
		ShopParser parser = ShopParser.getShopParser(shopType);
		return parser.shopFlush(actorId);
	}

	@Override
	public TResult<ShopInfoResponse> buyPermanent(ShopType shopType,long actorId) {
		ShopParser parser = ShopParser.getShopParser(shopType);
		return parser.buyPermanent(actorId);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		for(ShopType type:ShopType.values()){
			ShopParser parser = ShopParser.getShopParser(type);
			parser.onApplicationEvent();
		}
	}

	@Override
	public void onLogin(long actorId) {
		for(ShopType type:ShopType.values()){
			ShopParser parser = ShopParser.getShopParser(type);
			parser.onActorLogin(actorId);
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		for(ShopType type:ShopType.values()){
			ShopParser parser = ShopParser.getShopParser(type);
			GameEvent gameEvent = (GameEvent) paramEvent;
			parser.onGameEvent(gameEvent);
		}
	}

	@Override
	public void onZero() {
		for(ShopType type:ShopType.values()){
			ShopParser parser = ShopParser.getShopParser(type);
			parser.onZero();
		}
	}

	@Override
	public TResult<Map<Integer, Integer>> getOpenInfo(long actorId) {
		Map<Integer,Integer> map = new HashMap<>();
		for(ShopType type:ShopType.values()){
			ShopParser parser = ShopParser.getShopParser(type);
			TResult<ShopInfoResponse> result =  parser.getInfo(actorId);
			if(result.isFail()){
				map.put(type.getCode(), 0);
			}else{
				map.put(type.getCode(), 1);
			}
		}
		return TResult.sucess(map);
	}

}
