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
 *  返回施法者所在的整排好友(包括自己)
 * @author vinceruan
 *
 */
@Component
public class EffectTarget9 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();			
		if (attacker == null) {
			return targetList;
		}			
		int y = attacker.getTile().getY();
		for (Fighter fighter : friendTeam) {
			if (fighter.getTile().getY() == y) {
				targetList.add(fighter);
			}
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget9;
	}

}
