package com.jtang.gameserver.module.icon.hander.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.icon.model.IconVO;

public class IconResponse extends IoBufferSerializer{

	/**
	 * 解锁的头像id
	 */
	public List<Integer> heroIds = new ArrayList<>();
	
	/**
	 * 解锁的边框id
	 */
	public List<Integer> framId = new ArrayList<>();
	
	/**
	 * 现在使用的头像和边框
	 */
	public IconVO iconVO;
	
	public IconResponse(List<Integer> heroIds,List<Integer> framId,int heroId,int fram, byte sex){
		this.heroIds = heroIds;
		this.framId = framId;
		this.iconVO = new IconVO(heroId,fram,sex);
	}
	
	@Override
	public void write() {
		writeIntList(heroIds);
		writeIntList(framId);
		writeBytes(iconVO.getBytes());
	}
}
