package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;


/**
 * 瞬移数据包（瞬移）
 * @author ludd
 *
 */
public class TeleportAction extends IoBufferSerializer implements Action {
	public byte uid; //仙人的全局ID
	
	public byte x;  //坐标x
	
	public byte y; //坐标y
	
	public static TeleportAction valueOf(byte uid, Tile tile) {
		TeleportAction mr = new TeleportAction();
		mr.uid = uid;
		mr.x = (byte)tile.getX();
		mr.y = (byte)tile.getY();
		return mr;
	}
	
	public String format(String indentStr) {
		return String.format("%sTeleportAction:actor【%s】,position:【%s】\r\n", indentStr, uid, this.x+","+this.y);
	}
	
	@Override
	public void write() {
		this.writeByte(getActionType().getType());
		this.writeByte(this.uid);
		Tile tile = FightData.transform.get(this.x + "-" + this.y);
		writeByte((byte) tile.getX());
		writeByte((byte) tile.getY());
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.TELEPORT_ACTION;
	}

}
