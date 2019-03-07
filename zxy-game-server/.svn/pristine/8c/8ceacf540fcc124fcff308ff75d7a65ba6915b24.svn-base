package com.jtang.gameserver.module.love.model;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;
/**
 * 请求结婚信息
 * @author ludd
 *
 */
public class MarryAcceptVO extends IoBufferSerializer {
	/**
	 * 角色id
	 */
	private long actorId;
	/**
	 * 角色名
	 */
	private String actorName;
	/**
	 * 过期时间（秒）
	 */
	private int timeout;
	/**
	 * 角色头像
	 */
	private IconVO iconVO;
	public MarryAcceptVO(long actorId,String actorName, int timeout, IconVO iconVO) {
		super();
		this.actorId = actorId;
		this.actorName = actorName;
		this.timeout = timeout;
		this.iconVO = iconVO;
	}
	
	@Override
	public void write() {
		this.writeLong(this.actorId);
		this.writeString(this.actorName);
		this.writeInt(this.timeout);
		this.writeBytes(this.iconVO.getBytes());
	}
	
	
	
}
