package com.jtang.gameserver.module.adventures.achievement.handler.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
/**
 * 成就状态的推送
 * @author pengzy
 *
 */
public class AchieveAttributeResponse extends IoBufferSerializer{
	private static final Logger LOGGER = LoggerFactory.getLogger(AchieveAttributeResponse.class);
	/**
	 * 成就Id
	 */
	private int achieveId;
	
	private Map<AchieveAttributeKey, Object> changedValues;
	
	public AchieveAttributeResponse(int achieveId, Map<AchieveAttributeKey, Object> changedValues) {
		this.achieveId = achieveId;
		this.changedValues = changedValues;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write() {
		writeInt(achieveId);
		writeShort((short)changedValues.size());
		for(Entry<AchieveAttributeKey, Object> entry : changedValues.entrySet()){
			writeByte(entry.getKey().getCode());
			if(entry.getValue() instanceof Integer){
				writeInt((Integer)entry.getValue());
			}
			else if(entry.getValue() instanceof Byte){
				writeByte((Byte)entry.getValue());
			}
			else if(entry.getValue() instanceof List){
				writeIntList((List<Integer>)entry.getValue());
			}
			else{
				LOGGER.error("AchieveAttributeResponse中有未处理的数据类型需要write");
			}
		}
	}
}
