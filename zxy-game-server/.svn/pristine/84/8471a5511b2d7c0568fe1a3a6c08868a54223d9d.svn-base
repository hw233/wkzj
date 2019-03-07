package com.jtang.gameserver.module.luckstar.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.luckstar.module.LuckStarVO;

public class LuckStarResponse extends IoBufferSerializer {

	/**
	 * 使用次数
	 */
	public int useNum;

	/**
	 * 下次刷星剩余时间
	 */
	public int time;

	public LuckStarResponse(LuckStarVO item) {
		this.useNum = item.useNum;
		this.time = item.time;
	}

	@Override
	public void write() {
		writeInt(useNum);
		writeInt(time);
	}

}
