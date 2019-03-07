package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人上阵请求
 * @author vinceruan
 *
 */
public class AssignHeroRequest extends IoBufferSerializer {

	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	/**
	 * 对应阵型顶部列表的索引(从1开始)
	 */
	public int headIndex;
	
	/**
	 * 对应阵型3*3格子中的索引(从1开始)
	 */
	public int gridIndex;
	
	
	public AssignHeroRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		heroId = readInt();
		headIndex = readByte();
		gridIndex = readByte();		
	}
}
