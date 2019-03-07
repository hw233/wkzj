package com.jtang.gameserver.module.story.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 推送战场的星数(只有战场的星数比原来增加了才会推送)
 * @author vinceruan
 *
 */
public class UpdateBattleStarResponse extends IoBufferSerializer {
	/**
	 * 战场ID
	 */
	int battleId;
	
	/**
	 * 星数
	 */
	byte star;

	@Override
	public void write() {
		writeInt(battleId);
		writeByte(star);
	}
	
	public UpdateBattleStarResponse(int battleId, byte star) {
		this.battleId = battleId;
		this.star = star;
	}
}
