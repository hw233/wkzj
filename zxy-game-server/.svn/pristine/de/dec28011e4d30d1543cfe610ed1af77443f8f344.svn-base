package com.jtang.gameserver.module.extapp.questions.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class AnswerResponse extends IoBufferSerializer {

	/**
	 * 是否正确
	 * 0.错误 1.正确
	 */
	private int state;
	
	public void setState(int state){
		this.state = state;
	}
	
	@Override
	public void write() {
		writeInt(state);
	}
	
}
