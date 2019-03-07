package com.jtang.gameserver.server.router;

import static com.jtang.core.protocol.StatusCode.ACTOR_NOT_LOGIN;
import static com.jtang.core.protocol.StatusCode.MODULE_NOT_FOUND;
import static com.jtang.core.protocol.StatusCode.MODULE_NOT_OPEN;

import java.lang.reflect.Method;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.context.SpringContext;
import com.jtang.core.mina.router.Router;
import com.jtang.core.mina.router.RouterHandler;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.DataPacket;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.dataconfig.service.ModuleControlService;
import com.jtang.gameserver.server.firewall.Firewall;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 消息路由实现类
 * @author 0x737263
 *
 */
@Component("GatewayRouterImpl")
public class GatewayRouterImpl extends Router {
	
	@Autowired
	private PlayerSession playerSession;

	@Override
	public void forward(IoSession session, DataPacket dataPacket) {
		if(forwardValidate(session, dataPacket) == false) {
			Firewall firewall = (Firewall) SpringContext.getBean(Firewall.class);
			if (firewall.isEnableFirewall() && firewall.blockedByAuthCodeErrors(session, 1)) {
				LOGGER.warn(String.format("In blacklist: [ip: %s]", playerSession.getRemoteIp(session)));
				playerSession.closeIoSession(session, true);
			}
			return;
		}
		byte module = dataPacket.getModule();
		byte cmd = dataPacket.getCmd();
		RouterHandler handler = MODULE_MAPS.get(module);
		Method method = handler.getMethod(cmd);
		Response response = Response.valueOf(dataPacket.getModule(), dataPacket.getCmd());
		if (handler == null || method == null) {
			Long actorId = playerSession.getActorId(session);
			LOGGER.error(String.format("[forward] actorId:[%s], module:[%s], cmd:[%s]", actorId, module, cmd));
			response.setStatusCode(MODULE_NOT_FOUND);
			session.write(response);
			return;
		}
		
		if (ModuleControlService.isOpen(module) == false) {
			Long actorId = playerSession.getActorId(session);
			LOGGER.warn(String.format("[module not open] actorId:[%s], module:[%s], cmd:[%s]", actorId, module, cmd));
			response.setStatusCode(MODULE_NOT_OPEN);
			session.write(response);
			return;
		}
		
		
		//增加  根据cmd标注判断 帐号是否登陆，角色是否登陆.
		Cmd annotation = handler.getCmd(dataPacket.getCmd());
		if(annotation.CheckActorLogin()) {
			if(playerSession.isOnline(session) == false){
				response.setStatusCode(ACTOR_NOT_LOGIN);
				session.write(response);
				return;
			}
		}
		
		try {
			method.invoke(handler, session, dataPacket.getValue(), response);
		} catch (Exception exception) {
			Long actorId = playerSession.getActorId(session);
			LOGGER.error(String.format("[method.invoke] actorId:[%s]", actorId), exception.getCause());
		}
	}
}