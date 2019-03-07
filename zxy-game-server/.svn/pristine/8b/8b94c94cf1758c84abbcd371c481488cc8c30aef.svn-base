package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 提高目标的生命上限值(根据目标的基础生命上限值计算加成)
 * @author vinceruan
 *
 */
@Component
public class Parser1110 extends AbstractInBattleEffectParser{
	@Override
	public boolean castSkill(Fighter attacker, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int arg = target.getBaseHpMax();
		
		return addHpMaxComputedByOneArg(attacker, target, report, effect, context, arg);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1110;
	}
}
