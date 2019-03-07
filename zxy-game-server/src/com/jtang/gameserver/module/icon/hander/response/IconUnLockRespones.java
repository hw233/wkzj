package com.jtang.gameserver.module.icon.hander.response;

import com.jtang.core.protocol.IoBufferSerializer;

public class IconUnLockRespones extends IoBufferSerializer {
	
	/**
	 * 解锁的头像
	 */
	public int icon;
	
	public IconUnLockRespones(int heroId){
		this.icon = heroId;
	}
	
	@Override
	public void write() {
		writeInt(icon);
	}
	

}
