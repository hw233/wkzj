package com.jtang.gameserver.module.cdkey.handler.response;

import com.jtang.core.lop.result.ListLopResult;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.component.lop.response.CdkeyLOPResponse;

public class CdkeyResponse  extends IoBufferSerializer {
	
	private ListLopResult<CdkeyLOPResponse> list;
	
	public CdkeyResponse(ListLopResult<CdkeyLOPResponse> list) {
		this.list = list;
	}
	
	@Override
	public void write() {
		writeByte(list.successed ? (byte) 1 : (byte) 0);
		writeString(list.message);
		writeShort((short) list.item.size());
		for (CdkeyLOPResponse rsp : list.item) {
			writeInt(rsp.goodsId);
			writeInt(rsp.goodsNum);
		}
	}
}
