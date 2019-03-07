package com.jtang.gameserver.module.adventures.bable.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 自动登塔请求
 * @author ludd
 *
 */
public class BableAutoRequest extends IoBufferSerializer {

	
	/**
	 * 登天塔类型
	 */
	public byte bableType;
	
	/**
	 * 使用物品id
	 */
	public int useGoodsId;
	
	public BableAutoRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		bableType = readByte(); 
		useGoodsId = readInt();
	}
}
