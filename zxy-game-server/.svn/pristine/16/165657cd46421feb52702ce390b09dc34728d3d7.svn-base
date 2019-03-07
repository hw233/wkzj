package com.jtang.gameserver.module.skill.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.gameserver.module.battle.model.Action;
import com.jtang.gameserver.module.battle.model.AttributeChange;
import com.jtang.gameserver.module.battle.model.SkillTarget;

/**
 * 记录一个人被技能击中时属性(攻防血等)与动作(移动、掉物品)的变化
 * @author vinceruan
 *
 */
public class TargetReport {
	/**
	 * 受影响人的属性变化
	 */
	public SkillTarget targetAttr = null;
	
	/**
	 * 受影响人被技能击中后的动作:BufferAction、掉落物品等
	 */
	public List<Action> actions = new ArrayList<>();
	
	//如果valid为false，则数据包被丢弃,不会下发到客户端
	public boolean valid = true;
	
	public boolean isValid() {
		return valid;
	}
	
	public TargetReport(byte target) {
		this.targetAttr = new SkillTarget(target);
	}
	
	public void addAttrChange(AttributeChange attr) {
		targetAttr.attributeList.add(attr);
	}
}
