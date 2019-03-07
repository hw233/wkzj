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
 * 射程范围内的3个敌方
 * @author vinceruan
 *
 */
@Component
public class EffectTarget17 extends AbstractEffectTargetParser  {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam,
			Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		int x = attacker.getTile().getX();
		int y = attacker.getTile().getY();
		int atkScop = attacker.getAtkScope();
		
		int i = 0;
		for (Fighter target : enermyTeam) {
			if (target.isDead()) {
				continue;
			}
			int targetX = target.getTile().getX();
			int targetY = target.getTile().getY();
			if (Math.abs(targetX - x) <= atkScop && Math.abs(targetY - y) <= atkScop) { //范围是按方形计算的.
				list.add(target);	
				if (++i == 3) {
					break;
				}
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget17;
	}

}
