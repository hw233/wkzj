package com.jtang.gameserver.module.hero.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 魂魄突破(提高仙人的可潜修次数)
 * @author vinceruan
 *
 */
public class BreakThroughRequest extends IoBufferSerializer {
	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	public BreakThroughRequest(byte[] data) {
		super(data);
	}
	
	@Override
	public void read() {
		heroId = readInt();
	}

}
