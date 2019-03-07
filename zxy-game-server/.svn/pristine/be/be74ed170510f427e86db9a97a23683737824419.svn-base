package com.jtang.gameserver.module.user.handler;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_ID_VALIDATE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.AUTH_LOGIN_PLATFORM_TYPE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.FIRST_SELECT_HERO_NOT_IN_CONFIG;
import static com.jiatang.common.GameStatusCodeConstant.SERVER_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.TOKEN_VALIDATE_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.USER_LOGIN_ERROR;
import static com.jtang.core.protocol.StatusCode.SERVER_VERSION_ERROR;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.Game;
import com.jtang.gameserver.component.listener.ListenerFacade;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.UserDisabled;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.ActorNecessaryDataFacade;
import com.jtang.gameserver.module.user.facade.OnlineFacade;
import com.jtang.gameserver.module.user.facade.UserDisableFacade;
import com.jtang.gameserver.module.user.facade.UserFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.handler.request.ActorLoginRequest;
import com.jtang.gameserver.module.user.handler.request.ActorMontionRequest;
import com.jtang.gameserver.module.user.handler.request.ActorRenameRequest;
import com.jtang.gameserver.module.user.handler.request.CreateActorRequest;
import com.jtang.gameserver.module.user.handler.request.GetActorRequest;
import com.jtang.gameserver.module.user.handler.request.SaveGuideStepRequest;
import com.jtang.gameserver.module.user.handler.request.SavePushKeyRequest;
import com.jtang.gameserver.module.user.handler.request.UserLoginRequest;
import com.jtang.gameserver.module.user.handler.request.UserReconnectRequest;
import com.jtang.gameserver.module.user.handler.response.ActorBuyResponse;
import com.jtang.gameserver.module.user.handler.response.ActorLoginResponse;
import com.jtang.gameserver.module.user.handler.response.ActorNecessaryDataResponse;
import com.jtang.gameserver.module.user.handler.response.CreateActorResponse;
import com.jtang.gameserver.module.user.handler.response.GetActorResponse;
import com.jtang.gameserver.module.user.handler.response.UserDisabledResponse;
import com.jtang.gameserver.module.user.handler.response.UserLoginResponse;
import com.jtang.gameserver.module.user.helper.UserPushHelper;
import com.jtang.gameserver.module.user.model.ActorInfo;
import com.jtang.gameserver.module.user.platform.PlatformLoginResult;
import com.jtang.gameserver.module.user.type.KickOffType;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.UserReconnectData;

/**
 * 
 * @author 0x737263
 * 
 */
@Component
public class UserHandler extends GatewayRouterHandlerImpl {
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	ListenerFacade listenerFacade;
	@Autowired
	VipFacade vipFacade;
	@Autowired
	Broadcast broadcast;
	@Autowired
	UserDisableFacade UserDisableFacade;
	@Autowired
	UserFacade userFacade;
	@Autowired
	OnlineFacade onlineFacade;
	@Autowired
	private ActorNecessaryDataFacade actorNecessaryDataFacade;

	@Override
	public byte getModule() {
		return ModuleName.USER;
	}

	@Cmd(Id = UserCmd.HEART_BEAT, CheckActorLogin = false)
	public void heartBeat(IoSession session, byte[] bytes, Response response) {
		response.setValue(TimeUtils.getNowBytes());
		broadcast.push(playerSession.getActorId(session), response);
	}

	@Cmd(Id = UserCmd.USER_LOGIN, CheckActorLogin = false)
	public void userLogin(IoSession session, byte[] bytes, Response response) {

		// 如果服务端配置为空 则跳过.
		UserLoginRequest request = new UserLoginRequest(bytes);
		if (Game.getVersion().isEmpty() == false && request.mathVersion(Game.getVersion()) == false) {
			response.setStatusCode(SERVER_VERSION_ERROR);
			sessionWrite(session, response);
			return;
		}

		if (StringUtils.isBlank(request.token)) {
			response.setStatusCode(TOKEN_VALIDATE_ERROR);
			sessionWrite(session, response);
			return;
		}
		
		if (Game.allowPlatform(request.platformId) == false) {
			UserPushHelper.userLoginFail(session, AUTH_LOGIN_PLATFORM_TYPE_ERROR);
			return;
		}
		
		//debug模式则直接返回 token原始的字符串
		if (Game.isDebug()) {
			UserPushHelper.userLoginSucces(session, request.platformId, PlatformLoginResult.valueOf(request.token));
			return;
		}
		
		userFacade.putUserLoginQueue(session, request.platformId, request.token);
		

//		TResult<PlatformLoginResult> tokenResult = actorFacade.validateToken(request.platformId, request.token);
//		if (tokenResult.isFail()) {
//			response.setStatusCode(tokenResult.statusCode);
//			sessionWrite(session, response);
//			return;
//		}
//
//		// 表示用户帐号已经登陆
//		playerSession.setUserLogin(session, request.platformId, tokenResult.item.uid);
//		String key = playerSession.setLoginData(request.platformId, tokenResult.item.uid, tokenResult.item);
//
//		UserLoginResponse packet = new UserLoginResponse(tokenResult.item, key);
//		sessionWrite(session, response, packet);
	}

