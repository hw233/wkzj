package com.jtang.gameserver.module.app.model.extension.rule;

import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO20 implements BaseRuleConfigVO {

	/** 新号首日需要到达等级可获得奖励*/
	private int needLevel;

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_20;
	}

	@Override
	public void init(String record) {
		needLevel = Integer.valueOf(record);
	}

	/**
	 * @return the needLevel
	 */
	public int getNeedLevel() {
		return needLevel;
	}
}
