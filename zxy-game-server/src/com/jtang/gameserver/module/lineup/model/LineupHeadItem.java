package com.jtang.gameserver.module.lineup.model;

import java.io.Serializable;

/**
 * 阵型页面顶部列表的每一项
 * @author vinceruan
 * 注意两个名称:
 * headIndex:是指阵型页面顶部列表中的位置索引
 * gridIndex:是指阵型页面底部3*3格子的索引(按从左往右、从上往下的顺序排序,从1开始)
 */
public class LineupHeadItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 203033731660979735L;

	/**
	 * 位置索引(从1开始)
	 */
	public int headIndex;
	
	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	/**
	 * 阵位,即仙人在3*3阵型中的位置索引(从1开始)
	 */
	public int gridIndex;
	
	/**
	 * 装备的uuid
	 */
	public long atkEquipUuid;
	
	/**
	 * 防具的uuid
	 */
	public long defEquipUuid;
	
	/**
	 * 饰品的uuid
	 */
	public long decorationUuid;
	
	
}
