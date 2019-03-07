package com.jtang.gameserver.module.user.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.TOKEN_VALIDATE_ERROR;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.core.thread.NamedThreadFactory;
import com.jtang.gameserver.module.user.facade.UserFacade;
import com.jtang.gameserver.module.user.helper.UserPushHelper;
import com.jtang.gameserver.module.user.platform.PlatformInvoke;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;

/**
 * 用户相关的接口
 * @author 0x737263
 *
 */
@Component
public class UserFacadeImpl implements UserFacade {
	
	private static int POOL_SIZE = 10;
	private static int MAX_POOL_SIZE = 20;
	private static int KEEP_ALIVE_TIME = 900; //ms

	private ThreadPoolExecutor executor ;
	
	@Autowired
	private PlatformInvoke platformInvoke;
	
	@PostConstruct
	void initialize() {
		ThreadGroup threadGroup = new ThreadGroup("user login thread group");
		NamedThreadFactory threadFactory = new NamedThreadFactory(threadGroup, "user login thread");
//		Runnable submitTask = new Runnable() {
//			public void run() {
//				while (true) {
//					try {
//						Runnable task = executeThread.getQueue().take();
//						if (task != null) {
//							executeThread.submitTask(task);
//						}
//					} catch (Exception ex) {
//						LOGGER.error("Error: {}", ex);
//					}
//				}
//			}
//		};
//		executeThread.start(threadFactory);
		executor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
				threadFactory);
	}
	
	@Override
	public void putUserLoginQueue(final IoSession session, final int platformId, final String token) {

		executor.submit(new Runnable() {
			@Override
			public void run() {
				TResult<PlatformLoginResult> result = platformInvoke.login(platformId, token);
				if (result.isFail()) {
					UserPushHelper.userLoginFail(session, TOKEN_VALIDATE_ERROR);
					return;
				}
				UserPushHelper.userLoginSucces(session, platformId, result.item);
			}
		});
	}

}
