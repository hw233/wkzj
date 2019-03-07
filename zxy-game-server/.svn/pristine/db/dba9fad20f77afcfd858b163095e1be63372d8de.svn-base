package com.jtang.gameserver.module.snatch.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送抢夺属性变更
 * @author vinceruan
 *
 */
public class PushAchimentResponse extends IoBufferSerializer {
	/**
	 * 成就列表
	 * key为成就id
	 * value为已经完成的次数
	 */
	Map<Integer, Integer> achimentMap;
	
	public PushAchimentResponse(Map<Integer, Integer> map) {
		this.achimentMap = map;
	}

	@Override
	public void write() {
		writeIntMap(achimentMap);
	}

}
