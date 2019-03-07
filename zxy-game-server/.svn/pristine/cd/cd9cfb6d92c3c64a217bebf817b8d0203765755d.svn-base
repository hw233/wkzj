package com.jtang.gameserver.module.icon.model;

import com.jtang.core.protocol.IoBufferSerializer;

public class IconVO extends IoBufferSerializer {

	/**
	 * 头像
	 */
	public int icon;
	
	/**
	 * 边框
	 */
	public int fram;
	
	/**
	 * 性别
	 */
	public byte sex;
	public IconVO(int icon,int fram, byte sex){
		this.icon = icon;
		this.fram = fram;
		this.sex = sex;
	}
	
	@Override
	public void write() {
		writeInt(icon);
		writeInt(fram);
		writeByte(sex);
	}
}
