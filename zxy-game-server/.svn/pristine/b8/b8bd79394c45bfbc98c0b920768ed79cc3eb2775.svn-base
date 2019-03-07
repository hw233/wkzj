package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.model.OutbattleEffectConfig;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.model.SkillConfig;

/**
 * 主动技能、被动技能、技能效果配置管理类
 * @author vinceruan
 *
 */
@Component
public class SkillService extends ServiceAdapter {
	static Logger LOG = LoggerFactory.getLogger(SkillService.class);
	
	/** 主动技能 */
	private static Map<Integer, SkillConfig> skillConfigMap = new HashMap<Integer, SkillConfig>();
	
	/** 被动技能 */
	private static Map<Integer, PassiveSkillConfig> passiveSkillConfigMap = new HashMap<Integer, PassiveSkillConfig>();
	
	/** inbattle技能效果 */
	private static Map<Integer, InbattleEffectConfig> inbattleEffectMap = new HashMap<Integer, InbattleEffectConfig>();
	
	/** outbattle技能效果 */
	private static Map<Integer, OutbattleEffectConfig> outbattleEffectMap = new HashMap<Integer, OutbattleEffectConfig>();
	
	@Override
	public void clear() {
		skillConfigMap.clear();
		passiveSkillConfigMap.clear();
		inbattleEffectMap.clear();
		outbattleEffectMap.clear();
	}
	
	@Override
	public void initialize() {
		/** 读取技能效果并缓存 */
		List<InbattleEffectConfig> effectList = dataConfig.listAll(this, InbattleEffectConfig.class);
		if (effectList != null) {
			for (InbattleEffectConfig config : effectList) {
				int effectId = config.getEffectId();
				if (inbattleEffectMap.get(effectId) != null) {
					LOG.error(String.format("inbattleEffect表EffectID重复:%d", effectId));
				}
				inbattleEffectMap.put(effectId, config);
			}
		}
		
		List<OutbattleEffectConfig> outBattleEffectList = dataConfig.listAll(this, OutbattleEffectConfig.class);
		if (outBattleEffectList != null) {
			for (OutbattleEffectConfig config : outBattleEffectList) {
				int effectId = config.getEffectId();
				if (outbattleEffectMap.get(effectId) != null) {
					LOG.error(String.format("outbattleEffect表EffectID重复:%d", effectId));
				}
				outbattleEffectMap.put(effectId, config);
			}
		}
		
		List<PassiveSkillConfig> passiveSkillList = dataConfig.listAll(this, PassiveSkillConfig.class);
		for (PassiveSkillConfig config : passiveSkillList) {
			//检查技能id是否重复
			int skillId = config.getSkillId();
			if (passiveSkillConfigMap.get(skillId) != null) {
				LOG.error(String.format("PassiveConfig表SkillID重复,skillId=%d", skillId));
				continue;
			}
			passiveSkillConfigMap.put(config.getSkillId(), config);
		}
		
		Map<Integer, SkillConfig> tempSkillMap = new HashMap<Integer, SkillConfig>();
		
		/** 读取主动技能、注入对应的技能效果、缓存主动技能 */
		List<SkillConfig> skillList = dataConfig.listAll(this, SkillConfig.class);
		for (SkillConfig config : skillList) {
			config.clearSkillEffects();
			int skillId = config.getSkillId();
			int effectId = config.getEffectId();
			
			//这是被动技能
			if (passiveSkillConfigMap.get(skillId) != null) {
				if (tempSkillMap.get(skillId) != null) {
					LOG.error(String.format("SkillConfig表SkillId重复:%d", skillId));
				}
				tempSkillMap.put(skillId, config);
				continue;
			}
			
			//检查是否重复的技能id
			if (skillConfigMap.get(skillId) != null) {
				LOG.error(String.format("SkillConfig表SkillId重复:%d", skillId));
			}
			
			skillConfigMap.put(skillId, config);
			
			//检查技能效果是否存在
			InbattleEffectConfig effect = inbattleEffectMap.get(effectId);
			if (effect == null) {
				LOG.error(String.format("SkillConfig表对应的效果配置不存在,SkillId=%d,EffectId=%d", skillId, effectId));
				continue;
			}
			//检查是否多个技能对应同一个效果
//			if (effect.getSkillId() != 0) {
//				LOG.error(String.format("SkillConfig多个技能对应同一个InbattleEffect, SkillId=[%d,%d],EffectId=%d", skillId, effect.getSkillId(), effectId));
//				continue;
//			}
			effect.setSkillId(skillId);
			effect.setSkillName(config.getName());
			config.getSkillEffects().add(effect);
			skillConfigMap.put(skillId, config);
		}
	
		
		/** 读取被动技能、注入对应的技能效果、缓存被动技能 */
		for (PassiveSkillConfig config : passiveSkillList) {
			config.clean();
			int skillId = config.getSkillId();
			SkillConfig skill  = tempSkillMap.get(skillId);
			if (skill == null) {
				LOG.error(String.format("PassiveSkillConfig里面的技能ID:[%d]无法在SkillConfig找到", skillId));
				continue;
			}

			int effectId = skill.getEffectId();
			config.setName(skill.getName());
			config.setEffectId(effectId);
			
			if (config.getEffectType() == PassiveSkillConfig.EFFECT_TYPE_IN_BATTLE) {
				InbattleEffectConfig effect = inbattleEffectMap.get(effectId);
				if (effect == null) {
					LOG.error(String.format("SkillConfig表对应的inbattleEffect不存在,EffectId=%d", effectId));
					continue;
				}
//				if (effect.getSkillId() != 0) {
//					LOG.error(String.format("SkillConfig多个技能对应同一个inbattleEffect, SkillId=[%d,%d],EffectId=%d", skillId, effect.getSkillId(), effectId));
//					continue;
//				}
				effect.setSkillId(skillId);
				effect.setSkillName(config.getName());
				config.getSkillEffects().add(effect);
				passiveSkillConfigMap.put(skillId, config);
			} else {
				OutbattleEffectConfig effect = outbattleEffectMap.get(effectId);
				//检查效果是否存在
				if (effect == null) {
					LOG.error(String.format("SkillConfig表对应的outbattleEffect不存在,EffectId=%d", effectId));
					continue;
				}
				//检查是否多个技能对应同一个效果
//				if (effect.getSkillId() != 0) {
//					LOG.error(String.format("SkillConfig多个技能对应同一个OutbattleEffect, SkillId=[%d,%d],EffectId=%d", skillId, effect.getSkillId(), effectId));
//					continue;
//				}
				effect.setSkillId(skillId);
				effect.setSkillName(config.getName());
				config.getStatusEffects().add(effect);
				passiveSkillConfigMap.put(skillId, config);
			}
		}
	}

