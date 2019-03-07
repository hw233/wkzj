package com.jtang.gameserver.module.skill.effect.outbattle.parse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.gameserver.dataconfig.model.OutbattleEffectConfig;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.skill.effect.OutbattleParserKey;
import com.jtang.gameserver.module.skill.effect.outbattle.AbstractOutBattleEffectParser;

/**
 * 技能切换
 * @author vinceruan
 *
 */
@Component
public class Parser120 extends AbstractOutBattleEffectParser {
	
	@Override
	public List<BufferVO> parser(long actorId, int heroId, OutbattleEffectConfig effect) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		int skillId = hero.getSkillId();
		String expr = effect.getEffectExpr();
		int newSkill = Integer.parseInt(expr);
		
		if (skillId != newSkill) {
			hero.skillId = newSkill;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OutBattle,技能效果被激活且处理, heroId:[{}], effectName:[{}]", hero.getHeroId(), effect.getSkillName());
			}
			heroFacade.updateHero(actorId, hero);
			HeroPushHelper.pushUpdateHero(actorId, hero.getHeroId(), HeroVOAttributeKey.SKILL_ID);
		}
		
		return new ArrayList<>();
	}

	@Override
	public int getParserId() {
		return OutbattleParserKey.Parser120;
	}

}
