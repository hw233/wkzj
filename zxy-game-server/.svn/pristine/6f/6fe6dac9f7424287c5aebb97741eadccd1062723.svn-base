package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.RandomUtils;
import com.jtang.gameserver.module.battle.type.TileType;


/**
 * 战斗地图 用于处理战斗占位问题
 * 
 * @author 0x737263
 * 
 */
public class BattleMap {
	/**
	 * 一行有多少个格子
	 */
	public int rowGridCount = 3;
	
	/**
	 * 一列有多少个格子
	 */
	public int colGridCount = 8;

	/**
	 * 地图tile数组
	 */
	private int[][] tileArray;
	/**
	 * 权重数组，当前点为坐标原点，坐标轴的权重, 下标对应的权重如下 
	 * 
	 */
	private int[] weightArray_down = { 6, 7, 0, 1, 2, 3, 4, 5};
	private int[] weightArray_up = { 2, 3, 4, 5, 6, 7, 0, 1};

	public BattleMap(int rowGridCount, int colGridCount) {
		this.colGridCount =  colGridCount;
		this.rowGridCount = rowGridCount;
		tileArray = new int[rowGridCount][colGridCount];
		
//		for (int i = 0; i < rowGridCount; i++) {
//			for (int j = 0; j < colGridCount; j++) {
//				tileArray[i][j] = TileType.ROAD_POINT;
//			}
//		}
		reset();
		// tileAtkScope = new HashMap<String,List<Tile>>();
	}
	
	public void reset() {
		for (int i = 0; i < rowGridCount; i++) {
			for (int j = 0; j < colGridCount; j++) {
				tileArray[i][j] = TileType.ROAD_POINT;
			}
		}
	}

	/**
	 * 
	 * @param barrierLists
	 *            设置障碍点列表
	 */
	public BattleMap(List<Tile> barrierLists) {
		for (Tile t : barrierLists) {
			tileArray[t.getX()][t.getY()] = TileType.BARRIER_POINT;
		}
	}

	/**
	 * 设置障碍点
	 * 
	 * @param x
	 * @param y
	 * @param tileType
	 */
	public void setTile(Tile tile, int tileType) {
		if (checkTile(tile) == false) {
			return;
		}
		if (tileType == TileType.ROAD_POINT
				|| tileType == TileType.BARRIER_POINT) {
			tileArray[tile.getX()][tile.getY()] = tileType;
		}
	}

	/**
	 * 是否可走
	 * 
	 * @param x
	 * @param y
	 * @return    true 可走 fase不可走
	 */
	public Boolean isWalk(Tile tile) {
		if (checkTile(tile) == false) {
			return false;
		}

		return tileArray[tile.getX()][tile.getY()] == TileType.ROAD_POINT ? true
				: false;
	}
	
	/**
	 * 是否超出地图
	 * @param tile
	 * @return
	 */
	public boolean isOutOfMap(Tile tile) {
		return !checkTile(tile);
	}

	/**
	 * 根据当前tile和方向移动一格
	 * 
	 * @param fromTile	当前要移动的tile
	 * @param direction	移动方向(暂未定义,看怎么定义好一点)
	 * @return
	 */
	public Boolean move(Tile fromTile, int direction) {
		Boolean checkFlag = checkTile(fromTile);
		if (checkFlag == false) {
			return false;
		}
		int point = getTilePoint(fromTile);
		if (point != TileType.BARRIER_POINT) {
			return false;
		}
		// 当前八个方向判断是否可以走..并且寻路一格
		return true;
	}
	
	/**
	 * 移动多步
	 * @param fromTile
	 * @param toTile
	 * @return
	 */
	public Boolean move(Tile fromTile, Tile toTile) {
		if (!checkTile(fromTile) || !checkTile(toTile)) {
			return false;
		}
		int point = getTilePoint(fromTile);
		if (point != TileType.BARRIER_POINT) {
			return false;
		}
		point = getTilePoint(toTile);
		if (point != TileType.ROAD_POINT) {
			return false;
		}
		if (Math.abs(fromTile.getX() - toTile.getX()) + Math.abs(fromTile.getY() - toTile.getY()) != 1) { //不是相邻格子
			return false;
		}
		tileArray[fromTile.getX()][fromTile.getY()] = TileType.ROAD_POINT;
		tileArray[toTile.getX()][toTile.getY()] = TileType.BARRIER_POINT;
		return true;
	}
	
