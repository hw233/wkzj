package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/** 
 * 提高技能的触发几率.
 * @author vinceruan
 *
 */
@Component
public class Parser1130 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter attacker, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		report.valid = false;
		String expr[] = effect.getEffectExpr().split(Splitable.ELEMENT_SPLIT);
		int addRate = FormulaHelper.executeInt(expr[1]);		
		attacker.addSkillRateBuffer(Integer.valueOf(expr[0]), addRate);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[{}]释放技能[{}],对[{}]的技能[{}]的触发几率提高了[{}]", attacker.getName(), effect.getSkillName(), target.getName(), expr[0], addRate);
		}
		return true;
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1130;
	}
}
