package com.jtang.gameserver.module.skill.trigger.parser;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.skill.trigger.AbstractPassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

/**
 * 等级达到激活技能
 * @author vinceruan
 *
 */
@Component
public class TriggerLevel extends AbstractPassiveSkillTriggerParser {

	@Override
	protected int getType() {
		return SkillTriggerItem.TriggerLevel;
	}

	@Override
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup) {
		String triggerVal = skillConfig.getTriggerValue();
		if (triggerVal == null || triggerVal.trim() == "") {
			LOGGER.debug("角色:[{}] 当前的技能触发值不正确,技能ID：[{}] ", hero.getHeroId(), skillConfig.getSkillId());
			return false;
		}
		
		int level = hero.getLevel();
		int expectLevel = Integer.valueOf(triggerVal.trim());
		return level >= expectLevel;
	}
}
