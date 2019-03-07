package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 减少目标的防御力(在目标的基础防御力的基础上计算减少量)
 * @author vinceruan
 *
 */
@Component
public class Parser11420 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report,InbattleEffectConfig effect, Context context) {
		int defense = target.getDefense();
		return decrDefComputedByOneArg(caster, target, report, defense, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser11420;
	}
}
