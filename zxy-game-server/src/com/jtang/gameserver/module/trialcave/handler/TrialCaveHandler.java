package com.jtang.gameserver.module.trialcave.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.trialcave.facade.TrialCaveFacade;
import com.jtang.gameserver.module.trialcave.handler.request.TrialBattleRequest;
import com.jtang.gameserver.module.trialcave.handler.response.TrialCaveInfoResponse;
import com.jtang.gameserver.module.trialcave.handler.response.TrialCaveResetResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

/**
 * 试炼洞模块请求处理类
 * @author lig
 *
 */
@Component
public class TrialCaveHandler extends GatewayRouterHandlerImpl {
	@Autowired
	TrialCaveFacade trialCaveFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.TRIAL_CAVE;
	}
	
	/**
	 * 加载试炼洞信息
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=TrialCaveCmd.TRIAL_CAVE_INFO)
	public void loadTrialCaveInfo(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<TrialCave> result = this.trialCaveFacade.getTrialCaveInfo(actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
			return;
		}
		TrialCave cave = result.item;
		TrialCaveInfoResponse res = new TrialCaveInfoResponse(cave);
		sessionWrite(session, response, res);
	}
	
	/**
	 * 开始试炼战斗 
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=TrialCaveCmd.TRIAL_BATTLE)
	public void trial(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TrialBattleRequest request = new TrialBattleRequest(bytes);
		int entranceId = request.entranceId;
		Result res = this.trialCaveFacade.trialBattle(actorId, entranceId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("ActorId:[{%d}], battleId[{%d}], result:[{%d}]", actorId, entranceId, res.statusCode));
		}
		if (res.isFail()) {//如果是成功的话,将另有方法进行数据推送
			response.setStatusCode(res.statusCode);
			sessionWrite(session, response);
		}
	}
	
	/**
	 * 请求重置试炼次数
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=TrialCaveCmd.TRIAL_CAVE_RESET)
	public void updateTrialCave(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		TResult<Integer> result = this.trialCaveFacade.resetTrialCave(actorId);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		} else {
			TrialCaveResetResponse responses = new TrialCaveResetResponse(result.item);
			response.setValue(responses.getBytes());
		}
		sessionWrite(session, response);
	}
}
