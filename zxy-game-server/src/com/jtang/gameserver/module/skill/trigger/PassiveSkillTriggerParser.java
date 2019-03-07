package com.jtang.gameserver.module.skill.trigger;

import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;

/**
 * 被动技能的激活处理器
 * @author vinceruan
 *
 */
public interface PassiveSkillTriggerParser {
	/**
	 * 是否可以激活被动技能
	 * @param hero
	 * @param skillConfig
	 * @param companions
	 * @return
	 */
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup);
}
