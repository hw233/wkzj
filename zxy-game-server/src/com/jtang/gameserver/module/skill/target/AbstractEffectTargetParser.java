package com.jtang.gameserver.module.skill.target;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象的技能影响区域解析器
 * @author vinceruan
 *
 */
public abstract class AbstractEffectTargetParser implements EffectTargetParser {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private EffectTargetContext context;

	@PostConstruct
	void init() {
		context.putParser(getType(), this);
	}

	/**
	 * 技能类型
	 * 
	 * @return {@code Integer}		返回值
	 */
	protected abstract int getType();
	
}
