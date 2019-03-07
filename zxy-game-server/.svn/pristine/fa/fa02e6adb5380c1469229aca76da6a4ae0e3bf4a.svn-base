package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.buffer.type.BufferType;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 
 * @author vinceruan
 *
 */
@Component
public class Parser1144 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (target.isDead()) {
			report.valid = false;
			return false;
		}
		
		//加防御力
		int def = target.getBaseDefense();
		int x2 = 1;
		int addDef = FormulaHelper.executeCeilInt(effect.getEffectExpr(), def, x2);
		addDef = ensureAddValidDef(addDef);
		
		addBuffer(caster, target, report, AttackerAttributeKey.DEFENSE, effect, context, addDef, true, BufferType.ATTR_BUFFER);
		
		//加血
		int hpMax = target.getBaseHpMax();
		x2 = 2;
		int addHp = FormulaHelper.executeCeilInt(effect.getEffectExpr(), hpMax, x2);
		incrHpMax(caster, target, report, effect, context, addHp);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]增加了攻击力值[{}]和生命值[{}]", caster.getName(), effect.getSkillName(), target.getName(), addDef, addHp);
		}
		
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1144;
	}
}
