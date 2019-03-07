package com.jtang.core.event;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractReceiver implements Receiver {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	protected EventBus eventBus;

	@PostConstruct
	private void init() {
		for (String name : getEventNames())
			this.eventBus.register(name, this);
	}

	public abstract String[] getEventNames();
}