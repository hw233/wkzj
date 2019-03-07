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
 *  返回施法者前后左右的好友并，包含自己
 * @author ludd
 *
 */
@Component
public class EffectTarget29 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();		
		int x = attacker.getTile().getX();
		int y = attacker.getTile().getY();
		for (Fighter friend : friendTeam) {
			int diffx = Math.abs(friend.getTile().getX() - x);
			int diffy = Math.abs(friend.getTile().getY() - y);
			if ((diffx == 1 || diffy == 1) && diffx + diffy == 1) {
				targetList.add(friend);
			}
		}
		targetList.add(attacker); //添加自己
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget29;
	}

}
