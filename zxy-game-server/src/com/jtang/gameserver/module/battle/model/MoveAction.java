package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;


/**
 * 移动数据包
 * @author vinceruan
 *
 */
public class MoveAction extends IoBufferSerializer implements Action {
	
	public byte uid; //仙人的全局ID
	
	public byte x;  //坐标x
	
	public byte y; //坐标y
	
	public static MoveAction valueOf(byte uid, Tile tile) {
		MoveAction mr = new MoveAction();
		mr.uid = uid;
		mr.x = (byte)tile.getX();
		mr.y = (byte)tile.getY();
		return mr;
	}
	
	public String format(String indentStr) {
		return String.format("%sMoveAction:actor【%s】,position:【%s】\r\n", indentStr, uid, this.x+","+this.y);
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.MOVE_ACTION;
	}
	@Override
	public void write() {
		this.writeByte(getActionType().getType());
		this.writeByte(this.uid);
		Tile tile = FightData.transform.get(this.x + "-" + this.y);
		writeByte((byte) tile.getX());
		writeByte((byte) tile.getY());
	}
	

}
