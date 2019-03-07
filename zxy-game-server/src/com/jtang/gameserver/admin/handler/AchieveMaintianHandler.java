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
import com.jtang.gameserver.admin.facade.AchieveMaintianFacade;
import com.jtang.gameserver.admin.handler.request.DeleteAchievementRequest;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class AchieveMaintianHandler extends AdminRouterHandlerImpl {

	@Autowired
	AchieveMaintianFacade achieveMaintianFacade;
	
	protected final Log LOGGER = LogFactory.getLog(AchieveMaintianHandler.class);
	
	@Override
	public byte getModule() {
		return GameAdminModule.ACHIEVE;
	}
	
//	@Cmd(Id = AchieveMaintianCmd.ADD_ACHIEVE)
//	public void addAchieve(IoSession session, byte[] bytes, Response response){
//		AddAchievementRequest addAchieve = new AddAchievementRequest(bytes);
//		Result result = achieveMaintianFacade.addAchieve(addAchieve.actorId, addAchieve.achieveId);
//		if(result.isFail()){
//			response.setStatusCode(result.statusCode);
//		}
//		sessionWrite(session, response);
//		
//	}
	
	@Cmd(Id = AchieveMaintianCmd.DEL_ACHIEVE)
	public void delAchieve(IoSession session, byte[] bytes, Response response){
		DeleteAchievementRequest deleteAchieve = new DeleteAchievementRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ deleteAchieve.actorId+" ----- achieveId = " + deleteAchieve.achieveId);
		}
		Result result = achieveMaintianFacade.deleteAchieve(deleteAchieve.actorId, deleteAchieve.achieveId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}
		sessionWrite(session, response);
	}
}
