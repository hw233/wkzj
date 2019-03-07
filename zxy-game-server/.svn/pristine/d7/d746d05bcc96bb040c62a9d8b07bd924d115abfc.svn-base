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
 * 位于施法者前面的同列的友方人员
 * @author vinceruan
 *
 */
@Component
public class EffectTarget24 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<>();
		for(Fighter target : friendTeam) {
			if (attacker.getCamp().isBehind(attacker.getTile(), target.getTile())) {
				if (attacker.getTile().getX() == target.getTile().getX()) {
					list.add(target);
				}
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget24;
	}

}
