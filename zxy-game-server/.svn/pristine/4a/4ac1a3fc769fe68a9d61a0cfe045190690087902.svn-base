package com.jtang.gameserver.module.skill.target.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.skill.target.AbstractEffectTargetParser;
import com.jtang.gameserver.module.skill.type.EffectTarget;

/**
 * 所有友方人员中随机N个友方
 * @author ludd
 *
 */
@Component
public class EffectTarget34 extends AbstractEffectTargetParser  {

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
		if (inbattleEffectConfig.getTargetNum() > 0) {

			int[] randomIndex = RandomUtils.uniqueRandom(
					inbattleEffectConfig.getTargetNum(), 0, list.size() - 1);

			for (int i : randomIndex) {
				result.add(list.get(i));
			}
		} else {
			result = list;
		}
		return result;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget34;
	}

}
