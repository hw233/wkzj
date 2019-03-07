package com.jtang.gameserver.module.app.model;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.app.type.AppKey;

public class AppRecordVO extends IoBufferSerializer {

	/**
	 * 活动id
	 */
	public long appId;
	
	/**
	 * 扩展字段值
	 * key：{@code ExtKey}
	 */
	public Map<AppKey, Object> extRecord = new HashMap<>();
	
	public AppRecordVO(long appId, Map<AppKey, Object> extRecord) {
		super();
		this.appId = appId;
		this.extRecord = extRecord;
	}
	public AppRecordVO(long appId) {
		super();
		this.appId = appId;
		this.extRecord = new HashMap<>();
	}

	@Override
	public void write() {
		this.writeLong(this.appId);
		this.writeShort((short) extRecord.size());
		for (Map.Entry<AppKey, Object> entry : extRecord.entrySet()) {
			this.writeString(entry.getKey().getKey());
			this.writeObject(entry.getValue());
		}
	}

}
