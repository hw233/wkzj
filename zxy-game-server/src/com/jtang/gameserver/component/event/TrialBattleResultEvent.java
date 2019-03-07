package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 试练洞战斗结果事件
 * @author ludd
 *
 */
public class TrialBattleResultEvent extends GameEvent{

	/**
	 * 试练次数
	 */
	private int num;
	public TrialBattleResultEvent(long actorId, int num) {
		super(EventKey.TRIAL_BATTLE_RESULT, actorId);
		this.num = num;
	}
	
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(num);
		return list;
	}
	
	public int getNum() {
		return num;
	}

}
