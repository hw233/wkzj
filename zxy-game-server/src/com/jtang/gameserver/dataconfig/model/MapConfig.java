package com.jtang.gameserver.dataconfig.model;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;

@DataFile(fileName = "mapConfig")
public class MapConfig implements ModelAdapter {
	/**
	 * 地图ID
	 */
	private int mapId;
	
	/**
	 * 地图名称
	 */
	private String name;
	
	/**
	 * 地图格子列数
	 */
	private int gridCol;
	
	/**
	 * 地图格子行数
	 */
	private int gridRow;
	
	/**
	 * 最大回合数
	 */
	private int maxRound;

	@Override
	public void initialize() {
		
	}

	public int getMapId() {
		return mapId;
	}
	
	public String getName() {
		return name;
	}

	public int getGridCol() {
		return gridRow;
	}

	public int getGridRow() {
		return gridCol;
	}

	public int getMaxRound() {
		return maxRound;
	}

}
