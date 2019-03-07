package com.jtang.gameserver.module.adventures.bable.handler.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.bable.model.BableRankVO;
/**
 * 登天塔排名回复
 * @author ludd
 *
 */
public class BableRankResponse extends IoBufferSerializer {
	/**
	 * 排名列表
	 */
	private Map<Integer,List<BableRankVO>> ranks = new HashMap<>();
	
	public BableRankResponse(Map<Integer,List<BableRankVO>> ranks) {
		this.ranks = ranks;
	}
	
	@Override
	public void write() {
		this.writeShort((short) ranks.size());
		for (Entry<Integer,List<BableRankVO>> rank : ranks.entrySet()) {
			writeInt(rank.getKey());
			writeShort((short) rank.getValue().size());
			for(BableRankVO rankVO : rank.getValue()){
				writeBytes(rankVO.getBytes());
			}
		}
	}

}
