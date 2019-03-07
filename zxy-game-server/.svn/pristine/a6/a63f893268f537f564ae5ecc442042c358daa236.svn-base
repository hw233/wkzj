package com.jtang.gameserver.module.lineup.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 将两个仙人的位置对调
 * @author vinceruan
 *
 */
public class ExchangeHeroGridRequest extends IoBufferSerializer {
	/**
	 * 仙人的配置ID
	 */
	public int heroId1;
	
	/**
	 * 仙人的配置ID
	 */
	public int heroId2;
	
	@Override
	public void read() {
		this.heroId1 = readInt();
		this.heroId2 = readInt();
	}

	public ExchangeHeroGridRequest(byte[] bytes) {
		super(bytes);
	}
}
