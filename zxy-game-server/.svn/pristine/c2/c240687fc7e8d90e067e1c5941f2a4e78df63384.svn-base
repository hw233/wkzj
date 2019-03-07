package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 改变仙人在3*3格子中的位置
 * @author vinceruan
 *
 */
public class ChangeHeroGridRequest extends IoBufferSerializer {
	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	/**
	 * 目标格子的索引(从1开始)
	 */
	public int gridIndex;
	
	@Override
	public void read() {
		heroId = readInt();
		gridIndex = readByte();
	}
	
	public ChangeHeroGridRequest(byte[] bytes) {
		super(bytes);
	}
	
}
