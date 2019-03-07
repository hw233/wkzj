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
 * 随机选出一个受伤的好友
 * @author vinceruan
 *
 */
@Component
public class EffectTarget7 extends AbstractEffectTargetParser {

	@Override
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig) {
		//随机返回一个受伤的友方人员.
		List<Fighter> list = new ArrayList<Fighter>();
		for (Fighter friend : friendTeam) {
			if (friend.isHurt() && !friend.isDead()) {
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
		return EffectTarget.EffectTarget7;
	}

}
