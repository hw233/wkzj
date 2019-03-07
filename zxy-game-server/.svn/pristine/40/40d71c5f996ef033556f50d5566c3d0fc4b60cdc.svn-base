package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 故事通关事件
 * @author vinceruan
 *
 */
public class StoryPassedEvent extends GameEvent {
	
	/**
	 * 故事id
	 */
	public int storyId;
	
	/**
	 * 战场id
	 */
	public int battleId;
	
	/**
	 * 星级
	 */
	public int star;
	
	/**
	 * 次数
	 */
	public int times;
	
	/**
	 * 故事战斗类型 0：合作关卡 1：主线
	 */
	public int battleType;
	
	public StoryPassedEvent(long actorId, int storyId, int battleId, int star, int times,int battleType) {
		super(EventKey.STORY_PASSED, actorId);
		this.star = star;
		this.storyId = storyId;
		this.battleId = battleId;
		this.actorId = actorId;
		this.times = times;
		this.battleType = battleType;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(storyId);
		list.add(battleId);
		list.add(star);
		list.add(times);
		list.add(battleType);
		return list;
	}
}
