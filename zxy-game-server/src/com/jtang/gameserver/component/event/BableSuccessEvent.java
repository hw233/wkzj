package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;


/**
 * 登天塔登塔成功事件
 * @author ludd
 *
 */
public class BableSuccessEvent extends GameEvent {
	
	/**
	 * 当前登塔的id
	 */
	public int bableId;
	
	/**
	 * 楼层
	 */
	public int floor;
	
	/**
	 * 当前爬塔层数
	 */
	public int currentFloorNum;

	public BableSuccessEvent(long actorId, int bableId, int floor, int currentFloorNum) {
		super(EventKey.BABLE_SUCESS, actorId);
		this.bableId = bableId;
		this.floor = floor;
		this.currentFloorNum = currentFloorNum;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(bableId);
		list.add(floor);
		list.add(currentFloorNum);
		return list;
	}
}
