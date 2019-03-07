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
public class EffectTarget23 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		int distant = 1000;
		Fighter target = null;
		for (Fighter enemy : enermyTeam) {
			if (enemy.isDead()) {
				continue;
			}
			if (enemy.getTile().getX() == attacker.getTile().getX()) {
				int distant1 = Math.abs(attacker.getTile().getY() - enemy.getTile().getY());
				if (distant1 < distant) {
					distant = distant1;
					target = enemy;
				}
			}
		}
		
		if (target != null) {
			list.add(target);
		}
		
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget23;
	}

}
