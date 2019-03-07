package com.jtang.gameserver.module.chat.handler.response;

import com.jtang.gameserver.module.icon.model.IconVO;

public class SystemChatResponse extends ChatResponse {

	/**
	 * 类型 1.装备 2.仙人
	 */
	public int type;

	/**
	 * 仙人或魂魄id
	 */
	public int itemId;

	/**
	 * 获取类型
	 */
	public int getType;

	public SystemChatResponse(int msgType, String actorName, long actorId, int level, int vipLevel, int type, int itemId, int getType,IconVO iconVO) {
		super(msgType, actorName, actorId, level, vipLevel,iconVO);
		this.type = (byte) type;
		this.itemId = itemId;
		this.getType = getType;
	}

	@Override
	public void write() {
		super.write();
		writeByte((byte)this.type);
		writeInt(this.itemId);
		writeInt(this.getType);
	}

}
