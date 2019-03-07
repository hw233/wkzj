package com.jtang.core.schedule;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
@Component
public class ZeroListenerHandler implements ApplicationListener<ContextRefreshedEvent> {
	
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private Schedule schedule;
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addFixedTime(new Runnable() {
			
			@Override
			public void run() {
				Map<String, ZeroListener> cacheClazz = applicationContext.getBeansOfType(ZeroListener.class);
				for (Entry<String, ZeroListener> cacheEntry : cacheClazz.entrySet()) {
					cacheEntry.getValue().onZero();
					LOGGER.debug(String.format("零点调度, class:[%s]", cacheEntry.getValue().getClass().getName()));
				}
				
			}
		}, 0, 0, 2);
	}
}
