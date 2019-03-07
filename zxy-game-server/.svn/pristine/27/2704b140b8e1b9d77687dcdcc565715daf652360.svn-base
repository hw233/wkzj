package com.jtang.gameserver.module.extapp.plant.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dataconfig.model.PlantGlobalConfig;
import com.jtang.gameserver.dataconfig.service.PlantService;
import com.jtang.gameserver.module.extapp.plant.handler.PlantCmd;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantResponse;
import com.jtang.gameserver.module.extapp.plant.handler.response.PlantStateResponse;
import com.jtang.gameserver.module.extapp.plant.module.PlantVO;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class PlantPushHelper {

	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<PlantPushHelper> ref = new ObjectReference<PlantPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static PlantPushHelper getInstance() {
		return ref.get();
	}

	public static void pushPlantState(long actorId, PlantVO plantVO) {
		int level = ActorHelper.getActorLevel(actorId);
		PlantGlobalConfig config = PlantService.getPlantGlobalConfig();
		List<RewardObject> reward = PlantService.getAllReward(level);
		List<RewardObject> extReward = PlantService.getAllExtReward(level);
		PlantResponse plantResponse = new PlantResponse(plantVO,reward,extReward,config.costTicket);
		Response rsp = Response.valueOf(ModuleName.PLANT, PlantCmd.PUSH_PLANT_GROW, plantResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	public static void pushPlantOpen(long actorId,int isOpen){
		PlantStateResponse stateResponse = new PlantStateResponse(isOpen);
		Response rsp = Response.valueOf(ModuleName.PLANT, PlantCmd.PUSH_PLANT_STATE,stateResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

}
