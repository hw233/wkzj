package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 仙人升级事件
 * @author 0x737263
 *
 */
public class HeroLevelUpEvent extends GameEvent {

	/**
	 * 仙人id
	 */
	private int heroId;
	
	/**
	 * 仙人等级
	 */
	private int heroLevel;
	
	public HeroLevelUpEvent(long actorId, int heroId, int heroLevel) {
		super(EventKey.HERO_LEVEL_UP, actorId);
		this.heroId = heroId;
		this.heroLevel = heroLevel;
	}

	public int getHeroId() {
		return heroId;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(heroId);
		list.add(heroLevel);
		return list;
	}
	
}
