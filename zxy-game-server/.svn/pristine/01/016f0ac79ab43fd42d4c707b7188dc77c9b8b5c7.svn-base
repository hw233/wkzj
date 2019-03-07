package com.jtang.gameserver.module.skill.target.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.Tile;
import com.jtang.gameserver.module.skill.target.AbstractEffectTargetParser;
import com.jtang.gameserver.module.skill.type.EffectTarget;

/**
 * 返回目标身后的一列敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget12 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();
		Fighter target = context.getTargetEnermy();
		if (target == null) {
			return targetList;
		}
		
		//目标
		targetList.add(target);
		
		//目标身后的同列敌人
		Tile reference = target.getTile();
		for (Fighter fighter : enermyTeam) {
			if (fighter.getTile().getX() == reference.getX() && target.getCamp().isBehind(fighter.getTile(), reference)) {
				targetList.add(fighter);
			}
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget12;
	}

}
