package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;

/**
 * 福神宝箱每日开启任务事件
 * @author hezh
 *
 */
public class VIPBoxTaskEvent extends GameEvent{

	/** 开启个数*/
	private int openNum;
	
	public VIPBoxTaskEvent(long actorId,int openNum) {
		super(EventKey.OPEN_VIP_BOX, actorId);
		this.openNum = openNum;
	}
	
	public long getActorId() {
		return actorId;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		return list;
	}

	/**
	 * @return the openNum
	 */
	public int getOpenNum() {
		return openNum;
	}
}
