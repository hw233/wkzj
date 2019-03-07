package com.jtang.gameserver.module.skill.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.gameserver.dataconfig.model.OutbattleEffectConfig;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.module.skill.effect.outbattle.OutBattleEffectContext;
import com.jtang.gameserver.module.skill.effect.outbattle.OutBattleEffectParser;

public class SkillHeper {
	protected final static Logger LOGGER = LoggerFactory.getLogger(SkillHeper.class);
	
	
	/**
	 * 获取由技能引起的buffer
	 * @param buffers
	 * @param hero
	 * @param activeSkillSet
	 */
	public static List<BufferVO> getOutBattleBufferRaisedBySkill(long actorId, HeroVO hero, Collection<Integer> activeSkillSet, OutBattleEffectContext effectContext) {
		List<BufferVO> result = new ArrayList<>();
		for (int skillId : activeSkillSet) {
			PassiveSkillConfig skill = SkillService.getPassiveSkill(skillId);
			if (skill == null) {
				continue;
			}
			//如果不是战斗前需要处理的技能,直接跳过
			if (skill.getEffectType() != PassiveSkillConfig.EFFECT_TYPE_OUT_BATTLE) {
				continue;
			}
			
			OutbattleEffectConfig effect = SkillService.getOutbattleEffect(skill.getEffectId());
			
			OutBattleEffectParser effectParser = effectContext.getParser(effect.getParserId());
			if (effectParser != null) {
				List<BufferVO> list = effectParser.parser(actorId, hero.heroId, effect);
				for (BufferVO buffer : list) {
					buffer.sourceType = skill.getTriggerItem();
//					bufferFacade.addBuff(actorId, hero.getHeroId(), buffer);
				}
				result.addAll(list);
			} else {
				LOGGER.info("阵型调整效果处理器没法找到,id:[{}]", skill.getEffectId());
			}
		}
		
		return result;
	}
	
}
