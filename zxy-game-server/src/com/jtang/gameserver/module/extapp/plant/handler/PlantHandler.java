package com.jtang.gameserver.module.extapp.plant.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.plant.facade.PlantFacade;
import com.jtang.gameserver.module.extapp.plant.handler.request.PlantRequest;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantHarvestResponse;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantResponse;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class PlantHandler extends GatewayRouterHandlerImpl {

	@Autowired
	PlantFacade plantFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.PLANT;
	}
	
	@Cmd(Id = PlantCmd.GET_PLANT)
	public void getPlant(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PlantResponse> result = plantFacade.getPlant(actorId);
		response.setValue(result.item.getBytes());
		sessionWrite(session, response);
	}
	
	@Cmd(Id = PlantCmd.PLANT_QUICKEN)
	public void plantQuicken(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PlantResponse> result = plantFacade.plantQuicken(actorId);
		if(result.isOk()){
			response.setValue(result.item.getBytes());
		}else{
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = PlantCmd.PLANT_HARVEST)
	public void plantHarvest(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<PlantHarvestResponse> result = plantFacade.plantHarvest(actorId);
		if(result.isOk()){
			response.setValue(result.item.getBytes());
		}else{
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = PlantCmd.PLANT)
	public void plant(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		PlantRequest request = new PlantRequest(bytes);
		TResult<PlantResponse> result = plantFacade.plant(actorId,request.plantId);
		if(result.isOk()){
			response.setValue(result.item.getBytes());
		}else{
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}

}
