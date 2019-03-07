package com.jtang.gameserver.module.skill.target;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 技能目标作用域解析上下文
 * @author vinceruan
 *
 */
@Component
public class EffectTargetContext {
	/**
	 * 根据作用域类型注册的解析器集合
	 */
	private final Map<Integer, EffectTargetParser> parsers = new HashMap<Integer, EffectTargetParser>(10);

	/**
	 * 获取已注册的处理器
	 * 
	 * @param  type 						技能状态类型
	 * @return {@code SkillEffectParser}	技能效果作用域解析器
	 */
	public EffectTargetParser getParser(int type) {
		return parsers.get(type);
	}

	/**
	 * 注册技能解析器
	 * 
	 * @param type 					技能类型
	 * @param parser 				效果处理器
	 */
	public void putParser(int type, EffectTargetParser parser) {
		parsers.put(type, parser);
	}
}
