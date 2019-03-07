package com.jtang.gameserver.module.msg.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 删除留言的数据包
 * @author pengzy
 *
 */
public class RemoveMsgRequest extends IoBufferSerializer {

	public List<Long> mIdList;
	
	public RemoveMsgRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		mIdList = readLongList();
	}
}
