package com.jtang.gameserver.module.adventures.bable.model;

import com.jtang.core.utility.Splitable;

public class BableHistoryVO {
	
	/**
	 * 登天塔类型
	 */
	public int type;
	
	/**
	 * 登天塔层数
	 */
	public int floor;
	
	/**
	 * 通天币
	 */
	public int star;
	
	public BableHistoryVO(String[] str) {
		this.type = Integer.valueOf(str[0]);
		this.floor = Integer.valueOf(str[1]);
		this.star = Integer.valueOf(str[2]);
	}

	public BableHistoryVO(int type,int floor, int star) {
		this.type = type;
		this.floor = floor;
		this.star = star;
	}

	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(floor);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(star);
		return sb.toString();
	}
}
