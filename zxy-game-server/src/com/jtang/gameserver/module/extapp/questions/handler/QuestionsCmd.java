package com.jtang.gameserver.module.extapp.questions.handler;

public interface QuestionsCmd {

	/**
	 * 请求答题信息
	 * 请求:{@code Request}
	 * 返回:{@code QuestionsRepsone}
	 */
	byte QUESTIONS_INFO = 1;
	
	/**
	 * 请求:{@code AnswerRequest}
	 * 答题:{@code AnswerResponse}
	 */
	byte ANSWER = 2;
	
	/**
	 * 答题状态
	 * 请求:{@code Request}
	 * 返回:{@code AnswerStateResponse}
	 * 推送:{@code AnswerStateResponse}
	 */
	byte QUESTIONS_STATE = 3;
}
