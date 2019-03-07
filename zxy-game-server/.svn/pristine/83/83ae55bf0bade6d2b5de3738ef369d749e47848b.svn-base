package com.jtang.gameserver.module.battle.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.type.ActionType;

/**
 * 死亡动作包
 * @author ludd
 *
 */
public class DeadAction extends IoBufferSerializer implements Action {

	/**
	 * 仙人全局id
	 */
	public byte uid; 
	
	private DisapperAction disapperAction;
	
	public DeadAction(byte uid) {
		super();
		this.uid = uid;
	}
	
	public void setDisapperAction(DisapperAction disapperAction) {
		this.disapperAction = disapperAction;
	}
	
	public DisapperAction getDisapperAction() {
		return disapperAction;
	}

	@Override
	public String format(String indentStr) {
		return String.format("%sDeadAction:fighterId:【%s】\r\n", indentStr, uid);
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.DEAD_ACTION;
	}
	@Override
	public void write() {
		this.writeByte(this.getActionType().getType());
		this.writeByte(this.uid);
	}

}
