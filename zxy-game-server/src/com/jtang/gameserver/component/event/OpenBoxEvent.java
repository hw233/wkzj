package com.jtang.gameserver.component.event;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.event.GameEvent;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;

public class OpenBoxEvent extends GameEvent {

	/**
	 * 打开宝箱id
	 */
	public int boxId;

	/**
	 * 获得物品id
	 */
	public List<UseGoodsResult> results;


	public OpenBoxEvent(long actorId, int boxId, List<UseGoodsResult> results) {
		super(EventKey.OPEN_BOX, actorId);
		this.boxId = boxId;
		this.results = results;
	}
	
	@Override
	public List<Object> getParamsList() {
		List<Object> list = new ArrayList<>();
		list.add(actorId);
		list.add(boxId);
		for (UseGoodsResult result : results) {
			list.add(result.parse2String());
		}

		return list;
	}

}
