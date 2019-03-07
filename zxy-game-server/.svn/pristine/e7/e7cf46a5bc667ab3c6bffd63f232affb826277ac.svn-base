package com.jtang.gameserver.module.extapp.questions.facade;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.QuestionsRepsone;

public interface QuestionsFacade {

	/**
	 * 获取问答信息
	 * @param actorId
	 * @return
	 */
	public TResult<QuestionsRepsone> getInfo(long actorId);

	/**
	 * 答题
	 * @param actorId
	 * @param questionId
	 * @param option
	 * @return
	 */
	public TResult<AnswerResponse> answer(long actorId, int questionId,int option);

	/**
	 * 获取答题状态
	 * @return
	 */
	public TResult<AnswerStateResponse> getState();

}
