package com.jtang.gameserver.module.gift.handler.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送礼物状态变更
 * @author vinceruan
 *
 */
public class PushGiftStateResponse extends IoBufferSerializer {
	/**
	 * key参考{@code GiftStateType}
	 * value:盟友的id
	 */
	public Map<Long, Byte> map = new HashMap<Long, Byte>();

	@Override
	public void write() {
		writeShort((short) map.size());
		for(Entry<Long,Byte> entry:map.entrySet()){
			writeLong(entry.getKey());
			writeByte(entry.getValue());
		}
	}
	
	public PushGiftStateResponse(byte type, long allyActorId) {
		map.put(allyActorId,type);
	}
	
	public PushGiftStateResponse(){
		
	}
}
