package com.jtang.gameserver.module.extapp.questions.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

public class AnswerRequest extends IoBufferSerializer {

	
	/**
	 * 题目id
	 */
	public int questionId;
	
	/**
	 * 选择的答案
	 */
	public int option;
	
	public AnswerRequest(byte bytes[]){
		super(bytes);
	}
	
	@Override
	public void read() {
		this.questionId = readInt();
		this.option = readInt();
	}
}
