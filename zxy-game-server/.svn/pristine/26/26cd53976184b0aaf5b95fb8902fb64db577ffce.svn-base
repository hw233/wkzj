package com.jtang.gameserver.admin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class DBEntityQueueInfoResponse extends IoBufferSerializer {

	/**
	 * 队列任务数
	 */
	private int taskNum;
	/**
	 * 未保存实体数
	 */
	private int notSaveEntityNum;
	
	/**
	 * 未保存角色数
	 */
	private int notSaveActorSize;
	
	public DBEntityQueueInfoResponse(int taskNum, int notSaveEntityNum, int notSaveActorSize) {
		super();
		this.taskNum = taskNum;
		this.notSaveEntityNum = notSaveEntityNum;
		this.notSaveActorSize = notSaveActorSize;
	}


	@Override
	public void write() {
		this.writeInt(this.taskNum);
		this.writeInt(this.notSaveEntityNum);
		this.writeInt(this.notSaveActorSize);
	}

}
