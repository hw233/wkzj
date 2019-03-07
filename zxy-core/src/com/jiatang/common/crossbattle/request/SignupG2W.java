package com.jiatang.common.crossbattle.request;

import java.util.ArrayList;
import java.util.List;

import com.jiatang.common.crossbattle.model.ActorCrossData;
import com.jtang.core.protocol.IoBufferSerializer;

public class SignupG2W extends IoBufferSerializer {

	/**
	 * 本服务器参加跨服战数据
	 */
	public List<ActorCrossData> actorCrossData;
	
	public SignupG2W(List<ActorCrossData> actorCrossData) {
		super();
		this.actorCrossData = actorCrossData;
	}
 
	

	public SignupG2W(byte[] bytes) {
		super(bytes);
	}



	@Override
	public void write() {
		this.writeShort((short) actorCrossData.size());
		for (ActorCrossData data : actorCrossData) {
			this.writeBytes(data.getBytes());
		}
	}
	
	@Override
	protected void read() {
		short len = readShort();
		actorCrossData = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			ActorCrossData data = new ActorCrossData();
			data.readBuffer(this);
			actorCrossData.add(data);
		}
	}

}
