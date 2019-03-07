package com.jiatang.common.crossbattle.model;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 上次对战vo
 * @author ludd
 *
 */
public class LastBattleResultVO extends IoBufferSerializer {
	/**
	 * 主队serverId
	 */
	public int homeServerId;
	/**
	 * 客队serverId(轮空客场为0）
	 */
	public int otherServerId;
	/**
	 * 胜利serverId
	 */
	public int winServerId;
	public LastBattleResultVO(int homeServerId, int otherServerId, int winServerId) {
		super();
		this.homeServerId = homeServerId;
		this.otherServerId = otherServerId;
		this.winServerId = winServerId;
	}
	
	public LastBattleResultVO() {
	}
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.homeServerId = buffer.readInt();
		this.otherServerId = buffer.readInt();
		this.winServerId = buffer.readInt();
	}
	
	@Override
	public void write() {
		this.writeInt(this.homeServerId);
		this.writeInt(this.otherServerId);
		this.writeInt(this.winServerId);
	}
	
	
}
