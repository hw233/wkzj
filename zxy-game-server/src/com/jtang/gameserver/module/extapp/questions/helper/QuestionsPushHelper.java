package com.jtang.gameserver.module.extapp.questions.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.extapp.questions.handler.QuestionsCmd;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class QuestionsPushHelper {
	
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<QuestionsPushHelper> ref = new ObjectReference<QuestionsPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static QuestionsPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushQuestionState(long actorId,int state){
		AnswerStateResponse response = new AnswerStateResponse(state);
		Response rsp = Response.valueOf(ModuleName.QUESTIONS, QuestionsCmd.QUESTIONS_STATE,response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
}
