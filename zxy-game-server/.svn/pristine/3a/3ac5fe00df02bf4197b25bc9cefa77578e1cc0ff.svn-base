package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;
import com.jtang.gameserver.module.user.type.ActorAttributeKey;


public class DropActorPropertyAction extends IoBufferSerializer implements Action {
	/**
	 * action的类型
	 */
	public static final byte actionType = 7;
	
	/**
	 * 物品掉落位置
	 */
	public Position positioin;
	
	/**
	 * 掉落的类型
	 */
	public ActorAttributeKey property;
	
	/**
	 * 掉落的数量
	 */
	public int num;
	
	public DropActorPropertyAction(Tile tile, ActorAttributeKey key, int num) {
		this.positioin = new Position((byte)tile.getX(), (byte)tile.getY());
		this.property = key;
		this.num = num;
	}
	
	@Override
	public String format(String indentStr) {
		return String.format("%sDropActorPropertyAction:position:【%d,%d】, goods:【%d,%d】\r\n", indentStr,
				positioin.x, positioin.y, property.getCode(), num);
	}
	@Override
	public ActionType getActionType() {
		return ActionType.DROPACTORPROPERTY_ACTION;
	}
	
	@Override
	public void write() {
		this.writeByte(getActionType().getType());
		this.writeBytes(positioin.getBytes());
		writeByte(this.property.getCode());
		writeInt(this.num);
	}

}
