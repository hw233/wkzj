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
 * 自身所在整列友方仙人
 * @author ludd
 *
 */
@Component
public class EffectTarget33 extends AbstractEffectTargetParser  {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam,
			Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		for (Fighter target : friendTeam) {
			if (target.isDead()) {
				continue;
			}
			list.add(target);
		}
		
		List<Fighter> result = new ArrayList<>();
		Tile tile = attacker.getTile();
		for (Fighter fighter : list) {
			Tile targetTile = fighter.getTile();
			if (targetTile.getX() == tile.getX()) {
				result.add(fighter);
			}
		}
		return result;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget33;
	}

}
