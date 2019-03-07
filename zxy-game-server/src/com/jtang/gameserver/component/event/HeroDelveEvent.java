package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 仙人潜修事件
 * @author ludd
 *
 */
public class HeroDelveEvent extends GameEvent{

	private int heroId;
	
	public int delveNum;
	
	public HeroDelveEvent(long actorId, int heroId,int delveNum) {
		super(EventKey.HERO_DELVE, actorId);
		this.heroId = heroId;
		this.delveNum = delveNum;
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
		list.add(delveNum);
		return list;
	}

}
