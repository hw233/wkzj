package com.jtang.gameserver.module.app.model.extension.rule;

import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO10 implements BaseRuleConfigVO {

	/**
	 * 可领取的点券数
	 */
	public int ticket;

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_10;
	}

	@Override
	public void init(String record) {
		ticket = Integer.valueOf(record);
	}

}
