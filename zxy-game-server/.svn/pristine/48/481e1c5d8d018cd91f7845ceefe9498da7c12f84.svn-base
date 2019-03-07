package com.jtang.gameserver.module.lineup.model;

import java.util.ArrayList;
import java.util.List;



/**
 * 阵型中的英雄
 * @author pengzy
 *
 */
public class LineupHero {

	/**
	 * 阵位,代表在阵型中的格子索引(从1开始)
	 */
	public int index;
	
	/**
	 * 英雄id
	 */
	public int heroId;
	
	/**
	 * 武器的uuid列表
	 */
	public List<Long> equipList = new ArrayList<>();
	
	public LineupHero(LineupHeadItem grid) {
		this.index = grid.gridIndex;
		equipList.add(grid.atkEquipUuid);
		equipList.add(grid.defEquipUuid);
		equipList.add(grid.decorationUuid);
		this.heroId = grid.heroId;
	}
}
