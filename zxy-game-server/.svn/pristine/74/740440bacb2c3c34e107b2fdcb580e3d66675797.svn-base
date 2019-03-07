package com.jtang.gameserver.module.lineup.handler;

import static com.jiatang.common.GameStatusCodeConstant.LINEUP_NOT_EXITS;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.handler.request.AssignEquipRequest;
import com.jtang.gameserver.module.lineup.handler.request.AssignHeroRequest;
import com.jtang.gameserver.module.lineup.handler.request.ChangeHeroGridRequest;
import com.jtang.gameserver.module.lineup.handler.request.ChangeLineupRequest;
import com.jtang.gameserver.module.lineup.handler.request.ExchangeHeroGridRequest;
import com.jtang.gameserver.module.lineup.handler.request.UnAssignEquipRequest;
import com.jtang.gameserver.module.lineup.handler.request.UnAssignHeroRequest;
import com.jtang.gameserver.module.lineup.handler.request.ViewLineupRequest;
import com.jtang.gameserver.module.lineup.handler.response.LineupInfoResponse;
import com.jtang.gameserver.module.lineup.handler.response.ViewLineupResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
/**
 * 阵型 handler
 * @author vinceruan
 *
 */
@Component
public class LineupHandler extends GatewayRouterHandlerImpl {
	@Autowired
	LineupFacade lineupFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.LINEUP;
	}

	/**
	 * 获取阵型信息
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.LINEUP_INFO)
	public void getLineup(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		
		Lineup lineup = lineupFacade.getLineup(actorId);
		
		if (lineup == null) {
			response.setStatusCode(LINEUP_NOT_EXITS);
			sessionWrite(session, response);
			return;
		}
		
		LineupInfoResponse res = new LineupInfoResponse(lineup);
		sessionWrite(session, response, res);
	}
	
	/**
	 * 仙人上阵
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.ASSIGN_HERO)
	public void assignHero(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		AssignHeroRequest request = new AssignHeroRequest(bytes);
		
		int heroId = request.heroId;
		int gridIndex = request.gridIndex;
		int headIndex = request.headIndex;
		Result result = this.lineupFacade.assignHero(actorId, heroId, headIndex, gridIndex);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 仙人下阵
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.UNASSIGN_HERO)
	public void unassignHero(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		UnAssignHeroRequest request = new UnAssignHeroRequest(bytes);
		
		int heroId = request.heroId;	
		Result result = this.lineupFacade.unassignHero(actorId, heroId);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 装备上阵
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.ASSIGN_EQUIP)
	public void equip(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		AssignEquipRequest request = new AssignEquipRequest(bytes);
		
		long equipId = request.equipUuid;
		int headIndex = request.headIndex;		
		Result result = this.lineupFacade.assignEquip(actorId, equipId, headIndex);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 装备下阵
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.UNASSIGN_EQUIP)
	public void unequip(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		UnAssignEquipRequest request = new UnAssignEquipRequest(bytes);
		
		long equipId = request.equipUuid;		
		Result result = this.lineupFacade.unassignEquip(actorId, equipId, true);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 移动仙人,即改变其gridIndex值
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.CHANGE_HERO_GRID)
	public void changeHeroGrid(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ChangeHeroGridRequest request = new ChangeHeroGridRequest(bytes);
		
		int heroId = request.heroId;
		int gridIndex = request.gridIndex;		
		Result result = this.lineupFacade.changeHeroGrid(actorId, heroId, gridIndex);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 对换两个人的位置,即gridIndex值
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id=LineupCmd.EXCHANGE_HERO_GRID)
	public void exchangeHeroGrid(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		ExchangeHeroGridRequest request = new ExchangeHeroGridRequest(bytes);
		
		int heroId1 = request.heroId1;
		int heroId2 = request.heroId2;
		Result result = this.lineupFacade.exChangeHeroGrid(actorId, heroId1, heroId2);
		
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
	@Cmd(Id=LineupCmd.UNLOCK_LINEUP)
	public void unlockLineup(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);
		Result result = this.lineupFacade.manualUnlockLineup(actorId);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	@Cmd(Id=LineupCmd.VIEW_LINEUP)
	public void viewLineup(IoSession session, byte[] bytes, Response response) {
		ViewLineupRequest viewLineupRequest = new ViewLineupRequest(bytes);
		TResult<ViewLineupResponse> result = lineupFacade.getLineupInfo(viewLineupRequest.actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		} else {
			sessionWrite(session, response, result.item);
		}
	}
	
	/**
	 * 更换阵型
	 */
	@Cmd(Id=LineupCmd.CHANGE_LINEUP)
	public void changeLineup(IoSession session, byte[] bytes, Response response){
		ChangeLineupRequest request = new ChangeLineupRequest(bytes);
		long actorId = playerSession.getActorId(session);
		TResult<ViewLineupResponse> result = lineupFacade.changeLineup(actorId,request.linequpIndex);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
			sessionWrite(session, response);
		}else{
			sessionWrite(session, response, result.item);
		}
	}
	
	
}

