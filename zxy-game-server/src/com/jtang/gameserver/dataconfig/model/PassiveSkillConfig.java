package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;

/**
 * 被动技能配置
 * @author vinceruan
 *
 */
@DataFile(fileName = "passiveSkillConfig")
public class PassiveSkillConfig implements ModelAdapter {
	/**
	 * 技能的处理时机:战斗时
	 */
	@FieldIgnore
	public static final int EFFECT_TYPE_IN_BATTLE = 1;
	
	/**
	 * 技能的处理时机:非战斗时
	 */
	@FieldIgnore
	public static final int EFFECT_TYPE_OUT_BATTLE = 2;
	
	/**
	 * 技能id
	 */
	private int skillId;
		
	/**
	 * 触发条件 1.装备调整时触发 2.布阵时触发 3生命值上限达到 4.等级达到 5.攻击力达到 6.防御力达到
	 */
	private int triggerItem;
	
	/**
	 * 触发值,triggerItem为1，则这里填上装备的id，  如triggerItem为2，则这里的格式是 subCondition|val1_val2...的格式,subCondition是子条件,取值为: 1.齐上阵 2.同一排 3.同一列 4.在指定仙人后面
	 */
	private String triggerValue;
	
	/**
	 * 效果id
	 */
	@FieldIgnore
	private int effectId;
	
	@FieldIgnore
	private String name;
	
	/**
	 * 效果类型, 1为战斗中处理的效果， 2为战斗外处理的效果		  
	 */
	private int effectType;

	/**
	 * 技能效果列表
	 */
	@FieldIgnore
	private List<InbattleEffectConfig> skillEffects = new ArrayList<InbattleEffectConfig>();
	
	@FieldIgnore
	private List<OutbattleEffectConfig> statusEffects = new ArrayList<OutbattleEffectConfig>();

	@Override
	public void initialize() {
	}

	public int getSkillId() {
		return skillId;
	}

	public String getName() {
		return name;
	}

	public String getTriggerValue() {
		return triggerValue;
	}

	public int getEffectId() {
		return effectId;
	}

	public List<InbattleEffectConfig> getSkillEffects() {
		return skillEffects;
	}

	public int getTriggerItem() {
		return triggerItem;
	}

	public List<OutbattleEffectConfig> getStatusEffects() {
		return statusEffects;
	}

	public int getEffectType() {
		return effectType;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void clean(){
		skillEffects.clear();
		statusEffects.clear();
	}

}
