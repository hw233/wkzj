package com.jtang.gameserver.module.love.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.love.model.LoveRankInfo;

public class LoveRankListResponse extends IoBufferSerializer {

	/**
	 * 排行列表
	 */
	public List<LoveRankInfo> rankList = new ArrayList<>();
	
	public LoveRankListResponse(List<LoveRankInfo> rankList){
		this.rankList = rankList;
	}
	
	@Override
	public void write() {
		writeShort((short)rankList.size());
		for(LoveRankInfo loveRankInfo:rankList){
			writeBytes(loveRankInfo.getBytes());
		}
	}
	
}
