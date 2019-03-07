package com.jtang.gameserver.module.snatch.handler.response;

import java.util.ArrayList;
import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.snatch.model.ExchangeVO;

public class ReflushExchangeResponse extends IoBufferSerializer {
	
	/**
	 * 兑换列表
	 */
	public Collection<ExchangeVO> exchangeList = new ArrayList<>();
	
	/**
	 * 刷新券
	 */
	public int exchangeGoods;
	
	/**
	 * 需要的点券
	 */
	public int needTicket;
	
	public ReflushExchangeResponse(Collection<ExchangeVO> exchangeList,int exchangeGoods,int needTicket){
		this.exchangeList = exchangeList;
		this.exchangeGoods = exchangeGoods;
		this.needTicket = needTicket;
	}
	
	@Override
	public void write() {
		writeShort((short) exchangeList.size());
		for(ExchangeVO exchangeVO:exchangeList){
			writeBytes(exchangeVO.getBytes());
		}
		writeInt(exchangeGoods);
		writeInt(needTicket);
	}

}
