package com.jtang.gameserver.module.extapp.randomreward.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;

public class RewardVO extends IoBufferSerializer {

	/**
	 * 人物id
	 */
	public int id;
	
	/**
	 * 下次刷新时间
	 */
	public int flushTime;
	
	@Override
	public void write() {
		writeInt(id);
		writeInt(flushTime);
	}

	public String parser2String() {
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(flushTime);
		return sb.toString();
	}
}
