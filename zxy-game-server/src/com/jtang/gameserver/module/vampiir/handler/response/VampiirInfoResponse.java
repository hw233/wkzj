package com.jtang.gameserver.module.vampiir.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取吸灵室等级信息返回的结果
 * 
 * @author pengzy
 * 
 */
public class VampiirInfoResponse extends IoBufferSerializer {

	/** 吸灵室当前等级 */
	private int level;

	public VampiirInfoResponse(int level) {
		this.level = level;
	}

	@Override
	public void write() {
		writeInt(level);
	}
}
