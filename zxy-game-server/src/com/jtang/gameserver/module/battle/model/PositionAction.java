package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;

/**
 * 平移效果（带跳跃）
 * @author ludd
 *
 */
public class PositionAction extends IoBufferSerializer implements Action {

	public byte uid; // 仙人的全局ID

	public byte x; // 坐标x

	public byte y; // 坐标y

	public static PositionAction valueOf(byte uid, Tile tile) {
		PositionAction mr = new PositionAction();
		mr.uid = uid;
		mr.x = (byte) tile.getX();
		mr.y = (byte) tile.getY();
		return mr;
	}

	public String format(String indentStr) {
		return String.format("%sPositionAction:actor【%s】,position:【%s】\r\n", indentStr, uid, this.x + "," + this.y);
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.POSITION_ACTION;
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
