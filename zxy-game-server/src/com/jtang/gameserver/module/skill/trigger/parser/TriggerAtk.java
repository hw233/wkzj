package com.jtang.gameserver.module.skill.trigger.parser;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.skill.trigger.AbstractPassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

/**
 * 攻击力达到激活被动技能
 * @author vinceruan
 *
 */
@Component
public class TriggerAtk extends AbstractPassiveSkillTriggerParser {

	@Override
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup) {
		int req_atk =  Integer.valueOf(skillConfig.getTriggerValue());
		int atk = hero.getAtk();
		return atk >= req_atk;
	}

	@Override
	protected int getType() {
		return SkillTriggerItem.TriggerAtk;
	}

}
