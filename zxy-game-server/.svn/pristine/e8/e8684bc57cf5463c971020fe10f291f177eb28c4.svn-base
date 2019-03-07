package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 提高目标的防御值(根据施法者的防御力计算增加值)
 * @author vinceruan
 *
 */
@Component
public class Parser1141 extends AbstractInBattleEffectParser{
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int defense = caster.getBaseDefense();
		return addDefComputedByOneArg(caster, target, report, defense, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1141;
	}
}
