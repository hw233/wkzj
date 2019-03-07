package com.jtang.gameserver.module.sysmail.handler.response;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Sysmail;

/**
 * 系统邮件响应类
 * @author 0x737263
 *
 */
public class SysmailResponse extends IoBufferSerializer {
	
	private Sysmail sysmail;
	
	public SysmailResponse(Sysmail sysmail) {
		this.sysmail = sysmail;
	}

	@Override
	public void write() {
		writeLong(sysmail.getPkId());
		writeLong(sysmail.ownerActorId);
		writeString(sysmail.content);
		writeInt(sysmail.sendTime);
		writeInt(sysmail.isGet);

		writeShort((short) sysmail.getAttachGoodsList().size());
		for (RewardObject reward : sysmail.getAttachGoodsList()) {
			writeBytes(reward.getBytes());
		}
	}

}
