package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExtBuyHeroVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO15 implements BaseRuleConfigVO {

	public Map<Integer,ExtBuyHeroVO> map = new HashMap<>();
	
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_15;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record, Splitable.ELEMENT_SPLIT, Splitable.ATTRIBUTE_SPLIT);
		for(String[] str:list){
			ExtBuyHeroVO heroVO = new ExtBuyHeroVO(str);
			map.put(heroVO.heroId, heroVO);
		}
	}

}
