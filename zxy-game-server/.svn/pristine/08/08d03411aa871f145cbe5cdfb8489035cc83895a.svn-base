package com.jtang.gameserver.module.app.model.extension.rule;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO12 implements BaseRuleConfigVO{

	/**
	 * 充值金额
	 */
	private int num;
	
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_12;
	}

	@Override
	public void init(String record) {
		if(StringUtils.isBlank(record) == false){
			this.num = Integer.valueOf(record);
		}
	}
	
	public int getNum() {
		return num;
	}

}
