package com.jtang.gameserver.admin.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.GameAdminModule;
import com.jtang.gameserver.admin.facade.StoryMaintianFacade;
import com.jtang.gameserver.admin.handler.request.AddStoryRequest;
import com.jtang.gameserver.admin.handler.request.DeleteStoryRequest;
import com.jtang.gameserver.admin.handler.response.AddStoryResponse;
import com.jtang.gameserver.admin.handler.response.DeleteStoryResponse;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.server.router.AdminRouterHandlerImpl;

@Component
public class StoryMaintianHandler extends AdminRouterHandlerImpl {

	@Autowired
	StoryMaintianFacade storyMaintianFacade;
	
	protected final Log LOGGER = LogFactory.getLog(HeroMaintianHandler.class);

	@Override
	public byte getModule() {
		return GameAdminModule.STORY;
	}

	@Cmd(Id = StoryMaintianCmd.ADD_STORY)
	public void addStory(IoSession session, byte[] bytes, Response response) {
		AddStoryRequest addStory = new AddStoryRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ addStory.actorId+" ----- star = " + addStory.star);
		}
		TResult<Stories> result = storyMaintianFacade.addNextStory(addStory.actorId, addStory.star);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}else{
			AddStoryResponse addStoryResponse = new AddStoryResponse(result.item);
			response.setValue(addStoryResponse.getBytes());
		}
		sessionWrite(session, response);
	}

	@Cmd(Id = StoryMaintianCmd.DEL_STORY)
	public void deleteStory(IoSession session, byte[] bytes, Response response) {
		DeleteStoryRequest deleteStroy = new DeleteStoryRequest(bytes);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("actorId = "+ deleteStroy.actorId);
		}
		TResult<Stories> result = storyMaintianFacade.deleteLastStory(deleteStroy.actorId);
		if (result.isFail()) {
			response.setStatusCode(result.statusCode);
		}else{
			DeleteStoryResponse deleteStoryResponse = new DeleteStoryResponse(result.item);
			response.setValue(deleteStoryResponse.getBytes());
		}
		sessionWrite(session, response);
	}

}
