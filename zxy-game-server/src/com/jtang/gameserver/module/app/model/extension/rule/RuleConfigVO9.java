package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.List;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO9 implements BaseRuleConfigVO {
	
	/**
	 * 活动类型
	 */
	public int type;

	/**
	 * 额外参数
	 */
	public int ext;

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_9;
	}

	@Override
	public void init(String record) {
		List<Integer> list = StringUtils.delimiterString2IntList(record, Splitable.ATTRIBUTE_SPLIT);
		type = list.get(0);
		ext = list.get(1);
	}

}
