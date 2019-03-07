package com.jtang.gameserver.module.adventures.bable.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.bable.model.BableExchangeVO;
/**
 * 兑换物品， 请求兑换列表
 * @author ludd
 *
 */
public class BableExcangeGoodsResponse extends IoBufferSerializer {

	/**
	 * 物品列表(如果是请求兑换列表，返回全部可兑换的物品列表；如果是兑换结果，返回兑换后改变了的列表)
	 */
	private List<BableExchangeVO> items = new ArrayList<>();
	
	public BableExcangeGoodsResponse(BableExchangeVO bableExchangeVO) {
		items.add(bableExchangeVO);
	}
	
	public BableExcangeGoodsResponse(List<BableExchangeVO> items) {
		this.items = items;
	}
	@Override
	public void write() {
		writeShort((short)items.size());
		for (BableExchangeVO item : items) {
			writeBytes(item.getBytes());
		}
	}

}
