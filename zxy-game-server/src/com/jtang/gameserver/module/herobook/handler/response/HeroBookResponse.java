package com.jtang.gameserver.module.herobook.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.HeroBook;

/**
 * 图鉴回复
 * @author ludd
 *
 */
public class HeroBookResponse extends IoBufferSerializer {
	/**
	 * 已领取的奖励序号
	 */
	private List<Integer> getOrderId = new ArrayList<>();
	
	/**
	 * 点亮仙人id
	 */
	private List<Integer> historyHeroIds = new ArrayList<>();
	
	public HeroBookResponse(HeroBook heroBook){
		this.getOrderId.addAll( heroBook.getOrderIds());
		historyHeroIds.addAll(heroBook.historyHeroIds);
	}
	@Override
	public void write() {
		this.writeIntList(getOrderId);
		this.writeIntList(historyHeroIds);
	}

}
