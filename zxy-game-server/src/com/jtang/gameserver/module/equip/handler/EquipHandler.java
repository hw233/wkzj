package com.jtang.gameserver.module.equip.handler;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.handler.request.EquipSellRequest;
import com.jtang.gameserver.module.equip.handler.request.EquipUpRequest;
import com.jtang.gameserver.module.equip.handler.response.EquipListResponse;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class EquipHandler extends GatewayRouterHandlerImpl{

	@Autowired
	public EquipFacade equipFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.EQUIP;
	}

	/**
	 * 获取当前玩家所有装备
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = EquipCmd.GET_EQUIP_LIST)
	public void getEquipList(IoSession session, byte[] bytes, Response response) {
		long actorId = playerSession.getActorId(session);

		Collection<EquipVO> equipList = equipFacade.getList(actorId);
		int num = equipFacade.getComposeNum(actorId);
		EquipListResponse result = new EquipListResponse(equipList, num);
		sessionWrite(session, response, result);
	}
	
	/**
	 * 出售装备
	 * @param session
	 * @param bytes
	 * @param response
	 */
	@Cmd(Id = EquipCmd.SELL_EQUIP)
	public void sellEquip(IoSession session, byte[] bytes, Response response) {
		EquipSellRequest request = new EquipSellRequest(bytes);
		long actorId = playerSession.getActorId(session);
		
		short statusCode = equipFacade.sellEquip(actorId, EquipDecreaseType.SELL, request.uuid);
		response.setStatusCode(statusCode);
		sessionWrite(session, response);
	}
	
	/**
	 * 强化精炼装备到最高等级
	 */
	@Cmd(Id = EquipCmd.UP_EQUIP)
	public void upEquip(IoSession session, byte[] bytes, Response response){
		EquipUpRequest request = new EquipUpRequest(bytes);
		long actorId = playerSession.getActorId(session);
		Result result = equipFacade.upEquip(actorId,request.equipList);
		response.setStatusCode(result.statusCode);
		sessionWrite(session, response);
	}
	
}
