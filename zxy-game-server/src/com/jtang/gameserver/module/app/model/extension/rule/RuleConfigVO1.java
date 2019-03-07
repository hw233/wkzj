package com.jtang.gameserver.module.app.model.extension.rule;

import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO1 implements BaseRuleConfigVO {

	public int needMoney;

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_1;
	}

	@Override
	public void init(String record) {
		needMoney = Integer.valueOf(record);
	}

}
