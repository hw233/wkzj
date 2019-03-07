package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 
 * 提高防御力(在当前防御力的基础上面加成)
 * @author vinceruan
 *
 */
@Component
public class Parser11400 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter attacker, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int baseDefense = target.getDefense();
		return addDefComputedByOneArg(attacker, target, report, baseDefense, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser11400;
	}
}
