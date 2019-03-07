package com.jtang.gameserver.module.skill.trigger.parser;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.skill.trigger.AbstractPassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

/**
 * 防御值达到激活被动技能
 * @author vinceruan
 *
 */
@Component
public class TriggerDefense extends AbstractPassiveSkillTriggerParser {

	@Override
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup) {
		int req_defense = Integer.valueOf(skillConfig.getTriggerValue());
		int defense = hero.getDefense();
		return defense >= req_defense;
	}

	@Override
	protected int getType() {
		return SkillTriggerItem.TriggerDefense;
	}

}
