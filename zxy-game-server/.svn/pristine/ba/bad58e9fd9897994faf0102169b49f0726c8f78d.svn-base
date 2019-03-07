package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据攻击者的攻击力、被攻击者的防御力、被攻击者的星级计算伤害
 * @author vinceruan
 *
 */
@Component
public class Parser1108 extends AbstractInBattleEffectParser {
	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1108;
	}
	
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int arg1 = caster.getAtk();
		int arg2 = target.getDefense();
		int arg3 = target.getSpriteStar();
		
		return addHurtComputedByThreeArg(caster, target, report, effect, context, arg1, arg2, arg3);
	}
}
