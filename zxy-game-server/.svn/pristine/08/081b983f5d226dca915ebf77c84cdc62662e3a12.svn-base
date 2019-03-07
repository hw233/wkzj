package com.jtang.gameserver.module.skill.target.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.target.AbstractEffectTargetParser;
import com.jtang.gameserver.module.skill.type.EffectTarget;
/**
 * 低于5星的目标
 * @author ludd
 *
 */
@Component
public class EffectTarget30 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		Fighter target = context.getTargetEnermy();
		if (target != null && target.getSpriteStar() <= 5) {
			list.add(target);
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget30;
	}

}
