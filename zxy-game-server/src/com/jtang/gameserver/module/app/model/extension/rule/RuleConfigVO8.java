package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExchangeLoginVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO8 implements BaseRuleConfigVO {

	/**
	 * 奖励详情
	 * key: 可领奖天数
	 * value:奖励物品id
	 */
	public Map<Integer,ExchangeLoginVO> reward = new HashMap<>();
	
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_8;
	}

	@Override
	public void init(String record) {
		List<String[]> list = StringUtils.delimiterString2Array(record);
		for(String[] array:list){
			ExchangeLoginVO exchgeLoginVO = ExchangeLoginVO.valueOf(array);
			reward.put(exchgeLoginVO.loginDay, exchgeLoginVO);
		}
	}

}
