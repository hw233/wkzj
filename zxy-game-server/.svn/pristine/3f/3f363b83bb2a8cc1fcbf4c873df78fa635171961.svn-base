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
 *  返回目标所在的一列敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget11 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();
		Fighter target = context.getTargetEnermy();
		if (target == null) {
			return targetList;
		}			
		int x = target.getTile().getX();
		for (Fighter fighter : enermyTeam) {
			if (fighter.getTile().getX() == x) {
				targetList.add(fighter);
			}
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget11;
	}

}
