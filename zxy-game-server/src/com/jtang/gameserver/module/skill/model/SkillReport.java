package com.jtang.gameserver.module.skill.model;

import java.util.ArrayList;
import java.util.List;


/**
 * 一个技能打到多个人身上的数据包
 * @author vinceruan
 *
 */
public class SkillReport {
	/**
	 * 施法者
	 */
	public List<Byte> casters;
	
	/**
	 * 技能id
	 */
	public int skillId;
	
	/**
	 * 受影响的目标
	 */
	public List<TargetReport> targets = new ArrayList<>();
	
	/**
	 * 距离0：技能 1：近程普攻 2：远程普攻
	 */
	public byte distance;
	
	public SkillReport(byte caster, int skillId, byte distance) {
		this.casters = new ArrayList<>();
		this.casters.add(caster);
		this.skillId = skillId;
		this.distance = distance;
	}
	
	public SkillReport(List<Byte> casters, int skillId) {
		this.casters = casters;
		this.skillId = skillId;
	}
	
	/**
	 * 如果目标为空,则技能没有释放。
	 * @return
	 */
	public boolean isValid() {
		return targets.isEmpty() == false;
	}
}
