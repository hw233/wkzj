package com.jtang.gameserver.module.delve.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Delve;

/**
 * 潜修室传输对象
 * @author ludd
 *
 */
public class DelveVO extends IoBufferSerializer{
	/**
	 * 等级
	 */
	public short level;
	public static DelveVO valueOf(Delve delve){
		DelveVO vo = new DelveVO();
		vo.level = (short)delve.level;
		return vo;
	}

	@Override
	public void write() {
		this.writeShort(level);
	}
}
