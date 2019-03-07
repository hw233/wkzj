package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 仙人属性改变事件
 * @author pengzy
 *
 */
public class HeroAttributeChangeEvent extends GameEvent{

	private int heroId;
	
	public HeroAttributeChangeEvent(long actorId, int heroId) {
		super(EventKey.HERO_ATTRIBUTE_CHANGE, actorId);
		this.heroId = heroId;
	}
	
	public long getActorId() {
		return actorId;
	}
	
	public int getHeroId() {
		return heroId;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(heroId);
		return list;
	}

}
