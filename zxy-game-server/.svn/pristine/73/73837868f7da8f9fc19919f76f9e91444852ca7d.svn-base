package com.jtang.gameserver.module.app.handler.request;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 获取活动配置数据
 * @author ludd
 *
 */
public class GetAppGlobalRequest extends IoBufferSerializer {

	/**
	 * 活动id
	 */
	public List<Long> appIds;
	
	public GetAppGlobalRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.appIds = readLongList();
	}

}
