package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据施法者的基础攻击值减目标防御
 * @author ludd
 *
 */
public class Parser1146 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1146;
	}

	@Override
	protected boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int baseAtk = caster.getBaseAtk();
		return decrDefComputedByOneArg(caster, target, report, baseAtk, effect, context);
	}

}
