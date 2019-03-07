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
 * 随机一个友方受伤人员 (存活者且不包含自己)
 * @author ludd
 *
 */
@Component
public class EffectTarget28 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		//随机返回一个受伤的友方人员不包含自己.
		List<Fighter> list = new ArrayList<Fighter>();
		for (Fighter friend : friendTeam) {
			if (friend.isHurt() && !friend.isDead() && friend.getFighterId() != attacker.getFighterId()) {
				list.add(friend);
			}
		}
		List<Fighter> targetList = new ArrayList<Fighter>();
		if (list.size() > 0) {
			int index = RandomUtils.nextIntIndex(list.size());
			targetList.add(list.get(index));
		}
		return targetList;
	}

	@Override
	protected int getType() {
		return EffectTarget.EffectTarget28;
	}

}