	/**
	 * 从一个点跳到另外一个点
	 * @param fromTile
	 * @param toTile
	 * @return
	 */
	public Boolean jump(Tile fromTile, Tile toTile) {
		if (!checkTile(fromTile) || !checkTile(toTile)) {
			return false;
		}
		int point = getTilePoint(fromTile);
		if (point != TileType.BARRIER_POINT) {
			return false;
		}
		point = getTilePoint(toTile);
		if (point != TileType.ROAD_POINT) {
			return false;
		}
		
		tileArray[fromTile.getX()][fromTile.getY()] = TileType.ROAD_POINT;
		tileArray[toTile.getX()][toTile.getY()] = TileType.BARRIER_POINT;
		return true;
	}
	
	public boolean clearBarrier(Tile tile) {
		if (!checkTile(tile)) {
			return false;
		}
		
		tileArray[tile.getX()][tile.getY()] = TileType.ROAD_POINT;
		return true;
	}
	
	public boolean addBarrier(Tile tile) {
		if (!checkTile(tile)) {
			return false;
		}
		
		tileArray[tile.getX()][tile.getY()] = TileType.BARRIER_POINT;
		return true;
	}

	/**
	 * 根据tile和scope叁数获取周围范围
	 * 
	 * @param tile
	 * @param scope
	 * @return
	 */
	public List<Tile> getScopeTile(Tile tile, int scope) {
		List<Tile> list = new ArrayList<Tile>();
		// 循环周边范围 获取tile格子列表
		// TODO 该步骤的计算可以缓存,以放便后续调用
		Tile temp = null;
		int newPx = 0;
		int newPy = 0;
		scope = -scope;
		// 周边八个方向,去掉当前点
		for (int x = scope; x <= -scope; x++) {
			for (int y = scope; y <= -scope; y++) {
				// 过滤当前点
				if (y != 0 || x != 0) {
					newPx = tile.getX() + x;
					newPy = tile.getY() + y;
					temp = new Tile(newPx, newPy);
					if (checkTile(temp)) {
						list.add(temp);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 
	 * 获取相对目标行走的下一个点
	 * @param current
	 * @param target
	 * @return			没找到返回null
	 */
	public Tile getNextTile2Target(Tile current, Tile target){
		List<Tile> list = getFourDirScope(current);
		if(null == list || list.size() <= 0){
			return null;
		}
		Tile result = null;
		for (Tile tile : list) {
			if(!isWalk(tile)){
				continue;
			}
			if(null == result){
				result = tile;
				continue;
			}
			if(result.getDis(target) > tile.getDis(target))
			{
				result = tile;
			}
		}
		return result;
	}
	
	/**
	 * 获取当前点周围4个方向的点
	 * @param tile
	 * @return
	 */
	public List<Tile> getFourDirScope(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		Tile temp = null;
		int addX = 1;
		for (int i = -2; i <= 2; i++) {
			if(i == 0){
				addX = -addX;
				continue;
			}
			temp = new Tile();
			int xadd = i + addX;
			int yadd = ((xadd & 1) ^ 1) * addX;
			temp.setX(tile.getX() + xadd);
			temp.setY(tile.getY() + yadd);
			if(checkTile(temp))
			{
				list.add(temp);
			}
		}
		return list;
	}
	/**
	 * 获取当前点周围4个方向的点
	 * @param tile
	 * @return
	 */
	public List<Tile> getEightDirScope(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		Tile temp1 = new Tile(tile.getX() -1, tile.getY() - 1);
		Tile temp2 = new Tile(tile.getX(), tile.getY() - 1);
		Tile temp3 = new Tile(tile.getX() +1, tile.getY() - 1);
		Tile temp4 = new Tile(tile.getX() -1, tile.getY());
		Tile temp5 = new Tile(tile.getX() +1, tile.getY());
		Tile temp6 = new Tile(tile.getX() -1, tile.getY() + 1);
		Tile temp7 = new Tile(tile.getX() , tile.getY() + 1);
		Tile temp8 = new Tile(tile.getX() +1, tile.getY() +1);
		if (checkTile(temp1)){
			list.add(temp1);
		}
		if (checkTile(temp2)){
			list.add(temp2);
		}
		if (checkTile(temp3)){
			list.add(temp3);
		}
		if (checkTile(temp4)){
			list.add(temp4);
		}
		if (checkTile(temp5)){
			list.add(temp5);
		}
		if (checkTile(temp6)){
			list.add(temp6);
		}
		if (checkTile(temp7)){
			list.add(temp7);
		}
		if (checkTile(temp8)){
			list.add(temp8);
		}
		return list;
	}
	/**
	 * 目标点是否是当前点周围4个方向的点
	 * @param tile
	 * @param targetTile
	 * @return 1：是，2：否
	 */
	public byte isFourDirTile(Tile tile, Tile targetTile) {
		List<Tile> tiles = getEightDirScope(tile);
		if (tiles.contains(targetTile)) {
			return 1;
		}
		return 2;
	}


	/**
	 * 获取当前目标点的方向
	 * x负方向是0，逆时针增加0-7；
	 * @param pixelPoint
	 * @return 方向
	 * 
	 */
	private int getDirection(Tile currentTile, Tile newTile) {
		double angle = Math.atan2(newTile.getY() - currentTile.getY(),
				newTile.getX() - currentTile.getX());
		Long dire = Math.round((angle + Math.PI) / (Math.PI / 4)) - 1;
		int result = dire.intValue();
		if (result > 0) {
			return result - 1;
		} else {
			return 7;
		}
	}
	/**
	 * 获取方向
	 * @param currentTile
	 * @param newTile
	 * @return
	 */
	public byte getDirections(Tile currentTile, Tile newTile) {
		int xOffset = newTile.getX() - currentTile.getX();
		int yOffset = newTile.getY() - currentTile.getY();
		if (xOffset == 0 && yOffset < 0) {
			return 6;
		} else if (xOffset > 0 && yOffset < 0) {
			return 5;
		} else if (xOffset > 0 && yOffset == 0) {
			return 4;
		} else if (xOffset > 0 && yOffset > 0) {
			return 3;
		} else if (xOffset == 0 && yOffset > 0) {
			return 2;
		} else if (xOffset < 0 && yOffset > 0) {
			return 1;
		} else if (xOffset < 0 && yOffset ==  0) {
			return 0;
		} else {
			return 7;
		}
	}
	public static void main(String[] args) {
//		Tile newTile = new Tile(0, 2);
//		Tile currentTile = new Tile(0, 5);
//		byte result = getDirections(currentTile, newTile);
//		System.out.println(result);
	}
	/**
	 * 是否在前方
	 * @param currentTile
	 * @param targetTile
	 * @return 0：背后，1：前方
	 */
	public byte isBehind(Tile currentTile, Tile targetTile){
		if (currentTile == null || targetTile == null) {
			return 1;
		}
		if (currentTile.getY() > targetTile.getY()) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 获取离当前点最近的一个点
	 * 
	 * @param tile
	 *            当前点
	 * @param list
	 *            附近点数组
	 * @param dierect		当前的方向  方向，0 向上， 1向下
	 *            
	 * @return		返回当前点，如果错误，返回null
	 */
	public Tile getCloseTile(Tile tile, List<Tile> list, int dierect) {
		Tile result = null;
		if (!checkTile(tile)) {
			return result;
		}
		for (Tile tile2 : list) {
			if(!checkTile(tile2))
			{
				continue;
			}
			if (null == result) {
				result = tile2;
				continue;
			}
			int dis = result.getDis(tile);
			int dis2 = tile2.getDis(tile);
			if (dis > dis2){
				result = tile2;
			} else if(dis == dis2 && getWeight(tile, result, dierect) > getWeight(tile, tile2, dierect)){
				result = tile2;
			}
		}
		return result;
	}

	/**
	 * 获取当前点到目标点的权重
	 * @param currentTile	当前点
	 * @param newTile		目标点
	 * @param direct		方向，0 向上， 1向下
	 * @return
	 */
	private int getWeight(Tile currentTile, Tile newTile, int direct) {
		int cWeight = getDirection(currentTile, newTile);
		if(direct == 1){
			if(cWeight < 0 || cWeight >= weightArray_down.length){
				return 8;
			}
			return weightArray_down[cWeight];
		}
		if(cWeight < 0 || cWeight >= weightArray_up.length){
			return 8;
		}
		return weightArray_up[cWeight];
	}

	private int getTilePoint(Tile tile) {
		return tileArray[tile.getX()][tile.getY()];
	}

	private Boolean checkTile(Tile tile) {
		if (tile.getX() < 0 || tile.getX() >= rowGridCount) {
			return false;
		}
		if (tile.getY() < 0 || tile.getY() >= colGridCount) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取随机空位
	 * @return
	 */
	public Tile getRandomRoadTile() {
		List<Tile> tiles = new ArrayList<>();
		for (int i = 0; i < rowGridCount; i++) {
			for (int j = 0; j < colGridCount; j++) {
				if (tileArray[i][j] == TileType.ROAD_POINT) {
					Tile t = new Tile(i, j);
					tiles.add(t);
				}
			}
		}
		if (tiles.isEmpty()) {
			return null;
		}
		int index = RandomUtils.nextIntIndex(tiles.size());
		return tiles.get(index);
		
		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("地图数组:");
		for (int i = 0; i < rowGridCount; i++) {
			for (int j = 0; j < colGridCount; j++) {
				sb.append(tileArray[i][j]).append(",");
			}
		}
		sb.append("rowGridCount:").append(this.rowGridCount);
		sb.append("colGridCount:").append(this.colGridCount);
		
		return sb.toString();
	}
}
