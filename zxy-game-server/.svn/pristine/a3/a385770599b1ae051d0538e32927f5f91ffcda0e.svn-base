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
 * 施法者所在的列敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget3 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		int x = attacker.getTile().getX();
		
		for (Fighter target : enermyTeam) {			
			int targetX = target.getTile().getX();		
			if (targetX == x ) {
				list.add(target);
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget3;
	}

}
