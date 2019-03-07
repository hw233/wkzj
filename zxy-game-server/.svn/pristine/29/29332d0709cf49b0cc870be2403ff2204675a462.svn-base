package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.gameserver.module.battle.type.ActionType;

/**
 * <pre>
 * Action组合.
 * 客户端会同时播放里面的Action
 * </pre>
 * @author vinceruan
 *
 */
public class SpawnAction extends CompositeAction implements Action {
	
	/**
	 * action列表
	 */
	public List<Action> actions = new ArrayList<>();
	
	/**
	 * 添加action
	 * @param action
	 */
	public void add(Action action) {
		this.actions.add(action);
	}
	
	public String format(String indentStr) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s:{\r\n", indentStr, getClass().getSimpleName()));
		for (Action act : actions) {
			sb.append(act.format(indentStr+"+++"));
		}
		sb.append(indentStr + "}\r\n");
		return sb.toString();
	}
	
	public boolean isEmpty() {
		return this.actions.size() == 0;
	}
	
	@Override
	public ActionType getActionType() {
		return ActionType.SPAWN_ACTION;
	}
	
	@Override
	public void write() {
		writeByte(getActionType().getType());
		writeShort((short)actions.size());
		for (Action action : actions) {
			this.writeBytes(action.getBytes());
		}
	}
}
