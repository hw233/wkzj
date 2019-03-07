package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人下阵
 * @author vinceruan
 *
 */
public class UnAssignHeroRequest extends IoBufferSerializer {
	/**
	 * 仙人的配置ID
	 */
	public int heroId;

	public UnAssignHeroRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.heroId = readInt();
	}
}
