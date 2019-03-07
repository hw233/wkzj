package com.jtang.gameserver.module.extapp.questions.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.questions.facade.QuestionsFacade;
import com.jtang.gameserver.module.extapp.questions.handler.request.AnswerRequest;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.QuestionsRepsone;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class QuestionsHandler extends GatewayRouterHandlerImpl {

	@Autowired
	PlayerSession playerSession;
	
	@Autowired
	QuestionsFacade questionsFacade;
	
	@Override
	public byte getModule() {
		return ModuleName.QUESTIONS;
	}

	@Cmd(Id = QuestionsCmd.QUESTIONS_INFO)
	public void getInfo(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		TResult<QuestionsRepsone> result = questionsFacade.getInfo(actorId);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = QuestionsCmd.ANSWER)
	public void answer(IoSession session, byte[] bytes, Response response){
		long actorId = playerSession.getActorId(session);
		AnswerRequest request = new AnswerRequest(bytes);
		TResult<AnswerResponse> result = questionsFacade.answer(actorId,request.questionId,request.option);
		if(result.isFail()){
			response.setStatusCode(result.statusCode);
		}else{
			response.setValue(result.item.getBytes());
		}
		sessionWrite(session, response);
	}
	
	@Cmd(Id = QuestionsCmd.QUESTIONS_STATE)
	public void getState(IoSession session, byte[] bytes, Response response){
		TResult<AnswerStateResponse> result = questionsFacade.getState();
		sessionWrite(session, response, result.item);
	}
}
