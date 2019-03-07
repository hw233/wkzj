package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;
/**
 * 伤害加深
 * @author ludd
 *
 */
@Component
public class Parser2008 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser2008;
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
			break;
		}
		case DEFENSE: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseDefense());
			break;
		}
		case HP_MAX: {
			value = FormulaHelper.executeInt(values[1], caster.getBaseHpMax());
			break;
		}

		default:
			LOGGER.error(String.format("解析数值错误，类型:[%s]", type));
			return false;
		} 
		FighterBuffer fighterBuffer = new FighterBuffer(context.generateBufferId(), value, AttackerAttributeKey.HERT_CHANGE, caster, target, effect, true, BufferType.HURT_CHANGE_BUFFER);
		addBuffer(fighterBuffer, target, effect, context, report);
		
		return true;
	}

}
