package com.jtang.gameserver.module.notify.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 在抢夺或被抢夺之后通知盟友请求，通知成功后，客户端自行修改已通知字段
 * @author pengzy
 *
 */
public class NotifyAllyRequest extends IoBufferSerializer {

	/**
	 * 通知唯一ID，不同的通知，nId必不同，见NotifyVO信息结构
	 */
	public long nId;
	
	public NotifyAllyRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		nId = readLong();
	}

}
