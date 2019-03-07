package com.jtang.gameserver.component.listener.impl;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.listener.ActorLogoutListener;
import com.jtang.gameserver.component.listener.ListenerFacade;

/**
 * 对外监听统一处理
 * @author 0x737263
 *
 */
@Component
public class ListenerFacadeImpl implements ListenerFacade, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListenerFacadeImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 登陆监听注册列表
	 */
	private static Collection<ActorLoginListener> ACTOR_LOGIN_LIST = new ArrayList<ActorLoginListener>(1);

	/**
	 * 登出监听注册列表
	 */
	private static Collection<ActorLogoutListener> ACTOR_LOGOUT_LIST = new ArrayList<ActorLogoutListener>(1);

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, ActorLoginListener> loginMaps = applicationContext.getBeansOfType(ActorLoginListener.class);
		if (loginMaps != null && !loginMaps.isEmpty()) {
			ACTOR_LOGIN_LIST.addAll(loginMaps.values());
		}

		Map<String, ActorLogoutListener> logoutMaps = applicationContext.getBeansOfType(ActorLogoutListener.class);
		if (logoutMaps != null && !logoutMaps.isEmpty()) {
			ACTOR_LOGOUT_LIST.addAll(logoutMaps.values());
		}
		
		LOGGER.info("listener facade (login&logout) init complete!");
		
	}

	@Override
	public void addLoginListener(long actorId) {
		try {
			for (ActorLoginListener listener : ACTOR_LOGIN_LIST) {
				listener.onLogin(actorId);
			}
		} catch (Exception ex) {
			LOGGER.error("addLoginListener", ex);
		}
	}

	@Override
	public void addLogoutListener(long actorId) {
		try {
			for (ActorLogoutListener listener : ACTOR_LOGOUT_LIST) {
				listener.onLogout(actorId);
			}
		} catch (Exception ex) {
			LOGGER.error("addLogoutListener", ex);
		}
	}	

}
