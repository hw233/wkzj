package com.jtang.gameserver.module.snatch.model;

import java.io.Serializable;
import java.util.List;

/**
 * 旧敌
 * @author vinceruan
 *
 */
public class Enemy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2204570162679020494L;

	/**
	 * 仇恨类型：我曾经被对方抢夺
	 */
	public static int HATRED_TYPE_SNATCHED = 2;
	
	/**
	 * 仇恨类型：我曾经抢夺对方
	 */
	public static int HATRED_TYPE_SNATCH = 3;	
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 抢夺时间
	 */
	public int snatchTime;
	
	/**
	 * 仇恨类型
	 */
	public int hatredType;
	
	public Enemy(long actorId, int snatchTime, int hatredType) {
		this.actorId = actorId;
		this.snatchTime = snatchTime;
		this.hatredType = hatredType;
	}
	
	/**
	 * blog String转换为对象
	 * @param stringList	blob字符串
	 * @return
	 */
	public static Enemy valueOf(List<String> stringList) {
		return new Enemy(Long.valueOf(stringList.get(0)), Integer.valueOf(stringList.get(1)), Integer.valueOf(stringList.get(2)));
	}
	
}
