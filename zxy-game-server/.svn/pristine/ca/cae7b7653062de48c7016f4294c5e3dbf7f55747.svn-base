package com.jtang.gameserver.module.test.handler;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.mina.router.annotation.Cmd;
import com.jtang.core.protocol.Response;
import com.jtang.gameserver.server.router.GatewayRouterHandlerImpl;

@Component
public class TestHandler extends GatewayRouterHandlerImpl {

	@Override
	public byte getModule() {
		return ModuleName.BASE;
	}
	
	
	/**
	 * 基本数据类型测试
	 * @param session
	 * @param request
	 * @param response
	 */
	@Cmd(Id = TestCmd.DATA_TYPE)
	public void dataType(IoSession session, byte[] bytes, Response response) {

//		DataTypeRequest request = DataTypeRequest.valueOf(bytes);
//		
//		DataTypeResponse d = new DataTypeResponse();
//		d.byteValue = request.byteValue;
//		d.shortValue = request.shortValue;
//		d.intValue = request.intValue;
//		d.longValue = request.longValue;
//		d.floatValue = request.floatValue;
//		d.stringValue = request.stringValue;
//		d.list = request.list;
//		d.mapInt = request.mapInt;
//		d.mapString = request.mapString;
//		
//		response.setValue(d.getBytes());
//		session.write(response);
	}
	
	/**
	 * 基本数据类型测试
	 * @param session
	 * @param request
	 * @param response
	 */
	@Cmd(Id = TestCmd.COLLECTION_TYPE)
	public void collectionType(IoSession session, byte[] bytes,Response response) {

	}

}
