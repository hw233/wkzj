package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 反击(根据被伤害值计算反击的伤害值)
 * @author vinceruan
 *
 */
@Component
public class Parser1101 extends AbstractInBattleEffectParser{

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		if (context.getCommonAtkHurt() <= 0) {
			report.valid = false;
			return false;
		}
		
		int arg = context.getCommonAtkHurt();
		return addHurtComputedByOneArg(caster, target, report, arg, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1101;
	}

}
