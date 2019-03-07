package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.dbproxy.entity.Actor;

/**
 * 角色升级事件
 * @author vinceruan
 *
 */
public class ActorLevelUpEvent extends GameEvent {
	
	public  Actor actor;
	
	public int oldLevel;
	
	public ActorLevelUpEvent(Actor actor, int oldLevel) {
		super(EventKey.ACTOR_LEVEL_UP, actor.getPkId());
		this.actor = actor;
		this.oldLevel = oldLevel;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(oldLevel);

		return list;
	}

}
