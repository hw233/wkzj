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
 * 	目标一格射程范围的敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget25 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		Fighter target = context.getTargetEnermy();
		List<Fighter> list = new ArrayList<>();
		if (target != null) {
			for (Fighter e : enermyTeam) {
				int x = Math.abs(e.getTile().getX() - target.getTile().getX());
				int y = Math.abs(e.getTile().getY() - target.getTile().getY());
				int distant = Math.max(x, y);
				if (distant == 1) {
					list.add(e);
				}
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget25;
	}

}