package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;

/**
 * 闪避效果
 * @author ludd
 *
 */
public class DodgeAction extends IoBufferSerializer implements Action {

	/**
	 * 仙人全局id
	 */
	public byte uid;
	
	
	public DodgeAction(byte uid) {
		super();
		this.uid = uid;
	}
	
	@Override
	public void write() {
		this.writeByte(getActionType().getType());
		this.writeByte(this.uid);
	}


	@Override
	public String format(String indentStr) {
		return String.format("%sDodgeAction,fighterId:【%s】\r\n", indentStr, uid);
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.DODGE_ACTION;
	}

}
