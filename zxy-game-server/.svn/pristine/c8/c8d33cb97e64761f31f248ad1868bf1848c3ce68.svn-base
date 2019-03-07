package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据生命上限恢复一定量的的血.
 * @author vinceruan
 *
 */
@Component
public class Parser1102 extends AbstractInBattleEffectParser{

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report,InbattleEffectConfig effect, Context context) {
		int arg = target.getBaseHpMax();
		
		return addHpComputedByOneArg(caster, target, report, effect, context, arg);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1102;
	}
}
