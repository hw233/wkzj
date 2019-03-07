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
 * 找出施法者身后的好友
 * @author vinceruan
 *
 */
@Component
public class EffectTarget16 extends AbstractEffectTargetParser {

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget16;
	}

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();		
		int y = attacker.getTile().getY();
		for (Fighter fighter : friendTeam) {
			if (attacker.getCamp().isBehind(fighter.getTile(), attacker.getTile()) && Math.abs(fighter.getTile().getY() - y) == 1) {
				targetList.add(fighter);
			}
		}
		return targetList;
	}

}
