package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
//import com.jtang.sm2.module.skill.type.CastTarget;

/**
 * 主动技能配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "skillConfig")
public class SkillConfig implements ModelAdapter {

	/**
	 * 技能唯一Id
	 */
	private int skillId = 0;
	
	/**
	 * 技能名称
	 */
	private String name = "";
	
	/**
	 * 效果id
	 */
	private int effectId = 0;
	
	/**
	 * 技能效果列表
	 */
	@FieldIgnore
	private List<InbattleEffectConfig> skillEffects = new ArrayList<InbattleEffectConfig>();
	
	

	@Override
	public void initialize() {
		Assert.isTrue(skillId > 0 && effectId > 0, "skillId和effectId不能为空, skillId="+skillId);
	}
	
	public void clearSkillEffects() {
		skillEffects.clear();
	}
	
	public int getSkillId() {
		return skillId;
	}

	public String getName() {
		return name;
	}

	public int getEffectId() {
		return effectId;
	}
	
	public List<InbattleEffectConfig> getSkillEffects() {
		return skillEffects;
	}
}
