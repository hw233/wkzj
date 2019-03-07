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
 * 找出与目标及身后一排
 * @author vinceruan
 *
 */
@Component
public class EffectTarget22 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		Fighter target = context.getTargetEnermy();
		if (target == null) {
			return list;
		}
		
		list.add(target);
		
		for (Fighter enemy : enermyTeam) {
			int diff = enemy.getTile().getY() - target.getTile().getY();
			if (1 == Math.abs(diff) && enemy.getCamp().isBehind(enemy.getTile(), target.getTile())) {
				list.add(enemy);
			}
		}
		
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget22;
	}

}
