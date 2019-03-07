package com.jtang.gameserver.module.vampiir.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.vampiir.model.ExchangeVO;

public class ExchangeHeroSoulResponse extends IoBufferSerializer {
	
	/**
	 * 获得的物品列表
	 */
	public List<ExchangeVO> goodsList;
	
	public ExchangeHeroSoulResponse(List<ExchangeVO> list){
		this.goodsList = list;
	}

	@Override
	public void write() {
		writeShort((short) goodsList.size());
		for(ExchangeVO exchangeVO : goodsList){
			writeBytes(exchangeVO.getBytes());
		}
	}
}
