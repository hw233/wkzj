package com.jtang.gameserver.module.delve.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class LastDelveResponse extends IoBufferSerializer {
	/**
	 * 上一次潜修新增的攻击值(用于重修)
	 */
	public int lastDelveAtk;
	
	/**
	 * 上一次潜修新增的防御值(用于重修)
	 */
	public int lastDelveDefense;
	
	/**
	 * 上一次潜修新增的生命值(用于重修)
	 */
	public int lastDelveHp;
	
	public LastDelveResponse(int atk,int defense,int hp){
		this.lastDelveAtk = atk;
		this.lastDelveDefense = defense;
		this.lastDelveHp = hp;
	}
	
	@Override
	public void write() {
		writeInt(lastDelveAtk);
		writeInt(lastDelveDefense);
		writeInt(lastDelveHp);
	}
	
}
