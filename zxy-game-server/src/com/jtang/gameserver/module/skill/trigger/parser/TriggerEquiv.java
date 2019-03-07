package com.jtang.gameserver.module.skill.trigger.parser;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;
import com.jtang.gameserver.module.skill.trigger.AbstractPassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;
//import com.jtang.sm2.module.lineup.model.LineupGridItem;

/**
 * 穿上装备激活被动技能
 * @author vinceruan
 *
 */
@Component
public class TriggerEquiv extends AbstractPassiveSkillTriggerParser {

	@Autowired
	EquipFacade equipFacade;
	
	@Override
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup) {
		String equivs[] = skillConfig.getTriggerValue().split(Splitable.ELEMENT_SPLIT);
		Set<Integer> set = new HashSet<Integer>();
		for (String equiv : equivs) {
			set.add(Integer.valueOf(equiv));
		}

		LineupHeadItem item = null;
		for (LineupHeadItem headItem : lineup.getHeadItemList()) {
			if (headItem.heroId == hero.getHeroId()) {
				item = headItem;
				break;
			}
		}

		//仙人压根没上阵
		if (item == null) {
			return false;
		}

		LineupHeadItem grid = item;

		long actorId = lineup.getPkId();

		if (grid.atkEquipUuid != 0) {
			EquipVO e = this.equipFacade.get(actorId, grid.atkEquipUuid);
			set.remove(e.equipId);
		}

		if (grid.defEquipUuid != 0) {
			EquipVO e = this.equipFacade.get(actorId, grid.defEquipUuid);
			set.remove(e.equipId);
		}

		if (grid.decorationUuid != 0) {
			EquipVO e = this.equipFacade.get(actorId, grid.decorationUuid);
			set.remove(e.equipId);
		}

		boolean result =  set.isEmpty();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("角色:[{}] 的被动技能[{}]是否被激活,结果:[{}] ", hero.getHeroId(), skillConfig.getName(), result);
		}
		
		return result;
	}

	@Override
	protected int getType() {
		return SkillTriggerItem.TriggerEquip;
	}

}
