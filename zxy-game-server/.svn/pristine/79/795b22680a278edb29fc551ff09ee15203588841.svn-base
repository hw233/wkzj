package com.jtang.gameserver.module.battle.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.module.battle.model.Context;
import com.jtang.gameserver.module.battle.model.DodgeAction;
import com.jtang.gameserver.module.battle.model.Fighter;
import com.jtang.gameserver.module.battle.model.ImmunityAction;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.buffer.helper.BufferHelper;
import com.jtang.gameserver.module.skill.effect.inbattle.InBattleEffectParser;
import com.jtang.gameserver.module.skill.effect.inbattle.InBattleSkillEffectContext;
import com.jtang.gameserver.module.skill.target.EffectTargetContext;
import com.jtang.gameserver.module.skill.target.EffectTargetParser;
import com.jtang.gameserver.module.skill.type.EffectTarget;
import com.jtang.gameserver.module.skill.type.ProcessType;

/**
 * 战斗模块帮助类
 * @author vinceruan
 *
 */
@Component
public class FightProcessor {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final ObjectReference<FightProcessor> ref = new ObjectReference<FightProcessor>();
	
	@Autowired
	private EffectTargetContext effectTargetContext;
	
	@Autowired
	private InBattleSkillEffectContext effectParserContext;
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static FightProcessor getInstance() {
		return ref.get();
	}
	
	/**
	 * 某个战斗阶段，所有玩家的技能效果处理
	 * @param processType
	 * @param context
	 * @param spriteEffectMap
	 */
	public static void processSkillEffects(ProcessType processType, Context context) {
		Map<Fighter, Set<InbattleEffectConfig>> spriteEffectMap = context.getSpriteSkillMapByProcessType(processType.getCode());
		
		if (CollectionUtils.isEmpty(spriteEffectMap)) {
			return;
		}
		
		for (Entry<Fighter, Set<InbattleEffectConfig>> entry : spriteEffectMap.entrySet()) {			
			Fighter fighter = entry.getKey();
			Set<InbattleEffectConfig> effects = entry.getValue();
			processOneFighterSkillEffects(processType, context, fighter, effects);
		}
	}
	
	/**
	 * 某个战斗阶段，某个阵型的所有玩家的技能效果处理
	 * @param camp
	 * @param processType
	 * @param context
	 */
	public static void processSkillEffects(Camp camp , ProcessType processType, Context context) {
		Map<Fighter, Set<InbattleEffectConfig>> spriteEffectMap = context.getSpriteSkillMapByProcessType(processType.getCode());
		
		if (CollectionUtils.isEmpty(spriteEffectMap)) {
			return;
		}
		
		for (Entry<Fighter, Set<InbattleEffectConfig>> entry : spriteEffectMap.entrySet()) {			
			Fighter fighter = entry.getKey();
			if (fighter.getCamp() != camp) {
				continue;
			}
			Set<InbattleEffectConfig> effects = entry.getValue();
			processOneFighterSkillEffects(processType, context, fighter, effects);
		}
	}

	/**
	 * 某个战斗阶段, 某个玩家的技能效果处理
	 * @param processType
	 * @param context
	 * @param skillCaster
	 * @param effects
	 * @return 是否释放成功
	 */
	public static boolean processOneFighterSkillEffects(ProcessType processType, Context context, Fighter skillCaster, Set<InbattleEffectConfig> effects) {
		if (CollectionUtils.isEmpty(effects)) {
			return false;
		}
		
		context.setProcessType(processType);
		
		boolean result = false;
		for (InbattleEffectConfig effect : effects) {
			boolean result1 = FightProcessor.getInstance().processSkillEffect(skillCaster, context, effect);
			result = result || result1;
		}
		return result;
	}
	
