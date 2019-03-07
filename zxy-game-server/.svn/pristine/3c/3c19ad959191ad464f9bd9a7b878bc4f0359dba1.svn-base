package com.jtang.gameserver.module.extapp.rechargeapp.handler.response;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class RecharegeAppResponse extends IoBufferSerializer {

	/**
	 * 充值记录
	 */
	public Map<Integer,Integer> recharegeMap = new HashMap<>();
	
	public RecharegeAppResponse(Map<Integer,Integer> recharegeMap) {
		this.recharegeMap = recharegeMap;
	}

	@Override
	public void write() {
		writeIntMap(recharegeMap);
	}
	
}
