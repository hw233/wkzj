package com.jtang.gameserver.module.hero.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 消耗魂魄招募仙人请求
 * @author vinceruan
 *
 */
public class Soul2HeroRequest extends IoBufferSerializer {
	
	/**
	 * 仙人Id
	 */
	public int heroId;
	
	public Soul2HeroRequest(byte[] data) {
		super(data);
	}
	
	@Override
	public void read() {
		heroId = readInt();
	}
}