	@Cmd(Id = UserCmd.GET_ACTOR, CheckActorLogin = false)
	public void getActor(IoSession session, byte[] bytes, Response response) {
		if (playerSession.userIsLogin(session) == false) {
			response.setStatusCode(USER_LOGIN_ERROR);
			sessionWrite(session, response);
			return;
		}

		GetActorRequest request = new GetActorRequest(bytes);
		if (request.serverId != Game.getServerId()) {
			response.setStatusCode(SERVER_ID_ERROR);
			sessionWrite(session, response);
			return;
		}

		int platformType = playerSession.getPlatformType(session);
		String uid = playerSession.getUid(session);
		List<ActorInfo> actorInfos = actorFacade.getActorId(platformType, uid, request.serverId);
		GetActorResponse packet = new GetActorResponse(actorInfos);
//		packet.actorId = actorFacade.getActorId(platformType, uid, request.serverId);

		// 如果是新帐号，则记录oss日志
		if (actorInfos.isEmpty()) {
			GameOssLogger.newUser(uid, platformType, request.channelId, request.serverId);
		}

		sessionWrite(session, response, packet);
	}

	@Cmd(Id = UserCmd.CREATE_ACTOR, CheckActorLogin = false)
	public void createActor(IoSession session, byte[] bytes, Response response) {
		if (playerSession.userIsLogin(session) == false) {
			response.setStatusCode(USER_LOGIN_ERROR);
			sessionWrite(session, response);
			return;
		}

		CreateActorRequest request = new CreateActorRequest(bytes);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("serverid:[%d] actorname:[%s] heroId:[%d] channelId:[%d] ", request.serverId, request.actorName,
					request.heroId, request.channelId));
		}

		// 客户端选择的服必需需与game.xml配置一至
		if (request.serverId != Game.getServerId()) {
			response.setStatusCode(SERVER_ID_ERROR);
			sessionWrite(session, response);
			return;
		}
		
		if (HeroService.isSelectHeroId(request.heroId) == false) {
			response.setStatusCode(FIRST_SELECT_HERO_NOT_IN_CONFIG);
			sessionWrite(session, response);
			return;
		}

		int platformType = playerSession.getPlatformType(session);
		String uid = playerSession.getUid(session);
		String ip = playerSession.getRemoteIp(session);
		TResult<Long> tResult = actorFacade.createActor(platformType, uid, request.channelId, request.serverId, request.heroId, request.actorName,
				ip, request.sim, request.mac, request.imei);
		if (tResult.isFail()) {
			response.setStatusCode(tResult.statusCode);
			sessionWrite(session, response);
			return;
		}
		
		//检查设备是否封号
		TResult<UserDisabled> result = UserDisableFacade.isDisable(tResult.item, request.sim, request.mac, request.imei, ip);
		if (result.isFail()) {// 已经被封号
			UserDisabledResponse userDisabledResponse = new UserDisabledResponse(result.item);
			response.setStatusCode(result.statusCode);
			response.setValue(userDisabledResponse.getBytes());
			sessionWrite(session, response);
			UserPushHelper.kickOff(tResult.item, KickOffType.USER_BLOCK);
			return;
		}

		CreateActorResponse packet = new CreateActorResponse();
		packet.actorId = tResult.item;

		sessionWrite(session, response, packet);
	}
	@Cmd(Id = UserCmd.ACTOR_LOGIN, CheckActorLogin = false)
	public void actorLogin(IoSession session, byte[] bytes, Response response) {
		if (playerSession.userIsLogin(session) == false) {
			response.setStatusCode(USER_LOGIN_ERROR);
			sessionWrite(session, response);
			return;
		}

		ActorLoginRequest request = new ActorLoginRequest(bytes);
		int platformType = playerSession.getPlatformType(session);
		String uid = playerSession.getUid(session);
		List<ActorInfo> infos = actorFacade.getActorId(platformType, uid, request.serverId);
		boolean flag = false;
		for (ActorInfo actorInfo : infos) {
			if (actorInfo.actorId == request.actorId) {
				flag = true;
			}
		}
		if (flag == false) {
			response.setStatusCode(USER_LOGIN_ERROR);
			sessionWrite(session, response);
			return;
		}
		String ip = playerSession.getRemoteIp(session);

		TResult<UserDisabled> result = UserDisableFacade.isDisable(request.actorId, request.sim, request.mac, request.imei, ip);
		if (result.isFail()) {// 已经被封号
			UserDisabledResponse userDisabledResponse = new UserDisabledResponse(result.item);
			response.setStatusCode(result.statusCode);
			response.setValue(userDisabledResponse.getBytes());
			sessionWrite(session, response);
			UserPushHelper.kickOff(request.actorId, KickOffType.USER_BLOCK);
			return;
		}

//		if (request.actorId != actorId) {
//			response.setStatusCode(ACTOR_ID_VALIDATE_ERROR);
//			sessionWrite(session, response);
//			return;
//		}

		// 在线则踢掉
		if (playerSession.isOnline(request.actorId)) {
			UserPushHelper.kickOff(request.actorId, KickOffType.LOGIN_DUPLICATE);
		}

		Actor actor = actorFacade.getActor(request.actorId);
		if (actor == null) {
			response.setStatusCode(ACTOR_ID_VALIDATE_ERROR);
			sessionWrite(session, response);
			return;
		}

		playerSession.setServerId(session, request.serverId);
		playerSession.put2OnlineList(session, request.actorId, false); // 正式转正在线Session管理

		int ticket = vipFacade.getTicket(request.actorId);
		int vipLevel = vipFacade.getVipLevel(request.actorId);
		int totalTicket = vipFacade.getTotalRechargeTicket(request.actorId);
		ActorLoginResponse packet = new ActorLoginResponse(actor, ticket, totalTicket, vipLevel);
		sessionWrite(session, response, packet);

		// 加入在线表
		onlineFacade.add(request.actorId, request.sim, request.mac, request.imei, ip);
		
		// 角色登陆的数据下发成功后，再处理登陆事件
		listenerFacade.addLoginListener(request.actorId);
	}

	@Cmd(Id = UserCmd.SAVE_GUIDES_STEP)
	public void saveGuideStep(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		SaveGuideStepRequest request = new SaveGuideStepRequest(bytes);
		actorFacade.saveGuideStep(actorId, request.key, request.value);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("saveGuideStep  key:%s value:%s", request.key,request.value));
		}
	}

	@Cmd(Id = UserCmd.SAVE_MONTION)
	public void saveMontion(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ActorMontionRequest reuqest = new ActorMontionRequest(bytes);

		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.actorMontion(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, reuqest.mainType, reuqest.subType,
				reuqest.value);
	}
	@Cmd(Id = UserCmd.USER_RECONNECTION, CheckActorLogin = false)
	public void reconnect(IoSession session, byte[] bytes, Response response) {
		UserReconnectRequest request = new UserReconnectRequest(bytes);
		if (Game.getVersion().isEmpty() == false && request.mathVersion(Game.getVersion()) == false) {
			response.setStatusCode(SERVER_VERSION_ERROR);
			sessionWrite(session, response);
			return;
		}

		if (StringUtils.isBlank(request.connectionId)) {
			response.setStatusCode(TOKEN_VALIDATE_ERROR);
			sessionWrite(session, response);
			return;
		}

		TResult<UserReconnectData> tokenResult = playerSession.validateReconnect(request.connectionId, request.version);
		if (tokenResult.isFail()) {
			response.setStatusCode(tokenResult.statusCode);
			sessionWrite(session, response);
			return;
		}

		// 表示用户帐号已经登陆
		playerSession.setUserLogin(session, request.platformId, tokenResult.item.getUid());
		UserLoginResponse packet = new UserLoginResponse(tokenResult.item.getPlatformLoginResult(), tokenResult.item.getReconnectId());
		sessionWrite(session, response, packet);
	}
	
	@Cmd(Id = UserCmd.SAVE_PUSH_KEY)
	public void savePushKey(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		SavePushKeyRequest request = new SavePushKeyRequest(bytes);
		actorFacade.savePushKey(actorId, request.type, request.pushKey);
	}
	
	@Cmd(Id = UserCmd.ACTOR_RENAME)
	public void actorRename(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ActorRenameRequest request = new ActorRenameRequest(bytes);
		Result result = actorFacade.rename(actorId, request.actorName);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = UserCmd.FULL_ENERGY)
	public void fullEnergy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = actorFacade.costTicketFullEnergy(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = UserCmd.FULL_VIT)
	public void fullVit(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = actorFacade.costTicketFullVit(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = UserCmd.BUY_GOLD)
	public void getFullEnergy(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		Result result = actorFacade.costTicketBuyGold(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id = UserCmd.BUY_INFO)
	public void getFullVit(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<ActorBuyResponse> result = actorFacade.getActorBuy(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response,result.item);
		}
	}
	@Cmd(Id = UserCmd.GET_ACTOR_NECESSARY_DATA)
	public void getNecessaryData(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		ActorNecessaryDataResponse result = actorNecessaryDataFacade.get(actorId);
		sessionWrite(session, response,result);
	}
	
	
}
