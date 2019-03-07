package com.jtang.gameserver.server.filter;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.mina.SocketConstant;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * flash策略过滤器类.
 * 
 * @author 0x737263
 * 
 */
@Component
public class FlashPolicyFilter extends IoFilterAdapter {
	private static final Log LOGGER = LogFactory.getLog(FlashPolicyFilter.class);

	private final byte[] POLICY_REQUEST = "<policy-file-request/>".getBytes(SocketConstant.CHARSET);
	
	private static final byte[] POLICY_RESPONSE = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0"
			.getBytes(SocketConstant.CHARSET);
	
	
	@Autowired
	public PlayerSession playerSession;
	
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {

		// 首次接收数据，并且消息内容为<policy-file-request/>则返回策略文件
		Boolean firstRequest = playerSession.getFirstRequest(session);
		
		if (firstRequest == true) {
			
			firstRequest = false;
			playerSession.setFirstRequest(session, firstRequest);

			if ((message instanceof IoBuffer)) {
				IoBuffer input = (IoBuffer) message;
				input.mark();
				byte firstByte = input.get();
				input.reset();
				if (firstByte == 60) {
					if (input.remaining() < this.POLICY_REQUEST.length) {
						return;
					}
					input.mark();
					byte[] byteArray = new byte[this.POLICY_REQUEST.length];
					input.get(byteArray);
					if (Arrays.equals(byteArray, this.POLICY_REQUEST)) {
						if (LOGGER.isInfoEnabled()) {
							LOGGER.info(String.format("SESSION[%s] send flash security policy...", session.getId()));
						}
						session.write(POLICY_RESPONSE);
						return;
					}
					input.reset();
				}
			}
		}

		super.messageReceived(nextFilter, session, message);
	}
}
