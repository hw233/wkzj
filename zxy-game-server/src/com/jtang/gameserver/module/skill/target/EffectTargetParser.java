package com.jtang.gameserver.module.skill.target;

import java.util.List;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;

/**
 * 找出技能的作用目标
 * @author vinceruan
 *
 */
public interface EffectTargetParser {
	public List<Fighter> parseEffectTargets(Fighter attacker, List<Fighter> friendTeam, List<Fighter> enermyTeam, Context context, InbattleEffectConfig inbattleEffectConfig);
}
