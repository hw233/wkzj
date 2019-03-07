package com.jtang.core.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring应用程序上下文
 * 
 * @author 0x737263
 * 
 */
public class SpringContext {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringContext.class);
		
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("*springContext*.xml"); 
	
	private SpringContext() {
	}

	public static ApplicationContext getContext() {
		return appContext;
	}

	public static Object getBean(String name) {
		return appContext.getBean(name);
	}

	public static <T> Object getBean(Class<T> beanClazz) {
		String[] names = appContext.getBeanNamesForType(beanClazz);
		if ((names != null) && (names.length > 0)) {
			if (names.length == 1) {
				return appContext.getBean(names[0]);
			}
			LOGGER.error("[{}] interface class must be only!", beanClazz);
		} else {
			LOGGER.error("[{}] class not found!", beanClazz);
		}
		return null;
	}
}