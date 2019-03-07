package com.jtang.core.mina.router;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.DataPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;

/**
 * 消息路由
 * @author 0x737263
 *
 */
public abstract class Router {
	protected Log LOGGER = LogFactory.getLog(getClass());
	
	/**
	 * 模块id与模块句柄 关联列表
	 */
	protected Map<Byte, RouterHandler> MODULE_MAPS = new HashMap<Byte, RouterHandler>();
	
	/**
	 * 注册模块
	 * @param handler   接收handler
	 */
	public void register(RouterHandler handler) {
		if (handler != null) {
			byte module = handler.getModule();
			if (MODULE_MAPS.containsKey(module)) {
				throw new RuntimeException(String.format("module:[%d] duplicated key", module));
			}
			MODULE_MAPS.put(module, handler);
		}
	}
	
	/**
	 * 转发数据验证
	 * @param session
	 * @param dataPacket
	 * @return
	 */
	public boolean forwardValidate(IoSession session, DataPacket dataPacket) {
		if (session == null || dataPacket == null) {
			return false;
		}

		byte module = dataPacket.getModule();
		RouterHandler handler = MODULE_MAPS.get(module);
		if (handler == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(String.format("module:[%d] does not exist!", module));
			}
			return false;
		}

		byte cmd = dataPacket.getCmd();
		Method method = handler.getMethod(cmd);
		if (method == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(String.format("module:[%d],cmd:[%d] does not exist!",module, cmd));
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 路由转发
	 * @param ioSession  当前session
	 * @param msgPacket  消息包
	 */
	public abstract void forward(IoSession session, DataPacket dataPacket);

}