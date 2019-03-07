package com.jtang.gameserver.module.app.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取玩家活动记录
 * @author ludd
 *
 */
public class GetAppRecordRequest extends IoBufferSerializer {

	/**
	 * 活动id
	 */
	public List<Long> appIds;
	
	public GetAppRecordRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {			
		this.appIds = readLongList();
	}

}
