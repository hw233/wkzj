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
 * 目标身后的敌人
 * @author vinceruan
 *
 */
@Component
public class EffectTarget19 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<Fighter>();
		
		if (context.getTargetEnermy() == null) {
			return list;
		}
		
		Fighter target = context.getTargetEnermy();
		for (Fighter f : enermyTeam) {
			if(target.getCamp().isBehind(f.getTile(), target.getTile()) 
					&& target.getTile().getX() == f.getTile().getX() 
					&& Math.abs(target.getTile().getY() - f.getTile().getY()) == 1) {
				list.add(f);
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget19;
	}

}
