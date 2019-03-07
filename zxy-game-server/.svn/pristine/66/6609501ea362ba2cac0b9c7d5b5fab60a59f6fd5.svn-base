package com.jtang.gameserver.module.lineup.constant;

import com.jtang.gameserver.dataconfig.service.GlobalService;

/**
 * 阵型模块常量表
 * @author vinceruan
 *
 */
public class LineupRule {
	/**
	 * 阵型中每一行可以放置多少个仙人
	 */
	public static int LINEUP_GRID_COUNT_EACH_ROW; // = 3;
	
	/**
	 * 阵型中每一列可以放置多少个仙人
	 */
	public static int LINEUP_GRID_COUNT_EACH_COL; // = 3;
		
	/**
	 * 初始开放的阵型格子数
	 */
	public static int LINEUP_INIT_ACTIVED_GRID_COUNT; // = 1;
	
	/***
	 * 阵型中的行数
	 */
	public static int ROW_COUNT = LINEUP_GRID_COUNT_EACH_COL;
	
	/**
	 * 阵型中的列数
	 */
	public static int COL_COUNT = LINEUP_GRID_COUNT_EACH_ROW;
	
	/**
	 * 阵型总的格子数
	 */
	public static int MAX_GRID_COUNT = ROW_COUNT * COL_COUNT;
	
	static {

		LINEUP_GRID_COUNT_EACH_ROW = GlobalService.getInt("LINEUP_GRID_COUNT_EACH_ROW");
		LINEUP_GRID_COUNT_EACH_COL = GlobalService.getInt("LINEUP_GRID_COUNT_EACH_COL");
		LINEUP_INIT_ACTIVED_GRID_COUNT = GlobalService.getInt("LINEUP_INIT_ACTIVED_GRID_COUNT");
		
		ROW_COUNT = LINEUP_GRID_COUNT_EACH_COL;
		COL_COUNT = LINEUP_GRID_COUNT_EACH_ROW;
		MAX_GRID_COUNT = ROW_COUNT * COL_COUNT;
		
	}
}
