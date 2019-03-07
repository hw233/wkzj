package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗队伍
 * @author vinceruan
 *
 */
public class AttackTeam {
	/**
	 * 朝向(1:朝下 	 2:朝上)
	 */
	public byte face;
	
	/**
	 * 仙人列表
	 */
	public List<Attacker> attackerList;
	
	/**
	 * 气势
	 */
	public int morale;
	
	
	public AttackTeam(List<Fighter> list, byte face, int morale) {
		this.face = face;
		attackerList = new ArrayList<>();
		for (Fighter fighter : list) {
			attackerList.add(new Attacker(fighter));
		}
		this.morale = morale;
	}
	
	public String format(String indentStr) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%sface:%d\r\n", indentStr, face));
		sb.append(String.format("%sattackerList:\r\n", indentStr));
		sb.append(String.format("%smorale:\r\n", morale));
		for (Attacker atk : attackerList) {
			sb.append(atk.format(indentStr+"++"));
		}
		return sb.toString();
	}
}
