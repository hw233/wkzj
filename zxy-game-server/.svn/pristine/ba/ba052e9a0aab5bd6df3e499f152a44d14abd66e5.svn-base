package com.jtang.gameserver.module.power.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.power.model.PowerShopVO;

public class PowerShopInfoResponse extends IoBufferSerializer {

	/**
	 * 刷新的次数
	 */
	public int flushNum;
	
	/**
	 * 商品列表
	 */
	public Map<Integer,PowerShopVO> shopMap = new HashMap<>();
	
	public PowerShopInfoResponse(int flushNum,Map<Integer,PowerShopVO> shopList) {
		this.flushNum = flushNum;
		this.shopMap = shopList;
	}

	@Override
	public void write() {
		writeInt(flushNum);
		writeShort((short)shopMap.size());
		for(PowerShopVO powerShopVO:shopMap.values()){
			writeBytes(powerShopVO.getBytes());
		}
	}
}
