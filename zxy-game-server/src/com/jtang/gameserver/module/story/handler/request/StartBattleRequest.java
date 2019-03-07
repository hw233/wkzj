package com.jtang.gameserver.module.story.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 开始战斗(请求进入某一个战场)
 * @author vinceruan
 *
 */
public class StartBattleRequest extends IoBufferSerializer {
	
	/**
	 * 战场id
	 */
	public int battleId;
	
	/**
	 * 盟友id (如果为合作关卡则传盟友id，否则传0)
	 */
	public long allyActorId;

	@Override
	public void read() {
		this.battleId = readInt();
		this.allyActorId = readLong();
	}
	
	public StartBattleRequest(byte[] data) {
		super(data);
	}
}
