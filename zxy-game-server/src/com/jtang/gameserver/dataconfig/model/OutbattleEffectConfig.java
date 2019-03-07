package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;

/**
 * 状态效果配置
 * 
 * @author vinceruan
 * 
 */
@DataFile(fileName = "outbattleEffectConfig")
public class OutbattleEffectConfig implements ModelAdapter {
	/**
	 * 效果ID
	 */
	private int effectId;
	
	/**
	 * 解释器ID
	 */
	private int parserId;
	
	/**
	 * 效果表达式
	 */
	private String effectExpr;
	
	@FieldIgnore
	private int skillId;
	
	@FieldIgnore
	private String skillName;

	@Override
	public void initialize() {
		//预加载一次公式 
		Number[] args = new Number[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		FormulaHelper.execute(effectExpr, args);
	}

	public int getEffectId() {
		return effectId;
	}

	public int getParserId() {
		return parserId;
	}

	public String getEffectExpr() {
		return effectExpr;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
}
