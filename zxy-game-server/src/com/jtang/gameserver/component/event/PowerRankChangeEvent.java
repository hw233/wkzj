package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 最强势力排名变更事件
 * @author 0x737263
 *
 */
public class PowerRankChangeEvent extends GameEvent {

	/**
	 * 角色当前等级
	 */
	public int actorLevel;
	
	/**
	 * 旧名排名
	 */
	public int oldRank;
	
	/**
	 * 新排名
	 */
	public int newRank;
	
	public PowerRankChangeEvent(long actorId, int actorLevel, int oldRank, int newRank) {
		super(EventKey.POWER_RANK_CHANGE, actorId);
		this.actorLevel = actorLevel;
		this.oldRank = oldRank;
		this.newRank = newRank;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(actorLevel);
		list.add(oldRank);
		list.add(newRank);
		return list;
	}

}
