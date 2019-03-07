package com.jtang.gameserver.module.skill.type;

/**
 * 定义技能被激活的各种条件
 * 
 * @author vinceruan
 * 
 */
public interface SkillTriggerItem {
	
	/** 1 - 装备调整时触发 */
	public static final int TriggerEquip = 1;
	
	/** 2 - 阵型调整时触发 */
	public static final int TriggerLineup = 2;
		
	/** 3 - 生命值达到时触发 */
	public static final int TriggerHpMax = 3;
	
	/** 4 - 等级达到时触发 */
	public static final int TriggerLevel = 4;
	
	/** 5 - 攻击力达到时触发 */
	public static final int TriggerAtk = 5;
	
	/** 6 - 防御力达到时触发 */
	public static final int TriggerDefense = 6;
}
