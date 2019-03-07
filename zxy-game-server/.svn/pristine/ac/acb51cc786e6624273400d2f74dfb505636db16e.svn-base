package com.jtang.gameserver.module.user.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.user.model.ActorInfo;

/**
 * 获取角色的返回信息
 * @author 0x737263
 *
 */
public class GetActorResponse extends IoBufferSerializer {

	/**
	 * 角色基本信息列表
	 */
	public List<ActorInfo> list;
	
	public GetActorResponse(List<ActorInfo> list) {
		this.list = list;
	}
	
	@Override
	public void write() {
		this.writeShort((short) list.size());
		for (ActorInfo actorInfo : list) {
			this.writeBytes(actorInfo.getBytes());
		}
	}
}
