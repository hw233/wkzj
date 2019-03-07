package com.jtang.gameserver.module.ally.handler.response;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ally.type.AllyAttributeKey;

/**
 * 盟友属性更新
 * @author 0x737263
 *
 */
public class AllyAttributeUpdateResponse extends IoBufferSerializer {
	private static final Log LOGGER = LogFactory.getLog(AllyAttributeUpdateResponse.class);
	/**
	 * 盟友ID
	 */
	private long allyActorId;
	
	private Map<AllyAttributeKey, Object> attributes;
	
	public AllyAttributeUpdateResponse(long allyActorId, Map<AllyAttributeKey, Object> attributes) {
		this.allyActorId = allyActorId;
		this.attributes = attributes;
	}
	
	@Override
	public void write() {
		writeLong(allyActorId);
		writeShort((short)attributes.size());
		for(Entry<AllyAttributeKey, Object> attributeKey : attributes.entrySet()){
			writeByte(attributeKey.getKey().getCode());
			switch(attributeKey.getKey().getDataType()){
			case INT:
				writeInt((int) attributeKey.getValue());
				break;
			case LONG:
				writeLong((long) attributeKey.getValue());
				break;
			case SHORT:
				writeShort((short) attributeKey.getValue());
				break;
			case BYTE:
				writeByte((byte) attributeKey.getValue());
				break;
			case STRING:
				String value = (String) attributeKey.getValue();
				writeString(value);
				break;
			case INT_LIST:
				@SuppressWarnings("unchecked")
				List<Integer> list = (List<Integer>)attributeKey.getValue();
				writeIntList(list);
				break;
			default:
				LOGGER.error("不可序列化的类型:" + attributeKey.getValue().getClass());
			}
		}
		
	}

}
