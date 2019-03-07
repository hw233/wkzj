package com.jtang.gameserver.module.notify.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 删除信息的请求数据包
 * @author pengzy
 *
 */
public class RemoveNotifyRequest extends IoBufferSerializer {
	/**
	 * 需要被删除信息的Id列表
	 */
	public List<Long> nIds;
	
	public RemoveNotifyRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		nIds = readLongList();
	}

}
