package com.jtang.gameserver.module.extapp.questions.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class AnswerStateResponse extends IoBufferSerializer {

	/**
	 * 答题状态 0.关闭 1.开启
	 */
	public int state;
	
	public AnswerStateResponse(int state){
		this.state = state;
	}
	
	@Override
	public void write() {
		writeInt(state);
	}
}
