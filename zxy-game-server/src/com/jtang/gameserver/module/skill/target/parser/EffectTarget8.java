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
 *  返回目标身后的一排敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget8 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();
		Fighter target = context.getTargetEnermy();
		if (target == null) {
			return targetList;
		}			
		int y = target.getTile().getY();
		for (Fighter fighter : enermyTeam) {
			if (target.getCamp().isBehind(fighter.getTile(), target.getTile()) && Math.abs(fighter.getTile().getY() - y) == 1) {
				targetList.add(fighter);
			}
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget8;
	}

}
