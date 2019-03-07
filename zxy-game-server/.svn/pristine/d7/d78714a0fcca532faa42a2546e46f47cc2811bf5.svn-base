package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

public class TreasureChatResponse extends ChatResponse {

	/**
	 * 接受方id
	 */
	public long targetId;

	/**
	 * 接收方vip等级
	 */
	public int targetVipLevel;

	/**
	 * 接收方名字
	 */
	public String targetName;

	/**
	 * 接收方等级
	 */
	public int targetLevel;

	/**
	 * 装备类型 1：武器2：防具3：饰品
	 */
	public int type;

	/**
	 * 装备id
	 */
	public int equipId;
	
	/**
	 * 限制等级
	 */
	public int limitLevel;

	public TreasureChatResponse(int msgType, String sendName, long actorId,
			int level, int vipLevel, long targetId, int targetVipLevel,
			String targetName, int targetLevel, int type, int equipId, int limitLevel,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.targetId = targetId;
		this.targetVipLevel = targetVipLevel;
		this.targetName = targetName;
		this.targetLevel = targetLevel;
		this.type = (byte) type;
		this.equipId = equipId;
		this.limitLevel = limitLevel;
	}
	
	@Override
	public void write() {
		super.write();
		writeLong(targetId);
		writeInt(targetVipLevel);
		writeString(targetName);
		writeInt(targetLevel);
		writeByte((byte)type);
		writeInt(equipId);
		writeInt(limitLevel);
	}

}
