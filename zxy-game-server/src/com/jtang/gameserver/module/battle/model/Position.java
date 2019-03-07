package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class Position extends IoBufferSerializer {
	public byte x;
	public byte y;
	
	public Position(byte x, byte y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void write() {
		Tile tile = FightData.transform.get(this.x + "-" + this.y);
		writeByte((byte) tile.getX());
		writeByte((byte) tile.getY());
	}
}
