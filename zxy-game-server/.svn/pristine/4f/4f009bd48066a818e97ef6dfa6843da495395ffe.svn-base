package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;

/**
 * 复活
 * @author ludd
 *
 */
public class ReviveAction extends IoBufferSerializer implements Action {

	/**
	 * 仙人全局id
	 */
	public byte uid; 

	
	public ReviveAction(byte uid) {
		super();
		this.uid = uid;
	}

	@Override
	public String format(String indentStr) {
		return String.format("%sImmunityAction:fighterId:【%s】\r\n", indentStr, uid);
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.REVIVE_ACTION;
	}
	@Override
	public void write() {
		this.writeByte(this.getActionType().getType());
		this.writeByte(this.uid);
	}

}
