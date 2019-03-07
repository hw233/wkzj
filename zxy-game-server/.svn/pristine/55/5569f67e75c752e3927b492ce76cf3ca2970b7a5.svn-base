package com.jtang.gameserver.module.skill.trigger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 被动技能激活处理器
 * @author vinceruan
 *
 */
public abstract class AbstractPassiveSkillTriggerParser implements PassiveSkillTriggerParser {
	@Autowired
	protected PassiveSkillTriggerParserContext context;

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@PostConstruct
	void init() {
		context.putParser(getType(), this);
	}

	/**
	 * 触发类型
	 * 
	 * @return {@code int}		返回值
	 */
	protected abstract int getType();
}
