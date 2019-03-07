package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 根据攻击者的攻击力、被攻击者的防御力、攻击者和被击者的距离计算伤害
 * @author vinceruan
 *
 */
@Component
public class Parser1109 extends AbstractInBattleEffectParser {

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1109;
	}
	
	public boolean castSkill(Fighter caster, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int arg1 = caster.getAtk();
		int arg2 = target.getDefense();
		int distantx = Math.abs(caster.getTile().getX() - target.getTile().getX());
		int distanty = Math.abs(caster.getTile().getY() - target.getTile().getY());
		int arg3 = Math.max(distantx, distanty);
		return addHurtComputedByThreeArg(caster, target, report, effect, context, arg1, arg2, arg3);
	}
}
