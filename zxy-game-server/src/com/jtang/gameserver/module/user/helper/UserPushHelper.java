package com.jtang.gameserver.module.user.helper;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.user.handler.UserCmd;
import com.jtang.gameserver.module.user.handler.response.KickOffResponse;
import com.jtang.gameserver.module.user.handler.response.UserLoginResponse;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;
import com.jtang.gameserver.module.user.type.KickOffType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 用户消息推送帮助类
 * @author 0x737263
 *
 */
@Component
public class UserPushHelper {
	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<UserPushHelper> ref = new ObjectReference<UserPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	/**
	 * 踢人下线
	 * @param actorId
	 * @param kickOffType
	 */
	public static void kickOff(long actorId, KickOffType kickOffType) {
		KickOffResponse packet = new KickOffResponse(kickOffType.getCode());
		Response response = Response.valueOf(ModuleName.USER, UserCmd.KICK_OFF, packet.getBytes());

		IoSession session = ref.get().playerSession.getOnlineSession(actorId);
		ref.get().playerSession.push(session, response);
		ref.get().playerSession.delayCloseSession(session, 3);
	}
	
	/**
	 * 推送用户登陆失败
	 * @param session
	 * @param statusCode
	 */
	public static void userLoginFail(IoSession session, short statusCode) {
		Response response = Response.valueOf(ModuleName.USER, UserCmd.USER_LOGIN);
		response.setStatusCode(statusCode);
		ref.get().playerSession.push(session, response);
	}
	
	/**
	 * 推送用户登陆成功
	 * @param session
	 * @param loginResult
	 */
	public static void userLoginSucces(IoSession session, int platformId, PlatformLoginResult loginResult) {
		ref.get().playerSession.setUserLogin(session, platformId, loginResult.uid);

		String key = ref.get().playerSession.setLoginData(platformId, loginResult.uid, loginResult);
		UserLoginResponse packet = new UserLoginResponse(loginResult, key);

		Response response = Response.valueOf(ModuleName.USER, UserCmd.USER_LOGIN, packet.getBytes());
		ref.get().playerSession.push(session, response);
	}
	
}
