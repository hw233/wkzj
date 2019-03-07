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
 * 一格射程内的敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget21 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker,
			List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		int x = attacker.getTile().getX();
		int y = attacker.getTile().getY();
		
		for (Fighter enemy : enermyTeam) {
			int diffx = Math.abs(x - enemy.getTile().getX());
			int diffy = Math.abs(y - enemy.getTile().getY());
			if (diffx <= 1 && diffy <= 1) {
				list.add(enemy);
			}
		}
		
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget21;
	}
}
