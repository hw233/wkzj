package com.jtang.gameserver.module.skill.trigger;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PassiveSkillTriggerParserContext {
	/**
	 * 各种技能触发验证器
	 */
	private final Map<Integer, PassiveSkillTriggerParser> parsers = new HashMap<Integer, PassiveSkillTriggerParser>(10);

	/**
	 * 获取已注册的技能触发验证器
	 * 
	 * @param  type 						技能类型
	 * @return {@code SkillEffectParser}	技能触发验证器
	 */
	public PassiveSkillTriggerParser getParser(int type) {
		return parsers.get(type);
	}

	/**
	 * 技能触发验证器
	 * 
	 * @param type 					技能类型
	 * @param parser 				技能触发验证器
	 */
	public void putParser(int type, PassiveSkillTriggerParser parser) {
		parsers.put(type, parser);
	}
}
