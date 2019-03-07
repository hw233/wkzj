package com.jtang.gameserver.module.user.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 心跳请求
 * <pre>
 * 服务端会监听客户端连接状态。如果大于30秒没有发送任何数据包,则会主动断掉客户端.
 * 客户端需以每30秒内必需与服务端做一次通讯来告诉服务端网络是通畅的。(具体心跳周期可定5,10,15秒等)
 * </pre>
 * @author 0x737263
 *
 */
public class HeartBeatRequest extends IoBufferSerializer {

	@Override
	public void read() {
	}

}