	/**
	 * 处理技能触发
	 * @param skillCaster
	 * @param context
	 * @param effect
	 * @return 是否释放成功
	 */
	public boolean processSkillEffect(Fighter skillCaster, Context context, InbattleEffectConfig effect) {
		if (skillCaster.isDead()) {
			if (effect.getProcessType() != ProcessType.DIE.getCode() && effect.getProcessType() != ProcessType.AFTER_DEAD.getCode()) {
				return false;
			}
		}
		
		if (BufferHelper.isUnitInImmobilize(skillCaster.getBuffers()) > 0) {
			return false;
		}
		
		try {
			/** 第一步: 如果技能的释放有最大次数的限制,则判断是否可以释放*/
			if (effect.getMaxRelease() != -1 ) {
				int max = effect.getMaxRelease();
				int current = context.getSkillEeffectReleaseCount(skillCaster, effect);
				if (current >= max) {
					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug("用户:[{}], 角色:[{}], 效果ID:[{}], 技能:[{}],技能释放次数为[{}],已达到最大释放次数.",
								skillCaster.getFighterId(), skillCaster.getName(), effect.getEffectId(), effect.getSkillName(), current);
					}
					return false;
				}
			}
			
			/** 第二步: 如果有概率事件,计算是否命中,如果命中则触发技能效果,没有命中则直接返回*/
			if(!processSkillEffectRateHit(effect, skillCaster)) {
				if(LOGGER.isDebugEnabled()) {
					LOGGER.debug("概率计算没有命中,角色[{}]-[{}]释放的技能[{}]没有产生效果", 
							skillCaster.getFighterId(), skillCaster.getName(), effect.getSkillName());
				}
				return false;
			}

			//有些技能是针对敌方作用的,有些是针对友方作用的,这里需要将敌我双方找出来(注意有些技能会导致仙人发生内讧,这时敌我双方会发生变化)
			List<Fighter> friendTeam = BattleHelper.getFriends(skillCaster, context);
			List<Fighter> enermyTeam = BattleHelper.getEnemies(skillCaster, context);
			
			/**第三步: 筛选出所有受影响的目标.*/			
			List<Fighter> targetUnits = getSkillEffectUnits(skillCaster, friendTeam, enermyTeam, context, effect);
			Collections.sort(targetUnits);
			if (!CollectionUtils.isEmpty(targetUnits)) {
				InBattleEffectParser parser = effectParserContext.getParser(effect.getParserId());
				if (parser == null) {
					LOGGER.error("技能效果解释器无法找到,技能效果id:[{}]-[{}], 解析器id:[{}], 施法者:[{}], 角色[{}]", 
							new Object[] { effect.getEffectId(), effect.getSkillName(), effect.getParserId(), skillCaster.getFighterId(), skillCaster.getName()});
					return false;
				}
				//
				boolean result = parser.parser(skillCaster, targetUnits, effect, context);
				if (result) {
					context.addSkillEffectReaseCount(skillCaster, effect);
				}
				return result;
				
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("用户[{}] 角色[{}] 的技能效果[{}] - [{}] 找到对应的目标区域[{}]为空,无法释放效果",
							skillCaster.getFighterId(),skillCaster.getName(), effect.getEffectId(), effect.getSkillName(), effect.getTargetType());
				}
			}
			
		} catch(Exception e) {
			//打印错误而不抛出是因为,出错原因一般是配置文件错误, 即使技能处理出错,也还有普通攻击可以让整个战斗进行下去, 因此不应该直接让战斗退出, 影响用户体验.
			StringBuilder sd = new StringBuilder();
			sd.append("用户:").append(skillCaster.getFighterId()).append(" 角色:")
				.append(skillCaster.getName()).append(" 在处理技能效果id:").append(effect.getEffectId()).append("[").append(effect.getSkillName()).append("]时出错");
			LOGGER.error(sd.toString(), e);
		} finally {
		}
		return false;
	}
	
	/**
	 * 根据概率计算是否触发技能效果
	 * @param fighter
	 * @param context
	 * @param effect
	 * @return
	 */
	private boolean processSkillEffectRateHit(InbattleEffectConfig effect, Fighter skillCaster) {
		if (Game.skillDebug()) {
			return true;
		}
		int rate = effect.getRate();		
		rate += skillCaster.getSkillRateBuffer(effect.getEffectId());
		if (skillCaster.skillRate.containsKey(effect)) {
			int skillRate = skillCaster.skillRate.get(effect);
			int result = (skillRate * 411 + 3) % 1000;
			skillCaster.skillRate.put(effect, result);
			return result <= rate;
		} else {
			int result = RandomUtils.nextInt(0, 999);
			skillCaster.skillRate.put(effect, result);
			return result <= rate;
		}
//		return RandomUtils.is1000Hit(rate);
	}
	
	public List<Fighter> getSkillEffectUnits(Fighter skillCaster, List<Fighter> teams, List<Fighter> enermies, Context context, InbattleEffectConfig skillEffect) {
		if (skillEffect.getTargetType() == EffectTarget.EffectTarget1) {
			//技能目标是自己
			List<Fighter> targetList = new ArrayList<Fighter>();
			targetList.add(skillCaster);
			return targetList;
		} else if(skillEffect.getTargetType() == EffectTarget.EffectTarget4) {
			//技能目标是所有好友
			return teams;
		} else if(skillEffect.getTargetType() == EffectTarget.EffectTarget5) {
			//技能目标是所有敌人
			return enermies;
		} else if (skillEffect.getTargetType() == EffectTarget.EffectTarget2) {
			//技能目标是普通攻击时的受害者
			List<Fighter> targetList = new ArrayList<Fighter>();
			if (context.getTargetEnermy() != null) {
				targetList.add(context.getTargetEnermy());
			}
			return targetList;
		}
		
		//如果不是上面这些,则启动解析器进行解析
		
		EffectTargetParser parser = this.effectTargetContext.getParser(skillEffect.getTargetType());
		if (parser == null) {
			LOGGER.error("技能效果扩散区域解释器无法找到,技能效果id:[{}], 施法者:[{}]-[{}], 释放技能[{}], 目标类型:[{}]",
					skillEffect.getEffectId(), skillCaster.getFighterId(), skillCaster.getName(), skillEffect.getSkillName(), skillEffect.getTargetType());			
//			return null;
		}
		return parser.parseEffectTargets(skillCaster, teams, enermies, context, skillEffect);
	}
	
	/**
	 * 某个玩家在战斗的某个阶段时的技能效果处理
	 * @param skillCaster
	 * @param context
	 * @param processType
	 */
	public static boolean processSkillEffects(Fighter skillCaster, Context context, ProcessType processType) {
		Map<Fighter, Set<InbattleEffectConfig>> map = context.getSpriteSkillMapByProcessType(processType.getCode());
		if (map != null) {
			return processOneFighterSkillEffects(processType, context, skillCaster, map.get(skillCaster));
		}
		return false;
	}
	
	/**
	 * 尝试闪避
	 * @param fighter
	 * @param context
	 * @return
	 */
	public boolean tryDodge(Fighter fighter, Context context) {
		//尝试闪避
		boolean isDodge = BufferHelper.tryDodge(fighter);
		if (isDodge) {
			//下发闪避的战报
//			report.actions.add(new DodgeAction(fighter.getFighterId()));
			context.fightRecorder.recordAction(new DodgeAction(fighter.getFighterId()));
		}
		return isDodge;
	}
	/**
	 * 尝试免疫
	 * @param fighter
	 * @param context
	 * @return
	 */
	public boolean tryImmunity(Fighter fighter, Context context, int effectId) {
		//尝试闪避
		boolean isDodge = BufferHelper.tryImmunity(fighter, effectId);
		if (isDodge) {
			//下发闪避的战报
//			report.actions.add(new ImmunityAction(fighter.getFighterId()));
			context.fightRecorder.recordAction(new ImmunityAction(fighter.getFighterId()));
		}
		return isDodge;
	}
}
