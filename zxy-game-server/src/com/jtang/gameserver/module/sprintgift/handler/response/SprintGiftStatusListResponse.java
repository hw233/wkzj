package com.jtang.gameserver.module.sprintgift.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 所有未领取的礼包状态
 * @author ligang
 *
 */
public class SprintGiftStatusListResponse extends IoBufferSerializer {
	
	/**
	 * 礼包状态集合
	 * 格式：Map<等级：礼包状态>
	 * key 等级
	 * value 礼包状态  1 已经领取(不发)  2 已达到条件但未领取 3未达到条件 
	 */
	public Map<Integer, Integer> levelStatusMap = new HashMap<Integer, Integer>();
	
	public static SprintGiftStatusListResponse valueOf(Map<Integer, Integer> levelStatusMap) {
		SprintGiftStatusListResponse res = new SprintGiftStatusListResponse();
		res.levelStatusMap = levelStatusMap;
		return res;
	}
	
	
	@Override
	public void write() {
		writeShort((short)levelStatusMap.size());
		for (Map.Entry<Integer,Integer> entity : levelStatusMap.entrySet()) {
			writeInt(entity.getKey());
			writeInt(entity.getValue());
		}
	}
	
}
