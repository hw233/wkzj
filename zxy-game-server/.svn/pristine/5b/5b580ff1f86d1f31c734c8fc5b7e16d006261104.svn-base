package com.jtang.gameserver.module.skill.effect.inbattle.parse;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.effect.InbattleParserKey;
import com.jtang.gameserver.module.skill.effect.inbattle.AbstractInBattleEffectParser;
import com.jtang.gameserver.module.skill.model.TargetReport;

/**
 * 回复目标血量(根据施法者的攻击力、目标的仙人ID计算回复量)
 * @author vinceruan
 *
 */
@Component
public class Parser1104 extends AbstractInBattleEffectParser {

	@Override
	protected boolean castSkill(Fighter attacker, Fighter target, TargetReport report, InbattleEffectConfig effect, Context context) {
		int atk = attacker.getBaseAtk();
		int heroId = target.getHeroId();
		return addHpComputedByTwoArg(attacker, atk, heroId, target, report, effect, context);
	}

	@Override
	protected int getParserId() {
		return InbattleParserKey.Parser1104;
	}
}
