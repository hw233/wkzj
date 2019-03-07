package com.jtang.gameserver.module.luckstar.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class LuckStarRewardResponse extends IoBufferSerializer {

	/**
	 * 配置文件id
	 */
	public int id;

	/**
	 * 使用次数
	 */
	public int useNum;

	/**
	 * 下次刷星剩余时间
	 */
	public int time;

	public LuckStarRewardResponse(int id, int useNum, int time) {
		this.id = id;
		this.useNum = useNum;
		this.time = time;
	}

	@Override
	public void write() {
		writeInt(id);
		writeInt(useNum);
		writeInt(time);
	}

}
