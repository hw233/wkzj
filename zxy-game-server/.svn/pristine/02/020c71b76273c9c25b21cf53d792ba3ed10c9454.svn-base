package com.jtang.gameserver.admin.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.EquipMaintianFacade;
import com.jtang.gameserver.admin.handler.request.AddAllEquipsRequest;
import com.jtang.gameserver.admin.handler.request.DeleteEquipRequest;
import com.jtang.gameserver.admin.handler.request.GiveEquipRequest;
import com.jtang.gameserver.admin.handler.request.ModifyEquipLevelRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;
@Component
public class EquipMaintianHandler extends AdminRouterHandlerImpl {
	
	protected final Log LOGGER = LogFactory.getLog(EquipMaintianHandler.class);
	
	@Autowired
	EquipMaintianFacade equipMaintianFacade;
	
	@Cmd(Id = EquipMaintianCmd.ADD_EQUIP)
	public void giveEquip(IoSession session, byte[] bytes, Response response) {
		GiveEquipRequest giveEquipRequest = new GiveEquipRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ giveEquipRequest.actorId+" ----- equipId = " + giveEquipRequest.equipId);
		}
		Result result = equipMaintianFacade.addEquip(giveEquipRequest.actorId, giveEquipRequest.equipId);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

	@Cmd(Id = EquipMaintianCmd.DEL_EQUIP)
	public void DelEquip(IoSession session, byte[] bytes, Response response){
		DeleteEquipRequest deleteEquipRequest = new DeleteEquipRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ deleteEquipRequest.actorId+" ----- equipId = " + deleteEquipRequest.uuid);
		}
		Result result = equipMaintianFacade.deleteEquip(deleteEquipRequest.actorId, deleteEquipRequest.uuid);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = EquipMaintianCmd.MODIFY_EQUIP_LEVEL)
	public void ModifyEquipLevel(IoSession session, byte[] bytes, Response response){
		ModifyEquipLevelRequest modifyEquipLevel = new ModifyEquipLevelRequest(bytes);
		Result result = equipMaintianFacade.modifyEquipLevel(modifyEquipLevel.actorId, modifyEquipLevel.uuid, modifyEquipLevel.targetLevel);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	
	@Cmd(Id = EquipMaintianCmd.ADD_ALL_EQUIPS)
	public void addAllEquips(IoSession session, byte[] bytes, Response response){
		AddAllEquipsRequest addAllEquipsRequest = new AddAllEquipsRequest(bytes);
		Result result = equipMaintianFacade.addAllEquip(addAllEquipsRequest.actorId);
		if (result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Override
	public byte getModule() {
		return GameAdminModule.EQUIP;
	}
}
