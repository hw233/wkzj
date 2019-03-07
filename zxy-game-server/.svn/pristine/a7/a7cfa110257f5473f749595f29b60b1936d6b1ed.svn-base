package com.jtang.gameserver.module.ally.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 切磋总次数计数更新
 * @author pengzy
 *
 */
public class AllyFightCountResponse extends IoBufferSerializer{

	private int allyFightCount;
	
	public AllyFightCountResponse(int allyFightCount){
		this.allyFightCount = allyFightCount;
	}
	@Override
	public void write() {
		writeInt(allyFightCount);
	}

}
