package com.jtang.gameserver.module.love.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class LoveMonsterBossResponse extends IoBufferSerializer {

	/**
	 * 难度id
	 */
	public int id;
	
	/**
	 * boss剩余血量
	 */
	public int HP;
	
	public LoveMonsterBossResponse(int id,int HP){
		this.id = id;
		this.HP = HP;
	}
	
	@Override
	public void write() {
		writeInt(id);
		writeInt(HP);
	}
}
