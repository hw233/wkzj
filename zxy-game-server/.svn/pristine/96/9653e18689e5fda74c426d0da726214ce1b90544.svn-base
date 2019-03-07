package com.jtang.gameserver.module.adventures.bable.model;


import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class BableStateVO {
	
	/**
	 * 登天塔类型
	 */
	public int type;
	
	/**
	 * 层数
	 */
	public int floor;
	
	/**
	 * 登天币
	 */
	public int star;
	
	/**
	 * 已使用登天币
	 */
	public int useStar;
	
	/**
	 * 已使用重试次数
	 */
	public int useRetryNum;
	
	/**
	 * 登塔状态
	 */
	public byte state;
	
	/**
	 * 是否已跳层
	 * 0.未跳层
	 * 1.已跳层
	 */
	private int isSkip;
	
	
	public BableStateVO(){
		
	}
	
	/**
	 * 是否已跳层
	 * @return
	 */
	public boolean isSkip() {
		return isSkip == 1 ? true : false;
	}
	
	/**
	 * 设置已跳层
	 */
	public void setIsSkip(){
		this.isSkip = 1;
	}
	
	public BableStateVO(String bableState) {
		String []str = StringUtils.split(bableState, Splitable.ATTRIBUTE_SPLIT);
		this.type = Byte.valueOf(str[0]);
		this.floor = Integer.valueOf(str[1]);
		this.star = Integer.valueOf(str[2]);
		this.useStar = Integer.valueOf(str[3]);
		this.useRetryNum = Integer.valueOf(str[4]);
		this.state = Byte.valueOf(str[5]);
		this.isSkip = Integer.valueOf(str[6]);
	}

	public String parser2String(){
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(floor);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(star);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(useStar);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(useRetryNum);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(state);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(isSkip);
		return sb.toString();
	}
}
