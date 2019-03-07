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
 * 受到治疗时给目标加属性
 * @author ludd
 *
 */
@Component
public class Parser2007 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2007;
	}
	
	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		String[] values = StringUtils.split(effect.getEffectExpr(), Splitable.ELEMENT_SPLIT);
		byte type = Byte.valueOf(values[0]);
		
		AttackerAttributeKey key = AttackerAttributeKey.getByCode(type);
		
		int value = 0;
		switch (key) {
		case ATK: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseAtk());
			this.addAtkBuffer(caster, target, report, value, effect, context);
			break;
		}
		case DEFENSE: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseDefense());
			this.addDeffendsBuffer(caster, target, report, value, effect, context);
			break;
		}
		case HP_MAX: {
			incrHpMax(caster, target, report, effect, context, value);
			break;
		}

		default:
			LOGGER.error(String.format("解析数值错误，类型:[%s]", type));
			return false;
		}
		return true;
	}

}
