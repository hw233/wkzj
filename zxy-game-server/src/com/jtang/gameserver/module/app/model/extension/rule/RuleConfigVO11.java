package com.jtang.gameserver.module.app.model.extension.rule;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;
@AppVO
public class RuleConfigVO11 implements BaseRuleConfigVO{

	/**
	 * 翻倍倍数
	 */
	private int num;
	
	/**
	 * 持续时间 （秒）
	 */
	private int time;
	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_11;
	}

	@Override
	public void init(String record) {
		String[] items = StringUtils.split(record, Splitable.ATTRIBUTE_SPLIT);
		items = StringUtils.fillStringArray(items, 2, "1");
		this.num = Integer.valueOf(items[0]);
		this.time = Integer.valueOf(items[1]) * 60;
	}
	
	public int getNum() {
		return num;
	}
	
	public int getTime() {
		return time;
	}

}
