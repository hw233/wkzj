package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeBuyVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO5 implements BaseRuleConfigVO {

	public List<ExchangeBuyVO> buyList = new ArrayList<>();

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_5;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record);
		for (String[] strings : list) {
			buyList.add(ExchangeBuyVO.valueOf(strings));
		}
	}

}
