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
 * 找出与目标同排的敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget14 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		Fighter target = context.getTargetEnermy();
		if (target == null) {
			return list;
		}
		
		for (Fighter enemy : enermyTeam) {
			if (target.getTile().getY() == enemy.getTile().getY()) {
				list.add(enemy);
			}
		}
		
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget14;
	}

}
