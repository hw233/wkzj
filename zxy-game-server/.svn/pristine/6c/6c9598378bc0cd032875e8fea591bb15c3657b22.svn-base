package com.jtang.gameserver.module.notify.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除信息列表
 * @author pengzy
 *
 */
public class RemoveNotifyResponse extends IoBufferSerializer {

	private List<Long> notifyIds;

	public RemoveNotifyResponse(List<Long> notifyIds) {
		this.notifyIds = notifyIds;
	}

	@Override
	public void write() {
		writeShort((short) notifyIds.size());
		for (long id : notifyIds) {
			writeLong(id);
		}
	}
}
