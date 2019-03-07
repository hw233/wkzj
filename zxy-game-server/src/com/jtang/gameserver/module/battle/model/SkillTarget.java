package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.List;

public class SkillTarget {
	/**
	 * 技能释放的目标
	 */
	public byte targetId;
	
	/**
	 * 属性变化(如果是一些加状态的技能,该列表就为空)
	 */
	public List<AttributeChange> attributeList = new ArrayList<>();
	
	
	public SkillTarget(byte targetId, List<AttributeChange> attributeList) {
		this.targetId = targetId;
		this.attributeList = attributeList;
	}
	
	public SkillTarget(byte targetId, AttributeChange attr) {
		this.targetId = targetId;
		this.attributeList.add(attr);
	}
	
	public SkillTarget(byte targetId) {
		this.targetId = targetId;
	}
}
