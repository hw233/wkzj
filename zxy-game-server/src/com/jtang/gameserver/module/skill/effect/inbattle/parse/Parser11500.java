package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 提高目标的攻击力(根据施法者的攻击力计算加成值)
 * @author vinceruan
 *
 */
@Component
public class Parser11500 extends AbstractInBattleEffectParser {

	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int atk = caster.getAtk();
		return addAtkComputedByOneArg(caster, target, report, atk, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser11500;
	}
}
