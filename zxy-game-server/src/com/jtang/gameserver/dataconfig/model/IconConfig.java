package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "iconConfig")
public class IconConfig implements ModelAdapter {

	/**
	 * 头像星级
	 */
	public int star;
	
	/**
	 * 仙人id
	 */
	public int heroId;
	
	/**
	 * 解锁类型
	 */
	public int unLockType;
	
	/**
	 * 解锁条件
	 * 1.潜修解锁,解锁需要潜修次数
	 * 2.获取即解锁,填0
	 */
	public String unLockNum;
	
	@Override
	public void initialize() {
	}

}
