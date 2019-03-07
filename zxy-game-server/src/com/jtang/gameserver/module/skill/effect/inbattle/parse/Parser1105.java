package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 回复目标血量(根据施法者的生命上限计算回复量)
 * </pre>
 * @author vinceruan
 *
 */
@Component
public class Parser1105 extends AbstractInBattleEffectParser {

	@Override
	protected boolean castSkill(Fighter caster, Fighter target,TargetReport report, InbattleEffectConfig effect, Context context) {
		int arg = caster.getBaseHpMax();
		return this.addHpComputedByOneArg(caster, target, report, effect, context, arg);		
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1105;
	}
}
