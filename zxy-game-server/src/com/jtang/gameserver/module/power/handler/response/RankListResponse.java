package com.jtang.gameserver.module.power.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.power.model.PowerVO;

public class RankListResponse extends IoBufferSerializer {

	/**
	 * 排行列表
	 */
	public List<PowerVO> rankList = new ArrayList<>();
	
	public RankListResponse(List<PowerVO> rankList){
		this.rankList = rankList;
	}
	
	@Override
	public void write() {
		writeShort((short)rankList.size());
		for(PowerVO powerVO:rankList){
			writeBytes(powerVO.getBytes());
		}
	}
	
}
