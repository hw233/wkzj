package com.jtang.gameserver.module.snatch.handler.response;

import java.util.ArrayList;
import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.snatch.model.ExchangeVO;

public class ExchangeListResponse extends IoBufferSerializer {
	
	/**
	 * 兑换列表
	 */
	public Collection<ExchangeVO> list = new ArrayList<ExchangeVO>();
	
	/**
	 * 积分
	 */
	public int score;
	
	/**
	 * 刷新券
	 */
	public int exchangeGoods;
	
	/**
	 * 需要的点券
	 */
	public int needTicket;
	
	public ExchangeListResponse(Collection<ExchangeVO> list,int score,int exchangeGoods,int needTicket){
		this.list = list;
		this.score = score;
		this.exchangeGoods = exchangeGoods;
		this.needTicket = needTicket;
	}
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for(ExchangeVO exchangeVO:list){
			writeBytes(exchangeVO.getBytes());
		}
		writeInt(score);
		writeInt(exchangeGoods);
		writeInt(needTicket);
	}
}
