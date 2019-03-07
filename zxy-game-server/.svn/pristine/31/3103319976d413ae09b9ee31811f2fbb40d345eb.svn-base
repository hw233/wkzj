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
 * 紧贴着的正前方且生命值小于一半的好友
 * @author vinceruan
 *
 */
@Component
public class EffectTarget20 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter caster, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<>();
		
		Tile tile = caster.getCamp().getAheadPosition(caster.getTile());
		if (context.getBattleMap().isWalk(tile)) {//上面没人
			return list;
		}
		for (Fighter friend : friendTeam) {			
			if (friend.getTile().equals(tile)) {
				if (friend.isDead() == false && friend.getHp() * 2 < friend.getHpMax()) {
					list.add(friend);
				}
				break;
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget20;
	}

}
