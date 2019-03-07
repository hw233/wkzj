package com.jtang.gameserver.module.skill.effect.outbattle;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jtang.gameserver.module.hero.facade.HeroFacade;

public abstract class AbstractOutBattleEffectParser implements OutBattleEffectParser {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OutBattleEffectContext context;
	
	@Autowired
	protected HeroFacade heroFacade;

	@PostConstruct
	void init() {
		context.putParser(getParserId(), this);
	}

	public abstract int getParserId();
}
