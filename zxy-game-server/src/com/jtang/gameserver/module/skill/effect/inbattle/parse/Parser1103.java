package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 吸血(根据伤害值计算吸血量)
 * @author vinceruan
 *
 */
@Component
public class Parser1103 extends AbstractInBattleEffectParser{
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report,InbattleEffectConfig effect, Context context) {
		if (context.getCommonAtkHurt() <= 0) {
			report.valid = false;
			return false;
		}
		
		int arg = context.getCommonAtkHurt();
		return addHpComputedByOneArg(caster, target, report, effect, context, arg);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1103;
	}
}
