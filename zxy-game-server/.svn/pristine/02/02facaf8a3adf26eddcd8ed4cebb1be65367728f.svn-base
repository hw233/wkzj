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
 * 敌方男性仙人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget18 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> targetList = new ArrayList<Fighter>();
		for (Fighter f : enermyTeam) {
			if (f.getSpriteSex() == 1) {
				targetList.add(f);
			}
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget18;
	}

}
