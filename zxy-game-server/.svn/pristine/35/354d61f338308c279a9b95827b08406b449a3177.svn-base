package com.jtang.gameserver.module.sysmail.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Sysmail;

/**
 * 系统邮件列表响应
 * @author 0x737263
 *
 */
public class SysmailListResponse extends IoBufferSerializer {

	private List<SysmailResponse> list;
	
	public SysmailListResponse(List<Sysmail> list) {
		this.list = new ArrayList<>();
		for (Sysmail mail : list) {
			this.list.add(new SysmailResponse(mail));
		}
	}
	
	@Override
	public void write() {
		writeShort((short) list.size());
		for (SysmailResponse response : list) {
			writeBytes(response.getBytes());
		}

	}


}