	/**
	 * 获取主动技能配置
	 * @param id
	 * @return
	 */
	public static SkillConfig getSkill(int id) {
		if (!skillConfigMap.containsKey(id)){
//			LOG.warn(String.format("不存在主动技能id:[%s]", id));
			return null;
		}
		SkillConfig config = skillConfigMap.get(id);
		return config;
	}
	
	/**
	 * 获取被动技能配置
	 * @param id
	 * @return
	 */
	public static PassiveSkillConfig getPassiveSkill(int id) {
		if (!passiveSkillConfigMap.containsKey(id)){
			LOG.warn(String.format("不存在被动技能id:[%s]", id));
			return null;
		}
		PassiveSkillConfig config = passiveSkillConfigMap.get(id); 
		return config;
	}
	
	/**
	 * 获取战场内的效果配置
	 * @param id
	 * @return
	 */
	public static InbattleEffectConfig getInbattleEffect(int id) {
		if (!inbattleEffectMap.containsKey(id)){
			LOG.warn(String.format("战场内的效果配置id:[%s]", id));
			return null;
		}
		return inbattleEffectMap.get(id);
	}
	
	/**
	 * 获取战场外的效果配置
	 * @param id
	 * @return
	 */
	public static OutbattleEffectConfig getOutbattleEffect(int id) {
		if (!outbattleEffectMap.containsKey(id)){
			LOG.warn(String.format("战场外的效果配置id:[%s]", id));
			return null;
		}
		return outbattleEffectMap.get(id);
	}
}
