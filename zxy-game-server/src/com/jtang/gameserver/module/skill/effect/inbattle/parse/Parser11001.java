package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据被击者的属性 计算百分比伤害
 * @author ludd
 *
 */
@Component
public class Parser11001 extends AbstractInBattleEffectParser {
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		String[] strs = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(Byte.valueOf(strs[0]));
		int maxValue = Integer.valueOf(strs[2]);
		int hurtValue = 0;
		
		if (key.equals(AttackerAttributeKey.ATK)) {
			hurtValue = FormulaHelper.executeCeilInt(strs[1], target.getAtk()) ;
		} else if (key.equals(AttackerAttributeKey.DEFENSE)) {
			hurtValue = FormulaHelper.executeCeilInt(strs[1], target.getDefense()) ;
		} else if (key.equals(AttackerAttributeKey.HP_MAX)) {
			hurtValue = FormulaHelper.executeCeilInt(strs[1], target.getHpMax());
		} else {
			LOGGER.error(String.format("不支持的属性id:[%s]", key.getCode()));
			report.valid = false;
			return false;
		}
		hurtValue = hurtValue > maxValue ? maxValue : hurtValue;
		addHurt(caster, target, report, effect, context, hurtValue);
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser11001;
	}
}
