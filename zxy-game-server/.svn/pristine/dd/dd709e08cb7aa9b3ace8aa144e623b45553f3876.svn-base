package com.jtang.gameserver.module.notify.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 受邀请到试炼洞后，受邀请者领取奖励的请求，领取成功后，客户端自行修改已领取字段
 * @author pengzy
 *
 */
public class GetRewardRequest extends IoBufferSerializer {
	
	/**
	 * 通知唯一ID，不同的通知，nId必不同，见NotifyVO信息结构
	 */
	public long nId;
	
	public GetRewardRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		nId = readLong();
	}
}
