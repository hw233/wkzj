package com.jtang.gameserver.module.skill.target.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.target.AbstractEffectTargetParser;
import com.jtang.gameserver.module.skill.type.EffectTarget;

/**
 * 	施法者所在列的最近一个敌人及紧跟其身后的另一个敌人 
 * @author vinceruan
 *
 */
@Component
public class EffectTarget26 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		List<Fighter> list = new ArrayList<>();
		Map<Integer, Fighter> map = new HashMap<Integer, Fighter>();
		for (Fighter e : enermyTeam) {
			if (e.isDead()) {
				continue;
			}
			if (e.getTile().getX() == attacker.getTile().getX()) {
				map.put(e.getTile().getY(), e);
			}
		}
		Iterator<Integer> keys = map.keySet().iterator();
		if(keys.hasNext()) {
			int k1 = keys.next();
			if (keys.hasNext()) {
				int k2 = keys.next();
				Fighter enemy1 = map.get(k1);
				Fighter enemy2 = map.get(k2);
				if (enemy1.getCamp().isBehind(enemy2.getTile(), enemy1.getTile())) {
					list.add(enemy1);
					list.add(enemy2);
				}
			}
		}
		return list;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget26;
	}

}