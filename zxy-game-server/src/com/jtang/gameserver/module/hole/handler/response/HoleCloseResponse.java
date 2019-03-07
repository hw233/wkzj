//package com.jtang.gameserver.module.hole.handler.response;
//
//import com.jtang.core.protocol.IoBufferSerializer;
//
//public class HoleCloseResponse extends IoBufferSerializer {
//
//	/**
//	 * 洞府自增id
//	 */
//	private long id;
//
//	public HoleCloseResponse(long id) {
//		this.id = id;
//	}
//
//	@Override
//	public void write() {
//		writeLong(id);
//	}
//
//}
