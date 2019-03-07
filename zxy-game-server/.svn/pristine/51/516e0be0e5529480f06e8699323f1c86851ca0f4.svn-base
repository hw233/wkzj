package com.jtang.gameserver.module.app.model.extension.rule;

import java.util.Date;
import java.util.List;

import com.jtang.core.utility.DatePattern;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;
import com.jtang.gameserver.module.app.type.EffectId;

@AppVO
public class RuleConfigVO6 implements BaseRuleConfigVO {

	/**
	 * 开始刷新时间
	 */
	public int startFlushTime;
	
	/**
	 * 每次循环间隔时间(分钟)
	 */
	public int fixTime;

	@Override
	public EffectId getEffectId() {
		return EffectId.EFFECT_ID_6;
	}

	@Override
	public void init(String record) {
		List<String> list = StringUtils.delimiterString2List(record, Splitable.ATTRIBUTE_SPLIT);
		Date data = DateUtils.string2Date(list.get(0), DatePattern.PATTERN_NORMAL);
		startFlushTime = (int) (data.getTime()/1000);
		fixTime = Integer.valueOf(list.get(1));
	}

}
