package com.jtang.core.mina.router;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;

import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.DataPacket;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 模块处理句柄接口
 * 
 * @author 0x737263
 * 
 */
public abstract class RouterHandler {
	protected Log LOGGER = LogFactory.getLog(getClass());
	
	/**
	 * 方法映射列表
	 */
	private Map<Byte, Method> METHOD_MAPS = new HashMap<Byte, Method>();

	/**
	 * 命令映射列表
	 */
	private Map<Byte, Cmd> CMD_MAPS = new HashMap<Byte, Cmd>();
	
	/**
	 * 获取模块
	 * @param cmd
	 * @return
	 */
	public Method getMethod(byte cmd) {
		return METHOD_MAPS.get(cmd);
	}

	/**
	 * 获取命令注解
	 * @param cmd
	 * @return
	 */
	public Cmd getCmd(byte cmd) {
		return CMD_MAPS.get(cmd);
	}
	
	@PostConstruct
	private void init() {
		register();

		Method[] mList = this.getClass().getDeclaredMethods();
		for (Method m : mList) {
			Cmd c = m.getAnnotation(Cmd.class);
			if (c != null) {
				if (METHOD_MAPS.containsKey(c.Id())) {
					throw new RuntimeException(String.format("cmd annontation:[%d] duplicated key", c.Id()));
				}
				METHOD_MAPS.put(c.Id(), m);
				CMD_MAPS.put(c.Id(), c);

//				if (LOGGER.isDebugEnabled()) {
//					LOGGER.debug(String.format("add cmd success! module:[%s]  cmd:[%s]  methodname:[%s]", getModule(), c.Id(), m.toString()));
//				}
			}
		}
	}
	
	/**
	 * write
	 * @param session
	 * @param response
	 */
	public void sessionWrite(IoSession session, DataPacket dataPacket) {
		session.write(dataPacket);
	}

	/**
	 * write
	 * @param session
	 * @param response
	 * @param packet
	 */
	public void sessionWrite(IoSession session, DataPacket dataPacket, IoBufferSerializer buffer) {
		dataPacket.setValue(buffer.getBytes());
		session.write(dataPacket);
	}
	
	/**
	 * get module
	 * @return
	 */
	public abstract byte getModule();
	
	/**
	 * 实现类调用注册当前handler
	 */
	public abstract void register();

}