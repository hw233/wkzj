package com.jtang.gameserver.module.skill.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.event.AbstractReceiver;
import com.jtang.core.event.Event;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.HeroAttributeChangeEvent;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.type.BufferSourceType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.skill.effect.outbattle.OutBattleEffectContext;
import com.jtang.gameserver.module.skill.trigger.PassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.trigger.PassiveSkillTriggerParserContext;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

/**
 * 英雄升级、属性变更事件监听器 根据英雄等级、攻击力、防御力、生命值激活相应技能
 * 
 * @author vinceruan
 *
 */
@Component
public class HeroActivedSkillProcessor extends AbstractReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeroActivedSkillProcessor.class);
	@Autowired
	HeroFacade heroFacade;

	@Autowired
	BufferFacade bufferFacade;

	@Autowired
	OutBattleEffectContext effectContext;

	@Autowired
	PassiveSkillTriggerParserContext context;

	@Override
	public String[] getEventNames() {
		return new String[] { EventKey.HERO_ATTRIBUTE_CHANGE };
	}

	@Override
	public void onEvent(Event event) {
		if (event.getName() == EventKey.HERO_ATTRIBUTE_CHANGE) {
			HeroAttributeChangeEvent e = (HeroAttributeChangeEvent) event;
			recomputeHeroSkillAndBuffer(e.actorId, e.getHeroId());
		}
	}

	/**
	 * 重新计算英雄的激活技能和buffer加成
	 * 
	 * @param actorId
	 * @param hero
	 * @param heroId
	 */
	public void recomputeHeroSkillAndBuffer(long actorId, int heroId) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		ChainLock lock = LockUtils.getLock(hero);
		try {
			lock.lock();
			List<Integer> confSkills = HeroService.get(heroId)
					.getPassiveSkills();

			Set<Integer> currentSkillsSet = new HashSet<>();
			currentSkillsSet.addAll(hero.getPassiveSkillList());

			Set<Integer> activeSkillSet = new HashSet<>();
			Set<Integer> notactiveSkillSet = new HashSet<>();

			for (Integer skillId : confSkills) {
				PassiveSkillConfig skillConf = SkillService
						.getPassiveSkill(skillId);
				if (skillConf == null) {
					continue;
				}

				// 该技能已经激活
				// if (currentSkillsSet.contains(skillId)) {
				// continue;
				// }

				// 该技能不是由等级、攻防血触发的
				if (skillConf.getTriggerItem() != SkillTriggerItem.TriggerLevel
						&& skillConf.getTriggerItem() != SkillTriggerItem.TriggerAtk
						&& skillConf.getTriggerItem() != SkillTriggerItem.TriggerDefense
						&& skillConf.getTriggerItem() != SkillTriggerItem.TriggerHpMax) {
					continue;
				}

				PassiveSkillTriggerParser parser = context.getParser(skillConf
						.getTriggerItem());

				// 检查技能是否被激活
				boolean isActive = false;

				try {
					isActive = parser.isTrigger(hero, skillConf, null);
				} catch (Exception ex) {
					LOGGER.error("", ex.getMessage());
				}

				if (isActive) {
					activeSkillSet.add(skillId);
				} else {
					notactiveSkillSet.add(skillId);
				}
			}

			// 如果由新技能被激活,则更新英雄的技能
			if (activeSkillSet.size() > 0) {
				for (Integer activeSkillId : activeSkillSet) {
					if (activeSkillId != null
							&& hero.getPassiveSkillList().contains(
									activeSkillId) == false) {
						hero.getPassiveSkillList().add(activeSkillId);
					}
				}
				heroFacade.updateHero(actorId, hero);
				HeroPushHelper.pushUpdateHero(actorId, heroId,
						HeroVOAttributeKey.PASSIVE_SKILL);
			}
			if (notactiveSkillSet.size() > 0) {
				for (Integer notactiveSkillId : notactiveSkillSet) {
					if (notactiveSkillId != null) {
						hero.getPassiveSkillList().remove(notactiveSkillId);
					}
				}
				heroFacade.updateHero(actorId, hero);
				HeroPushHelper.pushUpdateHero(actorId, heroId,
						HeroVOAttributeKey.PASSIVE_SKILL);
			}

			// 重新生成buffer
			// 先移除
			bufferFacade.removeBufferBySourceType(actorId, heroId,
					BufferSourceType.ATTACK_BUFFER,
					BufferSourceType.DEFENSE_BUFFER,
					BufferSourceType.EQUIP_BUFFER, BufferSourceType.HP_BUFFER,
					BufferSourceType.LEVEL_BUFFER,
					BufferSourceType.LINEUP_BUFFER);
			// 计算获取buf
			List<BufferVO> addBuffs = SkillHeper
					.getOutBattleBufferRaisedBySkill(actorId, hero,
							hero.getPassiveSkillList(), effectContext);
			BufferVO[] buffArr = new BufferVO[addBuffs.size()];
			// 添加buff
			bufferFacade.addBuff(actorId, hero.getHeroId(),
					addBuffs.toArray(buffArr));
			// 推送客户端
			List<BufferVO> list = bufferFacade.getBufferList(actorId,
					hero.heroId);
			HeroPushHelper.pushHeroBuffers(actorId, hero.heroId, list);

		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
	}
}
