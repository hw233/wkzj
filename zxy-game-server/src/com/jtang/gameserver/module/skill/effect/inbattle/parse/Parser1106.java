package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据攻击者的攻击力, 被攻击者的防御力 计算伤害
 * @author vinceruan
 *
 */
@Component
public class Parser1106 extends AbstractInBattleEffectParser {
	@Override
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int atk = caster.getAtk();
		int defense = target.getDefense();
		
		return addHurtComputedByTwoArg(caster, target, report, effect, context, atk, defense);
	}
	
	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1106;
	}
}
