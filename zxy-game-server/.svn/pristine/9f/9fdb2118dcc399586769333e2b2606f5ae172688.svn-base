package com.jtang.gameserver.module.skill.effect.inbattle;

import java.util.List;

import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.Fighter;

/**
 * 技能效果解析接口
 * @author vinceruan
 *
 */
public interface InBattleEffectParser {
	/**
	 * 
	 * @param caster
	 * @param targets
	 * @param effect
	 * @param context
	 * @return 技能成功释放返回ture,否则返回false
	 */
	boolean parser(Fighter caster, List<Fighter> targets, InbattleEffectConfig effect, Context context);
}
